package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Controller", description = "사용자 API입니다.")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  // 사용자를 등록할 수 있다.
  // User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "사용자 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "사용자 생성 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "400", description = "중복된 정보를 가진 사용자 이미 존재해서 생성 불가", content = @Content(examples = @ExampleObject(value = "사용자가 이미 존재합니다."))),
  })
  public ResponseEntity<User> create(
      @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileCreateRequest =
        Optional.ofNullable(profile)
            .filter(p -> !p.isEmpty())
            .map(BinaryContentCreateRequest::fileFromRequest);

    User user = userService.create(userCreateRequest, profileCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(user);
  }

  // 사용자 정보를 수정할 수 있다.
  // User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileUpdateRequest);
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "사용자 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "400", description = "중복된 정보를 가진 사용자 이미 존재해서 생성 불가", content = @Content(examples = @ExampleObject(value = "사용자가 이미 존재합니다."))),
      @ApiResponse(responseCode = "404", description = "사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{userId} 사용자를 찾을 수 없습니다.")))
  })
  public ResponseEntity<User> update(
      @PathVariable UUID userId,
      @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) {
    Optional<BinaryContentCreateRequest> profileUpdateRequest =
        Optional.ofNullable(profile)
            .filter(p -> !p.isEmpty())
            .map(BinaryContentCreateRequest::fileFromRequest);

    User user = userService.update(userId, userUpdateRequest, profileUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }

  // 사용자를 삭제할 수 있다.
  // void delete(UUID userId);
  @DeleteMapping(value = "/{userId}")
  @Operation(summary = "사용자 정보 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "사용자 정보 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{userId} 사용자를 찾을 수 없습니다.")))
  })
  public ResponseEntity<Void> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build(); // 응답 바디가 없지만 ResponseEntity를 만들 때 build() 사용
  }

  // 모든 사용자를 조회할 수 있다.
  // List<UserDTO> findAll();
  @GetMapping
  @Operation(summary = "사용자 목록 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공", content = @Content(schema = @Schema(implementation = UserDTO.class))),
  })
  public ResponseEntity<List<UserDTO>> findAll() {
    List<UserDTO> userDTOList = userService.findAll();
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDTOList);
  }

  // 사용자의 온라인 상태를 업데이트할 수 있다.
  // UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
  @PatchMapping(value = "/{userId}/userStatus")
  @Operation(summary = "사용자 접속 상태 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자 접속 상태 정보 수정 성공", content = @Content(schema = @Schema(implementation = UserStatus.class))),
      @ApiResponse(responseCode = "404", description = "사용자 접속 상태 조회 불가", content = @Content(examples = @ExampleObject(value = "해당 사용자의 접속 정보를 찾을 수 없습니다.")))
  })
  public ResponseEntity<UserStatus> updateUserStatusByUserId(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
  ) {
    UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userStatus);
  }
}
