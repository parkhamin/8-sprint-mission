package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  @Override
  public Channel create(PublicChannelCreateRequest channelCreateRequest) {
    String name = channelCreateRequest.name();
    String description = channelCreateRequest.description();

    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    return channelRepository.save(channel);
  }

  @Override
  public Channel create(PrivateChannelCreateRequest channelCreateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    List<ReadStatus> readStatusList = userRepository.findAllById(
            channelCreateRequest.participantIds())
        .stream()
        .map(user -> new ReadStatus(user, createdChannel, createdChannel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatusList);
    return createdChannel;
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDTO find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toChannelDTO)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));
  }

  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest) {
    String newName = channelUpdateRequest.newName();
    String newDescription = channelUpdateRequest.newDescription();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("타입이 private인 채널은 수정할 수 없습니다.");
    }

    channel.update(newName, newDescription);
    return channel;
  }

  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다.");
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDTO> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toChannelDTO)
        .toList();
  }

  private ChannelDTO toChannelDTO(Channel channel) {
        /*
        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
        */
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      participantIds = readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(readStatus -> readStatus.getUser().getId())
          .toList();
    }

    return ChannelDTO.fromEntity(channel, lastMessageAt, participantIds);
  }
}
