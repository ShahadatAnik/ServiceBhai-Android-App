package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class signup extends AppCompatActivity {

    private EditText name, email, address, phone, password, rePassword;
    private Button save, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.password2);
        save = findViewById(R.id.save);

        save.setOnClickListener(v-> saveInfo());

    }
    void saveInfo(){
        System.out.println(name.getText().toString().trim());
        System.out.println(email.getText().toString().trim());
        System.out.println(address.getText().toString().trim());
        System.out.println(phone.getText().toString().trim());
        System.out.println(password.getText().toString().trim());
        System.out.println(rePassword.getText().toString().trim());
    }
}