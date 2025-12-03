package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static class SingletonHolder{
        private static final FileMessageRepository INSTANCE = new FileMessageRepository();
    }

    public static FileMessageRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private final File file = new File("message.ser");
    private Map<UUID, Message> messages = new HashMap<>();

    private FileMessageRepository(){
        loadFromFile(); // 생성될 때 기존의 데이터 불러오기(파일 -> (역직렬화) -> 객체 Map)
    }

    @Override
    public Message save(Message message) {
        messages.put(message.getId(), message);
        saveToFile();
        return message;
    }

    @Override
    public Message findById(UUID messageId) {
        return messages.get(messageId);
    }

    @Override
    public void deleteById(UUID messageId) {
        messages.remove(messageId);
        saveToFile();
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    private void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
