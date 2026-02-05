package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PostMapping(value = "/public")
  public ResponseEntity<ChannelDto> create(
      @Valid @RequestBody PublicChannelCreateRequest channelCreateRequest) {
    log.info("[ChannelController] 공개 채널 생성 요청 - 이름: {}", channelCreateRequest.name());

    ChannelDto channel = channelService.create(channelCreateRequest);

    log.info("[ChannelController] 공개 채널 생성 완료 - Id: {}", channel.id());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  @PostMapping(value = "/private")
  public ResponseEntity<ChannelDto> create(
      @Valid @RequestBody PrivateChannelCreateRequest channelCreateRequest) {
    log.info("[ChannelController] 비공개 채널 생성 요청");

    ChannelDto channel = channelService.create(channelCreateRequest);

    log.info("[ChannelController] 비공개 채널 생성 완료 - Id: {}", channel.id());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  @PatchMapping(value = "/{channelId}")
  public ResponseEntity<ChannelDto> update(
      @PathVariable UUID channelId,
      @Valid @RequestBody PublicChannelUpdateRequest channelUpdateRequest
  ) {
    log.info("[ChannelController] 공개 채널 수정 요청 - Id: {}", channelId);

    ChannelDto channel = channelService.update(channelId, channelUpdateRequest);

    log.info("[ChannelController] 공개 채널 수정 완료 - Id: {}", channel.id());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channel);
  }

  @DeleteMapping(value = "/{channelId}")
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    log.info("[ChannelController] 채널 삭제 요청 - Id: {}", channelId);

    channelService.delete(channelId);

    log.info("[ChannelController] 채널 삭제 완료 - Id: {}", channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @GetMapping
  public ResponseEntity<List<ChannelDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    log.info("[ChannelController] 특정 사용자가 볼 수 있는 채널 목록 조회 요청 - 사용자 Id: {}", userId);

    List<ChannelDto> channels = channelService.findAllByUserId(userId);

    log.info("[ChannelController] 특정 사용자가 볼 수 있는 채널 목록 조회 완료 - 채널 목록 size: {}", channels.size());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}
