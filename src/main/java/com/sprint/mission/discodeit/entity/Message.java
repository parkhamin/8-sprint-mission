package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message extends Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String content; // 메시지의 내용
    private final UUID sender; // 메시지 발신자
    private final UUID channelId; // 메시지를 보내는 채널의 번호

    public Message(String content, UUID sender, UUID channelId) {
        this.content = content;
        this.sender = sender;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        update();
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                ", channelId=" + channelId +
                '}' + super.toString();
    }
}
