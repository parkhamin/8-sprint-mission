package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String messageContent, UUID userId, UUID channelId);
    Message getMessage(UUID messageId);
    Message updateMessage(UUID messageId, String newContent);
    void deleteMessage(UUID messageId);
    List<Message> getAllMessages();
}
