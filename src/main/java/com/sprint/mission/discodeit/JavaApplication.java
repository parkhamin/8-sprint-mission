package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.stream.Collectors;

public class JavaApplication {
    public static void main(String[] args) {
        /*// 1. JCF**Service 테스트 버전
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();*/

        // 2. File**Service 테스트 버전
        UserService userService = FileUserService.getInstance();
        ChannelService channelService = FileChannelService.getInstance();
        MessageService messageService = FileMessageService.getInstance();

        System.out.println("사용자 CRUD 테스트");
        System.out.println("사용자 생성");
        User user1 = userService.createUser("user1");
        User user2 = userService.createUser("user2");
        User user3 = userService.createUser("user3");

        System.out.println("사용자 단건 조회");
        System.out.println("사용자 Id: " + user1.getId() + " 사용자 이름: " + user1.getUserName());

        System.out.println("사용자 다건 조회");
        userService.getAllUsers().stream().forEach(System.out::println);

        System.out.println("사용자 수정");
        System.out.print("수정 전: " + user1.getUserName() + " -> 수정 후: ");
        userService.updateUser(user1.getId(), "수정된 user1");
        System.out.println(user1.getUserName());

        System.out.println("사용자 삭제");
        System.out.println("----- 수정된 user1 사용자 삭제된 것 확인 -----");
        userService.deleteUser(user1.getId());
        userService.getAllUsers().stream().forEach(System.out::println);

        System.out.println("채널 CRUD 테스트");
        System.out.println("채널 생성");
        Channel channel1 = channelService.createChannel("ch01");
        Channel channel2 = channelService.createChannel("ch02");
        Channel channel3 = channelService.createChannel("ch03");

        System.out.println("채널 단건 조회");
        System.out.println("채널 Id: " + channel1.getId() + " 채널 이름: " + channel1.getChannelName());

        System.out.println("채널 다건 조회");
        channelService.getAllChannels().stream().forEach(System.out::println);

        System.out.println("채널 수정");
        System.out.print("수정 전: " + channel1.getChannelName() + " -> 수정 후: ");
        channelService.updateChannel(channel1.getId(), "수정된 ch01");
        System.out.println(user1.getUserName());

        System.out.println("채널 삭제");
        System.out.println("----- 수정된 ch01 사용자 삭제된 것 확인 -----");
        channelService.deleteChannel(channel1.getId());
        channelService.getAllChannels().stream().forEach(System.out::println);

        System.out.println("메시지 CRUD 테스트");
        System.out.println("사용자가 채널에 참가");

        channelService.addUserToChannel(user2.getId(), channel2.getId()); // user2가 ch02에 참여
        channelService.addUserToChannel(user2.getId(), channel3.getId()); // user2가 ch03에 참여
        channelService.addUserToChannel(user3.getId(), channel3.getId()); // user3가 ch03에 참여

        System.out.println("ch03 채널에 참여한 사용자 출력");
        Channel channel = channelService.getChannel(channel3.getId());
        String userNames3 = channel.getUsers().stream() // 채널 3 에 참여한 모든 사용자들의 목록 (set) 을 받아서 stream으로 변환
                .map(id -> userService.getUser(id).getUserName()) // id 값을 받아서 User 객체의 사용자 이름 필드에 접근
                .collect(Collectors.joining(", "));
        System.out.println("[" + channel3.getChannelName() + "] " + userNames3);

        System.out.println("메시지 생성");
        Message message1 = messageService.createMessage("안녕하세요!", user2.getId(), channel2.getId());
        Message message2 = messageService.createMessage("메시지 CRUD 테스트 중입니다!", user2.getId(), channel3.getId());
        Message message3 = messageService.createMessage("채널03도 안녕하세요!", user3.getId(), channel3.getId());

        System.out.println("메시지 단건 조회");
        System.out.println("메시지 Id: " + message1.getId() + " 메시지 내용: " + message1.getContent());

        System.out.println("메시지 다건 조회");
        messageService.getAllMessages().stream().forEach(System.out::println);

        System.out.println("메시지 수정");
        System.out.print("수정 전: " + message1.getContent() + " -> 수정 후: ");
        messageService.updateMessage(message1.getId(), "또 다시 안녕하세요!");
        System.out.println(message1.getContent());

        System.out.println("메시지 삭제");
        System.out.println("----- 또 다시 안녕하세요! 메시지 삭제된 것 확인 -----");
        messageService.deleteMessage(message1.getId());
        messageService.getAllMessages().stream().forEach(System.out::println);
    }
}
