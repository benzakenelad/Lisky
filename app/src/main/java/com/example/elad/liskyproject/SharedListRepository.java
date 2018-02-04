package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.elad.liskyproject.model.FirebaseModel;
import com.example.elad.liskyproject.model.SharedListData;



public class SharedListRepository {
    private static SharedListRepository instance = new SharedListRepository();
    private MutableLiveData<SharedListData> sharedListDataMutableLiveData;

    private SharedListRepository(){

    }

    public static SharedListRepository getInstance(){
        return instance;
    }

    public LiveData<SharedListData> getSpecificSharedListDataByListID(String sharedListID){
        synchronized (this){
            if(sharedListDataMutableLiveData == null){
                sharedListDataMutableLiveData = new MutableLiveData<>();
                FirebaseModel.getSharedListDataByIDAndObserve(new FirebaseModel.Callback<SharedListData>() {
                    @Override
                    public void onComplete(SharedListData data) {
                        if(data != null)
                            sharedListDataMutableLiveData.setValue(data);
                    }
                }, sharedListID);
            }
        }
        return sharedListDataMutableLiveData;
    }


}
