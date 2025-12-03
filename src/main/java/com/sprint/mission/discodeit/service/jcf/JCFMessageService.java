package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    // 메시지의 ID와 메시지 객체를 담을 객체 hash map 생성
    private final Map<UUID, Message> messages = new HashMap<>();
    private final UserService userService;
    private ChannelService channelService;

    public JCFMessageService(UserService userService) {
        this.userService = userService;
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        if (userService.getUser(message.getSender()) == null) throw new IllegalArgumentException("보내려는 사용자가 존재하지 않습니다.");
        Channel channel = channelService.getChannel(message.getChannelId());
        if (channel == null)
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        if (!channel.getUsers().contains(message.getSender()))
            throw new IllegalArgumentException("메시지를 보내려는 사용자가 채널에 참가하지 않았습니다.");

        messages.put(message.getId(), message);
        channel.addMessage(message.getId());
        return message;
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = messages.get(messageId);

        if (message != null) {
            message.updateContent(newContent);
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messages.remove(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }
}
