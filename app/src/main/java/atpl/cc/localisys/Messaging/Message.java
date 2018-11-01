package atpl.cc.localisys.Messaging;

/**
 * Created by designer on 30/3/18.
 */

public class Message {

    public String messageText;
    public String recipientId;
    public String senderId;
    public String timestamp;

    public Message() {
    }

    public Message(String messageText, String recipientId, String senderId, String timestamp) {
        this.messageText = messageText;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
