package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel extends Common{
    private String channelName; // 채널의 이름
    private Set<UUID> userIds = new HashSet<>(); // 채널에 들어온 사용자들의 ID들 (중복 불가능)

    public Channel(String channelName) { // // 생성자의 파라미터를 통해 초기화
        //super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public Set<UUID> getUserIds() {
        return userIds;
    }

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        update();
    }
}
