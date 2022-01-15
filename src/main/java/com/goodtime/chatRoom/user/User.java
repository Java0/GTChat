package com.goodtime.chatRoom.user;

public class User {

    private String userName;

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserName() {
        return userName;
    }

    public User(String userName, String nickname){
        this.userName = userName;
        this.nickname = nickname;
    }

    public User(){}


}
