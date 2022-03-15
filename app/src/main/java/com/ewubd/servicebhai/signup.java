package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
        String error = "";
        String prvname = name.getText().toString().trim();
        if(prvname.length()>15){
            error+="Name length is too high";
        }
        else{
            System.out.println("User name "+ name);
        }
        String Email = email.getText().toString().trim();
        if(isValidEmail(Email) ==  false){
            error+= "Wrong Email";
            error+= "\n";
        }
        else{
            System.out.println("Email "+ Email);
        }
        System.out.println(address.getText().toString().trim());

        String Phone = phone.getText().toString().trim();
        int phoneInt=Integer.parseInt(Phone);
        if(Phone.length()!= 11 || phoneInt <=0 ){
            error+= "Phone number got some error ";
            error+= "\n";
        }
        else{
            System.out.println("Phone "+ phoneInt);
        }
        System.out.println(password.getText().toString().trim());
        System.out.println(rePassword.getText().toString().trim());

        System.out.println("error"+ error);
        if(error==""){

        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}