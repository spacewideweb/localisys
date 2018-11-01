package atpl.cc.localisys.Messaging;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageUserId;
    private String postCreatorID;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser, String messageUserId,String postCreatorID) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
        this. postCreatorID =  postCreatorID;
    }

    public ChatMessage(){

    }

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(String postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
