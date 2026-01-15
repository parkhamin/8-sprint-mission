package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus Controller", description = "사용자의 읽음 상태 API입니다.")
public interface ReadStatusApi {

  @Operation(summary = "읽음 상태 정보 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "메시지 읽음 상태 정보 생성 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재해서 생성 불가", content = @Content(examples = @ExampleObject(value = "ReadStatus가 이미 존재합니다."))),
      @ApiResponse(responseCode = "404", description = "채널 또는 사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId | userId} 채널, 사용자(을/를) 찾을 수 없습니다.")))
  })
  ResponseEntity<ReadStatus> create(
      @Parameter(description = "메시지 읽음 상태 정") ReadStatusCreateRequest readStatusCreateRequest);

  @Operation(summary = "읽음 상태 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 읽음 상태 정보 수정 성공", content = @Content(schema = @Schema(implementation = ReadStatus.class))),
      @ApiResponse(responseCode = "404", description = "메시지 읽음 상태 조회 불가", content = @Content(examples = @ExampleObject(value = "{readStatusId}  ReadStatus를 찾을 수 없습니다.")))
  })
  ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 읽음 상태 Id") UUID readStatusId,
      @Parameter(description = "수정할 읽음 상태 정") ReadStatusUpdateRequest readStatusUpdateRequest);

  @Operation(summary = "특정 사용자의 읽음 상태 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 읽음 상태 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class))))
  })
  ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(description = "조회할 사용자 Id") UUID userId);
}
