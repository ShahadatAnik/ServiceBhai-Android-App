package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    MyDatabaseHealper DB;
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

        DB= new MyDatabaseHealper(this);
        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        int userid = myPref.getInt("loggedInID", -1);
        System.out.println(userid);
        if(userid!= -1){
            homePageProvoke();
        }
    }
    void signupPage(){
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }
    void login(){
        String privEmail = email.getText().toString().trim();
        String privPassword = password.getText().toString().trim();
        int userid = DB.getUser(privEmail, privPassword);
        if(userid==-1){
            System.out.println("Error on email or password");
        }
        else{
            System.out.println(userid);
            myPref.edit().putInt("loggedInID", userid).apply();
            homePageProvoke();
        }

    }
    void homePageProvoke(){
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}