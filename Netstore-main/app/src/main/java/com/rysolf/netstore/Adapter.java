package com.rysolf.netstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.rysolf.netstore.R;
import com.bumptech.glide.Glide;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context mContext;
    private List<Users> mUsers;

    public Adapter(Context mContext, List<Users> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();
        final Users user = mUsers.get(position);
        holder.nombre.setText(user.getName());
        holder.direccion.setText(user.getDireccion());
        holder.telefono.setText(user.getTelefono());
        holder. restaurant_item_rating.setRating((float) user.getAvgRating());
        holder.categoria.setText(user.getCategoria());
if (user.getCategoria()!=null){
        if(user.getCategoria().equals("Usuario")) {
            holder.categoria.setVisibility(View.GONE);
            holder.nombre.setVisibility(View.GONE);
            holder.direccion.setVisibility(View.GONE);
            holder.telefono.setVisibility(View.GONE);
            holder.itemView.setVisibility(View.GONE);

            holder.foto.setVisibility(View.GONE);
            holder.retive.setVisibility(View.GONE);
            holder.linear.setVisibility(View.GONE);
            holder.card.setVisibility(View.GONE);
            holder.restaurant_item_rating.setVisibility(View.GONE);
        }
        }







        Glide.with(mContext).load(user.getImage()).into(holder.foto);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(mContext, InfUsuario.class);
                intent.putExtra("nombre", user.getName());

                intent.putExtra("userid", user.getId());



                holder.foto.buildDrawingCache();
                Bitmap bitmap = holder.foto.getDrawingCache();
                intent.putExtra("categoria", user.getCategoria());
                intent.putExtra("telefono", user.getTelefono());
                intent.putExtra("direccion", user.getDireccion());
                intent.putExtra("longitud",  user.getLongitud());
                intent.putExtra("latitud", user.getLatitud());
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
        public TextView nombre, direccion, categoria,distancia,telefono,txt,txt1;
        public MaterialRatingBar restaurant_item_rating;
        public ImageView foto;
        public CardView card;
        public LinearLayout linear;
        public RelativeLayout retive;

        public ViewHolder(View itemView) {
            super(itemView);


            nombre = itemView.findViewById(R.id.username);


            card= itemView.findViewById(R.id.card123);
            nombre = itemView.findViewById(R.id.username);
            foto = itemView.findViewById(R.id.profile_image);
            direccion = itemView.findViewById(R.id.textView2);
            restaurant_item_rating= itemView.findViewById(R.id.rating_item_rating);
            categoria = itemView.findViewById(R.id.textView3);
            linear = itemView.findViewById(R.id.iz);
            retive = itemView.findViewById(R.id.relative);
            telefono = itemView.findViewById(R.id.Tel1);




        }

            }

        }


