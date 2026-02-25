package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest channelCreateRequest);

  ChannelDto create(PrivateChannelCreateRequest channelCreateRequest);

  ChannelDto find(UUID channelId); // 채널 조회

  ChannelDto update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest);

  void delete(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);
}
