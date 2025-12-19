package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel create(PublicChannelCreateRequest channelCreateRequest);
    Channel create(PrivateChannelCreateRequest channelCreateRequest);
    ChannelDTO find(UUID channelId); // 채널 조회
    Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest);
    void delete(UUID channelId);
    List<ChannelDTO> findAllByUserId(UUID userId);
}
