package com.example.collegedocumentsharing;

public class GroupsModel {

    private String name;

    private GroupsModel(){

    }

    public GroupsModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
