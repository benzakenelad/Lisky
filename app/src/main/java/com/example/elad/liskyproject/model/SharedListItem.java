package com.example.elad.liskyproject.model;


public class SharedListItem {
    private String itemID;
    private String itemName;
    private String description;
    private String imageURL;

    public SharedListItem(String itemName, String imageURL, String description){
        this.itemName = itemName;
        this.imageURL = imageURL;
        this.description = description;
    }

    public SharedListItem(){

    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
