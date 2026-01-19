package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
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
public class MessageController implements MessageApi {

  private final MessageService messageService;

  // 메시지를 보낼 수 있다.
  // Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> filesRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream() // 요청들을 스트림화
            .map(BinaryContentCreateRequest::fileFromRequest)
            .toList() // 스트림을 List로 변환
        ).orElse(new ArrayList<>()); // 요청이 null일 경우 처리

    MessageDto message = messageService.create(messageCreateRequest, filesRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(message);
  }

  // 메시지를 수정할 수 있다.
  // Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest);
  @PatchMapping(value = "/{messageId}")
  public ResponseEntity<MessageDto> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    MessageDto message = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(message);
  }

  // 메시지를 삭제할 수 있다.
  // void delete(UUID messageId);
  @DeleteMapping(value = "/{messageId}")
  public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
    messageService.delete(messageId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .build();
  }

  // 특정 채널의 메시지 목록을 조회할 수 있다
  // List<Message> findAllByChannelId(UUID channelId);
  @GetMapping
  public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId,
      @RequestParam(value = "cursor", required = false) UUID cursor, // 'page' 대신 'cursor'
      @RequestParam(value = "size", defaultValue = "50") int size
  ) {
    PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor, size);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }
}
