package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class problemShow extends AppCompatActivity {

    private ListView postShow;
    MyDatabaseHelper DB;
    ArrayList<postedProblem> arrayList;
    customProblemAdapter customProblemAdapter;
    int fromWorker = 0;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_show);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromWorker = extras.getInt("fromWorkers");
            category = extras.getString("category");
        }

        postShow = findViewById(R.id.postShow);
        DB= new MyDatabaseHelper(this);

        Login login = new Login();
        login.fetchDataproblemposting();

        arrayList = new ArrayList<>();
        fetchDataproblemposting();
        loadDatainList();

    }
    public void loadDatainList(){
        arrayList = DB.getProblems();
        customProblemAdapter = new customProblemAdapter(this,arrayList, 0, fromWorker, category);
        postShow.setAdapter(customProblemAdapter);
        customProblemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchDataproblemposting();
        loadDatainList();
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
}