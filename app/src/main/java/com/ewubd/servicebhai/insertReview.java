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
import android.widget.RadioButton;
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

public class insertReview extends AppCompatActivity {
    private EditText reviewRate,workerReview;
    private Button save, back;
    MyDatabaseHelper DB;
    private int workerid,userid;
    SharedPreferences myPref;
    private RadioButton rb_1, rb_2, rb_3, rb_4, rb_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        workerReview = findViewById(R.id.worker_review);

        save = findViewById(R.id.saveReview);
//        back = findViewById(R.id.back);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workerid = extras.getInt("workersidReview");
        }
        DB= new MyDatabaseHelper(this);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);

        save.setOnClickListener(v->saveReview());
        fetchDataRating();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchDataRating();
    }

    private void saveReview() {
        String rate="";
        boolean rb_1_IsChecked = rb_1.isChecked();
        if(rb_1_IsChecked== true){
            rate = "1";
        }
        boolean rb_2_IsChecked = rb_2.isChecked();
        if(rb_2_IsChecked== true){
            rate = "2";
        }
        boolean rb_3_IsChecked = rb_3.isChecked();
        if(rb_3_IsChecked== true){
            rate = "3";
        }
        boolean rb_4_IsChecked = rb_4.isChecked();
        if(rb_4_IsChecked== true){
            rate = "4";
        }
        boolean rb_5_IsChecked = rb_5.isChecked();
        if(rb_5_IsChecked== true){
            rate = "5";
        }

        String review = workerReview.getText().toString().trim();
        savetoAppServer(userid,workerid,rate,review);

    }

    private void savetoAppServer(int userid, int workerid, String rate, String review) {
        if (checkNetworkConnection()) {
//            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_RATING, response -> {
//                System.out.println("Name in :" + userid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.insertRating(userid,workerid,rate,review);
                        if(noError && review != ""){
                            System.out.println("Data Inserted");
                            Toast.makeText(getApplicationContext(),"Rating Has Been Posted Successfully",Toast.LENGTH_LONG).show();
                            workerReview.setText("");
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
                    params.put("raterID", String.valueOf(userid));
                    params.put("userID", String.valueOf(workerid));
                    params.put("rate", String.valueOf(rate));
                    params.put("review", review);
                    return params;
                }
            };
            MySingleton.getInstance(insertReview.this).addToRequestQue(stringRequest);
        } else {
            System.out.println("no network");
        }
    }

    public void fetchDataRating(){
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
                        int rateid = jo.getInt("rateid");
                        int raterID = jo.getInt("raterID");
                        int userID = jo.getInt("userID");
                        int rate = jo.getInt("rate");
                        String review = jo.getString("review");

                        System.out.println(rateid+" "+raterID+" "+userID+" "+rate+" "+review);

                        boolean bool = compareWithRatingRemote(rateid);

                        if(bool){
                            System.out.println("Data Already Present");
                        }
                        else{
                            Boolean noError = DB.insertRating(raterID, userID, String.valueOf(rate), review);
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
        obj.execute(DB.FETCH_RATING);
    }

    private boolean compareWithRatingRemote(int rateid) {
        ArrayList<Integer> id;
        DB = new MyDatabaseHelper(this);
        id = DB.getrateIDfromrating();
        for(int i=0;i<id.size();i++){
            System.out.println(rateid+" "+id.get(i));
            if(rateid == id.get(i)){
                return true;
            }
        }
        return false;
    }

    public boolean checkNetworkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}