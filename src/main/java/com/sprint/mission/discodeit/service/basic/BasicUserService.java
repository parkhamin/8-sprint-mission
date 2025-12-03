package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository; // user를 저장할 레포지토리

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(UUID userId) {
        User user = userRepository.findById(userId);

        if (user == null) throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        return user;
    }

    @Override
    public void updateUser(UUID userId, String newUserName) {
        User user = getUser(userId); // 없는 사용자면 이미 getUser에서 오류가 떴을 것! 즉, 이 메소드에서는 항상 user가 null이 아님// user.updateUserName(newUserName);
        user.updateUserName(newUserName);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        //User user = userRepository.findById(userId);
        User user = getUser(userId);
        userRepository.deleteById(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
