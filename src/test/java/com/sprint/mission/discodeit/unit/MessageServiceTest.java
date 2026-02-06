package com.sprint.mission.discodeit.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService basicMessageService;

  UUID channelId;
  UUID authorId;
  UUID messageId;
  Channel mockChannel;
  User mockAuthor;
  UserDto mockAuthorDto;
  Message mockMessage;
  MessageDto mockMessageDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();
    messageId = UUID.randomUUID();
    mockChannel = new Channel(ChannelType.PUBLIC, "test", "test channel");
    mockAuthor = new User("testUser", "testUser@naver.com", "test1234", null);
    mockAuthorDto = new UserDto(authorId, "testUser", "testUser@naver.com", null, true);
    mockMessage = new Message("안녕", mockChannel, mockAuthor, List.of());
    mockMessageDto = new MessageDto(messageId, Instant.now(), Instant.now(), "안녕", channelId,
        mockAuthorDto, List.of());
  }

  @Nested
  @DisplayName("메시지 생성 관련 테스트")
  class messageCreateTests {

    @Test
    @DisplayName("메시지 생성 성공인 경우")
    void create_shouldReturnMessageDto() {

      // given
      MessageCreateRequest messageReq = new MessageCreateRequest("안녕", channelId, authorId);

      given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(mockChannel));
      given(userRepository.findById(eq(authorId))).willReturn(Optional.of(mockAuthor));
      given(messageMapper.toDto(any(Message.class))).willReturn(mockMessageDto);

      // when
      MessageDto result = basicMessageService.create(messageReq, List.of());

      // then
      assertThat(result).isEqualTo(mockMessageDto);

      then(messageRepository).should().save(any(Message.class));
    }

    @Test
    @DisplayName("메시지 생성 실패 - 메시지를 보낸 사용자가 존재하지 않을 경우")
    void create_whenNotFoundAuthor_shouldThrowException() {

      // given
      MessageCreateRequest messageReq = new MessageCreateRequest("안녕", channelId, authorId);

      given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(mockChannel));
      given(userRepository.findById(eq(authorId))).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicMessageService.create(messageReq, List.of()))
          .isInstanceOf(UserNotFoundException.class);

      then(messageRepository).should(never()).save(any(Message.class));
    }
  }

  @Nested
  @DisplayName("메시지 수정 관련 테스트")
  class messageUpdateTests {

    @Test
    @DisplayName("메시지 수정 성공인 경우")
    void update_shouldReturnMessageDto() {

      // given
      MessageUpdateRequest messageReq = new MessageUpdateRequest("새로운 안녕");
      given(messageRepository.findById(eq(messageId))).willReturn(Optional.of(mockMessage));

      mockMessageDto = new MessageDto(messageId, Instant.now(), Instant.now(), "새로운 안녕", channelId,
          mockAuthorDto, List.of());
      given(messageMapper.toDto(any(Message.class))).willReturn(mockMessageDto);

      // when
      MessageDto result = basicMessageService.update(messageId, messageReq);

      // then
      assertThat(result).isEqualTo(mockMessageDto);

      then(messageRepository).should().findById(eq(messageId));
    }

    @Test
    @DisplayName("메시지 수정 실패 - 메시지가 존재하지 않을 경우")
    void update_whenMessageNotFound_shouldThrowException() {

      // given
      MessageUpdateRequest messageReq = new MessageUpdateRequest("새로운 안녕");
      given(messageRepository.findById(eq(messageId))).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicMessageService.update(messageId, messageReq))
          .isInstanceOf(MessageNotFoundException.class);

      then(messageRepository).should(never()).save(any(Message.class));
    }
  }

  @Nested
  @DisplayName("메시지 삭제 관련 테스트")
  class messageDeleteTests {

    @Test
    @DisplayName("메시지 삭제 성공인 경우")
    void delete_shouldSuccess() {
      // given
      given(messageRepository.findById(eq(messageId))).willReturn(Optional.of(mockMessage));

      // when
      basicMessageService.delete(messageId);

      // then
      then(messageRepository).should().delete(mockMessage);
      then(messageRepository).should().findById(eq(messageId));
    }

    @Test
    @DisplayName("메시지 삭제 실패 - 메시지가 존재하지 않을 경우")
    void delete_whenNotFoundMessage_shouldThrowException() {
      // given
      given(messageRepository.findById(eq(messageId))).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicMessageService.delete(messageId))
          .isInstanceOf(MessageNotFoundException.class);

      then(messageRepository).should(never()).delete(mockMessage);
    }
  }

  @Nested
  @DisplayName("메시지 조회 관련 테스트")
  class messageFindTests {

    @Test
    @DisplayName("특정 채널의 메시지 목록 조회 성공인 경우")
    void findByChannelId_shouldSuccess() {

      // given
      UUID cursor = UUID.randomUUID();
      UUID nextCursorId = UUID.randomUUID();
      Pageable pageable = PageRequest.of(0, 50);
      MessageDto mockDto = new MessageDto(nextCursorId, Instant.now(), Instant.now(), "content",
          channelId, mockAuthorDto, List.of());

      List<Message> messageList = List.of(mockMessage);
      Slice<Message> slice = new SliceImpl<>(messageList, pageable, true);
      List<MessageDto> dtoList = List.of(mockDto);

      given(messageRepository.findAllByCursor(eq(channelId), eq(cursor), eq(pageable))).willReturn(
          slice);
      given(messageMapper.toDto(mockMessage)).willReturn(mockDto);

      PageResponse<MessageDto> expectedResponse = new PageResponse<>(
          dtoList,
          nextCursorId,
          50,
          true,
          null
      );
      given(pageResponseMapper.fromSlice(any(Slice.class), eq(nextCursorId))).willReturn(
          expectedResponse);

      // when
      PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, cursor,
          pageable);

      // then
      assertThat(result).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("특정 채널의 메시지 목록 조회 실패 - 데이터가 없는 경우")
    void findByChannelId_shouldFailed() {
      UUID cursor = UUID.randomUUID();
      Pageable pageable = PageRequest.of(0, 50);

      List<Message> messageList = Collections.emptyList();
      Slice<Message> slice = new SliceImpl<>(messageList, pageable, false);

      given(messageRepository.findAllByCursor(eq(channelId), eq(cursor), eq(pageable))).willReturn(
          slice);

      PageResponse<MessageDto> expectedResponse = new PageResponse<>(
          Collections.emptyList(),
          null,
          50,
          false,
          null
      );
      given(pageResponseMapper.fromSlice(any(Slice.class), isNull())).willReturn(expectedResponse);

      // when
      PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, cursor,
          pageable);

      // then
      assertThat(result).isEqualTo(expectedResponse);
      assertThat(result.content()).isEmpty();
      assertThat(result.nextCursor()).isNull();
      assertThat(result.hasNext()).isFalse();

      then(messageMapper).shouldHaveNoInteractions();
    }
  }
}
