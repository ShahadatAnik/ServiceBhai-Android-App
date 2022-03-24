package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

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

        String[][] list = DB.getPostedProblems();
        ArrayList<ArrayList<String>> listoflist = new ArrayList<>();

        for(int j=0; j<DB.totalProblems(); j++){
            for (int i = 0; i <= 3; i++) {

                //listoflist.get(j).add(list[j][i]);

                System.out.println(list[j][i]);
            }
        }
        //System.out.println(listoflist);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}