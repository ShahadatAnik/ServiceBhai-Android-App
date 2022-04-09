package com.ewubd.servicebhai;

public class inboxArrayList {
    int fromID, toID;
    String messages;

    public inboxArrayList(int fromID , int toID, String messages) {
        this.fromID = fromID;
        this.toID = toID;
        this.messages = messages;
    }
    public inboxArrayList(){}

    public int getFromIDid() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

}
