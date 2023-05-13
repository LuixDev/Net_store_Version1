package com.rysolf.netstore;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.rysolf.netstore.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter1 extends ArrayAdapter<Users> {
    private Activity context;
    private int resource;
    private List<Users> listImage;



    public  Adapter1(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Users> objects ) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        int count1;
        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.username);
        TextView categoria = (TextView) v.findViewById(R.id.textView3);
        TextView count = (TextView) v.findViewById(R.id.count);
        ImageView img = (ImageView) v.findViewById(R.id.profile_image);

        tvName.setText(listImage.get(position).getName());
        categoria.setText(listImage.get(position).getCategoria());

        Glide.with(context).load(listImage.get(position).getImage()).into(img);

        if (( listImage.get(position).getCount() > 0 )) {
            listImage.get(position).setCount(listImage.get(position).getCount());

            count.setText(String.valueOf(listImage.get(position).getCount()));
            count.setVisibility(View.VISIBLE);
        } else {
            count.setVisibility(View.GONE);
        }

        return v;

    }

    public int getCount() {
        return this.listImage.size();
    }

}
