package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class signup extends AppCompatActivity {

    private EditText name, email, address, phone, password, rePassword;
    private Button save, login;
    MyDatabaseHealper DB;
    private RadioButton rdUser;
    private RadioButton rdWorker;

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
        save = findViewById(R.id.signUp);
        login = findViewById(R.id.login);

        save.setOnClickListener(v-> saveInfo());
        login.setOnClickListener(v->LoginPage());
        DB= new MyDatabaseHealper(this);
        rdUser = findViewById(R.id.rdUser);
        rdWorker = findViewById(R.id.rdWorker);


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
            Boolean noError = DB.insertUser(prvname, Email, prvAdress, Phone, prvPassword, checkedOne);
            if(noError==true){
                System.out.println("Data Inserted");
                LoginPage();
            }
            else System.out.println("Got some error");
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    void LoginPage(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
}