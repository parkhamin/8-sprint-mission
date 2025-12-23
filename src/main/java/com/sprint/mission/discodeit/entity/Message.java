package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String content; // 메시지의 내용
    private final UUID senderId; // 메시지 발신자
    private final UUID channelId; // 메시지를 보내는 채널의 번호
    private List<UUID> attachmentIds;

    public Message(String content, UUID channelId, UUID senderId,List<UUID> attachmentIds) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.attachmentIds = attachmentIds;
    }

    public void update(String newContent) {
        if (!newContent.equals(this.content) && newContent != null) {
            this.content = newContent;
            this.updatedAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "Message {" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", channelId=" + channelId +
                ", attachmentIds=" + attachmentIds +
                '}';
    }
}
