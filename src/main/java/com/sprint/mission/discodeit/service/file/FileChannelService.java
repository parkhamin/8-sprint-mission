package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private Map<UUID, Channel> channels = new HashMap<>();
    private final MessageService messageService;

    public FileChannelService(MessageService messageService) {
        this.messageService = messageService;
        load();
    }

    @Override
    public Channel createChannel(Channel channel) {
        channels.put(channel.getId(), channel);
        save();
        return channel;
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public void updateChannel(UUID channelId, String newChannelName) {
        Channel channel = channels.get(channelId);

        if (channel != null) {
            channel.updateChannelName(newChannelName);
            save();
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channels.remove(channelId);
        save();
    }

    @Override
    public void sendMessage(UUID channelId, UUID messageId) {
        Channel channel = channels.get(channelId);
        if (channel == null) throw new IllegalArgumentException("채널이 존재하지 않습니다.");

        Message message = messageService.getMessage(messageId);
        if (message == null) throw new IllegalArgumentException("보내려는 메시지가 존재하지 않습니다.");
        if (!channel.getUsers().contains(message.getSender())) throw new IllegalArgumentException("보내려는 사용자가 존재하지 않습니다.");
        channel.addMessage(messageId);
        save();
    }

    @Override
    public List<Channel> getAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void addUserToChannel(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        if (channel == null) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        channel.addUser(userId);
        save();
    }

    @Override
    public void removeUserFromChannel(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        if (channel == null) throw new IllegalArgumentException("존재하지 않는 채널입니다.");

        channel.removeUser(userId);
        save();
    }

    private void save() {
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        File file = new File("channel.ser");
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream("channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channels = (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
