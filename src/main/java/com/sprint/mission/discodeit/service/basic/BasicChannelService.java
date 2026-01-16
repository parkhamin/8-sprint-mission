package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest channelCreateRequest) {
    String name = channelCreateRequest.name();
    String description = channelCreateRequest.description();

    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest channelCreateRequest) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);

    List<ReadStatus> readStatusList = userRepository.findAllById(
            channelCreateRequest.participantIds())
        .stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();

    readStatusRepository.saveAll(readStatusList);
    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest) {
    String newName = channelUpdateRequest.newName();
    String newDescription = channelUpdateRequest.newDescription();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다."));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("타입이 private인 채널은 수정할 수 없습니다.");
    }

    channel.update(newName, newDescription);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException(channelId + " 채널을 찾을 수 없습니다.");
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    channelRepository.deleteById(channelId);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAllPublicOrSubscribed(mySubscribedChannelIds).stream()
        .map(channelMapper::toDto)
        .toList();
  }
}
