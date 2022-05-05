package com.ewubd.servicebhai;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class notificationcheck extends Service {
    SharedPreferences myPref;
    MyDatabaseHelper DB;
    public notificationcheck() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
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

        return START_STICKY;
    }

    void getNotification(){
        Intent intent = new Intent(this, inbox.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My Notification")
                .setSmallIcon(R.drawable.profile_layout_shaper)
                .setContentTitle("You have a new message")
                .setContentText("Someone Wants to Hire you")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
    }
}