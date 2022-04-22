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

public class customWorkerReviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<workerReviewClass> arrayList;
    int workersid, raterid,rateid,rate;
    String workerreview;
    public customWorkerReviewAdapter(Context context, ArrayList<workerReviewClass> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList == null ? 0 : arrayList.size();
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

        View rowView = inflater.inflate(R.layout.reviewcustomview, parent, false);

        TextView ratings = rowView.findViewById(R.id.ratings);
        TextView review = rowView.findViewById(R.id.review);
        TextView ratername = rowView.findViewById(R.id.ratings_userName);
        //name.setOnClickListener(v->problemopenpage());


        workerReviewClass workerReviewClass = arrayList.get(position);

        workersid = workerReviewClass.getUserID();
        raterid = workerReviewClass.getRaterID();
        rateid = workerReviewClass.getRateid();
        rate = workerReviewClass.getRate();
        workerreview = workerReviewClass.getReview();


        ratings.setText(String.valueOf(workerReviewClass.getRate()));
        review.setText(workerReviewClass.getReview());
        ratername.setText(workerReviewClass.getRatername());
        //System.out.println(workersid);

        return rowView;
    }
}
