package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class workersList extends AppCompatActivity {

    String category;
    private ListView workersList;
    MyDatabaseHelper DB;
    ArrayList<workersByCatagory> arrayList;
    customWorkersAdapter customWorkersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            category = extras.getString("category");
        }
        System.out.println(category);

        workersList = findViewById(R.id.workersListView);
        DB= new MyDatabaseHelper(this);

        arrayList = new ArrayList<>();
        loadDatainList();
    }

    public void loadDatainList(){
        arrayList = DB.workersByCatagory(category);
        customWorkersAdapter = new customWorkersAdapter(this,arrayList);
        workersList.setAdapter(customWorkersAdapter);
        customWorkersAdapter.notifyDataSetChanged();
    }
}