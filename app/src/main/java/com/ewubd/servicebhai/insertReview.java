package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class insertReview extends AppCompatActivity {
    private EditText reviewRate,workerReview;
    private Button save, back;
    MyDatabaseHelper DB;
    private int workerid,userid;
    SharedPreferences myPref;
    private RadioButton rb_1, rb_2, rb_3, rb_4, rb_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        workerReview = findViewById(R.id.worker_review);

        save = findViewById(R.id.saveReview);
//        back = findViewById(R.id.back);


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
        String rate="";
        boolean rb_1_IsChecked = rb_1.isChecked();
        if(rb_1_IsChecked== true){
            rate = "1";
        }
        boolean rb_2_IsChecked = rb_2.isChecked();
        if(rb_2_IsChecked== true){
            rate = "2";
        }
        boolean rb_3_IsChecked = rb_3.isChecked();
        if(rb_3_IsChecked== true){
            rate = "3";
        }
        boolean rb_4_IsChecked = rb_4.isChecked();
        if(rb_4_IsChecked== true){
            rate = "4";
        }
        boolean rb_5_IsChecked = rb_5.isChecked();
        if(rb_5_IsChecked== true){
            rate = "5";
        }

        String review = workerReview.getText().toString().trim();

        Boolean noError = DB.insertRating(userid,workerid,rate,review);
        if(noError && review != ""){
            System.out.println("Data Inserted");
            workerReview.setText("");
        }
        else System.out.println("Got some error");
    }
}