package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.elad.liskyproject.model.FirebaseModel;
import com.example.elad.liskyproject.model.SharedListData;
import com.example.elad.liskyproject.model.SharedListDetails;
import com.example.elad.liskyproject.model.SharedListItem;

import java.util.List;


public class SharedListViewModel extends ViewModel {
    private LiveData<SharedListData> sharedListData;

    public SharedListViewModel(){

    }

    public LiveData<SharedListData> getSpecificSharedListDataByListID(String sharedListID){
        return SharedListRepository.getInstance().getSpecificSharedListDataByListID(sharedListID);
    }

    public void addSharedListItemBySharedListID(SharedListItem sharedListItem, String sharedListID){
        SharedListRepository.getInstance().addSharedListItemBySharedListID(sharedListItem, sharedListID);
    }

    public void addUserEmailToSharedList(SharedListDetails sharedListDetails, String partnerEMAIL, final FirebaseModel.UserNotExistCallBack callback){
        SharedListRepository.getInstance().addUserEmailToSharedList(sharedListDetails, partnerEMAIL, callback);
    }

}
