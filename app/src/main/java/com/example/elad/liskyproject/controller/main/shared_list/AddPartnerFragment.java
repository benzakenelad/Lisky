package com.example.elad.liskyproject.controller.main.shared_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elad.liskyproject.MyApplication;
import com.example.elad.liskyproject.R;
import com.example.elad.liskyproject.model.firebase.FirebaseModel;
import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.view_models.MenuListViewModel;
import com.example.elad.liskyproject.model.view_models.SharedListViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddPartnerFragment extends android.support.v4.app.Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String sharedListID;
    private SharedListViewModel sharedListViewModel;
    private List<String> data = new ArrayList<>();
    private PartnersListAdapter adapter;
    private TextView partnerEmail;
    private SharedListDetails sharedListDetail;

    private OnFragmentInteractionListener mListener;

    public AddPartnerFragment() {
    }

    public static AddPartnerFragment newInstance(String sharedListID) {
        AddPartnerFragment fragment = new AddPartnerFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_partner, container, false);
        ListView listView = view.findViewById(R.id.add_partner_list);
        adapter = new PartnersListAdapter();
        listView.setAdapter(adapter);
        partnerEmail = view.findViewById(R.id.add_partner_new_user_email);
        Button addPartnerAddButton = view.findViewById(R.id.add_partner_add_button);
        addPartnerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partnerEMAIL = partnerEmail.getText().toString();
                if(!partnerEMAIL.contains("@") || partnerEMAIL.length() <= 3){
                    Toast.makeText(getActivity().getApplicationContext(), "Illegal mail address", Toast.LENGTH_SHORT).show();
                    return;
                }
                partnerEMAIL = partnerEMAIL.replace(".", "_");
                sharedListViewModel.addUserEmailToSharedList(sharedListDetail, partnerEMAIL, new FirebaseModel.UserNotExistCallBack() {
                    @Override
                    public void OnUserNotExist(String userEMAIL) {
                        Toast.makeText(MyApplication.getAppContext(), userEMAIL + " - User Email Not Registered", Toast.LENGTH_SHORT).show();
                    }
                });
                mListener.OnAddPartnerCancelOrAddButtonClickListener(sharedListID);
            }
        });
        Button addPartnerCancelButton = view.findViewById(R.id.add_partner_cancel_button);
        addPartnerCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OnAddPartnerCancelOrAddButtonClickListener(sharedListID);
            }
        });

        sharedListViewModel = ViewModelProviders.of(this).get(SharedListViewModel.class);
        sharedListViewModel.getSpecificSharedListDataByListID(sharedListID).observe(this, new Observer<SharedListData>() {
                    @Override
                    public void onChanged(@Nullable SharedListData sld) {
                        if(sld != null)
                            data = sld.getUsersList();
                        if(adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                }
        );
        MenuListViewModel menuListViewModel = ViewModelProviders.of(this).get(MenuListViewModel.class);
        menuListViewModel.getAllSharedListsDetails().observe(this, new Observer<List<SharedListDetails>>() {
            @Override
            public void onChanged(@Nullable List<SharedListDetails> sharedListsDetails) {
                for (SharedListDetails sld: sharedListsDetails) {
                    if(sld.getListID().equals(sharedListID)) {
                        sharedListDetail = sld;
                        Log.d("TAG", "shared list details us placed");
                        break;
                    }
                }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void OnAddPartnerCancelOrAddButtonClickListener(String sharedListID);
    }

    public class PartnersListAdapter extends BaseAdapter {

        private LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.partners_list_item, null);
            }
            String userEmail = data.get(position);
            TextView userEmailTextView = convertView.findViewById(R.id.partner_item_email);
            userEmailTextView.setText(userEmail);
            return convertView;
        }
    }
}
