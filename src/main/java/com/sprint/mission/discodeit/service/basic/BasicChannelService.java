package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicChannelService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        return channelRepository.save(channel);
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
    }

    @Override
    public Channel updateChannel(UUID channelId, String newChannelName) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        channel.updateChannelName(newChannelName);
        return channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
        channelRepository.deleteById(channelId);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void addUserToChannel(UUID userId, UUID channelId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Channel channel = getChannel(channelId); // 값이 null이었다면 getChannel 메서드를 통해 이미 오류가 났을 것.
        channel.addUser(userId);
        channelRepository.save(channel);
    }

    @Override
    public void removeUserFromChannel(UUID userId, UUID channelId) {
        userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Channel channel = getChannel(channelId); // 값이 null이었다면 getChannel 메서드를 통해 이미 오류가 났을 것.

        channel.removeUser(userId);
        channelRepository.save(channel);
    }
}
