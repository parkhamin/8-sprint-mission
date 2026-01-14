package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("api/channels")
@Tag(name = "Channel Controller", description = "채널 API입니다.")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널을 생성할 수 있다.
  // Channel create(PublicChannelCreateRequest channelCreateRequest);
  @PostMapping(value = "/public")
  @Operation(summary = "공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "공개 채널 생성 성공", content = @Content(schema = @Schema(implementation = Channel.class)))
  })
  public ResponseEntity<Channel> create(
      @RequestBody PublicChannelCreateRequest channelCreateRequest) {
    Channel channel = channelService.create(channelCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  // 비공개 채널을 생성할 수 있다.
  // Channel create(PrivateChannelCreateRequest channelCreateRequest);
  @PostMapping(value = "/private")
  @Operation(summary = "비공개 채널 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "비공개 채널 생성 성공", content = @Content(schema = @Schema(implementation = Channel.class)))
  })
  public ResponseEntity<Channel> create(
      @RequestBody PrivateChannelCreateRequest channelCreateRequest) {
    Channel channel = channelService.create(channelCreateRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(channel);
  }

  // 공개 채널의 정보를 수정할 수 있다.
  // Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest);
  @PatchMapping(value = "/{channelId}")
  @Operation(summary = "채널 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "채널 정보 수정 성공", content = @Content(schema = @Schema(implementation = Channel.class))),
      @ApiResponse(responseCode = "400", description = "비공개 채널은 정보 수정 불가", content = @Content(examples = @ExampleObject(value = "타입이 private인 채널은 수정할 수 없습니다."))),
      @ApiResponse(responseCode = "404", description = "채널 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId} 채널을 찾을 수 없습니다.")))
  })
  public ResponseEntity<Channel> update(
      @PathVariable UUID channelId,
      @RequestBody PublicChannelUpdateRequest channelUpdateRequest
  ) {
    Channel channel = channelService.update(channelId, channelUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channel);
  }

  // 채널을 삭제할 수 있다.
  // void delete(UUID channelId);
  @DeleteMapping(value = "/{channelId}")
  @Operation(summary = "채널 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "채널 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "채널 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId} 채널을 찾을 수 없습니다.")))
  })
  public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
  // List<ChannelDTO> findAllByUserId(UUID userId);
  @GetMapping
  @Operation(summary = "사용자가 참가한 채널 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "채널 목록 조회 성공", content = @Content(schema = @Schema(implementation = ChannelDTO.class)))
  })
  public ResponseEntity<List<ChannelDTO>> findAllByUserId(@RequestParam("userId") UUID userId) {
    List<ChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}
