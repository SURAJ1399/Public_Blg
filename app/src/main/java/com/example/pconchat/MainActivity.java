package com.example.pconchat;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button addpostbtn;
    FirebaseFirestore firebaseFirestore;
    BottomNavigationView mainbottomnavi;
    HomeFragment homeFragment;
    NotificationFragment notificationFragment;
    AccountFragment accountFragment;


    String currentuserid;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainbottomnavi = findViewById(R.id.mainbottomnavi);
        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();

        accountFragment = new AccountFragment();
        replacefragment(homeFragment);
        mainbottomnavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottomhome:
                        replacefragment(homeFragment);
                        return true;
                    case R.id.bottomaccount:
                        replacefragment(accountFragment);
                        return true;
                    case R.id.bottomnotification:
                        replacefragment(notificationFragment);
                        return true;
                    default:
                        return false;
                }


            }
        });
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        addpostbtn = findViewById(R.id.add_post_btn);
        addpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addpost = new Intent(MainActivity.this, NewpostActivity.class);
                startActivity(addpost);

            }
        });

    }




    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sendTologin();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout_button:
                mAuth.signOut();
                sendTologin();
                return true;
            case R.id.action_setting_button:
                Intent settingIntent=new Intent(MainActivity.this,SetupActivity.class);
                startActivity(settingIntent);
                return true;
            default:
                return false;


        }
    }

    private void sendTologin()
    {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private void replacefragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();

    }
}