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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    log.info("[UserStateService] 사용자 온라인 상태 생성 시작 - 사용자 Id: {}", userStatusCreateRequest.userId());

    User user = userRepository.findById(userStatusCreateRequest.userId())
        .orElseThrow(() -> {
          log.warn("[UserStateService] 사용자 온라인 상태 생성 실패 - 존재하지 않는 사용자 Id: {}",
              userStatusCreateRequest.userId());
          return new NoSuchElementException(
              userStatusCreateRequest.userId() + " 사용자를 찾을 수 없습니다.");
        });
    Instant lastActiveAt = userStatusCreateRequest.lastActiveAt();

    if (userStatusRepository.findByUserId(user.getId()).isPresent()) {
      log.warn("[UserStateService] 사용자 온라인 상태 생성 실패 - 이미 존재하는 사용자 온라인 상태: {}",
          user.getId());
      throw new IllegalArgumentException(user.getId() + "의 userStatus가 이미 존재합니다.");
    }

    UserStatus userStatus = new UserStatus(user, lastActiveAt);
    userStatusRepository.save(userStatus);

    log.info("[UserStatusService] 사용자 온라인 상태 생성 성공 - Id: {}", userStatus.getId());
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    log.debug("[UserStatusService] 사용자 온라인 상태 조회 시작 - Id: {}", userStatusId);

    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> {
          log.warn("[UserStatusService] 사용자 온라인 상태 조회 실패 - 존재하지 않는 Id: {}", userStatusId);
          return new NoSuchElementException(userStatusId + " UserStatus를 찾을 수 없습니다.");
        });
  }

  @Override
  public List<UserStatusDto> findAll() {
    log.debug("[UserStatusService] 전체 사용자의 온라인 상태 조회 시작");

    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest) {
    log.info("[UserStatusService] 사용자 온라인 상태 수정 시작 - 사용자 상태 Id: {}", userStatusId);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> {
          log.warn("[UserStatusService] 사용자 온라인 상태 수정 - 존재하지 않는 사용자 상태 Id: {}", userStatusId);
          return new NoSuchElementException(userStatusId + "UserStatus를 찾을 수 없습니다.");
        });

    Instant newLastActiveAt = userStatusUpdateRequest.newLastActiveAt();

    userStatus.update(newLastActiveAt);

    log.info("[UserStatusService] 사용자 온라인 상태 수정 완료 - 사용자 상태 Id: {}", userStatusId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public UserStatusDto updateByUserId(UUID userId,
      UserStatusUpdateRequest userStatusUpdateRequest) {
    log.info("[UserStatusService] userId 기반 사용자 온라인 상태 수정 시작 - 사용자 Id: {}", userId);

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> {
          log.warn("[UserStatusService] userId 기반 사용자 온라인 상태 수정 실패 - 존재하지 않는 사용자 Id: {}", userId);
          return new NoSuchElementException(userId + "사용자를 찾을 수 없습니다.");
        });

    Instant newLastActiveAt = userStatusUpdateRequest.newLastActiveAt();

    userStatus.update(newLastActiveAt);

    log.info("[UserStatusService] userId 기반 사용자 온라인 상태 수정 완료 - 사용자 Id: {}", userId);
    return userStatusMapper.toDto(userStatus);
  }

  @Transactional
  @Override
  public void delete(UUID userStatusId) {
    log.info("[UserStatusService] 사용자 온라인 상태 삭제 시작 - Id: {}", userStatusId);

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> {
          log.warn("[UserStatusService] 사용자 온라인 상태 삭제 실패 - 존재하지 않는 Id: {}", userStatusId);
          return new NoSuchElementException(userStatusId + "UserStatus를 찾을 수 없습니다.");
        });

    userStatusRepository.deleteById(userStatusId);
    log.info("[UserStatusService] 사용자 온라인 상태 삭제 완료 - Id: {}", userStatus.getId());
  }
}
