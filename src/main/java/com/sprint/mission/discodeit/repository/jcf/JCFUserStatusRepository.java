package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatuses;

    public JCFUserStatusRepository() {
        this.userStatuses = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatuses.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatuses.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return this.findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findAny();
    }

    @Override
    public boolean existsById(UUID id) {
        return this.userStatuses.containsKey(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.findByUserId(userId).ifPresent(foundUserStatus -> this.deleteById(foundUserStatus.getId()) );
    }

    @Override
    public void deleteById(UUID id) {
        this.userStatuses.remove(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatuses.values().stream().toList();
    }
}
