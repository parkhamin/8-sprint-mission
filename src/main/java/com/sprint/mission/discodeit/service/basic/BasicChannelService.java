package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(PublicChannelCreateRequest channelCreateRequest) {
        String channelName = channelCreateRequest.channelName();
        String description = channelCreateRequest.description();

        Channel channel = new Channel(ChannelType.PUBLIC, channelName, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest channelCreateRequest) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);

        for (UUID userId : channelCreateRequest.joinIds()) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), null);
            readStatusRepository.save(readStatus);
        }

        return createdChannel;
    }

    @Override
    public ChannelDTO find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channel -> toChannelDTO(channel))
                .orElseThrow(() -> new NoSuchElementException(channelId +" 채널을 찾을 수 없습니다."));
    }

    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest channelUpdateRequest) {
        String newChannelName = channelUpdateRequest.newChannelName();
        String newDescription = channelUpdateRequest.newDescription();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId +" 채널을 찾을 수 없습니다."));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("타입이 private인 채널은 수정할 수 없습니다.");
        }

        channel.update(newChannelName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException(channelId +" 채널을 찾을 수 없습니다."));

        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());

        channelRepository.deleteById(channelId);
    }

    @Override
    public List<ChannelDTO> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                            || mySubscribedChannelIds.contains(channel.getId())
                )
                .map(this::toChannelDTO)
                .toList();
    }

    private ChannelDTO toChannelDTO(Channel channel) {
        /*
        [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
        */
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }

        return ChannelDTO.fromEntity(channel, lastMessageAt, participantIds);
    }
}
