package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("User 통합 테스트")
public class UserIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("사용자 생성 성공")
  void createUser_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");

    MockMultipartFile userPart = new MockMultipartFile("userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userReq));

    MockMultipartFile profilePart = new MockMultipartFile(
        "profile",
        "profile.png",
        "image/png",
        "profile".getBytes(StandardCharsets.UTF_8));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .file(profilePart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.email").value("test@naver.com"))
        .andExpect(jsonPath("$.profile.fileName").value("profile.png"))
        .andExpect(jsonPath("$.online").value(true));
  }

  @Test
  @DisplayName("사용자 생성 실패")
  void createUser_Failed() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "no-email-format", "test1234");

    MockMultipartFile userPart = new MockMultipartFile("userCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userReq));

    // when & then
    mockMvc.perform(multipart("/api/users")
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("사용자 수정 성공")
  void updateUser_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto user = userService.create(userReq, Optional.empty());
    UUID userId = user.id();

    // given
    UserUpdateRequest userUpdateReq = new UserUpdateRequest("updateUser", "update@naver.com",
        "update1234");

    MockMultipartFile userPart = new MockMultipartFile("userUpdateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userUpdateReq));

    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", userId)
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("updateUser"))
        .andExpect(jsonPath("$.email").value("update@naver.com"));
  }

  @Test
  @DisplayName("사용자 수정 실패")
  void updateUser_Failed() throws Exception {

    // given
    UUID nonExistUserId = UUID.randomUUID();

    // given
    UserUpdateRequest userUpdateReq = new UserUpdateRequest("updateUser", "update@naver.com",
        "update1234");

    MockMultipartFile userPart = new MockMultipartFile("userUpdateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(userUpdateReq));

    // when & then
    mockMvc.perform(multipart("/api/users/{userId}", nonExistUserId)
            .file(userPart)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
              request.setMethod("PATCH");
              return request;
            }))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("사용자 삭제 성공")
  void deleteUser_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto user = userService.create(userReq, Optional.empty());
    UUID userId = user.id();

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("존재하지 않는 사용자 Id를 삭제하려고 할 시 예외가 발생해야 한다.")
  void deleteUser_Failed() throws Exception {

    // given
    UUID nonExistsId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/users/{userId}", nonExistsId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("전체 사용자 목록 조회")
  void findAllUsers_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    userService.create(userReq, Optional.empty());

    UserCreateRequest userReq2 = new UserCreateRequest("testUser2", "test2@naver.com", "test1234");
    userService.create(userReq2, Optional.empty());

    // when & then
    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username").value("testUser"))
        .andExpect(jsonPath("$[1].username").value("testUser2"))
        .andExpect(jsonPath("$[0].email").value("test@naver.com"))
        .andExpect(jsonPath("$[1].email").value("test2@naver.com"));
  }
}
