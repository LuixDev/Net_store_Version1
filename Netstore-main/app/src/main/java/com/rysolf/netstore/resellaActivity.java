package com.rysolf.netstore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.rysolf.netstore.R;
import com.rysolf.netstore.dialogos.dialog_rating;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class resellaActivity extends AppCompatActivity {


    @BindView(R.id.rv_product)
    RecyclerView rvProduct;
    private FloatingActionButton addPostBtn;
    private RatingAdapter mRatingAdapter;


    DatabaseReference ref;
    ArrayList<Rating> list;
    Context mContext;
    LinearLayoutManager lm;
    double count,resultado;
    double starPromedio;
    private RecyclerView mRatingsRecycler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resella);


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPostBtn = findViewById(R.id.add_post_btn);
        mRatingsRecycler = findViewById(R.id.recycler_ratings);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        lm = new LinearLayoutManager(this);
        mRatingsRecycler.setLayoutManager(lm);
        list = new ArrayList<>();
        String userid;
        Intent intent;
        intent=getIntent();
        userid=intent.getStringExtra("userid");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });


        mRatingAdapter = new RatingAdapter(mContext, list);
        mRatingsRecycler.setAdapter(mRatingAdapter);


        ref = FirebaseDatabase.getInstance().getReference().child("Reseña");

        ref.orderByChild("id").equalTo(userid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Rating ms = snapshot.getValue(Rating.class);

                        list.add(ms);

                    }
                    mRatingAdapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {Toast.makeText(resellaActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });





                  addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                 db.child("suma").child("id").equalTo(userid);
               db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            double  total = 0.00;

                            for (DataSnapshot ds : task.getResult().getChildren()) {
                                if(total!=0.0){
                                double total9 = ds.child("rating").getValue(double.class);
                                total = total+total9;
                                }


                            }




                            count =  mRatingAdapter.getItemCount();
                            Log.d("TAG", String.valueOf(total));
                            resultado = count;
                            HashMap<String,Object> hashMap=new HashMap<>();
                            if(count!=0){
                            hashMap.put("avgRating",total/count);

                            }

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                            DatabaseReference currentUserBD = mDatabase.child(userid);
                            currentUserBD.updateChildren(hashMap);


                        } else {
                            Log.d("TAG", task.getException().getMessage()); //Don't ignore potential errors!
                        }
                    }
                });
*/

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference unicaribeRef = rootRef.child("Reseña1").child(userid);


                unicaribeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        double total = 0;

                        for( DataSnapshot ds :dataSnapshot.getChildren()) {
                            Rating bazar = ds.getValue(Rating.class);
                            Double cost = bazar.getRating();
                            total = total + cost;
                           ;
                        }

                        count =  mRatingAdapter.getItemCount();
                        Log.d("TAG", String.valueOf(total));
                        resultado = count;
                        HashMap<String,Object> hashMap=new HashMap<>();
                        if(count!=0){
                            hashMap.put("avgRating",total/count);
                        }
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                        DatabaseReference currentUserBD = mDatabase.child(userid);
                        currentUserBD.updateChildren(hashMap);
                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                dialog_rating dialogo = new dialog_rating();

                Bundle args = new Bundle();
                // Colocamos el String
                args.putString("userid", userid);
                args.putDouble("recycler",count);
                dialogo.setArguments(args);

                dialogo.show(getSupportFragmentManager(), "Dialogo");


            }
                  });

}






    }



