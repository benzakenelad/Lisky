package com.example.elad.liskyproject.controller.main.shared_list;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elad.liskyproject.R;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;
import com.example.elad.liskyproject.model.view_models.SharedListViewModel;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;


public class AddItemToSharedListFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    private String sharedListID;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemDescription;
    private Bitmap imageBitmap;
    private OnCancelOrAddButtonClick mListener;
    private SharedListViewModel sharedListViewModel;

    public AddItemToSharedListFragment() {

    }

    public static AddItemToSharedListFragment newInstance(String sharedListID) {
        AddItemToSharedListFragment fragment = new AddItemToSharedListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, sharedListID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sharedListID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item_to_shared_list, container, false);
        itemName = view.findViewById(R.id.shared_list_item_addition_item_name);
        itemDescription = view.findViewById(R.id.shared_list_item_item_description);
        itemImage = view.findViewById(R.id.shared_list_item_image_id);
        Button addButton = view.findViewById(R.id.shared_list_item_addition_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageBitmap != null) {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new java.util.Date());
                    String name = itemName.getText().toString();
                    sharedListViewModel.saveImage(imageBitmap, (sharedListID+ name + currentDateTimeString).hashCode() + "" + name, new FirebaseModel.SaveImageListener() {
                        @Override
                        public void onComplete(String imageURL) {
                            finishAct(imageURL);
                        }

                        @Override
                        public void onFail() {
                            Log.d("TAG", "FAILED TO SAVE IMAGE");
                            mListener.onSharedListItemAdditionCancelOrAddButtonClick(sharedListID);
                        }
                    });
                }else
                    finishAct("empty");



            }
        });


        Button cancelButton = view.findViewById(R.id.shared_list_item_addition_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSharedListItemAdditionCancelOrAddButtonClick(sharedListID);
            }
        });

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        return view;
    }

    private void finishAct(String imageURL){
        SharedListItem item = new SharedListItem(itemName.getText().toString(),imageURL,itemDescription.getText().toString());
        sharedListViewModel.addSharedListItemBySharedListID(item,sharedListID);
        mListener.onSharedListItemAdditionCancelOrAddButtonClick(sharedListID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                imageBitmap = bitmap;
                itemImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCancelOrAddButtonClick) {
            mListener = (OnCancelOrAddButtonClick) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        sharedListViewModel = ViewModelProviders.of(this).get(SharedListViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCancelOrAddButtonClick {
        void onSharedListItemAdditionCancelOrAddButtonClick(String sharedListID);
    }
}
