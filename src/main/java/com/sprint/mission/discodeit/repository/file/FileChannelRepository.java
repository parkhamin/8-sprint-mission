package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static class SingletonHolder{
        private static final FileChannelRepository INSTANCE = new FileChannelRepository();
    }

    public static FileChannelRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private final File file = new File("channel.ser");
    private Map<UUID, Channel> channels = new HashMap<>();

    private FileChannelRepository() {
        loadFromFile();
    }

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        saveToFile();
        return channel;
    }

    @Override
    public Channel findById(UUID channelId) {
        return channels.get(channelId);
    }

    @Override
    public void deleteById(UUID channelId) {
        channels.remove(channelId);
        saveToFile();
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(channels.values());
    }

    private void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channels);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channels = (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
