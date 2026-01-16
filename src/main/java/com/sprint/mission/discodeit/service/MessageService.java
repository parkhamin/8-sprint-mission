package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDto find(UUID messageId);

  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void delete(UUID messageId);

  List<MessageDto> findAllByChannelId(UUID channelId);
}
