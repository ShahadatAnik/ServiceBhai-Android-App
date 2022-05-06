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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class createWorkersProfile extends AppCompatActivity {

    SharedPreferences myPref;
    Button save;
    MyDatabaseHelper DB;
    EditText nid, bio;
    int userid;
    private RadioButton rdElectrician;
    private RadioButton rdPlumber;
    private RadioButton rdmechanics;
    private RadioButton rdOther;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workers_profile);
        save = findViewById(R.id.saveWorkersProfile);
        nid = findViewById(R.id.et_nidNumber);
        bio = findViewById(R.id.et_Workersbio);
        rdElectrician = findViewById(R.id.rdElectrician);
        rdPlumber = findViewById(R.id.rdPlumber);
        rdmechanics = findViewById(R.id.rdmechanics);
        rdOther = findViewById(R.id.rdOther);
        save.setOnClickListener(v->saveInfo());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);
    }
    void saveInfo(){
        boolean electricianIsChecked = rdElectrician.isChecked();
        String checkedOne="";
        if(electricianIsChecked== true){
            checkedOne = "Electrical";
        }
        boolean plumberIsChecked = rdPlumber.isChecked();
        if(plumberIsChecked== true){
            checkedOne = "Plumber";
        }
        boolean mechanicsIsChecked = rdmechanics.isChecked();
        if(mechanicsIsChecked== true){
            checkedOne = "Mechanical";
        }

        boolean otherIsChecked = rdOther.isChecked();
        if(otherIsChecked== true){
            checkedOne = "Other";
        }
        String prv_nid = nid.getText().toString().trim();
        String prv_bio = bio.getText().toString().trim();
        int prvNid=Integer.parseInt(prv_nid);
        saveToAppServer(userid, checkedOne, prvNid, prv_bio);
    }

    private void saveToAppServer(int userid, String checkedOne, int prvNid, String prv_bio) {
        if (checkNetworkConnection()) {
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_WORKER, response -> {
                System.out.println("Name in :" + userid);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if (Response.equals("ok")) {
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.insertWorker(userid, checkedOne, prvNid, prv_bio);
                        if (noError == true) {
                            System.out.println("Data Inserted");
                            onBackPressed();
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
                    params.put("PersonID", String.valueOf(userid));
                    params.put("expertise", checkedOne);
                    params.put("NIDNumber", String.valueOf(prvNid));
                    params.put("bio", prv_bio);
                    return params;
                }
            };
            MySingleton.getInstance(createWorkersProfile.this).addToRequestQue(stringRequest);
        } else {
            System.out.println("no network");
        }
    }
    public boolean checkNetworkConnection () {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}