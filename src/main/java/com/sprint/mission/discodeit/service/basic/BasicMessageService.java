package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(UserRepository userRepository, MessageRepository messageRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message createMessage(Message message) {
        // getSender -> 메시지 보낸 사람의 UUID를 반환
        // userRepo에 메시지 보낸 사람과 동일한 Id를 가지고 있는지
        // 그 값이 null이라는 것은 그런 사용자가 없다는 뜻
        if (userRepository.findById(message.getSender()) == null) throw new IllegalArgumentException("보내려는 사용자가 없습니다.");

        // 메시지를 보낸 채널의 UUID를 반환
        // channelRepo에서 메시지를 보낸 채널과 동일한 Id를 가지고 있는지
        // 그 값이 null이라는 것은 채널이 없다는 뜻
        Channel channel = channelRepository.findById(message.getChannelId());
        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");

        // 채널이 존재해도 메시지 수신자가 그 채널에 참가해야 함.
        // 메시지 수신자가 채널의 참여자들 중 포함되는지
        if (!channel.getUsers().contains(message.getSender())) throw new IllegalArgumentException("메시지를 보내려는 사용자가 채널에 참가하지 않았습니다.");

        // 여기까지 왔다면 검증로직은 끝남
        messageRepository.save(message);
        channel.addMessage(message.getId());
        channelRepository.save(channel);

        return message;
    }

    @Override
    public Message getMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId);

        if (message == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        return message;
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = getMessage(messageId);
        message.updateContent(newContent);
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = getMessage(messageId);
        messageRepository.deleteById(message.getId());
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
