package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class createWorkersProfile extends AppCompatActivity {

    SharedPreferences myPref;
    Button save;
    MyDatabaseHelper DB;
    EditText nid, bio;
    int userid;
    private RadioButton rdElectrician;
    private RadioButton rdPlumber;
    private RadioButton rdmechanics;
    private RadioButton rdOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workers_profile);
        save = findViewById(R.id.saveWorkersProfile);
        nid = findViewById(R.id.et_nidNumber);
        bio = findViewById(R.id.et_Workersbio);
        rdElectrician = findViewById(R.id.rdElectrician);
        rdPlumber = findViewById(R.id.rdPlumber);
        rdmechanics = findViewById(R.id.rdmechanics);
        rdOther = findViewById(R.id.rdOther);
        save.setOnClickListener(v->saveInfo());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
    }
    void saveInfo(){
        boolean electricianIsChecked = rdElectrician.isChecked();
        String checkedOne="";
        if(electricianIsChecked== true){
            checkedOne = "Electrician";
        }
        boolean plumberIsChecked = rdPlumber.isChecked();
        if(plumberIsChecked== true){
            checkedOne = "Plumber";
        }
        boolean mechanicsIsChecked = rdmechanics.isChecked();
        if(mechanicsIsChecked== true){
            checkedOne = "Mechanics";
        }

        boolean otherIsChecked = rdOther.isChecked();
        if(otherIsChecked== true){
            checkedOne = "Other";
        }
        String prv_nid = nid.getText().toString().trim();
        String prv_bio = bio.getText().toString().trim();
        int prvNid=Integer.parseInt(prv_nid);
        Boolean noError = DB.insertWorker(userid, checkedOne, prvNid, prv_bio);
        if(noError==true){
            System.out.println("Data Inserted");
            onBackPressed();
        }
        else System.out.println("Got some error");
    }
}