package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class bidding extends AppCompatActivity {
    EditText bidingAmount, notes;
    Button save;
    SharedPreferences myPref;
    int userid;
    int postid;
    int amount = -1;
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
        String prvnote = notes.getText().toString().trim();
        try{
            amount = Integer.parseInt(bidingAmount.getText().toString().trim());
        } catch(NumberFormatException ex){
            Toast.makeText(getApplicationContext(),"Please Fill The Bidding Amount!!",Toast.LENGTH_LONG).show();
        }

        if(prvnote.equals("")){
            System.out.println("Got some error from bidding");
            Toast.makeText(getApplicationContext(),"Please Fill All The Input!!",Toast.LENGTH_LONG).show();
        }

        if(amount != -1 && !prvnote.equals("")){
            Boolean noError = DB.insertBidding(postid, userid, amount, prvnote);
            if(noError==true){
                System.out.println("Data Inserted");
                onBackPressed();
            }
            else System.out.println("Got some error");
        }
        else {
            System.out.println("Got some error from bidding");
            Toast.makeText(getApplicationContext(),"Please Fill All The Input!!",Toast.LENGTH_LONG).show();
        }
    }
    void postOpen(){
        Intent intent = new Intent(this, problemOpen.class);
        intent.putExtra("postid",postid);
        intent.putExtra("personid",userid);
        startActivity(intent);
    }
}