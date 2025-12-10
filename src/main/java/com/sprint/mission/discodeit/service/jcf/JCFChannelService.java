package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널의 ID와 채널의 정보를 담을 객체 hash map 생성
    private final Map<UUID, Channel> channels = new HashMap<>();

    public JCFChannelService() {}

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel getChannel(UUID channelId) {
        Channel channel = channels.get(channelId);

        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        return channel;
    }

    @Override
    public Channel updateChannel(UUID channelId, String newChannelName) {
        Channel channel = channels.get(channelId);

        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        channel.updateChannelName(newChannelName);

        return channel;
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (!channels.containsKey(channelId)) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        channels.remove(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }
}
