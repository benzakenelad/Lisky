package com.example.elad.liskyproject.model;


public class SharedListDetails {
    private String listName;
    private String listID;
    private int membersAmount;

    public SharedListDetails(String listName, String listID, int membersAmount) {
        this.listName = listName;
        this.listID = listID;
        this.membersAmount = membersAmount;
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

    public int getMembersAmount() {
        return membersAmount;
    }

    public void setMembersAmount(int membersAmount) {
        this.membersAmount = membersAmount;
    }
}
