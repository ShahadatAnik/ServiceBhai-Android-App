package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class problemOpen extends AppCompatActivity {

    int postid,personid;
    TextView probtitle, probcategory, probdetail;
    SharedPreferences myPref;
    Button delete, accept;
    MyDatabaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_open);
        Bundle extras = getIntent().getExtras();

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        int userid = myPref.getInt("loggedInID", -1);

        delete = findViewById(R.id.delete);
        accept = findViewById(R.id.accept);
        delete.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);

        delete.setOnClickListener(v->deletePost());
        accept.setOnClickListener(v->acceptWork());

        DB= new MyDatabaseHelper(this);

        String usertype = DB.userOrWorker(userid);
        System.out.println(usertype);


        probtitle = findViewById(R.id.prob_title);
        probcategory = findViewById(R.id.prob_category);
        probdetail = findViewById(R.id.prob_details);
        if (extras != null) {
            postid = extras.getInt("postid");
            personid = extras.getInt("personid");
        }
        if(userid == personid){
            delete.setVisibility(View.VISIBLE);
        }
        if(usertype.equals("Worker")){
            accept.setVisibility(View.VISIBLE);
        }
        //System.out.println(postid+" "+personid);
    }

    private void deletePost() {
        Boolean noError = DB.deletePost(postid);
        if(noError==true){
            problemShowactivity();
        }
        else System.out.println("Got some error");
    }

    private void problemShowactivity() {
        Intent intent = new Intent(this, problemShow.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        String problem[];
        problem = DB.getProblembyID(postid);
        probtitle.setText(problem[0]);
        probcategory.setText(problem[1]);
        probdetail.setText(problem[2]);
    }
    void acceptWork(){
        Intent intent = new Intent(this, chatbox.class);
        intent.putExtra("workersIDToSendMessage", personid);
        startActivity(intent);
    }
}