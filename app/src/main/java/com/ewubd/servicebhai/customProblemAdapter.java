package com.ewubd.servicebhai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customProblemAdapter extends BaseAdapter {

    Context context;
    ArrayList<postedProblem> arrayList;
    int id,personid, flag = 1, fromWorker;
    String category;
    public customProblemAdapter(Context context, ArrayList<postedProblem> arrayList, int flag, int fromWorker, String category){
        this.context = context;
        this.arrayList = arrayList;
        this.flag = flag;
        this.fromWorker = fromWorker;
        this.category = category;
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

        View rowView = inflater.inflate(R.layout.problemcustomview, parent, false);

        TextView title = rowView.findViewById(R.id.title);
        TextView post_type = rowView.findViewById(R.id.post_helptype);
        TextView post_desc = rowView.findViewById(R.id.tv_description);

        title.setText("");
        post_type.setText("");
        post_desc.setText("");

        postedProblem postedProblem = arrayList.get(position);

        if(fromWorker == 0){
            if(postedProblem.getTitle().length() > 15){
                title.setText(postedProblem.getTitle().substring(0,15)+"...");
            }
            else title.setText(postedProblem.getTitle());

            post_type.setText(postedProblem.getHelptype());
            if(postedProblem.getPostdetail().length() > 30){
                post_desc.setText(postedProblem.getPostdetail().substring(0,30)+"...");
            }
            else post_desc.setText(postedProblem.getPostdetail());
            if(flag == 0){
                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("postid",postedProblem.getId());
                        intent.putExtra("personid",postedProblem.getPersonid());
                        context.startActivity(intent);
                    }
                });
            }
        }
        else{
            if(category.equals("Electrician")){
                if(postedProblem.getHelptype().equals("Electrical")){
                    if(postedProblem.getTitle().length() > 15){
                        title.setText(postedProblem.getTitle().substring(0,15)+"...");
                    }
                    else title.setText(postedProblem.getTitle());

                    post_type.setText(postedProblem.getHelptype());
                    if(postedProblem.getPostdetail().length() > 30){
                        post_desc.setText(postedProblem.getPostdetail().substring(0,30)+"...");
                    }
                    else post_desc.setText(postedProblem.getPostdetail());
                    if(flag == 0){
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("postid",postedProblem.getId());
                                intent.putExtra("personid",postedProblem.getPersonid());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                else{
                    rowView = inflater.inflate(R.layout.blank_layout, parent, false);
                }
            }
            if(category.equals("Mechanics")){
                if(postedProblem.getHelptype().equals("Mechanical")){
                    if(postedProblem.getTitle().length() > 15){
                        title.setText(postedProblem.getTitle().substring(0,15)+"...");
                    }
                    else title.setText(postedProblem.getTitle());

                    post_type.setText(postedProblem.getHelptype());
                    if(postedProblem.getPostdetail().length() > 30){
                        post_desc.setText(postedProblem.getPostdetail().substring(0,30)+"...");
                    }
                    else post_desc.setText(postedProblem.getPostdetail());
                    if(flag == 0){
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("postid",postedProblem.getId());
                                intent.putExtra("personid",postedProblem.getPersonid());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                else{
                    rowView = inflater.inflate(R.layout.blank_layout, parent, false);
                }
            }

            if(category.equals("Plumber")){
                if(postedProblem.getHelptype().equals("Plumber")){
                    if(postedProblem.getTitle().length() > 15){
                        title.setText(postedProblem.getTitle().substring(0,15)+"...");
                    }
                    else title.setText(postedProblem.getTitle());

                    post_type.setText(postedProblem.getHelptype());
                    if(postedProblem.getPostdetail().length() > 30){
                        post_desc.setText(postedProblem.getPostdetail().substring(0,30)+"...");
                    }
                    else post_desc.setText(postedProblem.getPostdetail());
                    if(flag == 0){
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("postid",postedProblem.getId());
                                intent.putExtra("personid",postedProblem.getPersonid());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                else{
                    rowView = inflater.inflate(R.layout.blank_layout, parent, false);
                }
            }

            if(category.equals("Other")){
                if(postedProblem.getHelptype().equals("Other")){
                    if(postedProblem.getTitle().length() > 15){
                        title.setText(postedProblem.getTitle().substring(0,15)+"...");
                    }
                    else title.setText(postedProblem.getTitle());

                    post_type.setText(postedProblem.getHelptype());
                    if(postedProblem.getPostdetail().length() > 30){
                        post_desc.setText(postedProblem.getPostdetail().substring(0,30)+"...");
                    }
                    else post_desc.setText(postedProblem.getPostdetail());
                    if(flag == 0){
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("postid",postedProblem.getId());
                                intent.putExtra("personid",postedProblem.getPersonid());
                                context.startActivity(intent);
                            }
                        });
                    }
                }
                else{
                    rowView = inflater.inflate(R.layout.blank_layout, parent, false);
                }
            }


        }

        return rowView;
    }
}
