package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class problemShow extends AppCompatActivity {

    private ListView postShow;
    private Button print;
    MyDatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_show);

        postShow = findViewById(R.id.postShow);
        DB= new MyDatabaseHelper(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        DB.getPostedProblems(2);
    }
}