package com.ewubd.servicebhai;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
        while (true){
            DB= new MyDatabaseHelper(this);
            fetchDataMessages();
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

    public void fetchDataMessages(){
        DB = new MyDatabaseHelper(this);
        @SuppressLint("StaticFieldLeak")
        class dbManager extends AsyncTask<String,Void,String>
        {
            protected void onPostExecute(String data){
                try {
                    JSONArray ja = new JSONArray(data);
                    JSONObject jo = null;

                    for(int i =0;i<ja.length();i++){
                        jo=ja.getJSONObject(i);
                        int messageid = jo.getInt("messageid");
                        int fromID = jo.getInt("fromID");
                        int toID = jo.getInt("toID");
                        String message = jo.getString("message");
                        String datetime = jo.getString("datetime");

                        System.out.println(messageid+" "+fromID+" "+toID+" "+message+" "+datetime);

                        boolean bool = compareWithMessageRemote(messageid);

                        if(bool){
                            System.out.println("Data Already Present");
                        }
                        else{
                            Boolean noError = DB.sendMessages(fromID, toID, message);
                            if(noError){
                                System.out.println("Data Inserted");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer data = new StringBuffer();
                    String line;

                    while((line=br.readLine())!=null){
                        data.append(line+"\n");
                    }
                    br.close();
                    return data.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        dbManager obj =new dbManager();
        obj.execute(DB.FETCH_MESSAGES);
    }
    public boolean compareWithMessageRemote(int postID){
        ArrayList<Integer> id;
        DB = new MyDatabaseHelper(this);
        id = DB.getMessageFromRemote();
        for(int i=0;i<id.size();i++){
            System.out.println(postID+" "+id.get(i));
            if(postID == id.get(i)){
                return true;
            }
        }
        return false;
    }
}