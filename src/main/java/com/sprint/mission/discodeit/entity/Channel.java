package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends Common{
    private String channelName; // 채널의 이름
    private final Set<UUID> userIds = new HashSet<>(); // 채널에 들어온 사용자들의 ID들과 사용자들의 이름 (중복 불가능)
    private final List<UUID> messageIds = new ArrayList<>(); // 채널에 보낸 메시지들의 ID들 (중복 가능)

    public Channel(String channelName) { // 생성자의 파라미터를 통해 초기화
        //super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        update();
    }

    public Set<UUID> getUsers() {
        return userIds;
    }

    public void addUser(UUID uuid) { // 채널에 사용자가 입장
        userIds.add(uuid);
    }

    public void removeUser(UUID uuid) { // 사용자가 채널 퇴장
        userIds.remove(uuid);
    }

    public void addMessage(UUID messageId) { // 채널에 어떤 사용자가 메시지를 보냄
        messageIds.add(messageId);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", userIds=" + userIds +
                ", messageIds=" + messageIds +
                '}' + super.toString();
    }
}
