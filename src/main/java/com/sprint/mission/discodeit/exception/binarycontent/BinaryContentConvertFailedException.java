package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class BinaryContentConvertFailedException extends BinaryContentException {

  public BinaryContentConvertFailedException() {
    super(ErrorCode.BINARY_CONTENT_CONVERT_FAILED);
  }

  public BinaryContentConvertFailedException(Throwable cause) {
    super(ErrorCode.BINARY_CONTENT_CONVERT_FAILED, cause);
  }
}
