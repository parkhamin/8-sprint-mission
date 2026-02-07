package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("User 통합 테스트")
public class UserIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private UserService userService;

  @MockitoBean
  private BinaryContentStorage binaryContentStorage;

  private User savedUser;

  @BeforeEach
  void setUp() {
    BinaryContent profile = new BinaryContent("profile.png", 10L, "image/png");
    binaryContentRepository.save(profile);

    User user = new User("통합테스트회원", "integration@naver.com", "integration1234", profile);
    new UserStatus(user, Instant.now());
    savedUser = userRepository.save(user);
  }

  @Test
  @DisplayName("사용자 등록 시 트랜잭션이 올바르게 동작해야 한다.")
  void createUser_TransactionCommit_Success() {

    // given
    BinaryContentCreateRequest profileReq = new BinaryContentCreateRequest("myProfile.png",
        "image/png", new byte[]{1, 2, 3, 4, 5});
    UserCreateRequest userReq = new UserCreateRequest("newUser", "new@naver.com", "new1234");

    // when
    UserDto result = userService.create(userReq, Optional.of(profileReq));

    // then
    assertThat(result.username()).isEqualTo("newUser");
    assertThat(result.email()).isEqualTo("new@naver.com");

    User user = userRepository.findById(result.id()).orElseThrow();
    assertThat(user.getUsername()).isEqualTo("newUser");
    assertThat(user.getEmail()).isEqualTo("new@naver.com");

    BinaryContent profile = binaryContentRepository.findById(result.profile().id()).orElseThrow();
    assertThat(profile.getFileName()).isEqualTo("myProfile.png");

    verify(binaryContentStorage, times(1)).put(
        eq(user.getProfile().getId()), eq(profileReq.bytes()));
  }

  @Test
  @DisplayName("스토리지에 프로필 저장 실패 시 트랜잭션이 롤백되어야 한다.")
  void createUser_StorageFailed_TransactionRollback() {

    // given
    BinaryContentCreateRequest profileReq = new BinaryContentCreateRequest("myProfile.png",
        "image/png", new byte[]{1, 2, 3, 4, 5});
    UserCreateRequest userReq = new UserCreateRequest("newUser", "new@naver.com", "new1234");

    doThrow(new RuntimeException()).when(binaryContentStorage).put(any(), any());

    // when & then
    assertThatThrownBy(() ->
        userService.create(userReq, Optional.of(profileReq)))
        .isInstanceOf(BinaryContentSaveFailedException.class);

    Optional<User> result = userRepository.findByUsername("newUser");
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("사용자 수정 시 반영되어야 한다.")
  void updateUser_TransactionCommit_Success() {

    // given
    UserUpdateRequest userReq = new UserUpdateRequest("updateUser", "update@naver.com",
        "update1234");

    // when
    UserDto result = userService.update(savedUser.getId(), userReq, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("updateUser");
    assertThat(result.email()).isEqualTo("update@naver.com");

    User user = userRepository.findById(savedUser.getId()).orElseThrow();
    assertThat(user.getUsername()).isEqualTo("updateUser");
  }

  @Test
  @DisplayName("이미 존재하는 이름으로 수정 시 예외가 발생해야 한다.")
  void updateUser_DuplicateName_TransactionRollback() {

    // given
    User duplicateUser = new User("duplicateUser", "duplicate@naver.com", "duplicate1234", null);
    userRepository.save(duplicateUser);

    UserUpdateRequest userReq = new UserUpdateRequest("duplicateUser", "test@naver.com",
        "test1234");

    // when & then
    assertThatThrownBy(() -> userService.update(savedUser.getId(), userReq, Optional.empty()))
        .isInstanceOf(UsernameAlreadyExistsException.class);
  }

  @Test
  @DisplayName("사용자 삭제 시 더 이상 조회되지 않아야 한다.")
  void deleteUser_Success() {

    // when
    userService.delete(savedUser.getId());

    // then
    Optional<User> result = userRepository.findById(savedUser.getId());
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("존재하지 않는 사용자 Id를 삭제하려고 할 시 예외가 발생해야 한다.")
  void deleteUser_NotFound_Fail() {

    // given
    UUID nonExistsId = UUID.randomUUID();

    // when & then
    assertThatThrownBy(() -> userService.delete(nonExistsId))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("전체 사용자 목록을 조회하면 저장된 모든 사용자가 반환되어야 한다.")
  void findAllUsers_Success() {

    // given
    User savedUser2 = new User("통합테스트회원2", "integration2@naver.com", "integration1234", null);
    new UserStatus(savedUser2, Instant.now());
    userRepository.save(savedUser2);

    // when
    List<UserDto> result = userService.findAll();

    // then
    assertThat(result).hasSize(2);
    assertThat(result).extracting("username")
        .containsExactlyInAnyOrder("통합테스트회원", "통합테스트회원2");
  }

  @Test
  @DisplayName("사용자가 한 명도 저장되어 있지 않을 경우 전체 조회를 하면 빈 리스트가 반환되어야 한다.")
  void findAllUsers_Empty() {

    // given
    userRepository.deleteAll();

    // when
    List<UserDto> result = userService.findAll();

    // then
    assertThat(result).isEmpty();
  }
}
