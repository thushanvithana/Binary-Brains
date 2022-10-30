package com.example.artzoneapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home_to_Profile extends AppCompatActivity {

    BottomNavigationView bottomnav;
    FirebaseAuth loauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_to_profile);
        swichFragments(new Home_fragment());


        bottomnav = findViewById(R.id.bottomnavbar);
        loauth = FirebaseAuth.getInstance();

        bottomnav.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
                case R.id.home: swichFragments(new Home_fragment());break;
                case R.id.list:swichFragments(new List_fragment());break;
                case R.id.gallary:swichFragments(new Gallary());break;
                case R.id.profile:swichFragments(new Artist_Profile());break;
                case R.id.logout:userlogout();break;

            }

            return true;
        });
    }

    private void swichFragments(Fragment fragment){
        FragmentManager fgmanager = getSupportFragmentManager();
        FragmentTransaction fgtransaction = fgmanager.beginTransaction();
        fgtransaction.replace(R.id.htopframe,fragment);
        fgtransaction.commit();

    }

    private void userlogout(){
        loauth.signOut();
        startActivity(new Intent(Home_to_Profile.this,Login.class));
    }
}