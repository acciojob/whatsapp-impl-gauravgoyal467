package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<String ,User> userMap;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.userMap=new HashMap<String,User>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public int getCustomGroupCount() {
        return customGroupCount;
    }
    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    //1.
    public String createUser(String name, String mobile) throws Exception{
            if(userMap.containsKey(mobile)){
                throw new Exception("User already exists");
            }else{
                userMap.put(mobile,new User(name,mobile));
            }
        return "SUCCESS";
    }

    //2.
    public Group createGroup(List<User>users) {
        int n=users.size();
        Group grp=new Group();
        if(n==2){
            grp.setName(users.get(1).getName());
            grp.setNumberOfParticipants(n);
        }else{
            int count=getCustomGroupCount();
            setCustomGroupCount(count++);
            grp.setName("Group "+count++);
            grp.setNumberOfParticipants(n);
        }
        groupUserMap.put(grp,users);
        return grp;
    }

    //3.
    public int createMessage(String content){
        int id=getMessageId();
        int current=id++;
        Message m=new Message(current,content);
        setMessageId(current);
        return m.getId();
    }

    //4.
    public int sendMessage(Message message, User sender, Group group) throws Exception{
        boolean flag=true;
           if(!groupUserMap.containsKey(group)) {
               throw new Exception("Group does not exist");
           }
           List<User>users=new ArrayList<>();
           users=groupUserMap.get(group);
           for(User u:users) {
               if (u != sender) {
                   flag = false;
               }
           }
           if(flag==false) {
               throw new Exception("You are not allowed to send message");
           }
           List<Message>msg=new ArrayList<>();
           if(groupMessageMap.containsKey(group)){
               msg=groupMessageMap.get(group);
               msg.add(message);
           }else{
               msg.add(message);
           }
            groupMessageMap.put(group,msg);
            senderMap.put(message,sender);
           return groupMessageMap.get(group).size();
        }

    //5.
    public String changeAdmin(User approver, User user, Group group) throws Exception{
        boolean flag=true;
        if(!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }
        List<User>users=new ArrayList<>();
        users=groupUserMap.get(group);
        if(users.get(0) !=approver){
            throw new Exception("Approver does not have rights");
        }
        for(User u:users) {
            if (u !=user) {
                flag = false;
            }
        }
        if(flag==false) {
            throw new Exception("User is not a participant");
        }
        users.set(0,user);
        users.add(approver);
        groupUserMap.put(group,users);
        return "SUCCESS";
    }

    //6.
    public int removeUser(User user) throws Exception{
        boolean flag=false;int groupUserCount=0;int groupMsgCount=0;
        for(Group g:groupUserMap.keySet()){
            List<User>userlist=new ArrayList<>();
            userlist=groupUserMap.get(g);
            for(User u:userlist){
                if(u==user){
                    flag=true;
                    if(userlist.get(0)==user){
                        throw new Exception("Cannot remove admin");
                    }else{
                        userlist.remove(user);
                        for(Message msg:senderMap.keySet()){
                            if(senderMap.get(msg)==user){
                                int msgId=msg.getId();
                                List<Message>msgs=new ArrayList<>();
                                msgs=groupMessageMap.get(g);
                                for(Message m:msgs){
                                    if(m.getId()==msgId){
                                        msgs.remove(m);
                                    }
                                }
                                groupMessageMap.put(g,msgs);
                                senderMap.remove(msg,user);
                            }
                        }

                    }

                }
            }
        }
        if(flag==false){
            throw new Exception("User not found");
        }
        for(Group g:groupUserMap.keySet()) {
            groupUserCount = groupUserMap.get(g).size();
        }
        for(Group g:groupMessageMap.keySet()) {
            groupMsgCount = groupMessageMap.get(g).size();
        }
        int ans=groupUserCount+senderMap.size()+groupMsgCount;
        return ans ;
    }

    //7.
    public String findMessage(Date start, Date end, int K) throws Exception{
        List<Date>time=new ArrayList<>();
        for(Message m:senderMap.keySet()){
            Date d=new Date(m.getTimestamp().getTime());
            if(d.compareTo(start)>=0 && d.compareTo(start)<=0){
                time.add(m.getTimestamp());
            }
        }
        Date d=new Date();
        if(time.size()<K){
            throw new Exception("K is greater than the number of messages");
        }else{
            Collections.sort(time);
            d=time.get(time.size()-K);
        }
        String ans="";
        for(Message ms:senderMap.keySet()){
            if(ms.getTimestamp().compareTo(d)==0){
                ans=ms.getContent();
            }
        }
        return ans;
    }
}
