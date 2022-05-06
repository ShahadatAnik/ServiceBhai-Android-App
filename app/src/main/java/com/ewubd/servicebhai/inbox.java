package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        fetchDataMessages();
        loadDatainList();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchDataMessages();
        loadDatainList();
    }

    public void loadDatainList(){
        arrayList = DB.inboxMessages(userid);
        customInboxAdapter = new customInboxAdapter(this,arrayList);
        messagesListView.setAdapter(customInboxAdapter);
        customInboxAdapter.notifyDataSetChanged();
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