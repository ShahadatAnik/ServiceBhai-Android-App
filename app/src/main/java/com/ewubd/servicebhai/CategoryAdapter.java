package com.ewubd.servicebhai;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CatagoryListHomePageUser> {
    public CategoryAdapter(@NonNull Context context, ArrayList<CatagoryListHomePageUser> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listView = LayoutInflater.from(getContext()).inflate(R.layout.custom_grid_catagory, parent, false);
        }
        CatagoryListHomePageUser courseModel = getItem(position);
        TextView courseTV = listView.findViewById(R.id.tv_catagory_name);
        ImageView courseIV = listView.findViewById(R.id.iv_catagory_img);
        courseTV.setText(courseModel.getCategory_name());
        courseIV.setImageResource(courseModel.getImgid());
        return listView;
    }
}
