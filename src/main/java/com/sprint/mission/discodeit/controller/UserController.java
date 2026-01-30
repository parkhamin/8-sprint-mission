package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserApi {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 사용자를 등록할 수 있다.
  // User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileCreateRequest = toBinaryContentRequest(profile);

    UserDto user = userService.create(userCreateRequest, profileCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(user);
  }

  // 사용자 정보를 수정할 수 있다.
  // User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileUpdateRequest);
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileUpdateRequest = toBinaryContentRequest(profile);

    UserDto user = userService.update(userId, userUpdateRequest, profileUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }

  // 사용자를 삭제할 수 있다.
  // void delete(UUID userId);
  @DeleteMapping(value = "/{userId}")
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build(); // 응답 바디가 없지만 ResponseEntity를 만들 때 build() 사용
  }

  // 모든 사용자를 조회할 수 있다.
  // List<UserDTO> findAll();
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll() {
    List<UserDto> userDTOList = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDTOList);
  }

  // 사용자의 온라인 상태를 업데이트할 수 있다.
  // UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
  @PatchMapping(value = "/{userId}/userStatus")
  public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    UserStatusDto userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatus);
  }

  Optional<BinaryContentCreateRequest> toBinaryContentRequest(MultipartFile profile) {
    return Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .map(BinaryContentCreateRequest::fileFromRequest);
  }
}
