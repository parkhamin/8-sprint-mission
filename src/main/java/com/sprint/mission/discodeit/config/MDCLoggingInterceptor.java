package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID_HEADER = "Discodeit-Request-Id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    // 랜덤 UUID 생성
    String requestId = UUID.randomUUID().toString();

    // MDC에 정보 추가
    MDC.put("requestId", requestId);
    MDC.put("requestMethod", request.getMethod());
    MDC.put("requestUrl", request.getRequestURI());

    response.setHeader(REQUEST_ID_HEADER, requestId);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) {
    MDC.clear();
  }
}
