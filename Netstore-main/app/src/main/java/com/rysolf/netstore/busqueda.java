package com.rysolf.netstore;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

public class busqueda extends AppCompatActivity {


    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressBar Progress;
    private CasosUsoActividades usoActividades;



    DatabaseReference ref;
    ArrayList<Users> list;
    RecyclerView rv;
    Context mContext;
    LinearLayoutManager lm;
    Adapter adapter;
    SearchView searchView;

    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        usoActividades = new CasosUsoActividades(this);
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        rv = findViewById(R.id.search_list12);
        searchView = findViewById(R.id.buscador);
        Progress = (ProgressBar) findViewById(R.id.progressBar2);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(busqueda.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });


        textView = findViewById(R.id.textView);

        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        list = new ArrayList<>();


        adapter = new Adapter(mContext,list);
        rv.setAdapter(adapter);
        // View Holder Class


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Users ms = snapshot.getValue(Users.class);
                        rv.setVisibility(View.INVISIBLE);
                        Progress.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable(){

                            public void run(){
                                Progress.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);

                            };

                        }, 1000);

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


        searchView.post(new Runnable() {
            @Override
            public void run() {
                searchView.setOnQueryTextListener(queryTextListener);

                String str = getIntent().getStringExtra("dato");


                searchView.setQuery(str, false);
            }
        });



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));






        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(busqueda.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    void buscar(String s) {
                ArrayList<Users> milista = new ArrayList<>();
                for (Users obj : list) {
                    if (obj.getName().toLowerCase().contains(s.toLowerCase())) {
                        milista.add(obj);
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                textView.setVisibility(View.GONE);
                            }
                        }, 2000);


                    }
                   else if (obj.getCategoria().toLowerCase().contains(s.toLowerCase())) {
                        milista.add(obj);
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                textView.setVisibility(View.GONE);
                            }
                        }, 2000);


                    }
                    else if (milista.isEmpty()) {
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                textView.setVisibility(View.VISIBLE);
                            }
                        }, 2000);



                    }

                    else {
                        textView.setVisibility(View.GONE);
                    }
                }
                Adapter adapter = new Adapter(mContext,milista);

                rv.setAdapter(adapter);
            }







    }


