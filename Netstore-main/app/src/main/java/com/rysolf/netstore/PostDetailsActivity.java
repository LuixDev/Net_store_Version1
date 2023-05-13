package com.rysolf.netstore;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.rysolf.netstore.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView postDetailTitle, postDetailDesc;
    private ImageView postDetailImage;
    private Toolbar postDetailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postDetailToolbar = findViewById(R.id.post_detail_toolbar);
        setSupportActionBar(postDetailToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postDetailImage = findViewById(R.id.post_detail_image);
        postDetailTitle = findViewById(R.id.post_detail_title);
        postDetailDesc = findViewById(R.id.post_detail_desc);

        // Get Extras
        Intent postDetailIntent = getIntent();
        String title = postDetailIntent.getExtras().getString("Title");
        String desc = postDetailIntent.getExtras().getString("Desc");
        String imageUri = postDetailIntent.getExtras().getString("Image");

        getSupportActionBar().setTitle(title);

        // Set Extras
        postDetailTitle.setText(title);
        postDetailDesc.setText(desc);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);

        Glide.with(this).applyDefaultRequestOptions(requestOptions).load(imageUri).into(postDetailImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notificacion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
