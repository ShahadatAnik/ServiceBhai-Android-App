package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, signup;
    MyDatabaseHealper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signupButton);
        signup.setOnClickListener(v -> signupPage());
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(v -> login());
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);

        DB= new MyDatabaseHealper(this);
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
        }

    }
}