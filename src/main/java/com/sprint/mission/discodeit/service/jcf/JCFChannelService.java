package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널의 ID와 채널의 정보를 담을 객체 hash map 생성
    private final Map<UUID, Channel> channels = new HashMap<>();
    private final UserService userService;

    private JCFChannelService(UserService userService) {
        this.userService = userService;
    }

    private static class SingletonHolder {
        private static final JCFChannelService INSTANCE = new JCFChannelService(JCFUserService.getInstance());
    }

    public static JCFChannelService getInstance() {
        return JCFChannelService.SingletonHolder.INSTANCE;
    }

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

    @Override
    public void addUserToChannel(UUID userId, UUID channelId) {
        Channel channel = channels.get(channelId);
        User user = userService.getUser(userId);

        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        if (user == null) throw new IllegalArgumentException("사용자가 존재하지 않습니다.");

        channel.addUser(userId);
    }

    @Override
    public void removeUserFromChannel(UUID userId, UUID channelId) {
        Channel channel = channels.get(channelId);
        User user = userService.getUser(userId);

        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        if (user == null) throw new IllegalArgumentException("사용자가 존재하지 않습니다.");

        channel.removeUser(userId);
    }
}
