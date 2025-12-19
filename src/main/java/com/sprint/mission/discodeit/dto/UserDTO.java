package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String userName, // 사용자의 이름
        String email,
        UUID profileId,
        boolean isOnline
) {
}
