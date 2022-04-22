package com.ewubd.servicebhai;

public class inboxArrayList {
    int fromID, toID;
    String messages;
    String dateTime;
    String name;

    public inboxArrayList(int fromID , int toID, String messages, String dateTime, String name) {
        this.fromID = fromID;
        this.toID = toID;
        this.messages = messages;
        this.dateTime = dateTime;
        this.name = name;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
