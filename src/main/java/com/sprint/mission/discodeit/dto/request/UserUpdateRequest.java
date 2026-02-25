package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(min = 2, max = 50, message = "이름은 2자에서 50자 사이여야 합니다.")
    String newUsername,

    @Email(message = "유효한 이메일 형식이여야 합니다.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    String newEmail,

    @Size(max = 60, message = "비밀번호는 60자 이하여야 합니다.")
    String newPassword
) {

}
