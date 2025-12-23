package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUserName(String userName);
    void deleteById(UUID id);
    List<User> findAll();
    boolean existByUserName(String userName);
    boolean existByEmail(String email);
    boolean existById(UUID id);
}
