package com.example.elad.liskyproject;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.elad.liskyproject.model.SharedListDetails;


public class AddSharedListFragment extends android.support.v4.app.Fragment {

    private OnAddOrCancelButtonClickListener mListener;
    private MenuListViewModel menuListViewModel;
    private EditText sharedListName;
    public AddSharedListFragment() {

    }


    public static AddSharedListFragment newInstance() {
        AddSharedListFragment fragment = new AddSharedListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_shared_list_fragment, container, false);
        sharedListName = view.findViewById(R.id.new_shared_list_name);
        Button addButton = view.findViewById(R.id.new_shared_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = sharedListName.getText().toString();
                menuListViewModel.addSharedListForUser(listName);
                mListener.OnButtonClickAction();
            }
        });
        //TODO set on click button
        Button cancelButton = view.findViewById(R.id.new_shared_list_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnButtonClickAction();
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddOrCancelButtonClickListener) {
            mListener = (OnAddOrCancelButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        menuListViewModel = ViewModelProviders.of(this).get(MenuListViewModel.class);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAddOrCancelButtonClickListener {
        void OnButtonClickAction();
    }
}
