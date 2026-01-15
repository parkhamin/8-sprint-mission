package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest) {
    if (readStatusRepository.existsByUserIdAndChannelId(readStatusCreateRequest.userId(),
        readStatusCreateRequest.channelId())) {
      throw new IllegalArgumentException(
          readStatusCreateRequest.userId() + "의 readStatus가 이미 존재합니다.");
    }

    User user = userRepository.findById(readStatusCreateRequest.userId())
        .orElseThrow(() -> new NoSuchElementException(
            readStatusCreateRequest.userId() + " 사용자를 찾을 수 없습니다."));

    Channel channel = channelRepository.findById(readStatusCreateRequest.channelId())
        .orElseThrow(() -> new NoSuchElementException(
            readStatusCreateRequest.channelId() + " 채널을 찾을 수 없습니다."));
    Instant lastReadAt = readStatusCreateRequest.lastReadAt();

    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Transactional(readOnly = true)
  @Override
  public ReadStatus find(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + " ReadStatus를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId);
  }

  @Override
  public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    Instant lastReadAt = readStatusUpdateRequest.newLastReadAt();

    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + " ReadStatus를 찾을 수 없습니다."));

    readStatus.update(lastReadAt);
    return readStatus;
  }

  @Override
  public void delete(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new NoSuchElementException(readStatusId + " ReadStatus를 찾을 수 없습니다."));

    readStatusRepository.delete(readStatus);
  }
}
