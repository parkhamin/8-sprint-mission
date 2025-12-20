package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatuses;

    public JCFReadStatusRepository() {
        this.readStatuses = new HashMap<>();
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(this.readStatuses.get(id));
    }

    @Override
    public boolean existsById(UUID id) {
        return readStatuses.containsKey(id);
    }

    @Override
    public void deleteById(UUID id) {
        readStatuses.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(readStatus -> this.deleteById(readStatus.getId()));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatuses.values().stream()
                .filter(readStatus -> readStatus.getId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatuses.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }
}
