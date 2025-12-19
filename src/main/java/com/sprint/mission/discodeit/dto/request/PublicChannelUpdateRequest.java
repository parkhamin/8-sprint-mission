package com.sprint.mission.discodeit.dto.request;

public record PublicChannelUpdateRequest(
        String newChannelName,
        String newDescription
) {
}
