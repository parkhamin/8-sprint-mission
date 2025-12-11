package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String channelName; // 채널의 이름
    private final ChannelType type;
    private String description;

    public Channel(ChannelType type, String channelName, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.channelName = channelName;
        this.type = type;
        this.description = description;
    }

    public void update(String newChannelName , String newDescription) {
        boolean isUpdated = false;

        if (!this.channelName.equals(newChannelName) && newChannelName != null) {
            this.channelName = newChannelName;
            isUpdated = true;
        }

        if (!this.description.equals(newDescription) && newDescription != null) {
            this.description = newDescription;
            isUpdated = true;
        }

        if (isUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "Channel {" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", channelName='" + channelName + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
