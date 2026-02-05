package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @Size(min = 2, max = 100, message = "채널 이름은 2자에서 100자 사이여야 합니다.")
    String newName,

    @Size(max = 1000, message = "채널 설명은 최대 1000자까지 가능합니다.")
    String newDescription
) {

}
