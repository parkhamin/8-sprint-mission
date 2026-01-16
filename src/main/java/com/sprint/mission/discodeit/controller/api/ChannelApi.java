package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel Controller", description = "채널 API입니다.")
public interface ChannelApi {

  @Operation(summary = "공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "공개 채널 생성 성공", content = @Content(schema = @Schema(implementation = ChannelDto.class)))
  })
  ResponseEntity<ChannelDto> create(
      @Parameter(description = "공개 채널 생성 정보") PublicChannelCreateRequest channelCreateRequest);

  @Operation(summary = "비공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "비공개 채널 생성 성공", content = @Content(schema = @Schema(implementation = ChannelDto.class)))
  })
  ResponseEntity<ChannelDto> create(
      @Parameter(description = "비공개 채널 생성 정보") PrivateChannelCreateRequest channelCreateRequest);

  @Operation(summary = "채널 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "채널 정보 수정 성공", content = @Content(schema = @Schema(implementation = ChannelDto.class))),
      @ApiResponse(responseCode = "400", description = "비공개 채널은 정보 수정 불가", content = @Content(examples = @ExampleObject(value = "타입이 private인 채널은 수정할 수 없습니다."))),
      @ApiResponse(responseCode = "404", description = "채널 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId} 채널을 찾을 수 없습니다.")))
  })
  ResponseEntity<ChannelDto> update(
      @Parameter(description = "수정할 채널 Id") UUID channelId,
      @Parameter(description = "수정할 채널 정보") PublicChannelUpdateRequest channelUpdateRequest);

  @Operation(summary = "채널 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "채널 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "채널 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId} 채널을 찾을 수 없습니다.")))
  })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 채널 Id") UUID channelId);

  @Operation(summary = "사용자가 참가한 채널 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "채널 목록 조회 성공", content = @Content(schema = @Schema(implementation = ChannelDto.class)))
  })
  ResponseEntity<List<ChannelDto>> findAllByUserId(
      @Parameter(description = "조회할 사용자 Id") UUID userId);
}
