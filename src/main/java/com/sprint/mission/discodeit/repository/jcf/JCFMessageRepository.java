package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages;

    private JCFMessageRepository(){
        this.messages = new HashMap<>();
    }

    private static class SingletonHolder{
        private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
    }

    public static JCFMessageRepository getInstance(){
        return JCFMessageRepository.SingletonHolder.INSTANCE;
    }

    @Override
    public Message save(Message message) {
        this.messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.ofNullable(this.messages.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        this.messages.remove(id);
    }

    @Override
    public List<Message> findAll() {
        return (List<Message>) this.messages.values();
    }
}
