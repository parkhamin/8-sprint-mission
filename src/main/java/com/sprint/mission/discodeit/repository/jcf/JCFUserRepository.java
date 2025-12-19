package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    public JCFUserRepository(){
        this.users = new HashMap<>();
    }

    @Override
    public User save(User user) {
        this.users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(this.users.get(id));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return users.values().stream()
                .filter(user -> user.getUserName().equals(userName)).findAny();
    }

    @Override
    public void deleteById(UUID id) {
        this.users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return this.users.values().stream().toList();
    }

    @Override
    public boolean existByUserName(String userName) {
        return this.findAll().stream().anyMatch(user -> user.getUserName().equals(userName));
    }

    @Override
    public boolean existByEmail(String email) {
        return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existById(UUID id) {
        return users.containsKey(id);
    }
}
