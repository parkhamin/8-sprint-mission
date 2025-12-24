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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자를 등록할 수 있다.
    // User create(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    @RequestMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> create(
            @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
        ) {
        Optional<BinaryContentCreateRequest> profileCreateRequest =
                Optional.ofNullable(profile)
                        .filter(p -> !p.isEmpty())
                        .map(BinaryContentCreateRequest::fileToRequest);

        User user = userService.create(userCreateRequest, profileCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    // 사용자 정보를 수정할 수 있다.
    // User update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileUpdateRequest);
    @RequestMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> update(
            @RequestParam("userId") UUID userId,
            @RequestPart("userUpdateRequest")UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile
            ) {
        Optional<BinaryContentCreateRequest> profileUpdateRequest =
                Optional.ofNullable(profile)
                        .filter(p -> !p.isEmpty())
                        .map(BinaryContentCreateRequest::fileToRequest);

        User user = userService.update(userId, userUpdateRequest, profileUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    // 사용자를 삭제할 수 있다.
    // void delete(UUID userId);
    @RequestMapping(value = "/delete")
    public ResponseEntity<Void> delete(@RequestParam("userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build(); // 응답 바디가 없지만 ResponseEntity를 만들 때 build() 사용
    }

    // 모든 사용자를 조회할 수 있다.
    // List<UserDTO> findAll();
    @RequestMapping(value = "/findAll")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> userDTOList = userService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDTOList);
    }

    // 사용자의 온라인 상태를 업데이트할 수 있다.
    // UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    @RequestMapping(value = "/updateByUserId")
    public ResponseEntity<UserStatus> updateByUserId(
            @RequestParam("userId") UUID userId,
            @RequestBody UserStatusUpdateRequest userStatusUpdateRequest
            ) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userStatus);
    }
}
