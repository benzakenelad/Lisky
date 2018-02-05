package com.example.elad.liskyproject;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MenuListFragment.OnMenuListItemClickListener, AddSharedListFragment.OnAddOrCancelButtonClickListener, SharedListFragment.OnItemClickListener, AddItemToSharedListFragment.OnCancelOrAddButtonClick, AddPartnerFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MenuListFragment menuListFragment = MenuListFragment.newInstance();
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        tran.add(R.id.frame_id, menuListFragment, "TAG");
        tran.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        OnButtonClickAction();
    }

    // MenuListFragment
    @Override
    public void onMenuListItemClick(View view) {
        TextView text = view.findViewById(R.id.menu_list_item_id);
        String sharedListID = text.getText().toString();
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        SharedListFragment sharedListFragment = SharedListFragment.newInstance(sharedListID);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, sharedListFragment).commit();
    }

    // MenuListFragment
    @Override
    public void onMenuListAddButtonClick(View view) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        AddSharedListFragment addSharedListFragment = AddSharedListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, addSharedListFragment).commit();
    }

    // AddSharedListFragment
    @Override
    public void OnButtonClickAction() {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        MenuListFragment menuListFragment = MenuListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, menuListFragment).commit();
    }

    // SharedListFragment
    @Override
    public void onSharedListItemClick(View view) {
        // TODO
    }

    // SharedListFragment
    @Override
    public void onSharedListAddButtonClick(String sharedListID) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        AddItemToSharedListFragment addItemToSharedListFragment = AddItemToSharedListFragment.newInstance(sharedListID);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, addItemToSharedListFragment).commit();
    }

    // SharedListFragment
    @Override
    public void onSharedListAddPartnerButtonClick(String sharedListID) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        AddPartnerFragment addPartnerFragment = AddPartnerFragment.newInstance(sharedListID);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, addPartnerFragment).commit();
    }

    // AddItemToSharedListFragment
    @Override
    public void onSharedListItemAdditionCancelOrAddButtonClick(String sharedListID) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        SharedListFragment sharedListFragment = SharedListFragment.newInstance(sharedListID);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, sharedListFragment).commit();
    }

    // AddPartnerFragment
    @Override
    public void OnAddPartnerCancelOrAddButtonClickListener(String sharedListID) {
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();
        SharedListFragment sharedListFragment = SharedListFragment.newInstance(sharedListID);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, sharedListFragment).commit();
    }
}
