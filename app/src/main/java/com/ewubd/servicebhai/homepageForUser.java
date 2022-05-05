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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;

public class homepageForUser extends AppCompatActivity {

    Button electrician, plumber, mechanics, other, problempost, problemshow, profile, userinbox;
    Button logoutUser;
    SharedPreferences myPref;
    MyDatabaseHelper DB;
    GridView category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        setContentView(R.layout.activity_homepage_for_user);

//        category = findViewById(R.id.gv_category_home);
//
//        ArrayList<CatagoryListHomePageUser> courseModelArrayList = new ArrayList<CatagoryListHomePageUser>();
//        courseModelArrayList.add(new CatagoryListHomePageUser("DSA", R.drawable.plumber_24));
//        courseModelArrayList.add(new CatagoryListHomePageUser("JAVA", R.drawable.plumber_24));
//        courseModelArrayList.add(new CatagoryListHomePageUser("C++", R.drawable.plumber_24));
//
//        CategoryAdapter adapter = new CategoryAdapter(this, courseModelArrayList);
//        category.setAdapter(adapter);

        electrician = findViewById(R.id.electricianShow);
        plumber = findViewById(R.id.plumberShow);
        mechanics = findViewById(R.id.mechanicsShow);
        other = findViewById(R.id.otherShow);
        electrician.setOnClickListener(v->electricianActivity());
        plumber.setOnClickListener(v->plumberActivity());
        mechanics.setOnClickListener(v->mechanicsActivity());
        other.setOnClickListener(v->otherActivity());
        logoutUser = findViewById(R.id.logoutUser);
        logoutUser.setOnClickListener(v->logout());
        profile = findViewById(R.id.userProfileButton);
        profile.setOnClickListener(v->userProfilePage());
        userinbox = findViewById(R.id.userInbox);
        userinbox.setOnClickListener(v->userInbox());

        problempost = findViewById(R.id.problemPostUser);
        problempost.setOnClickListener(v -> problemPage());
        problemshow = findViewById(R.id.problemShowUser);
        problemshow.setOnClickListener(v -> {
            Intent intent = new Intent(this, problemShow.class);
            startActivity(intent);
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        int user = myPref.getInt("loggedInID", -1);
        int messageCount = DB.messageCountOfUser(user);


        int prevMessageCount = myPref.getInt("messageCount"+user, -1);
        if(prevMessageCount == -1 ){
            myPref.edit().putInt("messageCount"+user, 0).apply();
        }
        if(messageCount > prevMessageCount && messageCount!=0){
            //System.out.println("New Message");
            getNotification();
            myPref.edit().putInt("messageCount"+user, messageCount).apply();
        }

    }

    private void otherActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Other");
        startActivity(intent);
    }

    private void mechanicsActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Mechanics");
        startActivity(intent);
    }

    private void plumberActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Plumber");
        startActivity(intent);
    }

    private void electricianActivity() {
        Intent intent = new Intent(this, workersList.class);
        intent.putExtra("category", "Electrician");
        startActivity(intent);
    }

    void logout(){
        myPref.edit().putInt("loggedInID", -1).apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    void userProfilePage(){
        Intent intent = new Intent(this, userProfile.class);
        startActivity(intent);
    }
    void userInbox(){
        Intent intent = new Intent(this, inbox.class);
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
    void getNotification(){
        Intent intent = new Intent(this, inbox.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification")
                .setSmallIcon(R.drawable.profile_layout_shaper)
                .setContentTitle("You have a new message")
                .setContentText("Someone Wants to get your job")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}