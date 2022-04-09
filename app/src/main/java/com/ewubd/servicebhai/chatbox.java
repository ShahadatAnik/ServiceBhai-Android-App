package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class chatbox extends AppCompatActivity {
    int workersID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workersID = extras.getInt("workersIDToSendMessage");
        }
        System.out.println(workersID);
    }
}