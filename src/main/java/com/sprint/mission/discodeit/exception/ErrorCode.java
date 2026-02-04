package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  // 공통
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),

  // User 관련
  USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
  DUPLICATE_USER("사용자가 이미 존재합니다."),
  DUPLICATE_NAME("사용자의 이름이 이미 존재합니다"),
  DUPLICATE_EMAIL("사용자의 이메일이 이미 존재합니다"),
  INVALID_LOGIN("유효하지 않은 로그인 정보입니다"),

  // Channel 관련
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),

  // Message 관련
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),

  // BinaryContent 관련
  BINARY_CONTENT_NOT_FOUND("바이너리 컨텐츠를 찾을 수 없습니다."),
  BINARY_CONTENT_SAVE_FAILED("바이너리 컨텐츠를 저장하는 과정에서 오류가 발생했습니다."),

  // UserStatus 관련
  USER_STATUS_NOT_FOUND("사용자의 상태 정보를 찾을 수 없습니다."),
  DUPLICATE_USER_STATUS("사용자의 상태 정보가 이미 존재합니다."),

  // ReadStatus 관련
  READ_STATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다."),
  DUPLICATE_READ_STATUS("읽음 상태가 이미 존재합니다.");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }
}
