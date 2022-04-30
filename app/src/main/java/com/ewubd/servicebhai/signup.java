package com.ewubd.servicebhai;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    private EditText name, email, address, phone, password, rePassword;
    private Button save, login;
    MyDatabaseHelper DB;
    private RadioButton rdUser;
    private RadioButton rdWorker;
    SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        address = findViewById(R.id.et_address);
        phone = findViewById(R.id.et_phone);
        password = findViewById(R.id.et_pass);
        rePassword = findViewById(R.id.et_confirm_pass);
        rdUser = findViewById(R.id.rdUser);
        rdWorker = findViewById(R.id.rdWorker);
        save = findViewById(R.id.signUp);
        login = findViewById(R.id.login);

        save.setOnClickListener(v-> saveInfo());
        login.setOnClickListener(v->LoginPage());
        DB= new MyDatabaseHelper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        myPref.edit().putInt("loggedInID", -1).apply();



    }
    void saveInfo(){
        String error = "";
        String prvname = name.getText().toString().trim();
        if(prvname.length()>15){
            error+="Name length is too high";
        }
        else{
            System.out.println("User name "+ prvname);
        }
        String Email = email.getText().toString().trim();
        if(isValidEmail(Email) == false){
            error+= "Wrong Email";
            error+= "\n";
        }
        else{
            System.out.println("Email "+ Email);
        }
        String prvAdress = address.getText().toString().trim();

        String Phone = phone.getText().toString().trim();
        int phoneInt=Integer.parseInt(Phone);
        if(Phone.length()!= 11 || phoneInt <=0 ){
            error+= "Phone number got some error ";
            error+= "\n";
        }
        else{
            System.out.println("Phone "+ Phone);
        }

        boolean userIsChecked = rdUser.isChecked();
        String checkedOne="";
        if(userIsChecked== true){
            checkedOne = "User";
        }

        boolean workerIsChecked = rdWorker.isChecked();
        if(workerIsChecked== true){
            checkedOne = "Worker";
        }


        String prvPassword = password.getText().toString().trim();
        String prvPassword2 = rePassword.getText().toString().trim();
        if(prvPassword.equals(prvPassword2)){
            System.out.println(prvPassword);
        }
        else{
            error+= "Password error ";
            error+= "\n";
            System.out.println("Password Mismatch");
        }

        System.out.println("error"+ error);
        System.out.println(checkedOne);
        if(error==""){
            System.out.println("Insert data");
            String encryptedPassword = getMd5(prvPassword);
            saveToAppServer(prvname,Email,prvAdress,Phone,checkedOne,encryptedPassword);

        }
    }

    private void saveToAppServer(String prvname, String email, String prvAdress, String phone, String checkedOne, String encryptedPassword) {
        if(checkNetworkConnection()){
            System.out.println("ggwp");
            MyDatabaseHelper DB2 = new MyDatabaseHelper(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DB2.SERVER_URL, response -> {
                System.out.println("Name in :"+prvname);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    System.out.println(Response);
                    if(Response.equals("ok")){
                        System.out.println("Data Inserted in Remote DB");
                        Boolean noError = DB.insertUser(prvname, email, prvAdress, phone, encryptedPassword, checkedOne);
                        if(noError==true){
                            System.out.println("Data Inserted");
                            LoginPage();
                        }
                        else System.out.println("Got some error");
                    }
                    else{
                        System.out.println("Error Data not inserted in remote1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> System.out.println(error))
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("name",prvname);
                    params.put("email",email);
                    params.put("address",prvAdress);
                    params.put("phone",phone);
                    params.put("type",checkedOne);
                    params.put("password",encryptedPassword);
                    return params;
                }
            };
            MySingleton.getInstance(signup.this).addToRequestQue(stringRequest);
        }
        else{
            System.out.println("no network");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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

    void LoginPage(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }
}