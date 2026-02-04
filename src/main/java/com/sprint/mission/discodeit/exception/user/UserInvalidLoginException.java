package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class UserInvalidLoginException extends UserException {

  public UserInvalidLoginException() {
    super(ErrorCode.INVALID_LOGIN);
  }
}
