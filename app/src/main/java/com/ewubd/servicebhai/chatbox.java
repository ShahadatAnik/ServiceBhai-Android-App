package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class chatbox extends AppCompatActivity {
    int workersID;
    SharedPreferences myPref;
    ImageView sendMessages;
    EditText messagesBox;
    TextView inboxname;
    int userid;
    MyDatabaseHelper DB;
    ArrayList<chatArrayList> arrayList;
    customChatAdapter customChatAdapter;
    private ListView chatListView;
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
        chatListView = findViewById(R.id.chatListView);
        inboxname = findViewById(R.id.inboxname);
        String getname = DB.getUserame(workersID);
        inboxname.setText(getname);
        inboxname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatbox.this, userProfile.class);
                intent.putExtra("Flag", 1);
                intent.putExtra("userID", workersID);
                startActivity(intent);
            }
        });
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

    void sendingMessages(){
        String message = messagesBox.getText().toString().trim();

        if(!message.equals("")){
            savetoAppServer(userid, workersID, message);
        } else Toast.makeText(getApplicationContext(),"Write Something!!",Toast.LENGTH_LONG).show();

    }

    private void savetoAppServer(int userid, int workersID, String message) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_MESSAGES, response -> {
                System.out.println("Name in :" + userid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.sendMessages(userid, workersID, message);
                        if (noError == true) {
                            System.out.println("Message Send");
                            messagesBox.getText().clear();
                            loadDatainList();
                        } else System.out.println("Got some error");
                    } else {
                        System.out.println("Error Data not inserted in remote1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> System.out.println(error)) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat();
                    Calendar c = Calendar.getInstance();
                    params.put("fromID", String.valueOf(userid));
                    params.put("toID", String.valueOf(workersID));
                    params.put("message", message);
                    String date = dateFormat.format(c.getTime());
                    System.out.println(date);
                    params.put("datetime", date);
                    return params;
                }
            };
            MySingleton.getInstance(chatbox.this).addToRequestQue(stringRequest);
        } else {
            System.out.println("no network");
        }
    }

    public void loadDatainList(){
        arrayList = DB.chatBox(userid, workersID);
        customChatAdapter = new customChatAdapter(this,arrayList, userid);
        chatListView.setAdapter(customChatAdapter);
        customChatAdapter.notifyDataSetChanged();
    }
    public boolean checkNetworkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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