package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // 채널의 ID와 채널의 정보를 담을 객체 hash map 생성
    private final Map<UUID, Channel> channels = new HashMap<>();
    private final MessageService messageService;

    public JCFChannelService(MessageService messageService) {
        this.messageService = messageService;
    }

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
        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");

        Message message = messageService.getMessage(messageId);
        if (message == null) throw new IllegalArgumentException("보내려는 메시지가 존재하지 않습니다.");
        if (!channel.getUsers().contains(message.getSender())) throw new IllegalArgumentException("보내려는 사용자가 존재하지 않습니다.");
        channel.addMessage(messageId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        if (channel == null) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        channel.addUser(userId);
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        if (channel == null) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        channel.removeUser(userId);
    }
}
