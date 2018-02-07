package com.example.elad.liskyproject.controller.main.shared_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elad.liskyproject.R;
import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;
import com.example.elad.liskyproject.model.view_models.SharedListViewModel;

import java.util.List;


public class EditSharedListItemFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String sharedListID;
    private String itemID;
    private SharedListViewModel sharedListViewModel;
    private OnFragmentInteractionListener mListener;
    private TextView itemName;
    private TextView description;
    private ImageView itemImage;
    private ProgressBar spinner;

    public EditSharedListItemFragment() {

    }

    public static EditSharedListItemFragment newInstance(String sharedListID, String itemID) {
        EditSharedListItemFragment fragment = new EditSharedListItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, sharedListID);
        args.putString(ARG_PARAM2, itemID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sharedListID = getArguments().getString(ARG_PARAM1);
            itemID = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_shared_list_item, container, false);
        spinner = view.findViewById(R.id.edit_shared_list_item_spinner);
        spinner.setVisibility(View.VISIBLE);
        Button editButton = view.findViewById(R.id.edit_shared_list_item_edit_button);
        Button cancelButton = view.findViewById(R.id.edit_shared_list_item_cancel_button);
        Button deleteButton = view.findViewById(R.id.edit_shared_list_item_delete_button);
        itemName = view.findViewById(R.id.edit_shared_list_item_name);
        description = view.findViewById(R.id.edit_shared_list_item_description);
        itemImage = view.findViewById(R.id.edit_shared_list_item_image_view);
        sharedListViewModel = ViewModelProviders.of(this).get(SharedListViewModel.class);
        sharedListViewModel.getSpecificSharedListDataByListID(sharedListID).observe(this, new Observer<SharedListData>() {
                    @Override
                    public void onChanged(@Nullable SharedListData sld) {
                        List<SharedListItem> sharedListItems = sld.getSharedListItems();
                        for (SharedListItem item : sharedListItems) {
                            if (itemID.equals(item.getItemID())) {

                                if (!item.getImageURL().equals("empty"))
                                    sharedListViewModel.getImage(item.getImageURL(), new FirebaseModel.GetImageListener() {
                                        @Override
                                        public void onSuccess(Bitmap imageBMP) {
                                            spinner.setVisibility(View.GONE);
                                            itemImage.setImageBitmap(imageBMP);
                                        }

                                        @Override
                                        public void onFail() {
                                            Log.d("TAG", "Failed to get image 'EditSharedListItemFragment'");
                                        }
                                    });
                                else
                                    spinner.setVisibility(View.GONE);

                                break;
                            }
                        }
                    }
                }
        );

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = description.getText().toString();
                String name = itemName.getText().toString();
                SharedListItem sharedListItem = new SharedListItem(name, null, des);
                sharedListViewModel.editSharedListItem(sharedListID, itemID, sharedListItem);
                mListener.onEditSharedListItemAnyButtonClick(sharedListID);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEditSharedListItemAnyButtonClick(sharedListID);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedListViewModel.removeSharedListItemBySharedListID(sharedListID, itemID);
                mListener.onEditSharedListItemAnyButtonClick(sharedListID);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {
        void onEditSharedListItemAnyButtonClick(String sharedListID);
    }
}
