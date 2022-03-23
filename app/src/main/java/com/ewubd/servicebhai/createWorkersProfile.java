package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class createWorkersProfile extends AppCompatActivity {

    SharedPreferences myPref;
    Button save;
    MyDatabaseHelper DB;
    EditText expertise, nid;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workers_profile);
        save = findViewById(R.id.saveWorkersProfile);
        expertise = findViewById(R.id.expertise);
        nid = findViewById(R.id.nidNumber);
        save.setOnClickListener(v->saveInfo());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
    }
    void saveInfo(){
        String prv_exper = expertise.getText().toString().trim();
        String prv_nid = nid.getText().toString().trim();
        int prvNid=Integer.parseInt(prv_nid);
        Boolean noError = DB.insertWorker(userid, prv_exper, prvNid);
        if(noError==true){
            System.out.println("Data Inserted");
            Intent intent = new Intent(this, workersProfile.class);
            startActivity(intent);
        }
        else System.out.println("Got some error");
    }
}