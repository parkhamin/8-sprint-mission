package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널의 ID와 채널의 정보를 담을 객체 hash map 생성
    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel createChannel(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public void updateChannel(UUID channelId, String newChannelName) {
        Channel channel = channels.get(channelId);

        if (channel != null) {
            channel.updateChannelName(newChannelName);
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channels.remove(channelId);
    }

    @Override
    public void sendMessage(UUID channelId, UUID messageId) {
        Channel channel = channels.get(channelId);

        if (channel != null) {
            channel.addMessage(channelId);
        }
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }
}
