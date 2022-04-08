package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class homepageForUser extends AppCompatActivity {

    Button electrician, plumber, mechanics, other, logoutUser;
    SharedPreferences myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        setContentView(R.layout.activity_homepage_for_user);
        electrician = findViewById(R.id.electricianShow);
        plumber = findViewById(R.id.plumberShow);
        mechanics = findViewById(R.id.mechanicsShow);
        other = findViewById(R.id.otherShow);
        electrician.setOnClickListener(v->electricianActivity());
        plumber.setOnClickListener(v->plumberActivity());
        mechanics.setOnClickListener(v->mechanicsActivity());
        other.setOnClickListener(v->otherActivity());
        logoutUser = findViewById(R.id.logoutUser);
        logoutUser.setOnClickListener(v->logout());
    }

    private void otherActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Other");
        startActivity(intent);
    }

    private void mechanicsActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Mechanics");
        startActivity(intent);
    }

    private void plumberActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Plumber");
        startActivity(intent);
    }

    private void electricianActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Electrician");
        startActivity(intent);
    }

    void logout(){
        myPref.edit().putInt("loggedInID", -1).apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}