package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    private JCFUserRepository(){
        this.users = new HashMap<>();
    }

    private static class SingletonHolder{
        private static final JCFUserRepository INSTANCE = new JCFUserRepository();
    }

    public static JCFUserRepository getInstance(){
        return SingletonHolder.INSTANCE;
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
    public void deleteById(UUID id) {
        this.users.remove(id);
    }

    @Override
    public List<User> findAll() {
        return (List<User>) this.users.values();
    }
}
