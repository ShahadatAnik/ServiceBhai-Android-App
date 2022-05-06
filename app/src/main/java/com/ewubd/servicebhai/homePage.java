package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homePage extends AppCompatActivity {

    Button logout, profile, problemshow, inboxButton;
    SharedPreferences myPref;
    int userid;
    String workersCategory[];
    MyDatabaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        DB= new MyDatabaseHelper(this);
        logout = findViewById(R.id.homeLogout);
        logout.setOnClickListener(v->logout());
        profile = findViewById(R.id.userProfile);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        workersCategory = DB.getWorkersProfile(userid);
        //System.out.println(userid);

        profile.setOnClickListener(v->userProfile());
        problemshow = findViewById(R.id.problemShow);
        problemshow.setOnClickListener(v -> {
            Intent intent = new Intent(this, problemShow.class);
            intent.putExtra("fromWorkers",1);
            intent.putExtra("category",workersCategory[2]);
            startActivity(intent);
        });
        inboxButton = findViewById(R.id.inbox);
        inboxButton.setOnClickListener(v->inboxPro());

//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }

//        int user = myPref.getInt("loggedInID", -1);
//        int messageCount = DB.messageCountOfUser(user);
//
//
//        int prevMessageCount = myPref.getInt("messageCount"+user, -1);
//        if(prevMessageCount == -1 ){
//            myPref.edit().putInt("messageCount"+user, 0).apply();
//        }
//        if(messageCount > prevMessageCount && messageCount!=0){
//            //System.out.println("New Message");
//            getNotification();
//            myPref.edit().putInt("messageCount"+user, messageCount).apply();
//        }
        Intent intent = new Intent(this, notificationcheck.class);
        startService(intent);


    }

    void logout(){
        myPref.edit().putInt("loggedInID", -1).apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        Intent intent2 = new Intent(this, notificationcheck.class);
        stopService(intent2);
    }
    void userProfile(){
        Intent intent = new Intent(this, userProfile.class);
        startActivity(intent);
    }
    void problemPage(){
        Intent intent = new Intent(this, problemPosting.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    void inboxPro(){
        Intent intent=  new Intent(this, inbox.class);
        startActivity(intent);
    }
//    void getNotification(){
//        Intent intent = new Intent(this, inbox.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification")
//                .setSmallIcon(R.drawable.profile_layout_shaper)
//                .setContentTitle("You have a new message")
//                .setContentText("Someone Wants to Hire you")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(0, builder.build());
//    }


    @Override
    protected void onRestart() {
        super.onRestart();
        DB= new MyDatabaseHelper(this);
        workersCategory = DB.getWorkersProfile(userid);
    }
}