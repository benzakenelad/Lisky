package com.example.elad.liskyproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.elad.liskyproject.model.SharedListDetails;

import java.util.ArrayList;
import java.util.List;

public class MenuListFragment extends android.support.v4.app.Fragment {

    private OnMenuListItemClickListener mListener;

    // TODO temp data
//    private ArrayList<SharedListDetails> data = null;
    private MenuListViewModel menuListViewModel;
    private MenuListAdapter adapter;
    private List<SharedListDetails> data = new ArrayList<>();

    public MenuListFragment() {
//        data = new ArrayList<>();
//        for (int i = 0; i < 36; i++)
//            data.add(new SharedListDetails("name" + i, "" + i, ((i)*23)%11));

    }


    public static MenuListFragment newInstance() {
        MenuListFragment fragment = new MenuListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        ListView list = view.findViewById(R.id.menu_list_id);
        adapter = new MenuListAdapter();
        list.setAdapter(adapter);
        ImageButton addNewListButton = view.findViewById(R.id.add_new_list_button);
        addNewListButton.requestFocus();
        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddButtonClick(v);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuListItemClickListener) {
            mListener = (OnMenuListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        menuListViewModel = ViewModelProviders.of(this).get(MenuListViewModel.class);
        menuListViewModel.getAllSharedListsDetails().observe(this, new Observer<List<SharedListDetails>>() {
            @Override
            public void onChanged(@Nullable List<SharedListDetails> sharedListsDetails) {
                if (sharedListsDetails != null)
                    data = sharedListsDetails;
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnMenuListItemClickListener {
        void onItemClick(View view);
        void onAddButtonClick(View view);
    }

    public class MenuListAdapter extends BaseAdapter {

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
                convertView = inflater.inflate(R.layout.menu_list_item, null);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(v);
                    }
                });
            }
            SharedListDetails listInfo = data.get(position);
            TextView listName = convertView.findViewById(R.id.menu_list_item_name);
            listName.setText(listInfo.getListName());
            TextView listMembersAmount = convertView.findViewById(R.id.menu_list_item_members_amount);
            listMembersAmount.setText("Members: " + listInfo.getMembersAmount());
            TextView listID = convertView.findViewById(R.id.menu_list_item_id);
            listID.setText(listInfo.getListID());
            return convertView;
        }
    }
}
