package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.stream.Collectors;

public class FileJCFJavaApplication {
    public static void main(String[] args) {
        // 2. FILE**Service 버전
        UserService userService = new FileUserService(); // 인터페이스 타입으로 구현체를 받아서 사용한다는 의미
        FileMessageService messageService = new FileMessageService(userService, null);
        FileChannelService channelService = new FileChannelService(messageService);
        messageService.setChannelService(channelService);

        User user1 = new User("user1");
        User user2 = new User("user2");
        User user3 = new User("user3");

        System.out.println("==================== 사용자 생성 ====================");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userService.getAllUsers()
                .forEach(user -> System.out.println(user.getUserName()
                        + ", 객체가 만들어진 시간: " + user.getCreatedAt()));

        // 모든 회원 출력
        System.out.println("==================== 모든 사용자 조회 ====================");
        userService.getAllUsers().stream()
                .forEach(user -> System.out.println("ID: " + user.getId() + ", 이름: " + user.getUserName()));

        Channel channel1 = new Channel("ch01");
        Channel channel2 = new Channel("ch02");
        Channel channel3 = new Channel("ch03");

        System.out.println("==================== 채널 생성 ====================");
        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        channelService.getAllChannels()
                .forEach(channel -> System.out.println(channel.getChannelName()
                        + ", 객체가 만들어진 시간: " + channel.getCreatedAt()));

        System.out.println("==================== 모든 채널 조회 ====================");
        channelService.getAllChannels().stream()
                .forEach(channel -> System.out.println("채널 ID: " + channel.getId() + ", 채널 이름: " + channel.getChannelName()));

        System.out.println("==================== 사용자가 채널에 참여 ====================");
        /*
        채널 1              채널 2          채널 3
        사용자 1, 2, 3     사용자 2        사용자 2, 3
         */
        channel1.addUser(user1.getId()); // 사용자 1이 채널 1에 참여
        channel1.addUser(user2.getId());
        channel1.addUser(user3.getId());

        channel2.addUser(user2.getId());

        channel3.addUser(user2.getId());
        channel3.addUser(user3.getId());

        System.out.println("==================== 채널에 참여한 사용자 목록 ====================");
        /*String userNames1 = channel1.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel1.getChannelName() + "] " + userNames1);

        String userNames2 = channel2.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel2.getChannelName() + "] " + userNames2);

        String userNames3 = channel3.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel3.getChannelName() + "] " + userNames3);*/

        channelService.getAllChannels().forEach(channel -> {
            String userNames = channel.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                    .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                    .collect(Collectors.joining(", "));
            System.out.println("[" + channel.getChannelName() + "] " + userNames);
        });

        Message message1 = new Message("user2가 ch03에서 보내는 메시지입니다.", user2.getId(), channel3.getId());
        Message message2 = new Message("user1이 ch01에서 보내는 메시지입니다.", user1.getId(), channel1.getId());
        Message message3 = new Message("user3가 ch03에서 보내는 메시지입니다.", user3.getId(), channel3.getId());
        Message message4 = new Message("user2가 ch01에서 보내는 메시지입니다.", user2.getId(), channel1.getId());
        Message message5 = new Message("user2가 ch02에서 보내는 메시지입니다.", user2.getId(), channel2.getId());
        // Message message6 = new Message("잘못된 사용자", user1.getId(), channel2.getId());

        System.out.println("==================== 메시지 생성 ====================");
        messageService.createMessage(message1);
        messageService.createMessage(message2);
        messageService.createMessage(message3);
        messageService.createMessage(message4);
        messageService.createMessage(message5);
        // messageService.createMessage(message6);

        messageService.getAllMessages()
                .forEach(message -> System.out.println(message.getContent()
                        + ", 객체가 만들어진 시간: " + message.getCreatedAt()));

        //System.out.println("========== 메시지 보냄 ==========");
        channelService.sendMessage(channel3.getId(), message1.getId());
        channelService.sendMessage(channel1.getId(), message2.getId());
        channelService.sendMessage(channel3.getId(), message3.getId());
        channelService.sendMessage(channel1.getId(), message4.getId());
        channelService.sendMessage(channel2.getId(), message5.getId());
        //channelService.sendMessage(channel3, message6);

        /* 1. 모든 메시지들의 목록을 향상된 for문으로 작성
        for (Message message : messageService.getAllMessages()) {
            System.out.println(message.getContent()); // 전체 출력
        }*/

        /* 2. 향상된 for문 바탕으로 스트림과 람다식을 이용해서 작성
        messageService.getAllMessages().stream() // 모든 메시지들이 저장된 messages의 map을 stream으로 변환
                .filter(message -> message.getChannelId().equals(channel1.getId())) // 메시지의 채널 ID가 채널 1번과 같으면 필터
                        .forEach(message -> System.out.println("[" + channel1.getChannelName() + "] "
                                + message.getContent())); // 해당하는 요소들을 반복문을 통해 내용만 출력

        messageService.getAllMessages().stream() // 모든 메시지들이 저장된 messages
                .filter(message -> message.getChannelId().equals(channel2.getId()))
                .forEach(message -> System.out.println("[" + channel2.getChannelName() + "] "
                        + message.getContent()));

        messageService.getAllMessages().stream() // 모든 메시지들이 저장된 messages
                .filter(message -> message.getChannelId().equals(channel3.getId()))
                .forEach(message -> System.out.println("[" + channel3.getChannelName() + "] "
                        + message.getContent()));*/
        // 3. 각 채널에 순회한다는 점에서 이중 for-each문으로 발전
        System.out.println("==================== 메시지 목록 ====================");
        channelService.getAllChannels().forEach(channel -> {
            messageService.getAllMessages().stream() // 모든 메시지들이 저장된 messages의 map을 stream으로 변환
                    .filter(message -> message.getChannelId().equals(channel.getId())) // 메시지의 채널 ID가 채널 1번과 같으면 필터
                    .forEach(message -> System.out.println("[" + channel.getChannelName() + "] "
                            + message.getContent())); // 해당하는 요소들을 반복문을 통해 내용만 출력
        });

        // 사용자가 채널에서 나감
        System.out.println("==================== 사용자가 채널에서 나감 ====================");
        System.out.println("==================== 채널에 남아있는 사람 목록 ====================");
        channel1.removeUser(user1.getId());
        String userNames = channel1.getUsers().stream() // 채널 1 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel1.getChannelName() + "] " + userNames);

        System.out.println("==================== 사용자 정보 수정 ====================");
        System.out.print("이름: " + userService.getUser(user1.getId()).getUserName());
        userService.updateUser(user1.getId(), "변경된 user1");
        System.out.println(" -> " + userService.getUser(user1.getId()).getUserName() + ", 정보가 수정된 시간: " + user1.getUpdatedAt()); // user1 특정 사용자 조회

        System.out.println("==================== 채널 정보 수정 ====================");
        System.out.print("채널 이름: " + channelService.getChannel(channel1.getId()).getChannelName());
        channelService.updateChannel(channel1.getId(), "변경된 ch01");
        System.out.println(" -> " + channelService.getChannel(channel1.getId()).getChannelName() + ", 정보가 수정된 시간: " + channel1.getUpdatedAt());

        System.out.println("==================== 메시지 정보 수정 ====================");
        System.out.print("메시지 내용: " + messageService.getMessage(message1.getId()).getContent());
        messageService.updateMessage(message1.getId(),"user2가 ch03에서 보내는 변경된 메시지입니다.");
        System.out.println(" -> " + messageService.getMessage(message1.getId()).getContent() + ", 정보가 수정된 시간: " + message1.getUpdatedAt());

        System.out.println("==================== 메시지 삭제 후 모든 메시지 목록 ====================");
        System.out.println("\"user2가 ch03에서 보내는 변경된 메시지입니다.\" 삭제된 것 확인");
        messageService.deleteMessage(message1.getId());
        channelService.getAllChannels().forEach(channel -> {
            messageService.getAllMessages().stream() // 모든 메시지들이 저장된 messages의 map을 stream으로 변환
                    .filter(message -> message.getChannelId().equals(channel.getId())) // 메시지의 채널 ID가 채널 1번과 같으면 필터
                    .forEach(message -> System.out.println("[" + channel.getChannelName() + "] "
                            + message.getContent())); // 해당하는 요소들을 반복문을 통해 내용만 출력
        });

        // 채널 삭제
        System.out.println("==================== 채널 삭제 ====================");
        channelService.deleteChannel(channel1.getId());
        System.out.println("==================== 삭제 후 채널 목록 조회 ====================");
        System.out.println("\"변경된 ch01\" 채널 삭제된 것 확인");
        channelService.getAllChannels().stream()
                .forEach(channel -> System.out.println("채널 ID: " + channel.getId() + " 채널 이름: " + channel.getChannelName()));

        // 회원 삭제
        System.out.println("==================== 사용자 탈퇴 ====================");
        userService.deleteUser(user1.getId());
        System.out.println("\"변경된 user1\" 삭제된 것 확인");

        // 탈퇴 후 모든 회원 출력
        userService.getAllUsers()
                .stream()
                .forEach(user -> System.out.println("ID: " + user.getId() + " 이름: " + user.getUserName()));
    }
}
