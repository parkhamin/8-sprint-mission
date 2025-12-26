package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/api/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보를 생성할 수 있다.
    // ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);
    @RequestMapping(value = "/create")
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readStatus);
    }

    // 특정 채널의 메시지 수신 정보를 수정할 수 있다.
    // ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);
    @RequestMapping(value = "/update")
    public ResponseEntity<ReadStatus> update(
            @RequestParam("readStatusId") UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatus);
    }

    // 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
    // List<ReadStatus> findAllByUserId(UUID userId);
    @RequestMapping(value = "/findAllByUserId")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@RequestParam("userId") UUID userId) {
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusList);
    }
}
