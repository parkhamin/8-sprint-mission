package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {
  // 인터페이스에서 메서드에 몸체를 직접 작성할 경우 default 키워드 필요

  // slice를 PageResponse로 변환
  // slice는 totalElements 필요 없음
  // 모바일, 무한 스크롤, 대량 데이터 조회할 때 사용
  // -> 디스코드잇 적용
  default <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  // page를 PageResponse로 변환
  // 일반적인 웹 게시판(번호 UI 기반)
  default <T> PageResponse<T> fromPage(Page<T> page, Object nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
