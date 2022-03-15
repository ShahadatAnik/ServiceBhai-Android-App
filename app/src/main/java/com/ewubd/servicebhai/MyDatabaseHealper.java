package com.ewubd.servicebhai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHealper extends SQLiteOpenHelper {
    private static final String Database_name= "serviceBhai";
    private static final int Version= 1;


    private Context context;

    public MyDatabaseHealper(@Nullable Context context) {
        super(context, Database_name, null, Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            Toast.makeText(context,"Table Created ",Toast.LENGTH_LONG).show();
            db.execSQL("Create TABLE users (Personid INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),email varchar(50),address varchar(100),phone varchar(15),password varchar(50));");

        }
        catch (Exception e){
            Toast.makeText(context,"Error: "+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE if exists users;");
        onCreate(db);
    }
    public Boolean insertUser(String name, String email, String address, String phone, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("address", address);
        contentValues.put("phone", phone);
        contentValues.put("password", password);
        long result = DB.insert("users",null,contentValues);
        if(result==-1) return false;
        else return true;
    }

}
