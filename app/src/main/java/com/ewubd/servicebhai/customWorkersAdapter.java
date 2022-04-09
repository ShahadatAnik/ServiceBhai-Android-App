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

public class customWorkersAdapter extends BaseAdapter {

    Context context;
    ArrayList<workersByCatagory> arrayList;
    int workersid;
    String workersName;
    public customWorkersAdapter(Context context, ArrayList<workersByCatagory> arrayList){
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

        View rowView = inflater.inflate(R.layout.workerscustomview, parent, false);

        TextView name = rowView.findViewById(R.id.workersName);
        TextView bio = rowView.findViewById(R.id.bio);
        Button contact = rowView.findViewById(R.id.sendWorkerMessage);
        //name.setOnClickListener(v->problemopenpage());


        workersByCatagory workersByCatagory = arrayList.get(position);

        workersid = workersByCatagory.getWorkersid();
        workersName = workersByCatagory.getWorkesrName();

        name.setText(workersByCatagory.getWorkesrName());
        bio.setText(workersByCatagory.getBio());
        //System.out.println(workersid);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chatbox.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("workersIDToSendMessage", workersid);
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
