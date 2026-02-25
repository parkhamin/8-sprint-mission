package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "메시지 내용은 비어있을 수 없습니다.")
    @Size(max = 2000, message = "메시지 내용은 2000자 이하여야 합니다.")
    String content,

    @NotNull(message = "채널 Id는 필수입니다.")
    UUID channelId,

    @NotNull(message = "작성자 Id는 필수입니다.")
    UUID authorId
) {

}
