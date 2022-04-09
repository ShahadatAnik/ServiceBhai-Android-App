package com.ewubd.servicebhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class customInboxAdapter extends BaseAdapter {

    Context context;
    ArrayList<inboxArrayList> arrayList;
    int getfromID, gettoID;
    String getmessages;

    public customInboxAdapter(Context context, ArrayList<inboxArrayList> arrayList){
        this.context = context;
        this.arrayList = arrayList;
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

        View rowView = inflater.inflate(R.layout.inboxcustomview, parent, false);

        TextView messages = rowView.findViewById(R.id.messageText);
        TextView fromID = rowView.findViewById(R.id.fromUserText);
        TextView toID = rowView.findViewById(R.id.toUserText);
        Button sendToChat = rowView.findViewById(R.id.sendToChat);
        //title.setOnClickListener(v->problemopenpage());


        inboxArrayList inboxArrayList = arrayList.get(position);

        getfromID = inboxArrayList.getFromIDid();
        gettoID = inboxArrayList.getToID();
        getmessages = inboxArrayList.getMessages();

        messages.setText(getmessages);
        fromID.setText(String.valueOf(getfromID));
        toID.setText(String.valueOf(gettoID));
        sendToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chatbox.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("workersIDToSendMessage", inboxArrayList.getFromIDid());
                context.startActivity(intent);
            }
        });


        return rowView;
    }

//    private void problemopenpage() {
//        Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("postid",id);
//        intent.putExtra("personid",personid);
//        context.startActivity(intent);
//    }
}
