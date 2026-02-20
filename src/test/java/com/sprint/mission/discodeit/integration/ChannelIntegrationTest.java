package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DisplayName("Channel 통합 테스트")
public class ChannelIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("공개 채널 생성 성공")
  void createPublicChannel_Success() throws Exception {

    // given
    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다");

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelReq)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("[공지]"))
        .andExpect(jsonPath("$.description").value("공지 채널입니다"));
  }

  @Test
  @DisplayName("공개 채널 생성 실패")
  void createPublicChannel_Failed() throws Exception {

    // given
    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("1", "공지 채널입니다");

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelReq)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("비공개 채널 생성 성공")
  void createPrivateChannel_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto user = userService.create(userReq, Optional.empty());

    UserCreateRequest userReq2 = new UserCreateRequest("testUser2", "test2@naver.com", "test1234");
    UserDto user2 = userService.create(userReq2, Optional.empty());

    PrivateChannelCreateRequest channelReq = new PrivateChannelCreateRequest(
        List.of(user.id(), user2.id()));

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelReq)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.participants", hasSize(2)));
  }

  @Test
  @DisplayName("비공개 채널 생성 실패")
  void createPrivateChannel_Failed() throws Exception {
    PrivateChannelCreateRequest channelReq = new PrivateChannelCreateRequest(List.of());

    // when & then
    mockMvc.perform(post("/api/channels/private")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelReq)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("공개 채널 수정 성공")
  void updateChannel_Success() throws Exception {

    // given
    PublicChannelCreateRequest channelCreateReq = new PublicChannelCreateRequest("[공지]",
        "공지 채널입니다");
    ChannelDto channel = channelService.create(channelCreateReq);
    UUID channelId = channel.id();

    PublicChannelUpdateRequest channelUpdateReq = new PublicChannelUpdateRequest("[수정공지]", null);

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelUpdateReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(channelId.toString()))
        .andExpect(jsonPath("$.name").value("[수정공지]"))
        .andExpect(jsonPath("$.description").value("공지 채널입니다"));
  }

  @Test
  @DisplayName("공개 채널 수정 실패")
  void updateChannel_Failed() throws Exception {

    // given
    UUID nonExistChannelId = UUID.randomUUID();
    PublicChannelCreateRequest channelCreateReq = new PublicChannelCreateRequest("[공지]",
        "공지 채널입니다");

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", nonExistChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(channelCreateReq)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_Success() throws Exception {

    // given
    PublicChannelCreateRequest channelCreateReq = new PublicChannelCreateRequest("[공지]",
        "공지 채널입니다");
    ChannelDto channel = channelService.create(channelCreateReq);
    UUID channelId = channel.id();

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("채널 삭제 실패")
  void deleteChannel_Failed() throws Exception {

    // given
    UUID nonExistChannelId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/channels/{channelId}", nonExistChannelId))
        .andExpect(status().isNotFound());
  }
}
