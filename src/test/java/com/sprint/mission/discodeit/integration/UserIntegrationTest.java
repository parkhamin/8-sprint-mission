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
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
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

  private BinaryContent savedProfile;
  private User savedUser;

  @BeforeEach
  void setUp() {
    BinaryContent profile = new BinaryContent("profile.png", 10L, "image/png");
    savedProfile = binaryContentRepository.save(profile);

    User user = new User("통합테스트회원", "integration@naver.com", "integration1234", null);
    UserStatus status = new UserStatus(user, Instant.now());
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
  @DisplayName("사용자 조회가 올바르게 동작해야 한다.")
  void findUser_TransactionCommit_Success() {

    // when
    UserDto result = userService.find(savedUser.getId());

    // then
    assertThat(result.id()).isEqualTo(savedUser.getId());
    assertThat(result.username()).isEqualTo(savedUser.getUsername());
    assertThat(result.email()).isEqualTo(savedUser.getEmail());
    assertThat(result.online()).isTrue();
  }

  @Test
  @DisplayName("존재하지 않는 사용자 Id로 조회 시 트랜잭션이 롤백되어야 한다.")
  void findUser_NonExitsId_TransactionRollback() {

    // given
    UUID uuid = UUID.randomUUID();

    // when & then
    assertThatThrownBy(() -> userService.find(uuid))
        .isInstanceOf(UserNotFoundException.class);
  }
}
