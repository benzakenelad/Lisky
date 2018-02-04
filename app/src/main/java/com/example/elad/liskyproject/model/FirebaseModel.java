package com.example.elad.liskyproject.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseModel {

    public interface Callback<T> {
        void onComplete(T data);
    }

    public static void getAllSharedListsForUserEMAILAndObserver(final Callback<List<SharedListDetails>> callback, String userEMAIL){

        Log.d("TAG", "getAllSharedListsForUserEMAILAndObserver");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(userEMAIL);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SharedListDetails> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    SharedListDetails sld = snap.getValue(SharedListDetails.class);
                    list.add(sld);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });

    }

    public static void addSharedListForUserEMAIL(String sharedListName, String userEMAIL){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference demoRef = database.getReference("users").child(userEMAIL).push();
        final String sharedListID = demoRef.getKey();

        DatabaseReference saveRef = database.getReference("users").child(userEMAIL);
        SharedListDetails sharedListDetails = new SharedListDetails(sharedListName, sharedListID, 1);
        Map<String,Object> map = new HashMap<>();
        map.put(sharedListID, sharedListDetails);

        saveRef.updateChildren(map);

        createNewList(sharedListDetails,userEMAIL);
    }

    public static void createNewList(SharedListDetails sharedListDetails, String creatorEMAIL){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference createRef = database.getReference("lists").child(sharedListDetails.getListID());

        SharedListData sharedListData = new SharedListData();
        sharedListData.setSharedListID(sharedListDetails.getListID());
        sharedListData.getUsersList().add(creatorEMAIL);

        createRef.setValue(sharedListData);
    }

    public static void getSharedListDataByIDAndObserve(final Callback<SharedListData> callback ,String sharedListID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("lists").child(sharedListID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedListData sharedListData = dataSnapshot.getValue(SharedListData.class);
                callback.onComplete(sharedListData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }
}
