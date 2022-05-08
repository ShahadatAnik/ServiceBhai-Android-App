package com.ewubd.servicebhai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String Database_name= "serviceBhai";
    private static final int Version= 21;
    private int totalProblem;
    public static final String SERVER_URL = "https://servicevai.000webhostapp.com/ServiceBhai/insert.php";
    public static final String FETCH_USER = "https://servicevai.000webhostapp.com/ServiceBhai/selectUser.php";
    public static final String FETCH_WORKER = "https://servicevai.000webhostapp.com/ServiceBhai/selectWorker.php";
    public static final String FETCH_PROBLEM = "https://servicevai.000webhostapp.com/ServiceBhai/selectProblemPosting.php";

    public static final String FETCH_BIDDING = "https://servicevai.000webhostapp.com/ServiceBhai/selectBidding.php";

    public static final String SERVER_WORKER = "https://servicevai.000webhostapp.com/ServiceBhai/insertWorker.php";
    public static final String SERVER_PROBLEMPOSTING = "https://servicevai.000webhostapp.com/ServiceBhai/insertProblemPosting.php";
    public static final String SERVER_RATING = "https://servicevai.000webhostapp.com/ServiceBhai/insertRating.php";
    public static final String FETCH_RATING = "https://servicevai.000webhostapp.com/ServiceBhai/selectRating.php";
    public static final String SERVER_MESSAGES = "https://servicevai.000webhostapp.com/ServiceBhai/insertMessages.php";
    public static final String SERVER_BIDDING = "https://servicevai.000webhostapp.com/ServiceBhai/insertBidding.php";
    public static final String FETCH_MESSAGES = "https://servicevai.000webhostapp.com/ServiceBhai/selectMessages.php";
    public static final String SERVER_MARKASDONE = "https://servicevai.000webhostapp.com/ServiceBhai/markAsDone.php";
    public static final String SERVER_DELETEPOST = "https://servicevai.000webhostapp.com/ServiceBhai/deletePost.php";

    private Context context;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, Database_name, null, Version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL("Create TABLE users (Personid INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),email varchar(50) UNIQUE,address varchar(100),phone varchar(15),type varchar(15), password varchar(50));");
            db.execSQL("Create TABLE workers (workerid INTEGER PRIMARY KEY AUTOINCREMENT,PersonID INTEGER UNIQUE, expertise varchar(50), NIDNumber INTEGER, bio varchar(100), FOREIGN KEY (PersonID) REFERENCES users(Personid) )");
            db.execSQL("Create TABLE problemPosting (postid INTEGER PRIMARY KEY AUTOINCREMENT, PersonID INTEGER,title varchar(50), helptype varchar(50), postdetails varchar(100), markAsDone INTEGER DEFAULT 0, FOREIGN KEY (PersonID) REFERENCES users(Personid) )");
            db.execSQL("Create TABLE messages (messageid INTEGER PRIMARY KEY AUTOINCREMENT, fromID INTEGER, toID INTEGER, message varchar(100), datetime date, FOREIGN KEY (fromID) REFERENCES users(Personid), FOREIGN KEY (toID) REFERENCES users(Personid) )");
            db.execSQL("Create TABLE rating (rateid INTEGER PRIMARY KEY AUTOINCREMENT, raterID INTEGER, userID INTEGER, rate INTEGER,review varchar(100), FOREIGN KEY (userID) REFERENCES users(Personid), FOREIGN KEY (raterID) REFERENCES users(Personid) )");
            db.execSQL("Create TABLE biding (bidingid INTEGER PRIMARY KEY AUTOINCREMENT, postid INTEGER, userID INTEGER, biddingAmount INTEGER,comment varchar(100), FOREIGN KEY (postid) REFERENCES problemPosting(postid), FOREIGN KEY (userID) REFERENCES users(Personid))");
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
        db.execSQL("DROP TABLE if exists messages;");
        db.execSQL("DROP TABLE if exists rating;");
        db.execSQL("DROP TABLE if exists biding;");
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
        if(result==-1){

            return false;
        }
        else{

            return true;
        }
    }

    public ArrayList<postedProblem> getProblems(){
        ArrayList<postedProblem> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getProblem = sqLiteDatabase.rawQuery("SELECT p.postid,p.personid,p.title, u.name,p.helptype,p.postdetails,p.markAsDone FROM problemPosting p,users u  WHERE p.Personid = u.Personid;",null);
        while(getProblem.moveToNext()){
            int postid = getProblem.getInt(0);
            int personid = getProblem.getInt(1);
            String title = getProblem.getString(2);
            String name = getProblem.getString(3);
            String helptype = getProblem.getString(4);
            String postdetail = getProblem.getString(5);
            int marksAsDone = getProblem.getInt(6);
            //System.out.println(title);
            if(marksAsDone==0){
                postedProblem postedProblem = new postedProblem(postid,personid,title,name,helptype,postdetail, marksAsDone);
                arrayList.add(postedProblem);
            }

        }
        return arrayList;
    }
    public int totalProblems(){
        return totalProblem;
    }

    public String[] getProblembyID(int id){
        String problem[]= new String[6];
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor problems = sqLiteDatabase.rawQuery("SELECT title,helptype,postdetails from problemPosting WHERE postid='"+id+"';", null);
        if (problems.moveToFirst()) {
            for(int i=0;i<=2;i++){
                problem[i] = problems.getString(i);
            }
        }
        return problem;
    }

    public Boolean deletePost(int postid){
        SQLiteDatabase DB = this.getWritableDatabase();
        long result = DB.delete("problemPosting", "postid = ?", new String[]{String.valueOf(postid)});
        if(result==-1){
            Toast.makeText(context,"Please Try Again!!",Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            Toast.makeText(context,"Your Problem Has Been Deleted Successfully",Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public String userOrWorker (int personid){
        String userorworker = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getType = sqLiteDatabase.rawQuery("SELECT type from users WHERE Personid='"+personid+"';", null);
        if (getType.moveToFirst()) {
            userorworker = getType.getString(0);
        }
        return userorworker;
    }

    public ArrayList<workersByCatagory> workersByCatagory(String category){
        ArrayList<workersByCatagory> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getWorkers = sqLiteDatabase.rawQuery("SELECT w.workerid, u.name, w.bio, u.Personid FROM workers w, users u  WHERE w.PersonID = u.Personid and w.expertise ='"+category+"';",null);
        while(getWorkers.moveToNext()){
            int workersid = getWorkers.getInt(0);
            String workersname = getWorkers.getString(1);
            String workersbio = getWorkers.getString(2);
            int personid = getWorkers.getInt(3);
            workersByCatagory workersByCatagory = new workersByCatagory(workersid, workersname, workersbio, personid);
            arrayList.add(workersByCatagory);
        }
        return arrayList;
    }

    public Boolean sendMessages(int fromID, int toID, String message){
        SQLiteDatabase DB = this.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Calendar c = Calendar.getInstance();
//        Date date = new Date();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fromID", fromID);
        contentValues.put("toID", toID);
        contentValues.put("message", message);
        contentValues.put("datetime", dateFormat.format(c.getTime()));
        if(message.equals("")){
            Toast.makeText(context,"Write Something",Toast.LENGTH_LONG).show();
            return false;
        } else{
            long result = DB.insert("messages",null,contentValues);
            if(result==-1) return false;
            else return true;
        }

    }

    public ArrayList<inboxArrayList> inboxMessages(int id){
        ArrayList<inboxArrayList> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getWorkers = sqLiteDatabase.rawQuery("SELECT a.fromID, a.toID, a.message, a.datetime FROM messages a LEFT OUTER JOIN messages b ON a.fromID = b.fromID AND a.messageid < b.messageid WHERE (b.fromID IS NULL) and (a.toID = "+id+") ORDER BY a.messageid desc;",null);
        while(getWorkers.moveToNext()){
            int fromID = getWorkers.getInt(0);
            int toID = getWorkers.getInt(1);
            String messages = getWorkers.getString(2);
            String dateTime = getWorkers.getString(3);
            String name = getUserame(fromID);
            inboxArrayList inboxArrayList = new inboxArrayList(fromID, toID, messages, dateTime, name);
            arrayList.add(inboxArrayList);
        }
        return arrayList;
    }

    public ArrayList<chatArrayList> chatBox(int fromID, int toID){
        ArrayList<chatArrayList> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getWorkers = sqLiteDatabase.rawQuery("select fromID, message, datetime from messages  where  (fromID="+fromID+" and toID ="+toID+") or (fromID="+toID+" and toID="+fromID+") order by messageid asc;",null);
        while(getWorkers.moveToNext()){
            int fromIDD = getWorkers.getInt(0);
            String messages = getWorkers.getString(1);
            String date = getWorkers.getString(2);
            chatArrayList chatArrayList = new chatArrayList(fromIDD, messages, date);
            arrayList.add(chatArrayList);
        }
        return arrayList;
    }

    public int messageCountOfUser(int userid){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor count = sqLiteDatabase.rawQuery("select count(message) from messages where toID = "+userid+";",null);
        count.moveToFirst();
        int countInt= count.getInt(0);
        return countInt;
    }
    public Boolean insertRating(int raterID,int userID,String rate, String review){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("raterID", raterID);
        contentValues.put("userID", userID);
        contentValues.put("rate", rate);
        contentValues.put("review", review);
        long result = DB.insert("rating",null,contentValues);
        if(result==-1){

            return false;
        }
        else{

            return true;
        }
    }
    public ArrayList<workerReviewClass> getReviewbyID(int id){
        ArrayList<workerReviewClass> reviews = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor workerReviews = sqLiteDatabase.rawQuery("SELECT * from rating WHERE userID='"+id+"';", null);
        while(workerReviews.moveToNext()){
            int rateid = workerReviews.getInt(0);
            int raterid = workerReviews.getInt(1);
            int userID = workerReviews.getInt(2);
            int rate = workerReviews.getInt(3);
            String review = workerReviews.getString(4);
            String ratername = getUserame(raterid);
            workerReviewClass workerReviewClass =new workerReviewClass(rateid,raterid,userID,rate,review, ratername);
            reviews.add(workerReviewClass);
        }
        return reviews;
    }


    public String getUserame(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor count = sqLiteDatabase.rawQuery("select name from users where Personid = "+id+";",null);
        count.moveToFirst();
        String name= count.getString(0);
        return name;
    }

    public Boolean insertBidding(int postID,int userID,int biddingAmount, String notes){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("postid", postID);
        contentValues.put("userID", userID);
        contentValues.put("biddingAmount", biddingAmount);
        contentValues.put("comment", notes);
        long result = DB.insert("biding",null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList<biddingArrayList> bidding(int postID){
        ArrayList<biddingArrayList> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor bidding = sqLiteDatabase.rawQuery("select bidingid, postid, userID, biddingAmount, comment  from biding  where postid="+postID+";",null);
        while(bidding.moveToNext()){
            int biddingid = bidding.getInt(0);
            int postid = bidding.getInt(1);
            int userid = bidding.getInt(2);
            int biddingamount = bidding.getInt(3);
            String comment = bidding.getString(4);
            String biddername = getUserame(userid);
            int workersID = getWorkersID(userid);
            biddingArrayList biddingArrayList = new biddingArrayList(biddingid, postid, userid, biddingamount, comment, biddername, workersID);
            arrayList.add(biddingArrayList);
        }
        return arrayList;
    }

    public boolean markAsDone(int postID){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("markAsDone", 1);
        long result = DB.update("problemPosting",contentValues, "postid="+postID,null);
        if(result==-1) return false;
        else return true;
    }

    public ArrayList<postedProblem> history(int userID){
        ArrayList<postedProblem> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor getProblem = sqLiteDatabase.rawQuery("SELECT p.postid,p.personid,p.title, u.name,p.helptype,p.postdetails,p.markAsDone FROM problemPosting p,users u  WHERE p.Personid = u.Personid;",null);
        while(getProblem.moveToNext()){
            int postid = getProblem.getInt(0);
            int personid = getProblem.getInt(1);
            String title = getProblem.getString(2);
            String name = getProblem.getString(3);
            String helptype = getProblem.getString(4);
            String postdetail = getProblem.getString(5);
            int marksAsDone = getProblem.getInt(6);
            //System.out.println(title);
            if(marksAsDone==1 && userID == personid){
                postedProblem postedProblem = new postedProblem(postid,personid,title,name,helptype,postdetail, marksAsDone);
                arrayList.add(postedProblem);
            }

        }
        return arrayList;
    }

    public int getWorkersID(int userID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor count = sqLiteDatabase.rawQuery("select workerid from workers where PersonID = "+userID+";",null);
        count.moveToFirst();
        int workerid= count.getInt(0);
        return workerid;
    }

    public boolean hasWorkerProfile(int userID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor count = sqLiteDatabase.rawQuery("select workerid from workers where PersonID = "+userID+";",null);
        if(count.moveToFirst() == false){
            return false;
        }
        else{
            return true;
        }
    }

    public int getWorkersPersonID(int workersID){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor count = sqLiteDatabase.rawQuery("select PersonID from workers where workerid = "+workersID+";",null);
        count.moveToFirst();
        int workersPersonID= count.getInt(0);
        return workersPersonID;
    }
    public ArrayList<String> getEmail(){
        ArrayList<String> emailList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select email from users;",null);
        while(emails.moveToNext()){
            String emailfromSQL = emails.getString(0);
            emailList.add(emailfromSQL);
        }
        return emailList;
    }
    public ArrayList<Integer> getPersonIDfromworker(){
        ArrayList<Integer> personidlist = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select PersonID from workers;",null);
        while(emails.moveToNext()){
            int emailfromSQL = emails.getInt(0);
            personidlist.add(emailfromSQL);
        }
        return personidlist;
    }
    public ArrayList<Integer> getPersonIDfromproblem(){
        ArrayList<Integer> postidlist = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select postid, markAsDone from problemPosting;",null);
        while(emails.moveToNext()){
            int emailfromSQL = emails.getInt(0);
            postidlist.add(emailfromSQL);
        }
        return postidlist;
    }


    public ArrayList<Integer> getMessageFromRemote(){
        ArrayList<Integer> postidlist = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select messageid from messages;",null);
        while(emails.moveToNext()){
            int emailfromSQL = emails.getInt(0);
            postidlist.add(emailfromSQL);
        }
        return postidlist;
    }

    public ArrayList<Integer> getrateIDfromrating(){
        ArrayList<Integer> rateid = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select rateid from rating;",null);
        while(emails.moveToNext()){
            int emailfromSQL = emails.getInt(0);
            rateid.add(emailfromSQL);
        }
        return rateid;
    }

    public ArrayList<Integer> getBIDDING(){
        ArrayList<Integer> rateid = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor emails = sqLiteDatabase.rawQuery("select bidingid from biding;",null);
        while(emails.moveToNext()){
            int emailfromSQL = emails.getInt(0);
            rateid.add(emailfromSQL);
        }
        return rateid;
    }

}
