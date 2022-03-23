package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class problemPosting extends AppCompatActivity {

    private EditText helptype, postDetails;
    private Button savetodb;
    MyDatabaseHelper DB;
    SharedPreferences myPref;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_posting);

        helptype = findViewById(R.id.helpType);
        postDetails = findViewById(R.id.postDetails);
        savetodb = findViewById(R.id.saveToProblem);
        savetodb.setOnClickListener(v -> insertProblem());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
    }
    void insertProblem(){
        String error = "";
        String type = helptype.getText().toString().trim();
        if(type.length()>51){
            error += "Help Type length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Help Type "+ type);
        }
        String detail = postDetails.getText().toString().trim();
        if(detail.length()>101){
            error+= "Detail Length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Post Detail "+ detail);
        }
        if(error==""){
            System.out.println("Insert data To ProblemPosting Table");
            Boolean noError = DB.insertproblemPosting(userid,type,detail);
            if(noError){
                System.out.println("Data Inserted");
                problemshow();
            }
            else System.out.println("Got some error");
        }
    }
    void problemshow(){
        Intent i = new Intent(this, problemShow.class);
        startActivity(i);
    }
}