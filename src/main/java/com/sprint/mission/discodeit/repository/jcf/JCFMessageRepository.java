package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private JCFMessageRepository(){}

    private static class SingletonHolder{
        private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
    }

    public static JCFMessageRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    // 메시지들의 정보를 저장할 Map
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public void deleteById(UUID messageId) {
        messages.remove(messageId);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }
}
