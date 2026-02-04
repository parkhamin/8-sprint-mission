package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;
import java.util.UUID;

public class UserStatusAlreadyExistsException extends UserStatusException {

  public UserStatusAlreadyExistsException(UUID userStatusId) {
    super(ErrorCode.DUPLICATE_USER_STATUS, Map.of("userStatusId", userStatusId));
  }
}
