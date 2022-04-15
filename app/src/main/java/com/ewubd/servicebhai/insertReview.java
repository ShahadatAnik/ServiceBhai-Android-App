package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class insertReview extends AppCompatActivity {
    private EditText reviewRate,workerReview;
    private Button save, back;
    MyDatabaseHelper DB;
    private int workerid,userid;
    SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        reviewRate = findViewById(R.id.review_rate);
        workerReview = findViewById(R.id.worker_review);

        save = findViewById(R.id.saveReview);
        back = findViewById(R.id.back);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workerid = extras.getInt("workersidReview");
        }
        DB= new MyDatabaseHelper(this);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);

        save.setOnClickListener(v->saveReview());
    }

    private void saveReview() {
        String rating= reviewRate.toString();
        String review = workerReview.toString();
        int rate = Integer.parseInt(rating);

        Boolean noError = DB.insertRating(userid,workerid,rate,review);
        if(noError){
            System.out.println("Data Inserted");
            reviewRate.setText("");
            workerReview.setText("");
        }
        else System.out.println("Got some error");
    }
}