package com.ewubd.servicebhai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class customChatAdapter extends BaseAdapter {

    Context context;
    ArrayList<chatArrayList> arrayList;
    int getfromID;
    int userID;
    String getmessages;

    public customChatAdapter(Context context, ArrayList<chatArrayList> arrayList, int userID){
        this.context = context;
        this.arrayList = arrayList;
        this.userID = userID;
    }
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.chatcustomview, parent, false);

        TextView messages = rowView.findViewById(R.id.messageTextChat);

        chatArrayList chatArrayList = arrayList.get(position);

        getfromID = chatArrayList.getFromIDid();
        getmessages = chatArrayList.getMessages();

        messages.setText(getmessages);

        if(chatArrayList.getFromIDid()==userID){

            messages.setGravity(Gravity.RIGHT);
            messages.setBackgroundResource(R.drawable.signup_btn_shaper);
            messages.setTextColor(context.getResources().getColor(R.color.white));

        } else{
            messages.setGravity(Gravity.LEFT);
            messages.setTextColor(context.getResources().getColor(R.color.green));

        }

        //System.out.println(userID);


        return rowView;
    }

//    private void problemopenpage() {
//        Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("postid",id);
//        intent.putExtra("personid",personid);
//        context.startActivity(intent);
//    }
}
