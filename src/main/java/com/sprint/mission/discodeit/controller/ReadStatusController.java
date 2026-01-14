package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus Controller", description = "사용자의 읽음 상태 API입니다.")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  // 특정 채널의 메시지 수신 정보를 생성할 수 있다.
  // ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);
  @PostMapping
  @Operation(summary = "읽음 상태 정보 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "메시지 읽음 상태 정보 생성 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재해서 생성 불가", content = @Content(examples = @ExampleObject(value = "ReadStatus가 이미 존재합니다."))),
      @ApiResponse(responseCode = "404", description = "채널 또는 사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId | userId} 채널, 사용자(을/를) 찾을 수 없습니다.")))
  })
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
    ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(readStatus);
  }

  // 특정 채널의 메시지 수신 정보를 수정할 수 있다.
  // ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);
  @PatchMapping(value = "/{readStatusId}")
  @Operation(summary = "읽음 상태 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 읽음 상태 정보 수정 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "메시지 읽음 상태 조회 불가", content = @Content(examples = @ExampleObject(value = "{readStatusId}  ReadStatus를 찾을 수 없습니다.")))
  })
  public ResponseEntity<ReadStatus> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatus);
  }

  // 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
  // List<ReadStatus> findAllByUserId(UUID userId);
  @GetMapping
  @Operation(summary = "특정 사용자의 읽음 상태 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 읽음 상태 목록 조회 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class)))
  })
  public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatusList);
  }
}
