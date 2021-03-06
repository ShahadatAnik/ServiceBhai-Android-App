package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class userProfile extends AppCompatActivity {

    MyDatabaseHelper DB;
    SharedPreferences myPref;
    private TextView userName, userAddress, userEmail, userPhone, userType;
    private Button workersProfile, nid;
    ListView history;
    int userid;
    int flag, userID;
    ArrayList<postedProblem> arrayList;
    customProblemAdapter customProblemAdapter;
    TextView tv_history, tv_category, tv_bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            flag = extras.getInt("Flag");
            userID = extras.getInt("userID");
        }
        System.out.println(userID);

        DB= new MyDatabaseHelper(this);
        userName = findViewById(R.id.tv_profile_name_dynamic);
        userEmail = findViewById(R.id.tv_profile_email_dynamic);
        userAddress = findViewById(R.id.tv_profile_address_dynamic);
        userPhone = findViewById(R.id.tv_profile_phone_dynamic);
        userType = findViewById(R.id.tv_profile_type_dynamic);
        workersProfile = findViewById(R.id.workersProfile);
        nid = findViewById(R.id.show_upload_NID);
        workersProfile.setOnClickListener(v->workersProfileIntent());
        workersProfile.setVisibility(View.GONE);
        nid.setVisibility(View.GONE);
        nid.setOnClickListener(v->nidIn());
        history = findViewById(R.id.history_list_view);
        tv_history = findViewById(R.id.tv_history);
        tv_category = findViewById(R.id.tv_category);
        tv_bio = findViewById(R.id.tv_bio);


        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        if(flag == 1){
            String profile[]= DB.getUserProfleInfo(userID);
            userName.setText(profile[1]);
            userEmail.setText(profile[2]);
            userAddress.setText(profile[3]);
            userPhone.setText(profile[4]);
            userType.setText(profile[5]);

            if(DB.userOrWorker(userID).equals("Worker")){
                String getWorkersProfile[] = DB.getWorkersProfile(userID);
                tv_history.setText("Workers info");
                tv_category.setText(getWorkersProfile[2]);
                tv_bio.setText(getWorkersProfile[4]);
            }

            System.out.println(DB.userOrWorker(userid));
            if(profile[5].equals("Worker") && DB.userOrWorker(userid).equals("Worker")){
                workersProfile.setVisibility(View.VISIBLE);
            }
            loadDataInArrayList2();
        }
        else{
            String profile[]= DB.getUserProfleInfo(userid);
            userName.setText(profile[1]);
            userEmail.setText(profile[2]);
            userAddress.setText(profile[3]);
            userPhone.setText(profile[4]);
            userType.setText(profile[5]);

            if(profile[5].equals("Worker")){
                workersProfile.setVisibility(View.VISIBLE);
                nid.setVisibility(View.VISIBLE);
            }
            loadDataInArrayList();
        }

    }
    void workersProfileIntent(){
        Intent intent = new Intent(this, workersProfile.class);
        startActivity(intent);
    }
    void loadDataInArrayList(){
        arrayList = DB.history(userid);
        customProblemAdapter = new customProblemAdapter(this,arrayList, 1, 0,"");
        history.setAdapter(customProblemAdapter);
        customProblemAdapter.notifyDataSetChanged();
    }
    void loadDataInArrayList2(){
        arrayList = DB.history(userID);
        customProblemAdapter = new customProblemAdapter(this,arrayList, 1, 0,"");
        history.setAdapter(customProblemAdapter);
        customProblemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDataInArrayList();
    }

    void nidIn(){
        Intent intent = new Intent(this, showNID.class);
        startActivity(intent);
    }
}