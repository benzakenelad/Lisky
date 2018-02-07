package com.example.elad.liskyproject.model.entities;

import java.util.ArrayList;
import java.util.List;

public class SharedListData {
    private String sharedListName;
    private String sharedListID;
    private List<String> usersList;
    private List<SharedListItem> sharedListItems;

    public SharedListData(){
        usersList = new ArrayList<>();
        sharedListItems = new ArrayList<>();
    }

    public String getSharedListID() {
        return sharedListID;
    }

    public void setSharedListID(String sharedListID) {
        this.sharedListID = sharedListID;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }

    public List<SharedListItem> getSharedListItems() {
        return sharedListItems;
    }

    public void setSharedListItems(List<SharedListItem> sharedListItems) {
        this.sharedListItems = sharedListItems;
    }

    public String getSharedListName() {
        return sharedListName;
    }

    public void setSharedListName(String sharedListName) {
        this.sharedListName = sharedListName;
    }
}
