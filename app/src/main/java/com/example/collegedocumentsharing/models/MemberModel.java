package com.example.collegedocumentsharing.models;

public class MemberModel {

    String username;
    Boolean isOwner;

    public MemberModel(){
        //Require empty for firebase
    }

    public MemberModel(String username, Boolean isOwner) {
        this.username = username;
        this.isOwner = isOwner;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }
}
