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
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bidding extends AppCompatActivity {
    EditText bidingAmount, notes;
    Button save;
    SharedPreferences myPref;
    int userid;
    int postid;
    int amount = -1;
    MyDatabaseHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);
        bidingAmount = findViewById(R.id.bidingAmount);
        notes = findViewById(R.id.additionalnotes);
        save = findViewById(R.id.saveBid);

        save.setOnClickListener(v->saveBid());

        DB= new MyDatabaseHelper(this);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postid = extras.getInt("postID");
        }
        fetchDataBidding();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchDataBidding();
    }

    void saveBid(){
        String prvnote = notes.getText().toString().trim();
        try{
            amount = Integer.parseInt(bidingAmount.getText().toString().trim());
        } catch(NumberFormatException ex){
            System.out.println("Got some error from bidding");
        }

        if(amount != -1 && !prvnote.equals("")){
            saveToAppServer(postid, userid, amount, prvnote);
        }

        else {
            System.out.println("Got some error from bidding");
            Toast.makeText(getApplicationContext(),"Please Fill All The Input!!",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToAppServer(int postid, int userid, int amount, String prvnote) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_BIDDING, response -> {
                System.out.println("Name in :" + userid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.insertBidding(postid, userid, amount, prvnote);
                        if(noError==true){
                            System.out.println("Data Inserted Bidding");
                            Toast.makeText(getApplicationContext(),"Bidding Has Been Posted Successfully",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please Try Again!!",Toast.LENGTH_LONG).show();
                            System.out.println("Got some error");
                        }
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
                    params.put("postid", String.valueOf(postid));
                    params.put("userID", String.valueOf(userid));
                    params.put("biddingAmount", String.valueOf(amount));
                    params.put("comment", prvnote);
                    return params;
                }
            };
            MySingleton.getInstance(bidding.this).addToRequestQue(stringRequest);
        } else {
            System.out.println("no network");
        }
    }

    public boolean checkNetworkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    void postOpen(){
        Intent intent = new Intent(this, problemOpen.class);
        intent.putExtra("postid",postid);
        intent.putExtra("personid",userid);
        startActivity(intent);
    }

    public void fetchDataBidding(){
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
                        int bidingid = jo.getInt("bidingid");
                        int postid = jo.getInt("postid");
                        int userID = jo.getInt("userID");
                        int biddingAmount = jo.getInt("biddingAmount");
                        String comment = jo.getString("comment");

                        System.out.println(bidingid+" "+postid+" "+userID+" "+biddingAmount+" "+comment);

                        boolean bool = compareWithBiddingRemote(bidingid);

                        if(bool){
                            System.out.println("Data Already Present Bidding login");
                        }
                        else{
                            Boolean noError = DB.insertBidding(postid, userID, biddingAmount, comment);
                            if(noError){
                                System.out.println("Data Inserted Bidding login");
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
        obj.execute(DB.FETCH_BIDDING);
    }
    private boolean compareWithBiddingRemote(int rateid) {
        ArrayList<Integer> id;
        DB = new MyDatabaseHelper(this);
        id = DB.getBIDDING();
        for(int i=0;i<id.size();i++){
            System.out.println(rateid+" "+id.get(i));
            if(rateid == id.get(i)){
                return true;
            }
        }
        return false;
    }
}