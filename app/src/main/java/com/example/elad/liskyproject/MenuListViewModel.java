package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.elad.liskyproject.model.SharedListDetails;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MenuListViewModel extends ViewModel {
   private LiveData<List<SharedListDetails>> sharedLists;

    public MenuListViewModel(){
        sharedLists = MenuListRepository.getInstance().getAllSharedListsForUserEMAIL(getUserEMail());
    }

    public LiveData<List<SharedListDetails>> getAllSharedListsDetails() {
        return sharedLists;
    }


    public void deleteSharedListForUser(SharedListDetails sharedList){
        MenuListRepository.getInstance().deleteSharedListForUserEMAIL(sharedList, getUserEMail());
    }

    public void addSharedListForUser(String sharedListName){
        MenuListRepository.getInstance().addSharedListForUserEMAIL(sharedListName, getUserEMail());
    }




    private String getUserEMail(){
        String eMail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_");
        return eMail;
    }

}
