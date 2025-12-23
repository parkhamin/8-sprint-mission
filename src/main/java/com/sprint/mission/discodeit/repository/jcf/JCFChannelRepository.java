package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels;

    public JCFChannelRepository(){
        this.channels = new HashMap<>();
    }

    @Override
    public Channel save(Channel channel) {
        this.channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(this.channels.get(id));
    }

    @Override
    public void deleteById(UUID id) {
        this.channels.remove(id);
    }

    @Override
    public List<Channel> findAll() {
        return this.channels.values().stream().toList();
    }

    @Override
    public boolean existById(UUID id) {
        return this.channels.containsKey(id);
    }
}
