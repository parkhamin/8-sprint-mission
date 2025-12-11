package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
    static User setupUser(UserService userService){
        User user = userService.createUser("user1");
        return user;
    }

    static Channel setupChannel(ChannelService channelService){
        Channel channel = channelService.createChannel("TEST");
        return channel;
    }

    static Message messageCreateTest(MessageService messageService, Channel channel, User sender){
        Message message = messageService.createMessage("Hello!", sender.getId(), channel.getId());
        System.out.println("메시지 생성: " + message.getId());
        System.out.println("[" + channel.getChannelName() + "] " + message.getContent());
        return message;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        channelService.addUserToChannel(user.getId(), channel.getId());

        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
