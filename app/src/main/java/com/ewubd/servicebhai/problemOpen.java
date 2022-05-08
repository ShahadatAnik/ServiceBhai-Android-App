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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class problemOpen extends AppCompatActivity {

    int postid,personid;
    TextView probtitle, probdetail;
    SharedPreferences myPref;
    Button delete, accept, bid, markASDone, createWorkersProfile;
    MyDatabaseHelper DB;
    String userOrWorker;
    int user;

    ArrayList<biddingArrayList> arrayList;
    customBiddingAdapter customBiddingAdapter;
    private ListView biddingListView;
    int userid;
    Boolean hasWorkerProfile = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_open);
        biddingListView = findViewById(R.id.biddingListView);


        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);

        delete = findViewById(R.id.delete);
        accept = findViewById(R.id.accept);
        bid = findViewById(R.id.bidding);
        createWorkersProfile = findViewById(R.id.btn_create_workers_profile);


        markASDone = findViewById(R.id.btn_mark_as_done);
        markASDone.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);
        bid.setVisibility(View.INVISIBLE);
        createWorkersProfile.setVisibility(View.INVISIBLE);

        delete.setOnClickListener(v->deletePost());
        accept.setOnClickListener(v->acceptWork());
        bid.setOnClickListener(v->bidingIntent());
        markASDone.setOnClickListener(v->markAsDoneObj());
        createWorkersProfile.setOnClickListener(v->createWorkersProfileFunction());

        DB= new MyDatabaseHelper(this);

        userOrWorker = DB.userOrWorker(userid);
        System.out.println(userOrWorker);
        if(userOrWorker.equals("User")){
            user = 1;
        }
        else{
            user = 0;
        }

        probtitle = findViewById(R.id.tv_prob_title);
        probdetail = findViewById(R.id.tv_prob_details);

        Bundle extras = getIntent().getExtras();
        String usertype = DB.userOrWorker(userid);
        if (extras != null) {
            postid = extras.getInt("postid");
            personid = extras.getInt("personid");
        }
        if(userid == personid && user == 1){
            delete.setVisibility(View.VISIBLE);
            markASDone.setVisibility(View.VISIBLE);
        }
        if(usertype.equals("Worker")){
            accept.setVisibility(View.VISIBLE);
            hasWorkerProfile = DB.hasWorkerProfile(userid);
            if(hasWorkerProfile == true){
                bid.setVisibility(View.VISIBLE);
            }else{
                createWorkersProfile.setVisibility(View.VISIBLE);
            }
        }
        fetchDataBidding();
        loadDatainList();
    }

    private void createWorkersProfileFunction() {
        Intent intent = new Intent(this, createWorkersProfile.class);
        startActivity(intent);
    }

    private void deletePost() {
        deleteRemote(postid);

    }

    private void problemShowactivity() {
        Intent intent = new Intent(this, problemShow.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        String problem[];
        problem = DB.getProblembyID(postid);
        probtitle.setText(problem[0]);
        probdetail.setText(problem[2]);

        Bundle extras = getIntent().getExtras();
        String usertype = DB.userOrWorker(userid);
        if (extras != null) {
            postid = extras.getInt("postid");
            personid = extras.getInt("personid");
        }
        userOrWorker = DB.userOrWorker(userid);
        System.out.println(userOrWorker);
        if(userOrWorker.equals("User")){
            user = 1;
        }
        else{
            user = 0;
        }
        if(userid == personid && user == 1){
            delete.setVisibility(View.VISIBLE);
            markASDone.setVisibility(View.VISIBLE);
        }
        if(usertype.equals("Worker")){
            accept.setVisibility(View.VISIBLE);
            //bid.setVisibility(View.VISIBLE);
            hasWorkerProfile = DB.hasWorkerProfile(userid);
            if(hasWorkerProfile == true){
                bid.setVisibility(View.VISIBLE);
            }else{
                createWorkersProfile.setVisibility(View.VISIBLE);
            }
        }
        fetchDataBidding();
        loadDatainList();
    }

    void acceptWork(){
        Intent intent = new Intent(this, chatbox.class);
        intent.putExtra("workersIDToSendMessage", personid);
        startActivity(intent);
    }

    void bidingIntent(){
        Intent intent = new Intent(this, bidding.class);
        intent.putExtra("postID", postid);
        startActivity(intent);
    }

    public void loadDatainList(){
        arrayList = DB.bidding(postid);
        customBiddingAdapter = new customBiddingAdapter(this,arrayList, user);
        biddingListView.setAdapter(customBiddingAdapter);
        customBiddingAdapter.notifyDataSetChanged();
    }

    void markAsDoneObj(){
        markAsDoneRemote(postid);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String usertype = DB.userOrWorker(userid);
        userOrWorker = DB.userOrWorker(userid);
        System.out.println(userOrWorker);
        if(userOrWorker.equals("User")){
            user = 1;
        }
        else{
            user = 0;
        }
        if(userid == personid && user == 1){
            delete.setVisibility(View.VISIBLE);
            markASDone.setVisibility(View.VISIBLE);
        }
        if(usertype.equals("Worker")){
            accept.setVisibility(View.VISIBLE);
            //bid.setVisibility(View.VISIBLE);
            hasWorkerProfile = DB.hasWorkerProfile(userid);
            if(hasWorkerProfile == true){
                bid.setVisibility(View.VISIBLE);
            }else{
                createWorkersProfile.setVisibility(View.VISIBLE);
            }
        }
        fetchDataBidding();
        loadDatainList();
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
    public void markAsDoneRemote(int postid) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_MARKASDONE, response -> {
                System.out.println("Post id :" + postid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Updated in Remote DB");
                        Boolean noError = DB.markAsDone(postid);
                        if (noError == true) {
                            System.out.println("Data Updated in local DB");
                            Intent intent = new Intent(this, homepageForUser.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Post Added In History",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Try Again!!", Toast.LENGTH_LONG).show();
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
                    return params;
                }
            };
            MySingleton.getInstance(problemOpen.this).addToRequestQue(stringRequest);
        }
    }
    public void deleteRemote(int postid) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_DELETEPOST, response -> {
                System.out.println("Post id :" + postid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Deleted in Remote DB");
                        Boolean noError = DB.deletePost(postid);
                        if(noError==true){
                            Intent intent = new Intent(this, homepageForUser.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Post DELETED Successfully",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Try Again!!", Toast.LENGTH_LONG).show();
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
                    return params;
                }
            };
            MySingleton.getInstance(problemOpen.this).addToRequestQue(stringRequest);
        }
    }
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}