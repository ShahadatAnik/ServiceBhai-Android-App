package com.ewubd.servicebhai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Int3;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class showNID extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;
    SharedPreferences myPref;
    int userid;
    String imageUrl;
    ImageView nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_nid);
        nid = findViewById(R.id.nidview);

        myPref = getApplicationContext().getSharedPreferences("userId", MODE_PRIVATE);
        userid = myPref.getInt("loggedInID", -1);

        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").orderByChild("mName").equalTo("gg");
        Query query = FirebaseDatabase.getInstance().getReference("uploads").orderByChild("mName").equalTo(String.valueOf(userid));
        //System.out.println(query);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot.getValue());
                if(dataSnapshot.getValue() != null) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        upload upload = childSnapshot.getValue(upload.class);
                        //System.out.println(upload.getmImageUrl());
                        imageUrl = upload.getmImageUrl();
                        Picasso.get().load(imageUrl).into(nid);
                    }
                }
                else {
                    Intent intent = new Intent(showNID.this, uploadNID.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}