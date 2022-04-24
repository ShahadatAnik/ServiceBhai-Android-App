package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class bidding extends AppCompatActivity {
    EditText bidingAmount, notes;
    Button save;
    SharedPreferences myPref;
    int userid;
    int postid;
    MyDatabaseHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);
        bidingAmount = findViewById(R.id.bidingAmount);
        notes = findViewById(R.id.additionalnotes);
        save = findViewById(R.id.saveBid);

        save.setOnClickListener(v->saveBid());

        DB= new MyDatabaseHelper(this);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postid = extras.getInt("postID");
        }
    }

    void saveBid(){
        String prvBiddingAmount = bidingAmount.getText().toString().trim();
        String prvnote = notes.getText().toString().trim();
        //System.out.println(prvBiddingAmount);
        //System.out.println(prvnote);
        //System.out.println(userid);
        //System.out.println(postid);

        int amount = Integer.parseInt(prvBiddingAmount);

        Boolean noError = DB.insertBidding(postid, userid, amount, prvnote);
        if(noError==true){
            System.out.println("Data Inserted");
            postOpen();
        }
        else System.out.println("Got some error");

    }
    void postOpen(){
        Intent intent = new Intent(this, problemShow.class);
        startActivity(intent);
    }
}