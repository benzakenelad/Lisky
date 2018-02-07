package com.example.elad.liskyproject.model.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
                        if (hMap != null) {
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


        DatabaseReference updateRef = database.getReference("lists").child(sharedListDetails.getListID()).child("usersList");
        Map<String, Object> map = new HashMap<>();
        map.put(creatorEMAIL, creatorEMAIL);
        updateRef.updateChildren(map);
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
                if (value == null)
                    return;

                sharedListData.setSharedListName((String) value.get("sharedListName"));
                sharedListData.setSharedListID((String) value.get("sharedListID"));

                //sharedListData.setUsersList((List<String>) value.get("usersList"));
                List<String> usersList = new ArrayList<>();
                HashMap<Object, Object> haMap = (HashMap<Object, Object>) value.get("usersList");
                if (haMap != null)
                    for (Object userMail : haMap.values()) {
                        usersList.add((String) userMail);
                    }
                sharedListData.setUsersList(usersList);
                HashMap<Object, Object> hashMap = (HashMap<Object, Object>) value.get("sharedListItems");
                if (hashMap != null)
                    for (Object obj : hashMap.values()) {
                        HashMap<String, String> hMap = (HashMap<String, String>) obj;
                        if (hMap != null)
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


    public static void removeSharedListForUserEMAIL(String sharedListID, String userEMAIL) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("lists").child(sharedListID).child("usersList").child(userEMAIL).removeValue();
        database.getReference("users").child(userEMAIL).child(sharedListID).removeValue();
    }

    public static void removeSharedListItemBySharedListID(String sharedListID, String itemID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("lists").child(sharedListID).child("sharedListItems").child(itemID).removeValue();
    }

    public static void editSharedListItem(String sharedListID, String itemID, SharedListItem sharedListItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("lists").child(sharedListID).child("sharedListItems").child(itemID).child("itemName").setValue(sharedListItem.getItemName());
        database.getReference("lists").child(sharedListID).child("sharedListItems").child(itemID).child("description").setValue(sharedListItem.getDescription());
    }

    public interface SaveImageListener {
        void onComplete(String imageURL);
        void onFail();
    }

    public static void saveImage(Bitmap imageBmp, String itemID, final SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("images").child(itemID);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uri = taskSnapshot.getDownloadUrl();
                listener.onComplete(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFail();
            }
        });
    }

    public interface GetImageListener {
        void onSuccess(Bitmap imageBMP);
        void onFail();
    }

    public static void getImage(String imageURI, final GetImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference httpsReference = storage.getReferenceFromUrl(imageURI);

        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onFail();
            }
        });
    }
}
