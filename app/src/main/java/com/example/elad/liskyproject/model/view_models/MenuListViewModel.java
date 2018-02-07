package com.example.elad.liskyproject.model.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.repositories.MenuListRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MenuListViewModel extends ViewModel {
    private LiveData<List<SharedListDetails>> sharedLists;

    public MenuListViewModel() {
        sharedLists = MenuListRepository.getInstance().getAllSharedListsForUserEMAIL(getUserEMail());
    }

    public LiveData<List<SharedListDetails>> getAllSharedListsDetails() {
        return sharedLists;
    }


    public void addSharedListForUser(String sharedListName) {
        MenuListRepository.getInstance().addSharedListForUserEMAIL(sharedListName, getUserEMail());
    }

    public void removeSharedListForUserEMAIL(String sharedListID) {
        MenuListRepository.getInstance().removeSharedListForUserEMAIL(sharedListID, getUserEMail());
    }


    private String getUserEMail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_");
    }

}
