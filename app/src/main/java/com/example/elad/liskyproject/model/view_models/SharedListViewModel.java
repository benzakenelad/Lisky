package com.example.elad.liskyproject.model.view_models;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;

import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;
import com.example.elad.liskyproject.model.repositories.SharedListRepository;


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

    // add partner to list
    public void addUserEmailToSharedList(SharedListDetails sharedListDetails, String partnerEMAIL, final FirebaseModel.UserNotExistCallBack callback){
        SharedListRepository.getInstance().addUserEmailToSharedList(sharedListDetails, partnerEMAIL, callback);
    }

    public void removeSharedListItemBySharedListID(String sharedListID, String itemID){
        SharedListRepository.getInstance().removeSharedListItemBySharedListID(sharedListID,itemID);
    }

    public void editSharedListItem(String sharedListID, String itemID, SharedListItem sharedListItem) {
        SharedListRepository.getInstance().editSharedListItem(sharedListID,itemID,sharedListItem);
    }

    public void saveImage(Bitmap imageBmp, String itemID, final FirebaseModel.SaveImageListener listener){
        SharedListRepository.getInstance().saveImage(imageBmp, itemID, listener);
    }

    public void getImage(String imageURI, final FirebaseModel.GetImageListener listener) {
        SharedListRepository.getInstance().getImage(imageURI,listener);
    }
}
