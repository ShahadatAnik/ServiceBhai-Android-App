package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class problemOpen extends AppCompatActivity {

    int postid,personid;
    TextView probtitle, probdetail;
    SharedPreferences myPref;
    Button delete, accept, bid, markASDone;
    MyDatabaseHelper DB;
    String userOrWorker;
    int user;

    ArrayList<biddingArrayList> arrayList;
    customBiddingAdapter customBiddingAdapter;
    private ListView biddingListView;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_open);
        Bundle extras = getIntent().getExtras();
        biddingListView = findViewById(R.id.biddingListView);


        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);

        delete = findViewById(R.id.delete);
        accept = findViewById(R.id.accept);
        bid = findViewById(R.id.bidding);
        markASDone = findViewById(R.id.btn_mark_as_done);
        markASDone.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);
        bid.setVisibility(View.INVISIBLE);

        delete.setOnClickListener(v->deletePost());
        accept.setOnClickListener(v->acceptWork());
        bid.setOnClickListener(v->bidingIntent());
        markASDone.setOnClickListener(v->markAsDoneObj());

        DB= new MyDatabaseHelper(this);

        userOrWorker = DB.userOrWorker(userid);
        System.out.println(userOrWorker);
        if(userOrWorker.equals("User")){
            user = 1;
        }
        else{
            user = 0;
        }

        String usertype = DB.userOrWorker(userid);
        System.out.println(usertype);


        probtitle = findViewById(R.id.tv_prob_title);
        probdetail = findViewById(R.id.tv_prob_details);
        if (extras != null) {
            postid = extras.getInt("postid");
            personid = extras.getInt("personid");
        }
        if(userid == personid){
            delete.setVisibility(View.VISIBLE);
            markASDone.setVisibility(View.VISIBLE);
        }
        if(usertype.equals("Worker")){
            accept.setVisibility(View.VISIBLE);
            bid.setVisibility(View.VISIBLE);
        }
        //System.out.println(postid+" "+personid);

        loadDatainList();
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
        probdetail.setText(problem[2]);
    }

    void acceptWork(){
        Intent intent = new Intent(this, chatbox.class);
        intent.putExtra("workersIDToSendMessage", personid);
        startActivity(intent);
    }

    void bidingIntent(){
        Intent intent = new Intent(this, bidding.class);
        intent.putExtra("postID", postid);
        startActivity(intent);
    }

    public void loadDatainList(){
        arrayList = DB.bidding(postid);
        customBiddingAdapter = new customBiddingAdapter(this,arrayList, user);
        biddingListView.setAdapter(customBiddingAdapter);
        customBiddingAdapter.notifyDataSetChanged();
    }

    void markAsDoneObj(){
        System.out.println(DB.markAsDone(postid));
        Intent intent = new Intent(this, problemShow.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadDatainList();
    }
}