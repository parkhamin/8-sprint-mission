package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId);

        if (message == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        return message;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = getMessage(messageId);
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);
        messageRepository.deleteById(message.getId());
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
