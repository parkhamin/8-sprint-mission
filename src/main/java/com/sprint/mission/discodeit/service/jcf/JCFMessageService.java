package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    // 메시지의 ID와 메시지 객체를 담을 객체 hash map 생성
    private final Map<UUID, Message> messages = new HashMap<>();
    private final UserService userService;
    private final ChannelService channelService;

    private JCFMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    private static class SingletonHolder {
        private static final JCFMessageService INSTANCE  =  new JCFMessageService(JCFUserService.getInstance(), JCFChannelService.getInstance());
    }

    public static JCFMessageService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public Message createMessage(String messageContent, UUID userId, UUID channelId) {
        if (userService.getUser(userId) == null) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        if (channelService.getChannel(channelId) == null) throw new IllegalArgumentException("채널을 찾을 수 없습니다.");

        Message message = new Message(messageContent, userId, channelId);
        messages.put(message.getId(), message);
        return  message;
    }

    @Override
    public Message getMessage(UUID messageId) {
        Message message = messages.get(messageId);

        if (message == null) throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        return message;
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        Message message = messages.get(messageId);

        if (message == null) throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        message.update(newContent);
        return message;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messages.get(messageId);

        if (message == null) throw new IllegalArgumentException("메시지를 찾을 수 없습니다.");
        messages.remove(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }
}
