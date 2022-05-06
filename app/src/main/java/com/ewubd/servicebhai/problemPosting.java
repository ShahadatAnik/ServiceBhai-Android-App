package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class problemPosting<val> extends AppCompatActivity {

    private EditText postDetails, posttitle;
    private RadioButton rb_electrical, rb_mechanical, rb_plumber, rb_other;
    private Button savetodb;
    MyDatabaseHelper DB;
    SharedPreferences myPref;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_posting);

        rb_electrical = findViewById(R.id.rb_electrical);
        rb_mechanical = findViewById(R.id.rb_mechanical);
        rb_plumber = findViewById(R.id.rb_plumber);
        rb_other = findViewById(R.id.rb_other);

        posttitle = findViewById(R.id.et_problem_title);
        postDetails = findViewById(R.id.et_problem_details);
        savetodb = findViewById(R.id.btn_post_Post_Your_Problem);
        savetodb.setOnClickListener(v -> insertProblem());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);


    }
    void insertProblem(){
        String error = "";

        String type="";
        boolean electricalIsChecked = rb_electrical.isChecked();
        if(electricalIsChecked== true){
            type = "Electrical";
        }

        boolean mechanicalIsChecked = rb_mechanical.isChecked();
        if(mechanicalIsChecked== true){
            type = "Mechanical";
        }
        boolean plumberIsChecked = rb_plumber.isChecked();
        if(plumberIsChecked== true){
            type = "Plumber";
        }

        boolean otherIsChecked = rb_other.isChecked();
        if(otherIsChecked== true){
            type = "Other";
        }

        String detail = postDetails.getText().toString().trim();
        if(detail.length()>201 || detail.length()<1){
            error+= "Detail Length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Post Detail "+ detail);
        }
        String title = posttitle.getText().toString().trim();
        if(title.length()>31 || title.length()<1){
            error+= "title Length is too high";
            error+= "\n";
        }
        else{
            System.out.println("Post Title "+ title);
        }
        if(error.equals("") && !type.equals("") && !title.equals("") && !detail.equals("")){
            System.out.println("Insert data To Problem Posting Table");
            savetoAppServer(userid,title,type,detail);
        }else{
            Toast.makeText(getApplicationContext(),"Please Fill All The Input!!",Toast.LENGTH_LONG).show();
        }
    }

    private void savetoAppServer(int userid, String title, String type, String detail) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_PROBLEMPOSTING, response -> {
                System.out.println("Name in :" + userid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.insertproblemPosting(userid, title, type, detail);
                        if (noError == true) {
                            System.out.println("Data Inserted");
                            problemshow();
                            posttitle.setText("");
                            postDetails.setText("");
                            rb_electrical.setSelected(false);
                            rb_mechanical.setSelected(false);
                            Toast.makeText(getApplicationContext(),"Your Problem Has Been Posted Successfully",Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else{
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
                    params.put("PersonID", String.valueOf(userid));
                    params.put("title", title);
                    params.put("helptype", type);
                    params.put("postdetails", detail);
                    return params;
                }
            };
            MySingleton.getInstance(problemPosting.this).addToRequestQue(stringRequest);
        } else {
            System.out.println("no network");
        }
    }

    void problemshow(){
        Intent i = new Intent(this, problemShow.class);
        startActivity(i);
    }
    public boolean checkNetworkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}