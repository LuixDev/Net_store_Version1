package com.rysolf.netstore;


import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rysolf.netstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import com.rysolf.netstore.casos_uso.CasosUsoActividades;

public class siteAdmin extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar Progress;
    private CasosUsoActividades usoActividades;



    DatabaseReference ref;
    ArrayList<Users> list;
    RecyclerView rv;

    LinearLayoutManager lm;
    Adapter2 adapter;
    SearchView searchView;
    Context mContext;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_admin);

        usoActividades = new CasosUsoActividades(this);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        rv = findViewById(R.id.vista);
        searchView = findViewById(R.id.buscador1);


        textView = findViewById(R.id.textView);

        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list = new ArrayList<>();


        adapter = new Adapter2(mContext,list);
        rv.setAdapter(adapter);
        // View Holder Class


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Users ms = snapshot.getValue(Users.class);

                        list.add(ms);

                    }


                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);
                return true;
            }

        };




        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }


    void buscar(String s) {
        ArrayList<Users> milista = new ArrayList<>();
        for (Users obj : list) {
            if (obj.getName().toLowerCase().contains(s.toLowerCase())) {
                milista.add(obj);


            }





        }
        Adapter2 adapter = new Adapter2(mContext,milista);

        rv.setAdapter(adapter);
    }







}

