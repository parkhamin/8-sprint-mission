package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(Message message);
    Message getMessage(UUID messageId);
    void updateMessage(UUID messageId, String newContent);
    void deleteMessage(UUID messageId);
    List<Message> getAllMessages();
}
