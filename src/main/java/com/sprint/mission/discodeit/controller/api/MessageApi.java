package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message Controller", description = "메시지 API입니다.")
public interface MessageApi {

  @Operation(summary = "메시지 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "메시지 생성 성공", content = @Content(schema = @Schema(implementation = MessageDto.class))),
      @ApiResponse(responseCode = "404", description = "채널 또는 사용자 조회 불가", content = @Content(examples = @ExampleObject(value = "{channelId | authorId} 채널, 사용자(을/를) 찾을 수 없습니다.")))
  })
  ResponseEntity<MessageDto> create(
      @Parameter(
          description = "Message 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) MessageCreateRequest messageCreateRequest,
      @Parameter(
          description = "Message 첨부 파일들",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) List<MultipartFile> attachments);

  @Operation(summary = "메시지 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 수정 성공", content = @Content(schema = @Schema(implementation = MessageDto.class))),
      @ApiResponse(responseCode = "404", description = "메시지 조회 불가", content = @Content(examples = @ExampleObject(value = "{messageId} 메시지를 찾을 수 없습니다.")))
  })
  ResponseEntity<MessageDto> update(
      @Parameter(description = "수정할 메시지 Id") UUID messageId,
      @Parameter(description = "수정할 메시지 내용") MessageUpdateRequest messageUpdateRequest
  );

  @Operation(summary = "메시지 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "메시지 삭제 성공"),
      @ApiResponse(responseCode = "404", description = "메시지 조회 불가", content = @Content(examples = @ExampleObject(value = "{messageId} 메시지를 찾을 수 없습니다.")))
  })
  ResponseEntity<Void> delete(@Parameter(description = "삭제할 메시지 Id") UUID messageId);

  @Operation(summary = "특정 채널의 메시지 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "메시지 목록 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PageResponse.class))))
  })
  ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
      @Parameter(description = "조회할 채널 Id") UUID channelId,
      @Parameter(description = "어디까지 읽었는지 판단할 책갈피") Instant cursor,
      @Parameter(description = "페이지 정보") Pageable pageable);
}
