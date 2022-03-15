package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

public class signup extends AppCompatActivity {

    private EditText name, email, address, phone, password, rePassword;
    private Button save, login;
    MyDatabaseHealper DB;
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
        DB= new MyDatabaseHealper(this);

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
        if(isValidEmail(Email) ==  false){
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
        if(error==""){
            System.out.println("Insert data");
            Boolean noError = DB.insertUser(prvname, Email, prvAdress, Phone, prvPassword);
            if(noError==true){
                System.out.println("Data Inserted");
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            }
            else System.out.println("Got some error");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}