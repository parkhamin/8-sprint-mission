package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName); // 채널 생성
    Channel getChannel(UUID channelId); // 채널 조회
    Channel updateChannel(UUID channelId, String newChannelName);
    void deleteChannel(UUID channelId);
    List<Channel> getAllChannels();

    void addUserToChannel(UUID userId, UUID channelId);
    void removeUserFromChannel(UUID userId, UUID channelId);
}
