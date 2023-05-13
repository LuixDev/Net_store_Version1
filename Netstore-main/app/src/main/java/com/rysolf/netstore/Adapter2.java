package com.rysolf.netstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rysolf.netstore.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter2 extends RecyclerView.Adapter<Adapter2.ViewHolder> {
    Context mContext;
    private List<Users> mUsers;

    public Adapter2(Context mContext, List<Users> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unread_message, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();
        final Users user = mUsers.get(position);
        holder.nombre.setText(user.getName());



        holder.categoria.setText(user.getCategoria());
        holder.email.setText(user.getEmail());
        Glide.with(mContext).load(user.getImage()).into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InfUsuario.class);
                intent.putExtra("nombre", user.getName());

                intent.putExtra("userid", user.getId());
                holder.foto.buildDrawingCache();
                Bitmap bitmap = holder.foto.getDrawingCache();


                intent.putExtra("BitmapImage", bitmap);
                mContext.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, categoria,email;
        public ImageView foto;


        public ViewHolder(View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.username);
            foto = itemView.findViewById(R.id.profile_image);
            email = itemView.findViewById(R.id.textmsm);
            categoria = itemView.findViewById(R.id.textView3);


        }


    }


}