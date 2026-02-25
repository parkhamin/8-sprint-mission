package com.sprint.mission.discodeit.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DisplayName("Message 통합 테스트")
public class MessageIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MessageService messageService;

  @Autowired
  private UserService userService;

  @Autowired
  private ChannelService channelService;

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto author = userService.create(userReq, Optional.empty());

    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다.");
    ChannelDto channel = channelService.create(channelReq);

    MessageCreateRequest messageReq = new MessageCreateRequest("안녕", channel.id(), author.id());

    MockMultipartFile messagePart = new MockMultipartFile("messageCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageReq));

    MockMultipartFile attachmentsPart = new MockMultipartFile("attachments",
        "attachment.png",
        "image/png",
        "attachment".getBytes());

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart)
            .file(attachmentsPart))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.content").value("안녕"))
        .andExpect(jsonPath("$.channelId").value(channel.id().toString()))
        .andExpect(jsonPath("$.author.id").value(author.id().toString()))
        .andExpect(jsonPath("$.attachments", hasSize(1)))
        .andExpect(jsonPath("$.attachments[0].fileName").value("attachment.png"));
  }

  @Test
  @DisplayName("메시지 생성 실패")
  void createMessage_Failed() throws Exception {

    // given
    MessageCreateRequest messageReq = new MessageCreateRequest(null, UUID.randomUUID(),
        UUID.randomUUID());

    MockMultipartFile messagePart = new MockMultipartFile("messageCreateRequest",
        "",
        "application/json",
        objectMapper.writeValueAsBytes(messageReq));

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(messagePart))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto author = userService.create(userReq, Optional.empty());

    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다.");
    ChannelDto channel = channelService.create(channelReq);

    MessageCreateRequest messageCreateReq = new MessageCreateRequest("안녕", channel.id(),
        author.id());
    MessageDto message = messageService.create(messageCreateReq, List.of());
    UUID messageId = message.id();

    MessageUpdateRequest messageUpdateReq = new MessageUpdateRequest("수정된 안녕");

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(messageUpdateReq)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(messageId.toString()))
        .andExpect(jsonPath("$.content").value("수정된 안녕"));
  }

  @Test
  @DisplayName("메시지 수정 실패")
  void updateMessage_Failed() throws Exception {

    // given
    UUID nonExistMessageId = UUID.randomUUID();
    MessageUpdateRequest messageUpdateReq = new MessageUpdateRequest("수정된 안녕");

    // when & then
    mockMvc.perform(patch("/api/messages/{channelId}", nonExistMessageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(messageUpdateReq)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void deleteMessage_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto author = userService.create(userReq, Optional.empty());

    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다.");
    ChannelDto channel = channelService.create(channelReq);

    MessageCreateRequest messageCreateReq = new MessageCreateRequest("안녕", channel.id(),
        author.id());
    MessageDto message = messageService.create(messageCreateReq, List.of());
    UUID messageId = message.id();

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("메시지 삭제 실패")
  void deleteMessage_Failed() throws Exception {

    // given
    UUID nonExistMessageId = UUID.randomUUID();

    // when & then
    mockMvc.perform(delete("/api/messages/{messageId}", nonExistMessageId))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("특정 채널의 메시지 목록 조회 성공")
  void findAllByChannelId_Success() throws Exception {

    // given
    UserCreateRequest userReq = new UserCreateRequest("testUser", "test@naver.com", "test1234");
    UserDto author = userService.create(userReq, Optional.empty());

    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다.");
    ChannelDto channel = channelService.create(channelReq);

    MessageCreateRequest messageCreateReq = new MessageCreateRequest("안녕", channel.id(),
        author.id());
    MessageCreateRequest messageCreateReq2 = new MessageCreateRequest("안녕2", channel.id(),
        author.id());
    messageService.create(messageCreateReq, List.of());
    messageService.create(messageCreateReq2, List.of());

    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channel.id().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content[0].content").value("안녕2"))
        .andExpect(jsonPath("$.content[1].content").value("안녕"))
        .andExpect(jsonPath("$.hasNext").value(false));
  }
}
