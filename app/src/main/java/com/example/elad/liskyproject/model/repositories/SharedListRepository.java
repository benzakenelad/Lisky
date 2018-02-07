package com.example.elad.liskyproject.model.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;

import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;
import com.google.firebase.auth.FirebaseAuth;


public class SharedListRepository {
    private static SharedListRepository instance = new SharedListRepository();
    private MutableLiveData<SharedListData> sharedListDataMutableLiveData;

    private SharedListRepository() {

    }

    public static SharedListRepository getInstance() {
        return instance;
    }

    public LiveData<SharedListData> getSpecificSharedListDataByListID(String sharedListID) {
        synchronized (this) {
            if (sharedListDataMutableLiveData == null || ((sharedListDataMutableLiveData.getValue() != null) && (!sharedListDataMutableLiveData.getValue().getSharedListID().equals(sharedListID)))) {
                sharedListDataMutableLiveData = new MutableLiveData<>();
                FirebaseModel.getSharedListDataByIDAndObserve(new FirebaseModel.Callback<SharedListData>() {
                    @Override
                    public void onComplete(SharedListData data) {
                        if (data != null)
                            sharedListDataMutableLiveData.setValue(data);
                    }
                }, sharedListID);
            }
        }
        return sharedListDataMutableLiveData;
    }

    public void addSharedListItemBySharedListID(SharedListItem sharedListItem, String sharedListID) {
        FirebaseModel.addSharedListItemBySharedListID(sharedListItem, sharedListID);
    }

    public void addUserEmailToSharedList(SharedListDetails sharedListDetails, String partnerEMAIL, final FirebaseModel.UserNotExistCallBack callback) {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_");
        FirebaseModel.addUserEmailToSharedList(sharedListDetails, partnerEMAIL, currentUserEmail, callback);
    }

    public void removeSharedListItemBySharedListID(String sharedListID, String itemID){
        FirebaseModel.removeSharedListItemBySharedListID(sharedListID, itemID);
    }

    public void editSharedListItem(String sharedListID, String itemID, SharedListItem sharedListItem) {
        FirebaseModel.editSharedListItem(sharedListID,itemID,sharedListItem);
    }

    public void saveImage(Bitmap imageBmp, String itemID, final FirebaseModel.SaveImageListener listener){
        FirebaseModel.saveImage(imageBmp, itemID, listener);
    }

//    public void getImage(String imageURI, final FirebaseModel.GetImageListener listener) {
//        FirebaseModel.getImage(imageURI,listener);
//    }
}
