package com.ewubd.servicebhai;

public class chatArrayList {
    int fromID;
    String messages;

    public chatArrayList(int fromID, String messages) {
        this.fromID = fromID;
        this.messages = messages;
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

}
