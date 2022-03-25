package com.ewubd.servicebhai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customProblemAdapter extends BaseAdapter {

    Context context;
    ArrayList<postedProblem> arrayList;
    public customProblemAdapter(Context context, ArrayList<postedProblem> arrayList){
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

        View rowView = inflater.inflate(R.layout.problemcustomview, parent, false);

        TextView postid = rowView.findViewById(R.id.postid);
        TextView title = rowView.findViewById(R.id.title);
        TextView postername = rowView.findViewById(R.id.poster_name);
        TextView post_type = rowView.findViewById(R.id.post_helptype);
        TextView post_detail = rowView.findViewById(R.id.post_detail);

        postedProblem postedProblem = arrayList.get(position);

        postid.setText(String.valueOf(postedProblem.getId()));
        title.setText(postedProblem.getTitle());
        postername.setText(postedProblem.getName());
        post_type.setText(postedProblem.getHelptype());
        post_detail.setText(postedProblem.getPostdetail());
        System.out.println("Title "+title);

        return rowView;
    }
}
