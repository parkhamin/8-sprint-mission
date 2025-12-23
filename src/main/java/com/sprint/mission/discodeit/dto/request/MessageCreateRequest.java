package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
        String messageContent,
        UUID channelId,
        UUID senderId
) {
}
