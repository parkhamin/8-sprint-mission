package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
@Tag(name = "Auth Controller", description = "사용자 인증 API입니다.")
public class AuthController {

  private final AuthService authService;

  // 사용자는 로그인할 수 있다.
  // User login(LoginRequest loginRequest);
  @PostMapping(value = "/login")
  @Operation(summary = "로그인")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "사용자 정보 일치", content = @Content(schema = @Schema(implementation = User.class))),
      @ApiResponse(responseCode = "400", description = "비밀번호 불일치", content = @Content(examples = @ExampleObject(value = "비밀번호가 일치하지 않습니다."))),
      @ApiResponse(responseCode = "404", description = "사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{username} 사용자를 찾을 수 없습니다.")))
  })
  public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(user);
  }
}
