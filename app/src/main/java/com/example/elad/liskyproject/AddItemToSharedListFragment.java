package com.example.elad.liskyproject;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elad.liskyproject.model.SharedListItem;


public class AddItemToSharedListFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";


    private String sharedListID;
    private ImageView itemImage;
    private TextView itemName;
    private TextView itemDescription;

    private OnCancelOrAddButtonClick mListener;
    private SharedListViewModel sharedListViewModel;

    public AddItemToSharedListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
                // TODO resolve image url
                String imageURL = "fakeurl_com";
                SharedListItem item = new SharedListItem(itemName.getText().toString(),imageURL,itemDescription.getText().toString());
                sharedListViewModel.addSharedListItemBySharedListID(item,sharedListID);
                mListener.onSharedListItemAdditionCancelOrAddButtonClick(sharedListID);
            }
        });



        Button cancelButton = view.findViewById(R.id.shared_list_item_addition_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSharedListItemAdditionCancelOrAddButtonClick(sharedListID);
            }
        });
        return view;
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
