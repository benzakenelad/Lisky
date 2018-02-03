package com.example.elad.liskyproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements MenuListFragment.OnMenuListItemClickListener{

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
    public void onClick(View view) {
        // TODO open shared list fragment
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.frame_id)).commit();


        MenuListFragment menuListFragment = MenuListFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_id, menuListFragment).commit();
    }
}
