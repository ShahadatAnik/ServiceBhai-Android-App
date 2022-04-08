package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    MyDatabaseHelper DB;
    SharedPreferences myPref;
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
        int userid = myPref.getInt("loggedInID", -1);
        System.out.println(userid);
        if(userid!= -1){
            String userOrWorker = DB.userOrWorker(userid);
            if(userOrWorker.equals("Worker")) homePageProvoke();
            else homePageForUserProvoke();
        }
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
            System.out.println("Error on email or password");
        }
        else{
            System.out.println(userid);
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
}