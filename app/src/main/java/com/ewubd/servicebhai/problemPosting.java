package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class problemPosting<val> extends AppCompatActivity {

    private EditText postDetails, posttitle;
    private RadioButton rb_electrical, rb_mechanical, rb_plumber, rb_other;
    private Button savetodb;
    MyDatabaseHelper DB;
    SharedPreferences myPref;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_posting);

        rb_electrical = findViewById(R.id.rb_electrical);
        rb_mechanical = findViewById(R.id.rb_mechanical);
        rb_plumber = findViewById(R.id.rb_plumber);
        rb_other = findViewById(R.id.rb_other);

        posttitle = findViewById(R.id.et_problem_title);
        postDetails = findViewById(R.id.et_problem_details);
        savetodb = findViewById(R.id.btn_post_Post_Your_Problem);
        savetodb.setOnClickListener(v -> insertProblem());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);


    }
    void insertProblem(){
        String error = "";

        String type="";
        boolean electricalIsChecked = rb_electrical.isChecked();
        if(electricalIsChecked== true){
            type = "Electrical";
        }

        boolean mechanicalIsChecked = rb_mechanical.isChecked();
        if(mechanicalIsChecked== true){
            type = "Mechanical";
        }
        boolean plumberIsChecked = rb_plumber.isChecked();
        if(plumberIsChecked== true){
            type = "Plumber";
        }

        boolean otherIsChecked = rb_other.isChecked();
        if(otherIsChecked== true){
            type = "Other";
        }

        String detail = postDetails.getText().toString().trim();
        if(detail.length()>201 || detail.length()<1){
            error+= "Detail Length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Post Detail "+ detail);
        }
        String title = posttitle.getText().toString().trim();
        if(title.length()>31 || title.length()<1){
            error+= "title Length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Post Title "+ title);
        }
        if(error=="" && type != ""){
            System.out.println("Insert data To Problem Posting Table");
            Boolean noError = DB.insertproblemPosting(userid,title,type,detail);
            if(noError){
                System.out.println("Data Inserted");
                problemshow();
                posttitle.setText("");
                postDetails.setText("");
                rb_electrical.setSelected(false);
                rb_mechanical.setSelected(false);
            }
            else System.out.println("Got some error");
        }
    }
    void problemshow(){
        Intent i = new Intent(this, problemShow.class);
        startActivity(i);
    }
}