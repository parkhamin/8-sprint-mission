package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt; // 마지막으로 메시지를 표현하기 위함

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void update(Instant newLastReadAt) {
        boolean isUpdated = false;

        if (!newLastReadAt.equals(this.lastReadAt) && newLastReadAt != null) {
            this.lastReadAt = newLastReadAt;
        }

        if (isUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
