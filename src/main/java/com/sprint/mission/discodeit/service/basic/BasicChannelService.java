package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository; // 필요한 repository 필드로 선언
    private final MessageRepository messageRepository;

    public BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.channelRepository = channelRepository; // 생성자로 초기화
        this.messageRepository = messageRepository;
    }

    @Override
    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public Channel getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId);

        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다.");
        }
        return channel;
    }

    @Override
    public void updateChannel(UUID channelId, String newChannelName) {
        Channel channel = getChannel(channelId);
        channel.updateChannelName(newChannelName);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = getChannel(channelId);
        channelRepository.deleteById(channel.getId());
    }

    @Override
    public void sendMessage(UUID channelId, UUID messageId) {
        Channel channel = getChannel(channelId);
        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");

        Message message = messageRepository.findById(messageId);
        if (message == null) throw new IllegalArgumentException("보내려는 메시지가 존재하지 않습니다.");
        if (!channel.getUsers().contains(message.getSender())) throw new IllegalArgumentException("보내려는 사용자가 존재하지 않습니다.");
        channel.addMessage(messageId); // addMessage를 통해서 채널의 List에 messageId 추가
        channelRepository.save(channel); // List 변경된 것 반영해서 save하기
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = getChannel(channelId);
        channel.addUser(userId);
        channelRepository.save(channel); // 변경 반영
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = getChannel(channelId);
        channel.removeUser(userId);
        channelRepository.save(channel); // 변경 반영
    }
}
