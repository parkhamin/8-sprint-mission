package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel); // 채널 생성
    Channel getChannel(UUID channelId); // 채널 조회
    void updateChannel(UUID channelId, String newChannelName);
    void deleteChannel(UUID channelId);
    void sendMessage(UUID channelId, UUID messageId);
    List<Channel> getAllChannels(); //

    void addUserToChannel(UUID channelId, UUID userId);
    void removeUserFromChannel(UUID channelId, UUID userId);
}
