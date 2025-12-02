package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    // 저장로직 - CRUD
    Channel save(Channel channel);
    Channel findById(UUID channelId);
    void deleteById(UUID channelId);
    List<Channel> findAll();
}
