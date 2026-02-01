package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserDto(
    UUID id,
    String username, // 사용자의 이름
    String email,
    BinaryContentDto profile,
    Boolean online
) {

}
