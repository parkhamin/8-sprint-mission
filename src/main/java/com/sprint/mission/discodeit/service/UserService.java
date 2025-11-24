package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user); // 사용자 생성
    User getUser(UUID userId); // 특정 사용자 조회 -> 있는지 없는지
    void updateUser(UUID userId, String newUserName); // 사용자 수정
    void deleteUser(UUID userId); // 사용자 삭제
    List<User> getAllUsers(); // 모든 사용자 조회
}
