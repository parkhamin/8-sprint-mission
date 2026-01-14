package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    // 채널에 참가한 사람들
    List<UUID> participantIds
) {

}
