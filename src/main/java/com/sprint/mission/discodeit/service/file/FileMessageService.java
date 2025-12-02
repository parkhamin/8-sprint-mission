package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {
    private Map<UUID, Message> messages = new HashMap<>();
    private final UserService userService;
    private ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
        load();
    }

    public void setChannelService(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public Message createMessage(Message message) {
        if (userService.getUser(message.getSender()) == null) throw new IllegalArgumentException("보내려는 사용자가 존재하지 않습니다.");
        Channel channel = channelService.getChannel(message.getChannelId());
        if (channel == null)
            throw new IllegalArgumentException("채널이 존재하지 않습니다.");
        if (!channel.getUsers().contains(message.getSender()))
            throw new IllegalArgumentException("보내려는 사용자가 채널에 참가하지 않습니다.");

        messages.put(message.getId(), message);
        save();
        channel.addMessage(message.getId());
        return message;
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public void updateMessage(UUID messageId, String newContent) {
        Message message = messages.get(messageId);

        if (message != null) {
            message.updateContent(newContent);
            save();
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        messages.remove(messageId);
        save();
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(messages.values());
    }

    private void save() {
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        File file = new File("message.ser");
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
