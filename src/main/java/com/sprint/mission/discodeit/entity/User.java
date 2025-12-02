package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

public class User extends Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userName; // 사용자의 이름

    public User(String userName) { // 생성자의 파라미터를 통해 초기화
        //super(); 부모 클래스에 기본 생성자(파라미터 없는 생성자) 가 있으면 super()를 굳이 안 써도 동일하게 작동함.
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void updateUserName(String newUserName) {
        this.userName = newUserName;
        update();
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}' + super.toString();
    }
}
