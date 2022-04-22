package com.ewubd.servicebhai;

public class chatArrayList {
    int fromID;
    String messages;
    String date;

    public chatArrayList(int fromID, String messages, String date) {
        this.fromID = fromID;
        this.messages = messages;
        this.date = date;
    }
    public chatArrayList(){}

    public int getFromIDid() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
