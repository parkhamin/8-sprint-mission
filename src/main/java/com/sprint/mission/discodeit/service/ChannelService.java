package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(ChannelType type, String channelName, String description); // 채널 생성
    Channel getChannel(UUID channelId); // 채널 조회
    Channel updateChannel(UUID channelId, String newChannelName, String newDescription);
    void deleteChannel(UUID channelId);
    List<Channel> getAllChannels();
}
