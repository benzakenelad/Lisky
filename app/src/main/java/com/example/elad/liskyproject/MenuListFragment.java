package com.example.elad.liskyproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.elad.liskyproject.model.ListInfo;

import java.util.ArrayList;

public class MenuListFragment extends android.support.v4.app.Fragment {

    private OnMenuListItemClickListener mListener;

    // TODO temp data
    private ArrayList<ListInfo> data = null;

    public MenuListFragment() {
        // TODO temp data
        data = new ArrayList<>();
        for (int i = 0; i < 36; i++)
            data.add(new ListInfo("name" + i, "" + i, ((i + 51) * 23 + 43) % 17));

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
        MenuListAdapter adapter = new MenuListAdapter();
        list.setAdapter(adapter);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    public interface OnMenuListItemClickListener { // TODO update
        void onClick(View view);
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
                        mListener.onClick(v);
                    }
                });
            }
            ListInfo listInfo = data.get(position);
            TextView listName = convertView.findViewById(R.id.menu_list_item_name);
            listName.setText(listInfo.listName);
            TextView listMembersAmount = convertView.findViewById(R.id.menu_list_item_members_amount);
            listMembersAmount.setText("Members: " + listInfo.membersAmount);
            return convertView;
        }
    }
}
