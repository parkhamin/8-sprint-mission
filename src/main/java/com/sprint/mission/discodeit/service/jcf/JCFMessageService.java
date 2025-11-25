package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    // 메시지의 ID와 메시지 객체를 담을 객체 hash map 생성
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public Message createMessage(Message message) {
        messages.put(message.getId(), message);
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
