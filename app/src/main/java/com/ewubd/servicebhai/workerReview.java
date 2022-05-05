package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    TextView workerName;
    int workersPersonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_review);

        addReview = findViewById(R.id.addreview);
        ReviewList = findViewById(R.id.workerReviewList);
        workerName = findViewById(R.id.tv_worker_Name);

        addReview.setOnClickListener(v->addReview());

        DB= new MyDatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workerid = extras.getInt("workersidReview");
            loadDatainList();
        }
        workersPersonID = DB.getWorkersPersonID(workerid);
        workerName.setText(DB.getUserame(workersPersonID));
        //System.out.println(workersPersonID);
        workerName.setOnClickListener(v-> openProfile());
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
    void openProfile(){
        Intent intent = new Intent(this, userProfile.class);
        intent.putExtra("Flag", 1);
        intent.putExtra("userID", workersPersonID);
        startActivity(intent);
    }
}