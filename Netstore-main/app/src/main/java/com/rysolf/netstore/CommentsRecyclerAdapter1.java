package com.rysolf.netstore;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rysolf.netstore.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsRecyclerAdapter1 extends RecyclerView.Adapter<CommentsRecyclerAdapter1.ViewHolder> {

    public List<Comments> commentsList;
    public Context context;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public CommentsRecyclerAdapter1(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        return new  CommentsRecyclerAdapter1.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final  CommentsRecyclerAdapter1.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String currentUserId = mAuth.getCurrentUser().getUid();

        String commentMessage = commentsList.get(position).getMessage();
        holder.setCommentMessage(commentMessage);

        String user_id = commentsList.get(position).getUser_id();

        db.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userName, userImage);
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (commentsList != null) {
            return commentsList.size();
        } else {
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView commentMessage, commentUsername;
        private CircleImageView commentUserImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setCommentMessage(String message) {
            commentMessage = mView.findViewById(R.id.comment_message);
            commentMessage.setText(message);
        }

        public void setUserData(String name, String image) {
            commentUsername = mView.findViewById(R.id.comment_username);
            commentUserImageView = mView.findViewById(R.id.comment_image);

            commentUsername.setText(name);

            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(image).into(commentUserImageView);
        }
    }
}
