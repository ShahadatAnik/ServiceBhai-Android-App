package com.ewubd.servicebhai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String Database_name= "serviceBhai";
    private static final int Version= 10;
    private int totalProblem;

    private Context context;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            Toast.makeText(context,"Table Created ",Toast.LENGTH_LONG).show();
            db.execSQL("Create TABLE users (Personid INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),email varchar(50) UNIQUE,address varchar(100),phone varchar(15),type varchar(15), password varchar(50));");
            db.execSQL("Create TABLE workers (workerid INTEGER PRIMARY KEY AUTOINCREMENT,PersonID INTEGER UNIQUE, expertise varchar(50), NIDNumber INTEGER, bio varchar(100), FOREIGN KEY (PersonID) REFERENCES users(Personid) )");
            db.execSQL("Create TABLE problemPosting (postid INTEGER PRIMARY KEY AUTOINCREMENT, PersonID INTEGER,title varchar(50), helptype varchar(50), postdetails varchar(100), FOREIGN KEY (PersonID) REFERENCES users(Personid) )");
        }
        catch (Exception e){
            Toast.makeText(context,"Error: "+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if exists users;");
        db.execSQL("DROP TABLE if exists workers;");
        db.execSQL("DROP TABLE if exists problemPosting;");
        onCreate(db);
    }
    public Boolean insertUser(String name, String email, String address, String phone, String password, String type){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("address", address);
        contentValues.put("phone", phone);
        contentValues.put("password", password);
        contentValues.put("type", type);
        long result = DB.insert("users",null,contentValues);
        if(result==-1) return false;
        else return true;
    }
    public int getUser(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int id = -1;
        Cursor userId = sqLiteDatabase.rawQuery("SELECT Personid from users WHERE email='"+email+"'and password='"+password+"';", null);
        if (userId.moveToFirst()) {
            id = userId.getInt(0);
        }
        return id;
    }
    public String[] getUserProfleInfo(int id){
        String profile[]= new String[6];
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor profileInfo = sqLiteDatabase.rawQuery("SELECT * from users WHERE Personid='"+id+"';", null);
        if (profileInfo.moveToFirst()) {
            for(int i=1;i<=5;i++){
                profile[i] = profileInfo.getString(i);
            }
        }
        return profile;
    }
    public Boolean insertWorker(int personid, String expertise, int NIDNumber, String bio){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PersonID", personid);
        contentValues.put("expertise", expertise);
        contentValues.put("NIDNumber", NIDNumber);
        contentValues.put("bio", bio);
        long result = DB.insert("workers",null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    public String[] getWorkersProfile(int id){
        String profile[]= new String[6];
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor profileInfo = sqLiteDatabase.rawQuery("SELECT * from workers WHERE PersonID='"+id+"';", null);
        if (profileInfo.moveToFirst()) {
            for(int i=0;i<=4;i++){
                    profile[i] = profileInfo.getString(i);
            }
        }
        return profile;
    }
    public Boolean insertproblemPosting(int PersonID,String title,String helptype, String postdetails){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PersonID", PersonID);
        contentValues.put("title", title);
        contentValues.put("helptype", helptype);
        contentValues.put("postdetails", postdetails);
        long result = DB.insert("problemPosting",null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    public ArrayList<postedProblem> getProblems(){
        ArrayList<postedProblem> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getProblem = sqLiteDatabase.rawQuery("SELECT p.postid,p.title, u.name,p.helptype,p.postdetails FROM problemPosting p,users u  WHERE p.Personid = u.Personid;",null);
        while(getProblem.moveToNext()){
            int postid = getProblem.getInt(0);
            String title = getProblem.getString(1);
            String name = getProblem.getString(2);
            String helptype = getProblem.getString(3);
            String postdetail = getProblem.getString(4);
            System.out.println(title);

            postedProblem postedProblem = new postedProblem(postid,title,name,helptype,postdetail);
            arrayList.add(postedProblem);
        }
        return arrayList;
    }
    public int totalProblems(){
        return totalProblem;
    }


}
