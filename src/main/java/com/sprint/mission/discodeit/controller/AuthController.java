package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;

  @PostMapping(value = "/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
    log.info("[AuthController] 로그인 요청 - 이름: {}", loginRequest.username());

    UserDto user = authService.login(loginRequest);

    log.info("[AuthController] 로그인 완료 - 이름: {}", user.username());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }
}
