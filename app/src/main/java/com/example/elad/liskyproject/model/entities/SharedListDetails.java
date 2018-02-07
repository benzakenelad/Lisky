package com.example.elad.liskyproject.model.entities;


public class SharedListDetails {
    private String listName;
    private String listID;

    public SharedListDetails(String listName, String listID) {
        this.listName = listName;
        this.listID = listID;
    }

    public SharedListDetails(){

    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

}
