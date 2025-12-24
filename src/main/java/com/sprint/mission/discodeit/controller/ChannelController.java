package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
@RequestMapping("api/channel")
@ResponseBody
public class ChannelController {
    private final ChannelService channelService;

    // 공개 채널을 생성할 수 있다.
    // Channel create(PublicChannelCreateRequest channelCreateRequest);
    @RequestMapping(value = "/publicCreate")
    public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest channelCreateRequest) {
        Channel channel = channelService.create(channelCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }

    // 비공개 채널을 생성할 수 있다.
    // Channel create(PrivateChannelCreateRequest channelCreateRequest);
    @RequestMapping(value = "/privateCreate")
    public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest channelCreateRequest) {
        Channel channel = channelService.create(channelCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(channel);
    }

    // 공개 채널의 정보를 수정할 수 있다.
    // Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest);
    @RequestMapping(value = "/update")
    public ResponseEntity<Channel> update(
            @RequestParam("channelId") UUID channelId,
            @RequestBody PublicChannelUpdateRequest channelUpdateRequest
    ) {
        Channel channel = channelService.update(channelId, channelUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(channel);
    }

    // 채널을 삭제할 수 있다.
    // void delete(UUID channelId);
    @RequestMapping(value = "/delete")
    public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId) {
        channelService.delete(channelId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    // 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.
    // List<ChannelDTO> findAllByUserId(UUID userId);
    @RequestMapping("/findChannelsByUserId")
    public ResponseEntity<List<ChannelDTO>> findChannelsByUserId(@RequestParam("userId") UUID userId){
        List<ChannelDTO> Channels = channelService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Channels);
    }
}
