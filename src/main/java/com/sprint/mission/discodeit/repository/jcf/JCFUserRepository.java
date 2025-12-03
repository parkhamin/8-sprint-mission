package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private JCFUserRepository(){}

    private static class SingletonHolder{
        private static final JCFUserRepository INSTANCE = new JCFUserRepository();
    }

    public static JCFUserRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    // User 정보를 저장할 Map
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        users.remove(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
