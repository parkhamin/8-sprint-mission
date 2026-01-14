package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/messages")
@Tag(name = "Message Controller", description = "메시지 API입니다.")
public class MessageController {

  private final MessageService messageService;

  // 메시지를 보낼 수 있다.
  // Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "메시지 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "메시지 생성 성공", content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "채널 또는 사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId | authorId} 채널, 사용자(을/를) 찾을 수 없습니다.")))
  })
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> filesRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream() // 요청들을 스트림화
            .map(BinaryContentCreateRequest::fileFromRequest)
            .toList() // 스트림을 List로 변환
        ).orElse(new ArrayList<>()); // 요청이 null일 경우 처리

    Message message = messageService.create(messageCreateRequest, filesRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(message);
  }

  // 메시지를 수정할 수 있다.
  // Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest);
  @PatchMapping(value = "/{messageId}")
  @Operation(summary = "메시지 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 수정 성공", content = @Content(schema = @Schema(implementation = Message.class))),
      @ApiResponse(responseCode = "404", description = "메시지 조회 불가", content = @Content(examples = @ExampleObject(value = "{messageId} 메시지를 찾을 수 없습니다.")))
  })
  public ResponseEntity<Message> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    Message message = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(message);
  }

  // 메시지를 삭제할 수 있다.
  // void delete(UUID messageId);
  @DeleteMapping(value = "/{messageId}")
  @Operation(summary = "메시지 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "메시지 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "메시지 조회 불가", content = @Content(examples = @ExampleObject(value = "{messageId} 메시지를 찾을 수 없습니다.")))
  })
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }

  // 특정 채널의 메시지 목록을 조회할 수 있다
  // List<Message> findAllByChannelId(UUID channelId);
  @GetMapping
  @Operation(summary = "특정 채널의 메시지 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공", content = @Content(schema = @Schema(implementation = Message.class)))
  })
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }

}
