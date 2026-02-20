package com.sprint.mission.discodeit.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserInvalidLoginException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicAuthService basicAuthService;

  UUID userId;
  User user;
  UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    user = new User("testUser", "test@naver.com", "test1234", null);
    userDto = new UserDto(userId, "testUser", "test@naver.com", null, true);
  }

  @Test
  @DisplayName("로그인 성공인 경우")
  void login_ShouldReturnUserDto() {

    // given
    LoginRequest loginReq = new LoginRequest("testUser", "test1234");
    given(userRepository.findByUsername(eq(user.getUsername()))).willReturn(Optional.of(user));
    given(userMapper.toDto(user)).willReturn(userDto);

    // when
    UserDto result = basicAuthService.login(loginReq);

    // then
    assertThat(result).isEqualTo(userDto);
  }

  @Test
  @DisplayName("로그인 실패 - 존재하지 않는 사용자인 경우")
  void login_WhenNotFoundUser_ThrowException() {

    // given
    LoginRequest loginReq = new LoginRequest("nonExistUser", "test1234");
    given(userRepository.findByUsername(eq("nonExistUser"))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicAuthService.login(loginReq))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호가 일치하지 않을 경우")
  void login_WhenWrongPassword_ThrowException() {

    // given
    LoginRequest loginReq = new LoginRequest("testUser", "0000000");
    given(userRepository.findByUsername(eq("testUser"))).willReturn(Optional.of(user));

    // when & then
    assertThatThrownBy(() -> basicAuthService.login(loginReq))
        .isInstanceOf(UserInvalidLoginException.class);

    then(userRepository).should().findByUsername(anyString());
    then(userMapper).shouldHaveNoInteractions();
  }
}
