package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.elad.liskyproject.model.SharedListData;
import com.example.elad.liskyproject.model.SharedListDetails;

import java.util.List;


public class SharedListViewModel extends ViewModel {
    private LiveData<SharedListData> sharedListData;

    public SharedListViewModel(){

    }

    public LiveData<SharedListData> getSpecificSharedListDataByListID(String sharedListID){
        return SharedListRepository.getInstance().getSpecificSharedListDataByListID(sharedListID);
    }

}
