package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT= 2000;
    MyDatabaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchData();
        new Handler().postDelayed(() -> {
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
            finish();
        },SPLASH_TIME_OUT);

    }
    public void fetchData(){
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
                        int id = jo.getInt("Personid");
                        String name = jo.getString("name");
                        String email = jo.getString("email");
                        String address = jo.getString("address");
                        String phone = jo.getString("phone");
                        String type = jo.getString("type");
                        String password = jo.getString("password");

                        System.out.println(id+" "+name+" "+email+" "+address+" "+phone+" "+type+" "+password);

                        boolean bool = compareWithRemote(email);

                        if(bool){
                            System.out.println("Data Already Present");
                        }
                        else{
                            Boolean noError = DB.insertUser(name, email, address, phone, password, type);
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
        obj.execute(DB.FETCH_USER);
    }
    private boolean compareWithRemote(String remail) {
        ArrayList<String> emailFromSql;
        DB = new MyDatabaseHelper(this);
        emailFromSql = DB.getEmail();
        for(int i=0;i<emailFromSql.size();i++){
            System.out.println(remail+" "+emailFromSql.get(i));
            if(remail.equals(emailFromSql.get(i))){

                return true;
            }
        }
        return false;
    }
}