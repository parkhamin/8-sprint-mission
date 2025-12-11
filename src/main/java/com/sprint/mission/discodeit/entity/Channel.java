package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Getter
public class Channel extends Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String channelName; // 채널의 이름
    private Set<UUID> users; // 채널에 들어온 사용자들의 목록(중복 불가)

    public Channel(String channelName) { // 생성자의 파라미터를 통해 초기화
        super();
        this.users = new HashSet<>();
        this.channelName = channelName;
    }

    /*public String getChannelName() {
        return channelName;
    }*/

    public void addUser(UUID uuid) { // 채널에 사용자가 입장
        users.add(uuid);
    }
    public void removeUser(UUID uuid) { // 사용자가 채널 퇴장
        users.remove(uuid);
    }

    /*public Set<UUID> getUsers() {
        return users;
    }*/

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        update();
    }

    @Override
    public String toString() {
        return "Channel {" +
                "channelName='" + channelName + '\'' +
                '}' + " , " + super.toString();
    }
}
