package com.sprint.mission.discodeit.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService basicUserService;

  UUID userId;
  String username;
  String email;
  String password;
  User mockUser;
  UserDto mockUserDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "testUser";
    email = "test@naver.com";
    password = "test1234";

    mockUser = new User(username, email, password, null);
    mockUserDto = new UserDto(userId, username, email, null, true);
  }

  @Nested
  @DisplayName("사용자 생성 관련 테스트")
  class UserCreateTests {

    @Test
    @DisplayName("사용자 생성 성공")
    void create_shouldReturnUserDto() {

      // given
      UserCreateRequest userReq = new UserCreateRequest(username, email, password);

      given(userRepository.existsByUsername(username)).willReturn(false);
      given(userRepository.existsByEmail(email)).willReturn(false);
      given(userMapper.toDto(any(User.class))).willReturn(mockUserDto);

      // when
      UserDto result = basicUserService.create(userReq, Optional.empty());

      // then
      assertThat(result).isEqualTo(mockUserDto);

      then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 - 중복된 이름인 경우")
    void create_whenDuplicateName_shouldThrowException() {

      // given
      UserCreateRequest userReq = new UserCreateRequest(username, email, password);

      given(userRepository.existsByUsername(userReq.username())).willReturn(true);

      // when
      assertThatThrownBy(() -> basicUserService.create(userReq, Optional.empty()))
          .isInstanceOf(UsernameAlreadyExistsException.class);

      // then
      then(userRepository).should(never()).save(any(User.class));
      then(userRepository).should(never()).existsByEmail(anyString());
    }
  }

  @Nested
  @DisplayName("사용자 수정 관련 테스트")
  class UserUpdateTests {

    @Test
    @DisplayName("사용자 수정 성공인 경우 ")
    void update_shouldReturnUserDto() {

      // given
      UserUpdateRequest userReq = new UserUpdateRequest("newTestUser", "newTestEmail@naver.com",
          "test1234");

      given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
      given(userRepository.existsByUsername(userReq.newUsername())).willReturn(false);
      given(userRepository.existsByEmail(userReq.newEmail())).willReturn(false);

      UserDto updatedDto = new UserDto(userId, userReq.newUsername(), userReq.newEmail(), null,
          true);

      given(userMapper.toDto(any(User.class))).willReturn(updatedDto);

      // when
      UserDto result = basicUserService.update(userId, userReq, Optional.empty());

      // then
      assertThat(result).isEqualTo(updatedDto);

      then(userRepository).should().findById(userId);
    }

    @Test
    @DisplayName("사용자 수정 실패 - 사용자를 찾지 못 했을 경우")
    void update_whenNotFoundUser_shouldThrowException() {

      // given
      UserUpdateRequest userReq = new UserUpdateRequest("newTestUser", "newTestEmail@naver.com",
          "test1234");

      // when & then
      given(userRepository.findById(userId)).willReturn(Optional.empty());
      assertThatThrownBy(() -> basicUserService.update(userId, userReq, Optional.empty()))
          .isInstanceOf(UserNotFoundException.class);
    }
  }

  @Nested
  @DisplayName("사용자 삭제 관련 테스트")
  class UserDeleteTests {

    @Test
    @DisplayName("사용자 삭제 성공인 경우")
    void delete_shouldSuccess() {

      // given
      given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

      // when
      basicUserService.delete(userId);

      // then
      then(userRepository).should().findById(userId);
      then(userRepository).should().deleteById(userId);
    }

    @Test
    @DisplayName("사용자 삭제 실패 - 사용자를 찾지 못 했을 경우")
    void delete_whenNotFoundUser_shouldThrowException() {

      // given
      given(userRepository.findById(userId)).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicUserService.delete(userId)).isInstanceOf(
          UserNotFoundException.class);

      then(userRepository).should(never()).delete(any(User.class));
    }
  }
}
