package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private Map<UUID, User> users = new HashMap<>();

    public FileUserService() {
        load();
    }

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        save();
        return user;
    }

    @Override
    public User getUser(UUID userId) {
        return users.get(userId);
    }

    @Override
    public void updateUser(UUID userId, String newUserName) {
        User user = users.get(userId);

        if (user != null) {
            user.updateUserName(newUserName);
            save();
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        users.remove(userId);
        save();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void save() {
        try (FileOutputStream fos = new FileOutputStream("user.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        File file = new File("user.ser");
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream("user.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             users = (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
