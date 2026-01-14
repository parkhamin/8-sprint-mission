package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent Controller", description = "첨부파일 API입니다.")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  // 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
  // BinaryContent find(UUID binaryContentId);
  @GetMapping("/{binaryContentId}")
  @Operation(summary = "첨부파일 단건 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부파일 단건 조회 성공", content = @Content(schema = @Schema(implementation = BinaryContent.class))),
      @ApiResponse(responseCode = "404", description = "첨부파일 조회 불가", content = @Content(examples = @ExampleObject(value = "{binaryContentId} BinaryContent를 찾을 수 없습니다.")))
  })
  public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContent);
  }

  // List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds);
  @GetMapping
  @Operation(summary = "첨부파일 다건 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "첨부파일 다건 조회 성공", content = @Content(schema = @Schema(implementation = BinaryContent.class))),
  })
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContents);
  }
}
