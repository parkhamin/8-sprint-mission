package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class, UserMapper.class})
public interface MessageMapper {

  // source(Message 엔티티에 있는 Channel 객체 안에 id 값)을 MessageDto 중 channelId 필드값에 꽂아줘.
  @Mapping(target = "channelId", source = "channel.id")
  MessageDto toDto(Message message);
}
