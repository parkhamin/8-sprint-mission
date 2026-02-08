package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Channel 통합 테스트")
public class ChannelIntegrationTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReadStatusRepository readStatusRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ChannelService channelService;

  private User savedUser;
  private User savedUser2;
  private Channel savedPublicChannel;
  private Channel savedPrivateChannel;

  @BeforeEach
  void setUp() {
    savedUser = new User("테스트회원1", "test@naver.com", "test1234", null);
    new UserStatus(savedUser, Instant.now());

    savedUser2 = new User("테스트회원2", "test2@naver.com", "test1234", null);
    new UserStatus(savedUser2, Instant.now());

    userRepository.save(savedUser);
    userRepository.save(savedUser2);

    savedPublicChannel = new Channel(ChannelType.PUBLIC, "[테스트]", "테스트 채널입니다.");
    channelRepository.save(savedPublicChannel);

    savedPrivateChannel = new Channel(ChannelType.PRIVATE);
    channelRepository.save(savedPrivateChannel);
  }

  @Test
  @DisplayName("공개 채널 생성 시 트랜잭션이 올바르게 동작해야 한다.")
  void createPublicChannel_TransactionCommit_Success() {

    // given
    PublicChannelCreateRequest channelReq = new PublicChannelCreateRequest("[공지]", "공지 채널입니다.");

    // when
    ChannelDto result = channelService.create(channelReq);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result.name()).isEqualTo("[공지]");
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);

    Channel channel = channelRepository.findById(result.id()).orElseThrow();
    assertThat(channel.getName()).isEqualTo("[공지]");
    assertThat(channel.getDescription()).isEqualTo("공지 채널입니다.");
  }

  @Test
  @DisplayName("비공개 채널 생성 시 참여자들의 읽음 상태가 함께 생성되어야 한다.")
  void createPrivateChannel_TransactionCommit_Success() {

    // given
    List<UUID> participants = List.of(savedUser.getId(), savedUser2.getId());
    PrivateChannelCreateRequest channelReq = new PrivateChannelCreateRequest(participants);

    // when
    ChannelDto result = channelService.create(channelReq);

    // then
    assertThat(result.id()).isNotNull();
    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
    assertThat(result.participants().size()).isEqualTo(2);

    Channel channel = channelRepository.findById(result.id()).orElseThrow();
    assertThat(channel.getType()).isEqualTo(ChannelType.PRIVATE);

    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channel.getId());
    assertThat(readStatuses).hasSize(2);
  }

  @Test
  @DisplayName("공개 채널 수정 시 반영되어야 한다.")
  void updateChannel_TransactionCommit_Success() {

    // given
    PublicChannelUpdateRequest channelReq = new PublicChannelUpdateRequest("[이름수정테스트]", null);

    // when
    ChannelDto result = channelService.update(savedPublicChannel.getId(), channelReq);

    // then
    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(result.name()).isEqualTo("[이름수정테스트]");
    assertThat(result.description()).isEqualTo("테스트 채널입니다.");

    Channel channel = channelRepository.findById(result.id()).orElseThrow();
    assertThat(channel.getType()).isEqualTo(ChannelType.PUBLIC);
    assertThat(channel.getName()).isEqualTo("[이름수정테스트]");
    assertThat(channel.getDescription()).isEqualTo("테스트 채널입니다.");
  }

  @Test
  @DisplayName("비공개 채널을 수정하려고 할 시 예외가 발생해야 한다.")
  void updateChannel_PrivateChannel_Fail() {

    // given
    PublicChannelUpdateRequest channelReq = new PublicChannelUpdateRequest("[이름수정테스트]",
        "공지 채널입니다.");

    // when & then
    assertThatThrownBy(() -> channelService.update(savedPrivateChannel.getId(), channelReq))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  @DisplayName("채널 삭제 시 더 이상 조회되지 않아야 한다.")
  void deleteChannel_Success() {

    // given
    messageRepository.save(new Message("테스트", savedPublicChannel, savedUser, null));
    readStatusRepository.save(new ReadStatus(savedUser, savedPublicChannel, Instant.now()));

    // when
    channelService.delete(savedPublicChannel.getId());

    // then
    assertThat(channelRepository.findById(savedPublicChannel.getId())).isEmpty();
    assertThat(messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
        savedPublicChannel.getId())).isEmpty();
    assertThat(readStatusRepository.findAllByChannelId(savedPublicChannel.getId())).isEmpty();
  }

  @Test
  @DisplayName("존재하지 않는 채널 Id로 삭제하려고 할 시 예외가 발생해야 한다.")
  void deleteChannel_NotFount_Fail() {

    // given
    UUID NonExistsChannelId = UUID.randomUUID();

    // when & then
    assertThatThrownBy(() -> channelService.delete(NonExistsChannelId))
        .isInstanceOf(ChannelNotFoundException.class);
  }
}
