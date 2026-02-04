package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserDto login(LoginRequest loginRequest) {
    log.info("[AuthService] 로그인 시작 - 이름: {}", loginRequest.username());

    String username = loginRequest.username();
    String password = loginRequest.password();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.warn("[AuthService] 로그인 실패 - 존재하지 않는 사용자: {}", username);
          return new NoSuchElementException(username + " 사용자를 찾을 수 없습니다.");
        });

    if (!user.getPassword().equals(password)) {
      log.warn("[AuthService] 로그인 실패 - 이름: {}", username);
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    log.info("[AuthService] 로그인 완료 - 이름: {}", username);
    return userMapper.toDto(user);
  }
}
