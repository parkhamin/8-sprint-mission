package com.sprint.mission.discodeit.entity;

import java.util.UUID;

abstract class Common { // 공통으로 사용 -> 추상클래스로 선언
    // final : 변경 불가능, 한 번 값이 할당되면 변경할 수 없음
    // private final : 객체 수준에서 불변, 한 번 값을 넣으면 다시 변경 불가능
    // 객체마다 다른데 변경은 되면 안될 때 사용
    // private static final : 클래스 수준에서 단 하나만 존재하는 불변하는 값
    // 모든 객체에서 공통으로 사용되는 상수(최대 길이, 설정값, 고정 문자열 등)
    // protected: 하위 클래스가 접근해야함.
    protected final UUID id; // 객체를 식별하기 위한 id
    protected final long createdAt; // 객체 생성 시간
    protected long updatedAt; // 객체 수정 시간

    protected Common() { // 필드 변수들 생성자에서 초기화
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

    @Override
    public String toString() {
        return "Common{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
