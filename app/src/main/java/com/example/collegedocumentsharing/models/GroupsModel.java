package com.example.collegedocumentsharing.models;

import java.util.List;

public class GroupsModel {

    private String name;
    private String code;
    private String owner;
    private List<String> members;

    private GroupsModel(){
        //Require empty for firebase
    }
    public GroupsModel(String name, String code, String owner, List<String> members) {
        this.name = name;
        this.code = code;
        this.owner = owner;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
