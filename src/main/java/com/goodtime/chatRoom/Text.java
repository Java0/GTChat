package com.goodtime.chatRoom;

public class Text {

    private String sender;

    private String type;

    private String content;

    public String getContent() {
        return content;
    }

    public Text(){}

    public Text( String type, String sender, String content) {
        this.sender = sender;
        this.type = type;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getType() {
        return type;
    }





}
