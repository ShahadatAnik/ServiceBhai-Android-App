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
    int id,personid, flag = 1;
    public customProblemAdapter(Context context, ArrayList<postedProblem> arrayList, int flag){
        this.context = context;
        this.arrayList = arrayList;
        this.flag = flag;
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



        postedProblem postedProblem = arrayList.get(position);

        id = postedProblem.getId();
        personid = postedProblem.getPersonid();


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
                    int id = postedProblem.getId();
                    Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("postid",id);
                    intent.putExtra("personid",personid);
                    context.startActivity(intent);
                }
            });
        }


        return rowView;
    }

    private void problemopenpage() {
        Intent intent = new Intent(context, problemOpen.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("postid",id);
        intent.putExtra("personid",personid);
        context.startActivity(intent);
    }
}
