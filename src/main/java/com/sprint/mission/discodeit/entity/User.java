package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends Common{
    private String userName; // 사용자의 이름
    private Set<UUID> channelIds = new HashSet<>(); // 사용자가 들어간 채널의 목록(중복되면 안되므로 list보다는 set)

    public User(String userName) { // 생성자의 파라미터를 통해 초기화
        //super(); 부모 클래스에 기본 생성자(파라미터 없는 생성자) 가 있으면 super()를 굳이 안 써도 동일하게 작동함.
        this.userName = userName;
    }

    public Set<UUID> getChannelIds() {
        return channelIds;
    }

    public String getUserName() {
        return userName;
    }

    public void updateUserName(String newUserName) {
        this.userName = newUserName;
        update();
    }
}
