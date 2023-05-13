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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
    Context mContext;
    private List<Users> mUsers;

    public AdapterUser(Context mContext, List<Users> mUsers) {
        this.mUsers = mUsers;
        this.mContext = mContext;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_user,parent,false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();
        final Users user = mUsers.get(position);
        holder.nombre.setText(user.getName());


        holder.categoria.setText(user.getCategoria());
        holder.nombre.setVisibility(View.GONE);
        holder.categoria.setVisibility(View.GONE);
        holder.foto.setVisibility(View.GONE);
        if (user.getCategoria()!=null){
            if(user.getCategoria().equals("Usuario")) {
                holder.nombre.setVisibility(View.VISIBLE);
                holder.categoria.setVisibility(View.VISIBLE);
                holder.foto.setVisibility(View.VISIBLE);
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


        public ViewHolder(View itemView) {
            super(itemView);


            nombre = itemView.findViewById(R.id.username);



            nombre = itemView.findViewById(R.id.username);
            foto = itemView.findViewById(R.id.profile_image);
            direccion = itemView.findViewById(R.id.textmsm);

            categoria = itemView.findViewById(R.id.textView3);

            telefono = itemView.findViewById(R.id.Tel1);




        }

    }

}