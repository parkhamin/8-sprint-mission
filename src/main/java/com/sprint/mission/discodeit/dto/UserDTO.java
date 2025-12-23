package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;

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
    public static UserDTO fromEntity(
            User user, boolean online
    ) {
        return new UserDTO(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUserName(),
                user.getEmail(),
                user.getProfileId(),
                online
        );
    }
}
