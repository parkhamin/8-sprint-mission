package com.sprint.mission.discodeit.dto.request;

public record UserUpdateRequest(
        String newUserName,
        String newEmail,
        String newPassword
) {
}
