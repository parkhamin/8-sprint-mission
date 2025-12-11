package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message extends Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String content; // 메시지의 내용
    private final UUID senderId; // 메시지 발신자
    private final UUID channelId; // 메시지를 보내는 채널의 번호

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    /*public String getContent() {
        return content;
    }*/

    public void updateContent(String newContent) {
        this.content = newContent;
        update();
    }

    /*public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }*/

    @Override
    public String toString() {
        return "Message {" +
                "content='" + content + '\'' +
                ", senderId=" + senderId +
                ", channelId=" + channelId +
                '}' + " , " +super.toString();
    }
}
