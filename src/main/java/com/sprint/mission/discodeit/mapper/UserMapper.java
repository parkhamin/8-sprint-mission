package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    if (user == null) {
      return null;
    }

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentMapper.toDto(user.getProfile()),
        Optional.ofNullable(user.getStatus())
            .map(UserStatus::isOnline)
            .orElse(false)
    );
  }
}
