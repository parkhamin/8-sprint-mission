package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Transactional
  @Override
  public UserStatusDto create(UserStatusCreateRequest userStatusCreateRequest) {
    User user = userRepository.findById(userStatusCreateRequest.userId())
        .orElseThrow(() -> new NoSuchElementException(
            userStatusCreateRequest.userId() + " 사용자를 찾을 수 없습니다."));
    Instant lastActiveAt = userStatusCreateRequest.lastActiveAt();

    if (userStatusRepository.findByUserId(user.getId()).isPresent()) {
      throw new IllegalArgumentException(user.getId() + "의 userStatus가 이미 존재합니다.");
    }

    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);

    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException(userStatusId + " UserStatus를 찾을 수 없습니다."));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException(userStatusId + "UserStatus를 찾을 수 없습니다."));

    Instant newLastActiveAt = userStatusUpdateRequest.newLastActiveAt();

    userStatus.update(newLastActiveAt);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + "사용자를 찾을 수 없습니다."));

    Instant newLastActiveAt = userStatusUpdateRequest.newLastActiveAt();

    userStatus.update(newLastActiveAt);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new NoSuchElementException(userStatusId + "UserStatus를 찾을 수 없습니다."));

    userStatusRepository.deleteById(userStatusId);
  }
}
