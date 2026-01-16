package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(
            () -> new NoSuchElementException(messageCreateRequest.channelId() + " 채널을 찾을 수 없습니다."));
    User author = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(
            () -> new NoSuchElementException(messageCreateRequest.authorId() + " 사용자를 찾을 수 없습니다."));

    String content = messageCreateRequest.content();

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          return new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
        })
        .toList();

    Message message = new Message(content, channel, author, attachments);
    messageRepository.save(message);

    return messageMapper.toDto(message);
  }

  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다."));
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
    String newContent = messageUpdateRequest.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다."));
    message.update(newContent);
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(messageId + " 메시지를 찾을 수 없습니다."));

    messageRepository.delete(message);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }
}
