package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateRequest userStatusCreateRequest) {
        UUID userId = userStatusCreateRequest.userId();
        Instant lastConnectedAt = userStatusCreateRequest.lastConnectAt();

        if (!userRepository.existById(userId)) {
            throw new IllegalArgumentException(userId + " 사용자를 찾을 수 없습니다.");
        }

        if (userStatusRepository.existsById(userId)) {
            throw new IllegalArgumentException(userId + "의 userStatus가 이미 존재합니다.");
        }

        UserStatus userStatus = new UserStatus(userId, lastConnectedAt);
        userStatusRepository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException(userStatusId + " UserStatus를 찾을 수 없습니다."));
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusRepository.findAll().stream().toList();
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
        Instant newLastConnectAt = userStatusUpdateRequest.newLastConnectAt();

        UserStatus userStatus = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException(userStatusId + "UserStatus를 찾을 수 없습니다."));

        userStatus.update(newLastConnectAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest) {
        Instant newLastConnectAt = userStatusUpdateRequest.newLastConnectAt();

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + "사용자를 찾을 수 없습니다."));

        userStatus.update(newLastConnectAt);
        return userStatusRepository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        if (!userStatusRepository.existsById(userStatusId)) {
            throw new NoSuchElementException(userStatusId + " UserStatus를 찾을 수 없습니다.");
        }
        userStatusRepository.deleteById(userStatusId);
    }
}
