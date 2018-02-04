package com.example.elad.liskyproject.model;

import java.util.ArrayList;
import java.util.List;

public class SharedListData {
    private String sharedListID;
    private List<String> usersList;
    private List<SharedListItem> sharedListItems;

    public SharedListData(String sharedListID, List<String> usersList, List<SharedListItem> sharedListItems) {
        this.sharedListID = sharedListID;
        this.usersList = usersList;
        this.sharedListItems = sharedListItems;
    }
    public SharedListData(){
        usersList = new ArrayList<String>();
        sharedListItems = new ArrayList<SharedListItem>();
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
}
