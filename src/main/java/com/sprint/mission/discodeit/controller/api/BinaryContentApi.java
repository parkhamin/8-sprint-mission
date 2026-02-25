package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
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
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent Controller", description = "첨부파일 API입니다.")
public interface BinaryContentApi {

  @Operation(summary = "첨부파일 단건 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부파일 단건 조회 성공", content = @Content(schema = @Schema(implementation = BinaryContentDto.class))),
      @ApiResponse(responseCode = "404", description = "첨부파일 조회 불가", content = @Content(examples = @ExampleObject(value = "{binaryContentId} BinaryContent를 찾을 수 없습니다.")))
  })
  ResponseEntity<BinaryContentDto> find(
      @Parameter(description = "조회할 첨부파일 Id") UUID binaryContentId);

  @Operation(summary = "첨부파일 다건 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부파일 다건 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContentDto.class)))
      ),
  })
  ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @Parameter(description = "조회할 첨부파일 Id 목록") List<UUID> binaryContentIds);

  @Operation(summary = "첨부파일 다운로드")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부파일 다운로드 성공", content = @Content(schema = @Schema(implementation = Resource.class)))
  })
  ResponseEntity<?> download(@Parameter(description = "다운로드 할 파일 Id") UUID binaryContentId);
}
