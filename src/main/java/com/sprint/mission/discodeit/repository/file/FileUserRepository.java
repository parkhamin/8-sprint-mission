package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static class SingletonHolder{
        private static final FileUserRepository INSTANCE = new FileUserRepository();
    }

    public static FileUserRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    // User의 정보들을 직렬화하여 저장할 파일
    private final File file = new File("user.ser");

    // User의 정보를 저장할 Map
    private Map<UUID, User> users = new HashMap<>();

    private FileUserRepository(){
        loadFromFile();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        saveToFile();
        return user;
    }

    @Override
    public User findById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        users.remove(userId);
        saveToFile();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private void saveToFile() {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            users = (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
