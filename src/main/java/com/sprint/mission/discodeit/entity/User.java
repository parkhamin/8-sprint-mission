package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String userName; // 사용자의 이름
    private String email;
    private String password;
    private UUID profileId;

    public User(String userName, String email, String password, UUID profileId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String newUserName, String newEmail, String newPassword, UUID newProfileId) {
        boolean isUpdate = false;

        if (!this.userName.equals(newUserName) && newUserName != null) {
            this.userName = newUserName;
            isUpdate = true;
        }

        if (!this.email.equals(newEmail) && newEmail != null) {
            this.email = newEmail;
            isUpdate = true;
        }

        if (!this.password.equals(newPassword) && newPassword != null) {
            this.password = newPassword;
            isUpdate = true;
        }

        if (!this.profileId.equals(newProfileId) && newProfileId != null) {
            this.profileId = newProfileId;
            isUpdate = true;
        }

        if (isUpdate) {
            this.updatedAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "User {" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profileId=" + profileId +
                '}';
    }
}
