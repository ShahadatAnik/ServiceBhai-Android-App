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

public class customBiddingAdapter extends BaseAdapter {

    Context context;
    ArrayList<biddingArrayList> arrayList;
    int workersid, personID;
    String workersName;
    public customBiddingAdapter(Context context, ArrayList<biddingArrayList> arrayList){
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

        View rowView = inflater.inflate(R.layout.biddingcustomview, parent, false);

        TextView biddername = rowView.findViewById(R.id.biddername);
        TextView biddingamount = rowView.findViewById(R.id.biddingamount);
        TextView biddernote = rowView.findViewById(R.id.biddernotes);


        biddingArrayList biddingArrayList = arrayList.get(position);



        biddername.setText(biddingArrayList.getUsername());
        biddingamount.setText(String.valueOf(biddingArrayList.getBiddingamount())+" BDT");
        biddernote.setText(biddingArrayList.getComment());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, chatbox.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("workersIDToSendMessage", biddingArrayList.getUserid());
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
