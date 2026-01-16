package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      participants = readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(readStatus -> userMapper.toDto(readStatus.getUser()))
          .toList();
    }

    Instant lastMessageAt = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            channel.getId())
        .map(Message::getCreatedAt)
        .orElse(Instant.now());

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        lastMessageAt
    );
  }
}
