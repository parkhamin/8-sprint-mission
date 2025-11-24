package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();

        User user1 = new User("박하민");
        User user2 = new User("박하은");
        User user3 = new User("박룽지");

        System.out.println("========== 사용자를 생성합니다. ==========");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        System.out.println("==================== 특정 사용자 조회 ====================");
        System.out.println("ID: " + userService.getUser(user1.getId()) + " 이름: " + userService.getUser(user1.getId()).getUserName()); // user1 사용자 조회

        System.out.println("==================== 사용자 정보 수정 ====================");
        System.out.print("이름: " + userService.getUser(user1.getId()).getUserName());
        userService.updateUser(user1.getId(), "변경된 박하민");
        System.out.println(" -> " + userService.getUser(user1.getId()).getUserName()); // user1 사용자 조회

        // 모든 회원 출력
        System.out.println("==================== 모든 사용자 조회 ====================");
        userService.getAllUsers()
                .stream()
                .forEach(user -> System.out.println("ID: " + user.getId() + " 이름: " + user.getUserName()));

        // 회원 삭제
        System.out.println("==================== 사용자 탈퇴 ====================");
        userService.deleteUser(user1.getId()); // 변경된 박하민 삭제

        // 삭제 후 모든 회원 출력
        userService.getAllUsers()
                .stream()
                .forEach(x -> System.out.println("ID: " + x.getId() + " 이름: " + x.getUserName()));
    }
}
