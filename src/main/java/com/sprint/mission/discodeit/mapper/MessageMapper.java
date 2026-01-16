package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }

    UserDto authorDto = userMapper.toDto(message.getAuthor());

    List<BinaryContentDto> attachmentDtos = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        authorDto,
        attachmentDtos
    );
  }
}
