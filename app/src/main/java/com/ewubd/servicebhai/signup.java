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
import android.widget.Toast;

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

        if(Phone.length()!= 11 ){
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
        if(error.equals("") && !prvname.equals("") && !Email.equals("") && !prvAdress.equals("")
                && !Phone.equals("") && !prvPassword.equals("") && !checkedOne.equals("") ){
            System.out.println("Insert data");
            String encryptedPassword = getMd5(prvPassword);
            Boolean noError = DB.insertUser(prvname, Email, prvAdress, Phone, encryptedPassword, checkedOne);
            if(noError==true){
                System.out.println("Data Inserted");
                LoginPage();
            }
            else{
                Toast.makeText(getApplicationContext(),"Please Fill All The Fields!!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please Fill All The Fields!!",Toast.LENGTH_LONG).show();
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

}