package com.example.elad.liskyproject.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;

import java.util.List;


public class MenuListRepository {
    private static final MenuListRepository instance = new MenuListRepository();
    private MutableLiveData<List<SharedListDetails>> sharedListsLiveData;

    private MenuListRepository(){

    }

    public static MenuListRepository getInstance(){
        return instance;
    }

    public LiveData<List<SharedListDetails>> getAllSharedListsForUserEMAIL(String userEMAIL){
        synchronized (this){
            if(sharedListsLiveData == null){
                sharedListsLiveData = new MutableLiveData<>();
                FirebaseModel.getAllSharedListsForUserEMAILAndObserver(new FirebaseModel.Callback<List<SharedListDetails>>() {
                    @Override
                    public void onComplete(List<SharedListDetails> data) {
                        if(data != null)
                            sharedListsLiveData.setValue(data);
                    }
                }, userEMAIL);
            }
        }
        return sharedListsLiveData;
    }


    public void addSharedListForUserEMAIL(String sharedListName, String userEMAIL){
        FirebaseModel.addSharedListForUserEMAIL(sharedListName, userEMAIL);
    }

    public void removeSharedListForUserEMAIL(String sharedListID, String userEMAIL){
        FirebaseModel.removeSharedListForUserEMAIL(sharedListID,userEMAIL);
    }
}
