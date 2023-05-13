package com.rysolf.netstore;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.rysolf.netstore.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import com.rysolf.netstore.Notifications.Token;


public class Notificacion1 extends AppCompatActivity  {


    private List<Users> list;
    private List<Comments> list1;
    FirebaseUser fuser;
    DatabaseReference reference;
    Task<Void> reference1;
    Context mContext;

    private List<Chatlist> usersList;
    Adapter1 adapter1;
    public ListView listview;
    TextView textview;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        listview = findViewById(R.id.list1);
        textview = findViewById(R.id.textView6);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolb2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notificacion");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        usersList = new ArrayList<>();

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setupIntent = new Intent(Notificacion1.this, MainActivity.class);
                startActivity(setupIntent);
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Notificacion1.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notidelete:

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Chatlist");
                DatabaseReference currentUserBD = mDatabase.child(fuser.getUid());
                currentUserBD.removeValue();


                break;


        }
        return super.onOptionsItemSelected(item);
    }


    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {
        list = new ArrayList<>();




                reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users user = snapshot.getValue(Users.class);
                            for (Chatlist chatlist : usersList) {
                                if (user.getId().equals(chatlist.getId()) ||
                                        (chatlist.getId().equals(user.getId()))) {
                                    list.add(user);


                                }

                            }
                        }

                        adapter1 = new Adapter1(Notificacion1.this, R.layout.unread_message, list);
                        if (adapter1.isEmpty()) {
                            textview.setVisibility(View.VISIBLE);
                        } else {
                            textview.setVisibility(View.INVISIBLE);
                        }

                        listview.setAdapter(adapter1);

                        adapter1.notifyDataSetChanged();
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(Notificacion1.this, MessageActivity.class);
                                Users data = (Users) parent.getItemAtPosition(position);


                                intent.putExtra("userid", data.getId());
                                intent.putExtra("nombre", data.getName());
                                intent.putExtra("bit1", data.getImage());
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference unicaribeRef = rootRef.child("Users1").child(data.getId());
                                unicaribeRef.removeValue();

                                startActivity(intent);
                            }
                        });
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(Notificacion1.this)
                        .setTitle("Notificacion")
                        .setMessage("Desea eliminar esta notificacion?")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Users data = (Users) parent.getItemAtPosition(position);
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Chatlist");
                                DatabaseReference currentUserBD = mDatabase.child(fuser.getUid());
                                currentUserBD.child(data.getId()).removeValue();
                                Toast.makeText(getApplicationContext(), "se ha eliminado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        )
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }
                        )

                        .show();
                return true;
            }



        });
            }


}










