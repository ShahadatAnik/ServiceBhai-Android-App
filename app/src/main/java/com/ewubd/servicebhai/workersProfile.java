package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class workersProfile extends AppCompatActivity {

    MyDatabaseHelper DB;
    SharedPreferences myPref;
    TextView expertise, NID, workerBio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_profile);

        DB= new MyDatabaseHelper(this);
        expertise = findViewById(R.id.expertiseText);
        NID = findViewById(R.id.nidText);
        workerBio = findViewById(R.id.workerbioText);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        int userid = myPref.getInt("loggedInID", -1);
        String profile[]= DB.getWorkersProfile(userid);
        if(profile[0]==null){
            Intent intent = new Intent(this, createWorkersProfile.class);
            startActivity(intent);
        }

        expertise.setText(profile[2]);
        NID.setText(profile[3]);
        workerBio.setText(profile[4]);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int userid = myPref.getInt("loggedInID", -1);
        String profile[]= DB.getWorkersProfile(userid);
        if(profile[0]==null){
            Intent intent = new Intent(this, createWorkersProfile.class);
            startActivity(intent);
        }

        expertise.setText(profile[2]);
        NID.setText(profile[3]);
        workerBio.setText(profile[4]);
    }
}