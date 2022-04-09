package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class homePage extends AppCompatActivity {

    Button logout, profile, problempost, problemshow, inboxButton;
    SharedPreferences myPref;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        logout = findViewById(R.id.homeLogout);
        logout.setOnClickListener(v->logout());
        profile = findViewById(R.id.userProfile);
        problempost = findViewById(R.id.problemPost);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        System.out.println(userid);

        profile.setOnClickListener(v->userProfile());
        problempost.setOnClickListener(v -> problemPage());
        problemshow = findViewById(R.id.problemShow);
        problemshow.setOnClickListener(v -> {
            Intent intent = new Intent(this, problemShow.class);
            startActivity(intent);
        });
        inboxButton = findViewById(R.id.inbox);
        inboxButton.setOnClickListener(v->inboxPro());

    }
    void logout(){
        myPref.edit().putInt("loggedInID", -1).apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    void userProfile(){
        Intent intent = new Intent(this, userProfile.class);
        startActivity(intent);
    }
    void problemPage(){
        Intent intent = new Intent(this, problemPosting.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    void inboxPro(){
        Intent intent=  new Intent(this, inbox.class);
        startActivity(intent);
    }
}