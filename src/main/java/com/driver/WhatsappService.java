package com.driver;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;
        import java.util.*;

@Service
public class WhatsappService {

    WhatsappRepository whatsappRepository=new WhatsappRepository();
    //1.
    public String createUser(String name, String mobile) throws Exception{
        return whatsappRepository.createUser(name,mobile);
    }

    //2.
    public Group createGroup(List<User>users) {
        return whatsappRepository.createGroup(users);
    }

    //3.
    public int createMessage(String content){
        return whatsappRepository.createMessage(content);
    }

    //4.
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        return whatsappRepository.sendMessage(message, sender, group);
    }

    //5.
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        return whatsappRepository.changeAdmin(approver, user, group);
    }

    //6.
    public int removeUser(User user) throws Exception{
        return whatsappRepository.removeUser(user);
    }

    //7.
    public String findMessage(Date start, Date end, int K) throws Exception{
        return whatsappRepository.findMessage(start, end, K);
    }
}