package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class workerReview extends AppCompatActivity {
    private Button addReview;
    private ListView ReviewList;
    private int workerid;
    MyDatabaseHelper DB;
    ArrayList<workerReviewClass> arrayList;
    customWorkerReviewAdapter customWorkerReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_review);

        addReview = findViewById(R.id.addreview);
        ReviewList = findViewById(R.id.workerReviewList);

        addReview.setOnClickListener(v->addReview());

        DB= new MyDatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workerid = extras.getInt("workersidReview");
            loadDatainList();
        }
    }
    public void loadDatainList(){
        arrayList = DB.getReviewbyID(workerid);
        customWorkerReviewAdapter = new customWorkerReviewAdapter(this,arrayList);
        ReviewList.setAdapter(customWorkerReviewAdapter);
        customWorkerReviewAdapter.notifyDataSetChanged();
    }

    private void addReview() {
        Intent intent = new Intent(this, insertReview.class);
        intent.putExtra("workersidReview", workerid);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDatainList();
    }
}