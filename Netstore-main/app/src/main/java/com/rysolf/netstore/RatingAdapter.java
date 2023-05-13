
 package com.rysolf.netstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rysolf.netstore.R;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * RecyclerView adapter for a bunch of Ratings.
 */
public class RatingAdapter extends  RecyclerView.Adapter<RatingAdapter.ViewHolder> {


    Context mContext;
    private List<Rating> rate;

    public RatingAdapter(Context mContext, List<Rating> rate) {


        this.rate = rate;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public  RatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);

        RatingAdapter.ViewHolder holder = new RatingAdapter.ViewHolder(view);

        return holder;
    }


    public void onBindViewHolder(@NonNull RatingAdapter.ViewHolder holder, int position) {
        mContext = holder.itemView.getContext();
        final Rating rating = rate.get(position);

        holder.nameView.setText(rating.getUsuario());
        holder.ratingBar.setRating((float) rating.getRating());

        holder.textView.setText(rating.getTexto());
        holder.date.setText((CharSequence) rating.getTiempo());
    }

    @Override
    public int getItemCount() {
        return rate.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView nameView;
        MaterialRatingBar ratingBar;
        TextView textView;
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.rating_item_name);
            ratingBar = itemView.findViewById(R.id.rating_item_rating);
            textView = itemView.findViewById(R.id.rating_item_text);
            date = itemView.findViewById(R.id. rating_item_date);

        }






        }

    }



