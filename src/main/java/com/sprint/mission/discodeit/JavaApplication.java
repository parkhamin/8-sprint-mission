package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.UUID;
import java.util.stream.Collectors;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService(); // 인터페이스 타입으로 구현체를 받아서 사용한다는 의미
        ChannelService channelService = new JCFChannelService();

        User user1 = new User("user1");
        User user2 = new User("user2");
        User user3 = new User("user3");

        System.out.println("========== 사용자 생성 ==========");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        System.out.println("==================== 특정(단일) 사용자 조회 ====================");
        System.out.println("ID: " + userService.getUser(user1.getId())
                + " 이름: " + userService.getUser(user1.getId()).getUserName()); // user1 사용자 조회

        System.out.println("==================== 사용자 정보 수정 ====================");
        System.out.print("이름: " + userService.getUser(user1.getId()).getUserName());
        userService.updateUser(user1.getId(), "변경된 user1");
        System.out.println(" -> " + userService.getUser(user1.getId()).getUserName()); // user1 사용자 조회

        // 모든 회원 출력
        System.out.println("==================== 모든 사용자 조회 ====================");
        userService.getAllUsers().stream()
                .forEach(user -> System.out.println("ID: " + user.getId() + " 이름: " + user.getUserName()));

        Channel channel1 = new Channel("ch01");
        Channel channel2 = new Channel("ch02");
        Channel channel3 = new Channel("ch03");

        System.out.println("========== 채널 생성 ==========");
        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        System.out.println("========== 특정 채널 조회 ==========");
        System.out.println("채널 ID: " + channelService.getChannel(channel1.getId())
                + " 채널 이름: " + channelService.getChannel(channel1.getId()).getChannelName());

        System.out.println("========== 채널 정보 수정 ==========");
        System.out.print("채널 이름: " + channelService.getChannel(channel1.getId()).getChannelName());
        channelService.updateChannel(channel1.getId(), "변경된 ch01");
        System.out.println("-> " + channelService.getChannel(channel1.getId()).getChannelName());

        //System.out.println("========== 사용자가 채널에 참여 ==========");
        /*
        채널 1              채널 2          채널 3
        사용자 1, 2, 3     사용자 2        사용자 2, 3
         */
        channel1.addUser(user1.getId()); // 사용자 1이 채널 1에 참여
        channel1.addUser(user2.getId());
        channel1.addUser(user3.getId());

        channel2.addUser(user2.getId());

        System.out.println("========== 채널에 참여한 사용자 목록 조회 ==========");
        String userNames = channel1.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel1.getChannelName() + "] " + userNames);

        // 사용자가 채널에서 나감
        System.out.println("========== 사용자가 나가고 나머지 사용자 목록 조회 ==========");
        channel1.removeUser(user1.getId());
        String un = channel1.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel1.getChannelName() + "] " + un);


        System.out.println("==================== 모든 채널 조회 ====================");
        channelService.getAllChannels().stream()
                .forEach(channel -> System.out.println("채널 ID: " + channel.getId() + " 채널 이름: " + channel.getChannelName()));

        //channelService.sendMessage(channel1.getId(), ); 메시지 객체 아직 미구현

        // 채널 삭제
        System.out.println("==================== 채널 삭제 ====================");
        channelService.deleteChannel(channel1.getId());
        System.out.println("==================== 삭제 후 채널 목록 조회 ====================");
        channelService.getAllChannels().stream()
                .forEach(channel -> System.out.println("채널 ID: " + channel.getId() + " 채널 이름: " + channel.getChannelName()));

        // 회원 삭제
        System.out.println("==================== 사용자 탈퇴 ====================");
        userService.deleteUser(user1.getId());

        // 탈퇴 후 모든 회원 출력
        userService.getAllUsers()
                .stream()
                .forEach(x -> System.out.println("ID: " + x.getId() + " 이름: " + x.getUserName()));
    }
}
