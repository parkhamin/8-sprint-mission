package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentConvertFailedException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일명은 필수입니다.")
    @Size(max = 255, message = "파일명은 255자 이하여야 됩니다.")
    String fileName,

    @NotBlank(message = "컨텐츠 타입은 필수입니다.")
    String contentType,

    @NotEmpty(message = "파일 내용이 비어있을 수 없습니다.")
    byte[] bytes
) {

  public static BinaryContentCreateRequest fileFromRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new BinaryContentConvertFailedException();
    }

    try {
      return new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new BinaryContentConvertFailedException(e);
    }
  }
}
