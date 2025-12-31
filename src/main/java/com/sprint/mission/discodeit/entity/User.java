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
  private String username; // 사용자의 이름
  private String email;
  private String password;
  private UUID profileId;

  public User(String username, String email, String password, UUID profileId) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.username = username;
    this.email = email;
    this.password = password;
    this.profileId = profileId;
  }

  public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
    boolean isUpdate = false;

    if (!this.username.equals(newUsername) && newUsername != null) {
      this.username = newUsername;
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
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", profileId=" + profileId +
        '}';
  }
}
