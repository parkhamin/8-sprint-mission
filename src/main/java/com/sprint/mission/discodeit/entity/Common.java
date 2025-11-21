package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Common {
    protected UUID id; // 객체를 식별하기 위한 id
    protected long createdAt; // 객체 생성 시간
    protected long updatedAt; // 객체 수정 시간

    public Common() { // 필드 변수들 생성자에서 초기화
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }
    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    protected void update(){
        this.updatedAt = System.currentTimeMillis();
    }
}
