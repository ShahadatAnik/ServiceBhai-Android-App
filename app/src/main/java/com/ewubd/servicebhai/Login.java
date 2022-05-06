package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    MyDatabaseHelper DB;
    SharedPreferences myPref;
    static int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup_btn_login_page);
        signup.setOnClickListener(v -> signupPage());

        login = findViewById(R.id.login_btn_login_page);
        login.setOnClickListener(v -> login());

        email = findViewById(R.id.et_email_login_page);
        password = findViewById(R.id.et_pass_login_page);

        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
        //System.out.println(userid);
        if(userid!= -1){
            String userOrWorker = DB.userOrWorker(userid);
            if(userOrWorker!=null){
                if(userOrWorker.equals("Worker")) homePageProvoke();
                else homePageForUserProvoke();
            }
            else{
                signupPage();
            }
        }
        fetchDataWORKER();
        fetchDataproblemposting();
        fetchDataRating();
    }
    void signupPage(){
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
    void login(){
        String privEmail = email.getText().toString().trim();
        String privPassword = password.getText().toString().trim();
        String encrytpedPassword = getMd5(privPassword);
        int userid = DB.getUser(privEmail, encrytpedPassword);
        if(userid==-1){
            Toast.makeText(getApplicationContext(),"Please Input Valid Email and Password",Toast.LENGTH_SHORT).show();
            System.out.println("Error on email or password");
        }
        else{
            //System.out.println(userid);
            myPref.edit().putInt("loggedInID", userid).apply();
            String userOrWorker = DB.userOrWorker(userid);
            if(userOrWorker.equals("Worker")) homePageProvoke();
            else homePageForUserProvoke();
        }

    }
    void homePageProvoke(){
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }
    void homePageForUserProvoke(){
        Intent intent = new Intent(this, homepageForUser.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public static String getMd5(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public int getUserid(){
        return this.userid;
    }

    public void fetchDataWORKER(){
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
                        int id = jo.getInt("workerID");
                        int personID = jo.getInt("PersonID");
                        String expertise = jo.getString("expertise");
                        int nid = jo.getInt("NIDNumber");
                        String bio = jo.getString("bio");

                        System.out.println(id+" "+personID+" "+expertise+" "+nid+" "+bio);

                        boolean bool = compareWithRemote(personID);

                        if(bool){
                            System.out.println("Data Already Present");
                        }
                        else{
                            Boolean noError = DB.insertWorker(personID, expertise, nid, bio);
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
        obj.execute(DB.FETCH_WORKER);
    }
    public void fetchDataproblemposting(){
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
                        int postid = jo.getInt("postid");
                        int personID = jo.getInt("PersonID");
                        String title = jo.getString("title");
                        String helptype = jo.getString("helptype");
                        String postdetails = jo.getString("postdetails");

                        System.out.println(postid+" "+personID+" "+title+" "+helptype+" "+postdetails);

                        boolean bool = compareWithproblemRemote(postid);

                        if(bool){
                            System.out.println("Data Already Present");
                        }
                        else{
                            Boolean noError = DB.insertproblemPosting(personID, title, helptype, postdetails);
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
        obj.execute(DB.FETCH_PROBLEM);
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

    private boolean compareWithRemote(int personID) {
        ArrayList<Integer> id;
        DB = new MyDatabaseHelper(this);
        id = DB.getPersonIDfromworker();
        for(int i=0;i<id.size();i++){
            System.out.println(personID+" "+id.get(i));
            if(personID == id.get(i)){
                return true;
            }
        }
        return false;
    }
    private boolean compareWithproblemRemote(int postID) {
        ArrayList<Integer> id;
        DB = new MyDatabaseHelper(this);
        id = DB.getPersonIDfromproblem();
        for(int i=0;i<id.size();i++){
            System.out.println(postID+" "+id.get(i));
            if(postID == id.get(i)){
                return true;
            }
        }
        return false;
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


}