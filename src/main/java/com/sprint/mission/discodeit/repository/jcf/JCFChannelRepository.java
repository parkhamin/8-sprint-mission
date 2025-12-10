package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> channels;

    private JCFChannelRepository(){
        this.channels = new HashMap<>();
    }

    private static class SingletonHolder{
        private static final JCFChannelRepository INSTANCE = new JCFChannelRepository();
    }

    public static JCFChannelRepository getInstance(){
        return SingletonHolder.INSTANCE;
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
        return (List<Channel>) this.channels.values();
    }
}
