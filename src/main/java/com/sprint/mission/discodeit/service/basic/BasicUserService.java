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

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        String userName = userCreateRequest.userName();
        String email = userCreateRequest.email();
        String password = userCreateRequest.password();

        if (userRepository.existByUserName(userName)){ // userName 중복 확인
            throw new IllegalArgumentException(userName + " 사용자가 이미 존재합니다.");
        }

        if (userRepository.existByEmail(email)){
            throw new IllegalArgumentException(email + " 사용자가 이미 존재합니다.");
        }

        UUID nullableProfileId = profileCreateRequest
                .map(profileRequest -> {
                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        User user = new User(userName, email, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(createdUser.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return createdUser;
    }

    @Override
    public UserDTO find(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> toUserDTO(user))
                .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));
    }

    @Override
    public User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));

        String newUserName = userUpdateRequest.newUserName();
        String newEmail = userUpdateRequest.newEmail();
        String newPassword = userUpdateRequest.newPassword();

        if (userRepository.existByUserName(newUserName)){ // userName 중복 확인
            throw new IllegalArgumentException(newUserName + " 사용자가 이미 존재합니다.");
        }

        if (userRepository.existByEmail(newEmail)){
            throw new IllegalArgumentException(newEmail + " 사용자가 이미 존재합니다.");
        }

        UUID nullableProfileId = profileCreateRequest
                .map(profileRequest -> {
                    Optional.ofNullable(user.getProfileId())
                            .ifPresent(binaryContentRepository::deleteById); // 프로필을 업데이트 하기 전 기존 프로필 제거

                    String fileName = profileRequest.fileName();
                    String contentType = profileRequest.contentType();
                    byte[] bytes = profileRequest.bytes();
                    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType, bytes);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);

        user.update(newUserName, newEmail, newPassword, nullableProfileId);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException(userId + " 사용자를 찾을 수 없습니다."));

        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> list = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            UserDTO userDTO = toUserDTO(user);
            list.add(userDTO);
        }
        return list;
    }

    private UserDTO toUserDTO(User user) {
        Boolean online = userStatusRepository.findByUserId(user.getId())
                .map(userStatus -> userStatus.isOnline())
                .orElse(null);

        return new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
