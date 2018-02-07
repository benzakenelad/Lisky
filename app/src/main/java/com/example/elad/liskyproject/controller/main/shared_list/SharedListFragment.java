package com.example.elad.liskyproject.controller.main.shared_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elad.liskyproject.R;
import com.example.elad.liskyproject.model.entities.SharedListData;
import com.example.elad.liskyproject.model.entities.SharedListItem;
import com.example.elad.liskyproject.model.view_models.SharedListViewModel;


public class SharedListFragment extends android.support.v4.app.Fragment {
    private static final String ARG_PARAM1 = "param1";

    private String sharedListID;
    private TextView sharedListName;
    private SharedListData sharedListData = new SharedListData();
    private SharedListAdapter adapter;
    private OnItemClickListener mListener;
    private ProgressBar spinner;

    public SharedListFragment() {

    }

    public static SharedListFragment newInstance(String listID) {
        SharedListFragment fragment = new SharedListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, listID);
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
        View view = inflater.inflate(R.layout.fragment_shared_list, container, false);
        spinner = view.findViewById(R.id.shared_list_spinner);
        spinner.setVisibility(View.VISIBLE);
        ListView list = view.findViewById(R.id.shared_list_list);
        adapter = new SharedListAdapter();
        list.setAdapter(adapter);
        Button addItemButton = view.findViewById(R.id.shared_list_add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSharedListAddButtonClick(sharedListID);
            }
        });
        Button addPartnerButton = view.findViewById(R.id.shared_list_add_partner_button);
        addPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSharedListAddPartnerButtonClick(sharedListID);
            }
        });

        sharedListName = view.findViewById(R.id.shared_list_title);
        sharedListName.setPaintFlags(sharedListName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        SharedListViewModel sharedListViewModel = ViewModelProviders.of(this).get(SharedListViewModel.class);
        sharedListViewModel.getSpecificSharedListDataByListID(sharedListID).observe(this, new Observer<SharedListData>() {
                    @Override
                    public void onChanged(@Nullable SharedListData sld) {
                        if(spinner.getVisibility() == View.VISIBLE)
                            spinner.setVisibility(View.GONE);
                        if(sld != null)
                            sharedListData = sld;
                        if(adapter != null)
                            adapter.notifyDataSetChanged();
                        if(sharedListName != null)
                            sharedListName.setText(sld.getSharedListName());
                    }
                }
        );
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener) {
            mListener = (OnItemClickListener) context;
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

    public interface OnItemClickListener {
        void onSharedListItemClick(View view, String sharedListID);
        void onSharedListAddButtonClick(String sharedListID);
        void onSharedListAddPartnerButtonClick(String sharedListID);
    }

    public class SharedListAdapter extends BaseAdapter {

        private LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() {
            return sharedListData.getSharedListItems().size();
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
                convertView = inflater.inflate(R.layout.shared_list_item, null);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSharedListItemClick(v, sharedListID);
                    }
                });
            }
            SharedListItem sharedListItem = sharedListData.getSharedListItems().get(position);
            TextView itemName = convertView.findViewById(R.id.shared_list_item_name);
            itemName.setText(sharedListItem.getItemName());
            TextView itemDescription = convertView.findViewById(R.id.shared_list_item_description);
            itemDescription.setText(sharedListItem.getDescription());
            TextView itemID = convertView.findViewById(R.id.shared_list_item_id);
            itemID.setText(sharedListItem.getItemID());

            return convertView;
        }
    }
}
