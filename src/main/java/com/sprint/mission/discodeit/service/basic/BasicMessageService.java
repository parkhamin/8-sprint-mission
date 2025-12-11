package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService{
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    /*public BasicMessageService(UserRepository userRepository, ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }*/

    @Override
    public Message createMessage(String messageContent, UUID userId, UUID channelId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        if (!channel.getUsers().contains(user.getId())) throw new IllegalArgumentException("해당 채널에 참여 중이 아닌 사용자입니다.");

        Message message = new Message(messageContent, userId, channelId);
        return messageRepository.save(message);
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    }

    @Override
    public Message updateMessage(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        message.updateContent(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        messageRepository.deleteById(messageId);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
