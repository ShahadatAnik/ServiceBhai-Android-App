package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class inbox extends AppCompatActivity {

    SharedPreferences myPref;
    MyDatabaseHelper DB;
    ArrayList<inboxArrayList> arrayList;
    customInboxAdapter customInboxAdapter;
    private ListView messagesListView;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        messagesListView = findViewById(R.id.messagesListView);
        DB= new MyDatabaseHelper(this);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        arrayList = new ArrayList<>();
        loadDatainList();

    }

    public void loadDatainList(){
        arrayList = DB.inboxMessages(userid);
        customInboxAdapter = new customInboxAdapter(this,arrayList);
        messagesListView.setAdapter(customInboxAdapter);
        customInboxAdapter.notifyDataSetChanged();
    }
}