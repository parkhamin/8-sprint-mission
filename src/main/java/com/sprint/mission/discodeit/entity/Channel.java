package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class Channel extends Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String channelName; // 채널의 이름

    public Channel(String channelName) { // 생성자의 파라미터를 통해 초기화
        super();
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

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
