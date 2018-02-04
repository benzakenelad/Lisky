package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.elad.liskyproject.model.FirebaseModel;
import com.example.elad.liskyproject.model.SharedListDetails;

import java.util.List;

/**
 * Created by elad on 2/4/2018.
 */

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

    public void deleteSharedListForUserEMAIL(SharedListDetails sharedList, String userEMAIL){

    }

    public void addSharedListForUserEMAIL(String sharedListName, String userEMAIL){
        FirebaseModel.addSharedListForUserEMAIL(sharedListName, userEMAIL);
    }
}
