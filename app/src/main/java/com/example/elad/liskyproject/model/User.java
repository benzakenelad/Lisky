package com.example.elad.liskyproject.model;

import java.util.List;

public class User {
    private String userEMAIL;
    private List<SharedListDetails> referencedSharedLists;

    public User(){

    }

    public String getUserEMAIL() {
        return userEMAIL;
    }

    public void setUserEMAIL(String userEMAIL) {
        this.userEMAIL = userEMAIL;
    }

    public List<SharedListDetails> getReferencedSharedLists() {
        return referencedSharedLists;
    }

    public void setReferencedSharedLists(List<SharedListDetails> referencedSharedLists) {
        this.referencedSharedLists = referencedSharedLists;
    }
}
