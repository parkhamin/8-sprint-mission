package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();
    String password = userCreateRequest.password();

    if (userRepository.existsByUsername(username)) { // username 중복 확인
      throw new IllegalArgumentException(username + " 사용자가 이미 존재합니다.");
    }

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException(email + " 사용자가 이미 존재합니다.");
    }

    BinaryContent profile = profileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          return binaryContentRepository.save(binaryContent);
        })
        .orElse(null);

    User user = new User(username, email, password, profile);
    User createdUser = userRepository.save(user);

    UserStatus userStatus = new UserStatus(createdUser, Instant.now());
    userStatusRepository.save(userStatus);

    return createdUser;
  }

  @Transactional(readOnly = true)
  @Override
  public UserDTO find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toUserDTO)
        .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));
  }

  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    String newPassword = userUpdateRequest.newPassword();

    if (newUsername != null && newUsername.equals(user.getUsername())) {
      if (userRepository.existsByUsername(newUsername)) { // username 중복 확인
        throw new IllegalArgumentException(newUsername + " 사용자가 이미 존재합니다.");
      }
    }

    if (newEmail != null && newEmail.equals(user.getEmail())) {
      if (userRepository.existsByEmail(newEmail)) {
        throw new IllegalArgumentException(newEmail + " 사용자가 이미 존재합니다.");
      }
    }

    BinaryContent newProfile = null;

    if (profileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = profileCreateRequest.get();
      if (user.getProfile() != null) {
        binaryContentRepository.delete(user.getProfile());
      }

      byte[] bytes = profileRequest.bytes();
      newProfile = new BinaryContent(
          profileRequest.fileName(),
          (long) bytes.length,
          profileRequest.contentType(),
          profileRequest.bytes()
      );

      binaryContentRepository.save(newProfile);
    }

    user.update(newUsername, newEmail, newPassword, newProfile);
    return user;
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));

    userRepository.deleteById(userId);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDTO> findAll() {
    return userRepository.findAll().stream()
        .map(user -> toUserDTO(user))
        .toList();
  }

  private UserDTO toUserDTO(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(userStatus -> userStatus.isOnline())
        .orElse(false);

    return UserDTO.fromEntity(user, online);
  }
}
