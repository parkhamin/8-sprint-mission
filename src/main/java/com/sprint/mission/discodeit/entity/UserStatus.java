package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID userId;
    private Instant lastConnectAt;

    public UserStatus(UUID userId, Instant lastConnectAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.lastConnectAt = lastConnectAt;
    }

    public void update(Instant newLastConnectAt){
        boolean isUpdated = false;

        if (!newLastConnectAt.equals(this.lastConnectAt) && newLastConnectAt != null) {
            this.lastConnectAt = newLastConnectAt;
        }

        if (isUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public boolean isOnline() {
        return Instant.now().minusSeconds(300).isBefore(this.lastConnectAt);
    }
}
