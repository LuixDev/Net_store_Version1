package com.rysolf.netstore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.rysolf.netstore.Notifications.Client;
import com.rysolf.netstore.Notifications.Data;
import com.rysolf.netstore.Notifications.MyResponse;
import com.rysolf.netstore.Notifications.Sender;
import com.rysolf.netstore.Notifications.Token;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.rysolf.netstore.APIService;
import com.rysolf.netstore.Adapter1;
import com.rysolf.netstore.Comments;
import com.rysolf.netstore.CommentsRecyclerAdapter;
import com.rysolf.netstore.R;
import com.rysolf.netstore.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference,bas;
    Intent intent;
    ImageView imageView;
    String currenttime;
    TextView textView;
    APIService apiService;
    ValueEventListener seenListener;

    SimpleDateFormat simpleDateFormat;
    ImageButton btn_send;
    Task<Void> reference1;

    Calendar calendar;
    EditText text_send;
    DatabaseReference ref;
    boolean notify = false;
    private ArrayList<Users> chatRoomArrayList;

    CommentsRecyclerAdapter commentsRecyclerAdapter;
    List<Comments> commentsList;
    RecyclerView recyclerView;
    private Adapter1 mAdapter;
    int count;
    private int resource;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        SimpleDateFormat simpleDateFormat;
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        chatRoomArrayList = new ArrayList<>();
        mAdapter = new Adapter1(this, resource ,chatRoomArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile);
        username = findViewById(R.id.name);
        btn_send = findViewById(R.id.sendBtn);
        text_send = findViewById(R.id.messageBox);

        intent = getIntent();
        String userid = intent.getStringExtra("userid");


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(), userid, msg);



                } else {

                    Toast.makeText(com.rysolf.netstore.MessageActivity.this, "No puede enviar mensaje vacio", Toast.LENGTH_SHORT).show();
                }


                text_send.setText("");

            }
        });



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = getIntent().getStringExtra("nombre");
                Object bitmap = getIntent().getStringExtra("bit1");
                String categoria = getIntent().getStringExtra("categoria");
                Bitmap bitmap1 = (Bitmap) intent.getParcelableExtra("BitmapImage");



                username.setText(nombre);
                if(bitmap1 !=null) {
                    profile_image.setImageBitmap(bitmap1);
                }
                else {
                    Glide.with(getApplicationContext()).load(bitmap).into(profile_image);
                }
                readMessage(fuser.getUid(), userid);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);






    }







    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);
                    Comments chat = snapshot.getValue(Comments.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);

                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();

        String nombre = getIntent().getStringExtra("nombre");
        String userid=intent.getStringExtra("userid");
        String categoria = getIntent().getStringExtra("categoria");

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen", false);
        hashMap.put("name", nombre);
        hashMap.put("categoria", categoria);
        Date date=new Date();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        currenttime=simpleDateFormat.format(calendar.getTime());
        hashMap.put("time",  currenttime);
        reference.child("Chats").push().setValue(hashMap);

/*
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                DatabaseReference currentUserBD = mDatabase.child(sender);
                if (count == 0) {
                    count=1;
                    hashMap.put("count", count);
                    currentUserBD.push().updateChildren(hashMap);
                } else if(count==1){
                    count=2;
                    hashMap.put("count", count);
                    currentUserBD.push().updateChildren(hashMap);
                }
                else if(count==2){
                    count=3;
                    hashMap.put("count", count);
                    currentUserBD.push().updateChildren(hashMap);
                }
                else if(count==3){
                    count=4;
                    hashMap.put("count", count);
                    currentUserBD.push().updateChildren(hashMap);
                }
                else if(count==4){
                    count=5;
                    hashMap.put("count", count);
                    currentUserBD.push().updateChildren(hashMap);
                }
*/


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users1");
        DatabaseReference currentUserBD = mDatabase.child(sender);
        HashMap<String,Object> hashMap2=new HashMap<>();

        hashMap2.put("count", 1);

        currentUserBD.push().updateChildren(hashMap2);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference unicaribeRef = rootRef.child("Users1").child(sender);


        unicaribeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int total = 0;

                for( DataSnapshot ds :dataSnapshot.getChildren()) {
                    Users bazar = ds.getValue(Users.class);
                    int cost = bazar.getCount();
                    total = total + cost;

                }




                HashMap<String,Object> hashMap3=new HashMap<>();
                if(count!=0){

                    hashMap3.put("count",total);
                }
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                DatabaseReference currentUserBD = mDatabase.child(sender);
                currentUserBD.child(receiver).updateChildren(hashMap3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference lac = FirebaseDatabase.getInstance().getReference();
        DatabaseReference lac1 = lac.child("Users").child(sender).child(receiver);


        lac1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);

                    int  conteo = dataSnapshot.child("count").getValue(Integer.class);

                    HashMap<String,Object> hashMap4=new HashMap<>();


                    hashMap4.put("count",conteo);

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    DatabaseReference currentUserBD = mDatabase.child(sender);
                    currentUserBD.updateChildren(hashMap4);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
















        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")

                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                if ( notify) {

                    sendNotifiaction(receiver, user.getName(), msg);
                    user.setCount(count++);


                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void readMessage(final String myid, final String userid){
        commentsList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentsList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Comments chat=snapshot.getValue(Comments.class);
                    if (chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||
                            chat.getReceiver().equals(userid)&&chat.getSender().equals(myid)){
                        commentsList.add(chat);

                    }
                    commentsRecyclerAdapter=new CommentsRecyclerAdapter( com.rysolf.netstore.MessageActivity.this, commentsList);
                    recyclerView.setAdapter(commentsRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    protected void onResume() {
        super.onResume();
        notify = true;

    }





    private void sendNotifiaction(String receiver, final String name, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    intent=getIntent();
                    String userid=intent.getStringExtra("userid");

                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, name+": "+message, "Nuevo Mensaje",
                            userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Toast.makeText(MessageActivity.this, "Algo fue mal!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, "inesperado error!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}