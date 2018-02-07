package com.example.elad.liskyproject.controller.main.menu_list;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.elad.liskyproject.R;
import com.example.elad.liskyproject.model.entities.SharedListDetails;
import com.example.elad.liskyproject.model.view_models.MenuListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MenuListFragment extends android.support.v4.app.Fragment {

    private OnMenuListItemClickListener mListener;

    private MenuListViewModel menuListViewModel;
    private MenuListAdapter adapter;
    private List<SharedListDetails> data = new ArrayList<>();
    private ProgressBar spinner;

    public MenuListFragment() {
    }


    public static MenuListFragment newInstance() {
        return new MenuListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);
        spinner = view.findViewById(R.id.menu_list_spinner);
        spinner.setVisibility(View.VISIBLE);
        TextView title = view.findViewById(R.id.menu_list_title);
        title.setPaintFlags(title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ListView list = view.findViewById(R.id.menu_list_id);
        adapter = new MenuListAdapter();
        list.setAdapter(adapter);
        Button addNewListButton = view.findViewById(R.id.add_new_list_button);
        addNewListButton.requestFocus();
        addNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMenuListAddButtonClick(v);
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
                if(spinner.getVisibility() == View.VISIBLE)
                    spinner.setVisibility(View.GONE);
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
        void onMenuListItemClick(View view);
        void onMenuListAddButtonClick(View view);
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
                final View constView = convertView;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        mListener.onMenuListItemClick(view);
                    }
                });
                ImageButton removeItemButton = convertView.findViewById(R.id.menu_list_remove_item_button);
                removeItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        builder.setTitle("Delete List")
                                .setMessage("Are you sure you want to delete this list?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        TextView sharedListID = constView.findViewById(R.id.menu_list_item_id);
                                        menuListViewModel.removeSharedListForUserEMAIL(sharedListID.getText().toString());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });

            }
            SharedListDetails listInfo = data.get(position);
            TextView listName = convertView.findViewById(R.id.menu_list_item_name);
            listName.setText(listInfo.getListName());
            TextView listID = convertView.findViewById(R.id.menu_list_item_id);
            listID.setText(listInfo.getListID());
            return convertView;
        }
    }
}
