package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class chatbox extends AppCompatActivity {
    int workersID;
    SharedPreferences myPref;
    Button sendMessages;
    EditText messagesBox;
    int userid;
    MyDatabaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);
        Bundle extras = getIntent().getExtras();
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        if (extras != null) {
            workersID = extras.getInt("workersIDToSendMessage");
        }
        userid = myPref.getInt("loggedInID", -1);
        sendMessages = findViewById(R.id.sendMessageButton);
        sendMessages.setOnClickListener(v-> sendingMessages());
        messagesBox = findViewById(R.id.messageBox);
    }
    void sendingMessages(){
        System.out.println(workersID);
        System.out.println(userid);
        String message = messagesBox.getText().toString().trim();
        System.out.println(message);
        Boolean noError = DB.sendMessages(userid, workersID, message);
        if(noError==true){
            System.out.println("Message Send");
            messagesBox.getText().clear();
        }
        else System.out.println("Got some error");
    }
}