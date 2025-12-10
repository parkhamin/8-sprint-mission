package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class BasicJavaApplication {
    static User setupUser(UserService userService){
        User user = userService.createUser("user1");
        return user;
    }

    static Channel setupChannel(ChannelService channelService){
        Channel channel = channelService.createChannel("TEST");
        return channel;
    }

    static Message messageCreateTest(MessageService messageService, Channel channel, User sender){
        Message message = messageService.createMessage("Hello! JCF*Repository Test!", sender.getId(), channel.getId());
        System.out.println("메시지 생성: " + message.getId());
        System.out.println("[" + channel.getChannelName() + "] " + message.getContent());
        return message;
    }

    public static void main(String[] args) {
        // 각 service와 repository 필요함
        // service는 repository에 의존함. 즉 repository 먼저 선언해야함.
        // 1. JCF*Repository
        /*System.out.println("======== JCF*Repository 구현체 활용 테스트 ========");
        UserRepository userRepo = JCFUserRepository.getInstance();
        MessageRepository messageRepo = JCFMessageRepository.getInstance();
        ChannelRepository channelRepo = JCFChannelRepository.getInstance();*/

        // 2. File*Repository
        System.out.println("======== File*Repository 구현체 활용 테스트 ========");
        UserRepository userRepo = FileUserRepository.getInstance();
        MessageRepository messageRepo = FileMessageRepository.getInstance();
        ChannelRepository channelRepo = FileChannelRepository.getInstance();

        UserService userService = new BasicUserService(userRepo);
        ChannelService channelService = new BasicChannelService(userRepo, channelRepo);
        MessageService messageService = new BasicMessageService(userRepo, channelRepo, messageRepo);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        /*messageCreateTest(messageService, channel, user);
        사용자가 채널에 참여하지 않았기 때문에 오류가 난다.
         */

        channelService.addUserToChannel(user.getId(), channel.getId());

        messageCreateTest(messageService, channel, user); // 테스트
    }
}
