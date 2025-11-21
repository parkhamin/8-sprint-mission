package com.sprint.mission.discodeit.entity;

public class Message extends Common {
    private String content; // 메시지의 내용
    private User sender; // 메시지 발신자
    private Channel channelId; // 메시지를 보내는 채널의 번호

    public Message(String content, User sender, Channel channelId) {
        this.content = content;
        this.sender = sender;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public User getSender() {
        return sender;
    }

    public Channel getChannelId() {
        return channelId;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
        update();
    }
}
