package com.rysolf.netstore;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rysolf.netstore.R;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.File;
import java.util.List;

public class CommentsRecyclerAdapter<QBChatDialog> extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGT = 1;
    public List<Comments> commentsList;
    public Context context;


    FirebaseUser fuser;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    public CommentsRecyclerAdapter(Context context, List<Comments> commentsList) {
        this.commentsList = commentsList;

        this.context = context;
    }


    @NonNull
    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == MSG_TYPE_RIGT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            context = parent.getContext();

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            return new CommentsRecyclerAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            context = parent.getContext();

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            return new CommentsRecyclerAdapter.ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {
        Comments chat = commentsList.get(position);
        String user_id = commentsList.get(position).getUser_id();




        holder.show_message.setText(chat.getMessage());
        holder.date.setText((CharSequence) chat.getTime());

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message,date;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.timeofmessage);
            show_message=itemView.findViewById(R.id.sendermessage);
            imageView = (ImageView) itemView.findViewById(R.id.image);


        }





    }
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(commentsList.get(position).getSender().equals(fuser.getUid())){
            return  MSG_TYPE_RIGT;
        }else
        {return  MSG_TYPE_LEFT;}


    }

}
