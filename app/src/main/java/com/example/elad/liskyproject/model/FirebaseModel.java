package com.example.elad.liskyproject.model;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.elad.liskyproject.MenuListViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FirebaseModel {

    public interface Callback<T> {
        void onComplete(T data);
    }

    public interface UserNotExistCallBack {
        void OnUserNotExist(String userEMAIL);
    }

    public static void getAllSharedListsForUserEMAILAndObserver(final Callback<List<SharedListDetails>> callback, String userEMAIL) {

        Log.d("TAG", "getAllSharedListsForUserEMAILAndObserver");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(userEMAIL);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<SharedListDetails> list = new LinkedList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Object object = snap.getValue();
                    if (object instanceof HashMap) {
                        HashMap<Object, Object> hMap = (HashMap<Object, Object>) object;
                        if(hMap != null) {
                            String listID = (String) hMap.get("listID");
                            String listName = (String) hMap.get("listName");
                            list.add(new SharedListDetails(listName, listID));
                        }
                    }
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });

    }


    public static void addSharedListForUserEMAIL(String sharedListName, String userEMAIL) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference demoRef = database.getReference("users").child(userEMAIL).push();
        final String sharedListID = demoRef.getKey();

        DatabaseReference saveRef = database.getReference("users").child(userEMAIL);
        SharedListDetails sharedListDetails = new SharedListDetails(sharedListName, sharedListID);
        Map<String, Object> map = new HashMap<>();
        map.put(sharedListID, sharedListDetails);

        saveRef.updateChildren(map);

        createNewList(sharedListDetails, userEMAIL);
    }

    private static void createNewList(SharedListDetails sharedListDetails, String creatorEMAIL) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference createRef = database.getReference("lists").child(sharedListDetails.getListID());

        SharedListData sharedListData = new SharedListData();
        sharedListData.setSharedListID(sharedListDetails.getListID());
        //sharedListData.getUsersList().add(creatorEMAIL);
        sharedListData.setSharedListName(sharedListDetails.getListName());
        createRef.setValue(sharedListData);


        DatabaseReference updareRef = database.getReference("lists").child(sharedListDetails.getListID()).child("usersList");
        Map<String, Object> map = new HashMap<>();
        map.put(creatorEMAIL, creatorEMAIL);
        updareRef.updateChildren(map);
    }

    public static void getSharedListDataByIDAndObserve(final Callback<SharedListData> callback, String sharedListID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("lists").child(sharedListID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //SharedListData sharedListData = dataSnapshot.getValue(SharedListData.class);
                SharedListData sharedListData = new SharedListData();
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                sharedListData.setSharedListName((String) value.get("sharedListName"));
                sharedListData.setSharedListID((String) value.get("sharedListID"));


                //sharedListData.setUsersList((List<String>) value.get("usersList"));
                List<String> usersList = new ArrayList<>();
                HashMap<Object, Object> haMap = (HashMap<Object, Object>) value.get("usersList");
                for (Object userMail : haMap.values()) {
                    usersList.add((String) userMail);
                }
                sharedListData.setUsersList(usersList);
                HashMap<Object, Object> hashMap = (HashMap<Object, Object>) value.get("sharedListItems");
                if (hashMap != null)
                    for (Object obj : hashMap.values()) {
                        HashMap<String, String> hMap = (HashMap<String, String>) obj;
                        sharedListData.getSharedListItems().add(convertHashMapToSharedListItem(hMap));
                    }

                //sharedListData.setSharedListItems((List<SharedListItem>)value.get("sharedListItems"));
                callback.onComplete(sharedListData);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public static void addSharedListItemBySharedListID(SharedListItem sharedListItem, String sharedListID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference demoRef = database.getReference("lists").child(sharedListID).child("sharedListItems").push();
        final String itemID = demoRef.getKey();

        sharedListItem.setItemID(itemID);
        DatabaseReference saveRef = database.getReference("lists").child(sharedListID).child("sharedListItems");
        Map<String, Object> map = new HashMap<>();
        map.put(itemID, sharedListItem);
        saveRef.updateChildren(map);
    }

    private static SharedListItem convertHashMapToSharedListItem(HashMap<String, String> hMap) {
        SharedListItem item = new SharedListItem();
        item.setItemID(hMap.get("itemID"));
        item.setDescription(hMap.get("description"));
        item.setImageURL(hMap.get("imageURL"));
        item.setItemName(hMap.get("itemName"));
        return item;
    }

    public static void addUserEmailToSharedList(final SharedListDetails sharedListDetails, final String partnerEMAIL, final String currentUserEmail, final UserNotExistCallBack callback) {


        FirebaseAuth.getInstance().fetchProvidersForEmail(partnerEMAIL.replace("_", ".")).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if (!task.getResult().getProviders().isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference updateListRef = database.getReference("lists").child(sharedListDetails.getListID()).child("usersList");
                    Map<String, Object> map = new HashMap<>();
                    map.put(partnerEMAIL, partnerEMAIL);
                    updateListRef.updateChildren(map);

                    database = FirebaseDatabase.getInstance();
                    DatabaseReference updateUsersRef = database.getReference("users").child(partnerEMAIL);
                    Log.d("TAG", "partner email is " + partnerEMAIL);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put(sharedListDetails.getListID(), sharedListDetails);
                    updateUsersRef.updateChildren(map2);
                } else
                    callback.OnUserNotExist(partnerEMAIL);
            }
        });

    }
}
