package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages;

    public JCFMessageRepository(){
        this.messages = new HashMap<>();
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
    public List<Message> findAllByChannelId(UUID channelId) {
        return messages.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        this.messages.remove(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.messages.containsKey(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId).stream()
                .forEach(message -> this.deleteById(message.getId()));
    }
}
