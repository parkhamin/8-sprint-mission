package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    // 사용자의 ID와 사용자의 정보를 담은 사용자 객체를 쌍으로 데이터를 저장하기 위해 Hash map 사용
    private final Map<UUID, User> users = new HashMap<>();

    private JCFUserService() {}

    private static class SingletonHolder {
        private static final JCFUserService INSTANCE = new JCFUserService();
    }

    public static JCFUserService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public User createUser(String userName, String email ,String password) {
        User user = new User(userName, email, password);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(UUID userId) {
        User user = users.get(userId);

        if (user == null) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        return user;
    }

    @Override
    public User updateUser(UUID userId, String newUserName, String newEmail, String newPassword) {
        User user = users.get(userId);

        if (user == null) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        user.update(newUserName, newEmail, newPassword);

        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        if (!users.containsKey(userId)) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
