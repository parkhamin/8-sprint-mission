package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @PostMapping
  public ResponseEntity<ReadStatusDto> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    log.info("[ReadStatusController] 읽음 상태 생성 요청 - 채널 Id: {}, 사용자 Id: {}",
        readStatusCreateRequest.channelId(), readStatusCreateRequest.userId());

    ReadStatusDto readStatus = readStatusService.create(readStatusCreateRequest);

    log.info("[ReadStatusController] 읽음 상태 생성 완료 - Id: {}", readStatus.id());
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatus);
  }

  @PatchMapping(value = "/{readStatusId}")
  public ResponseEntity<ReadStatusDto> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    log.info("[ReadStatusController] 읽음 상태 수정 요청 - Id: {}", readStatusId);

    ReadStatusDto readStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);

    log.info("[ReadStatusController] 읽음 상태 수정 완료 - Id: {}", readStatus.id());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatus);
  }

  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam("userId") UUID userId) {
    log.info("[ReadStatusController] 특정 사용자의 읽음 상태 목록 조회 요청 - 사용자 Id: {}", userId);

    List<ReadStatusDto> readStatusList = readStatusService.findAllByUserId(userId);

    log.info("[ReadStatusController] 특정 사용자의 읽음 상태 목록 조회 완료 - 목록 size: {}",
        readStatusList.size());
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusList);
  }
}
