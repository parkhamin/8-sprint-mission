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
  private String name; // 채널의 이름
  private final ChannelType type;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.name = name;
    this.type = type;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    boolean isUpdated = false;

    if (newName != null && !newName.equals(this.name)) {
      this.name = newName;
      isUpdated = true;
    }

    if (newDescription != null && !newDescription.equals(this.description)) {
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
        ", name='" + name + '\'' +
        ", type=" + type +
        ", description='" + description + '\'' +
        '}';
  }
}
