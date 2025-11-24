package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    // 사용자의 ID와 사용자의 정보를 담은 사용자 객체를 쌍으로 데이터를 저장하기 위해 Hash map 사용
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(UUID userId) {
        return users.get(userId);
    }

    @Override
    public void updateUser(UUID userId, String newUserName) {
        User user = users.get(userId); // 사용자의 ID로 사용자의 정보를 수정할 한 명의 사용자 객체를 가져와서 저장.

        /*if (users.containsKey(userId)) { // 만약 그 사용자의 ID가 hashmap에 있다면 (중복 검사 -> 불필요한 코드)
            user.updateUserName(newUserName); // 그 객체의 정보를 수정
        }*/
        if (user != null) { // 사용자의 ID를 hashmap에서 찾았다면 user 객체는 null이 아닐 것
            user.updateUserName(newUserName);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
