package com.rysolf.netstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rysolf.netstore.Fragments.FragmentPrincipal;
import com.rysolf.netstore.Fragments.FragmentPublicacion;
import com.rysolf.netstore.R;
import com.rysolf.netstore.dialogos.DialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class InfUsuario extends AppCompatActivity {



    public TextView nombre1,categoria1,direccion1,telefono;
   private Button chat;
    public ImageView foto;
    String userid;
    Double latitud,longitud;
    private FirebaseAuth mAuth;
    private String currentUserId;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inf_usuario);

        nombre1 = findViewById(R.id.username);
        categoria1 = findViewById(R.id.textView3);
        direccion1 = findViewById(R.id.textView2);
        foto = findViewById(R.id.profile_image);
        telefono = findViewById(R.id.textView5);

        Intent intent;
        intent=getIntent();
         userid=intent.getStringExtra("userid");

        String nombre = getIntent().getStringExtra("nombre");

        String categoria = getIntent().getStringExtra("categoria");
        String telefono1 = getIntent().getStringExtra("telefono");
        String direccion = getIntent().getStringExtra("direccion");
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");




        foto.setImageBitmap(bitmap);
        nombre1.setText(nombre);
        categoria1.setText(categoria);
        direccion1.setText(direccion);

        telefono.setText(telefono1);
        chat = findViewById(R.id.chat);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        if(currentUserId != null) {
            if (userid != null) {
                if (userid.equals(currentUserId)) {


                    chat.setVisibility(View.GONE);
                }
            }
        }
        else{

        }
        chat.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {



                Intent intent=new Intent(getApplicationContext(),MessageActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("nombre", nombre);
                intent.putExtra("categoria", categoria);
                foto.buildDrawingCache();
                Bitmap bitmap = foto.getDrawingCache();
                intent.putExtra("BitmapImage", bitmap);
                foto.buildDrawingCache();

                startActivity(intent);





            }


        });



        
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolb1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        Bundle bundle =new Bundle();
        String name = getIntent().getStringExtra("nombre");
        bundle.putString("name",name);
        String id=intent.getStringExtra("userid");
        bundle.putString("id",id);
        FragmentPublicacion fb= new  FragmentPublicacion();
        fb.setArguments(bundle);


        Bundle bundle1 =new Bundle();

            latitud = getIntent().getDoubleExtra("latitud", 0);


                longitud = getIntent().getDoubleExtra("longitud", 0);

                bundle1.putDouble("latitud", latitud);
                String id1 = intent.getStringExtra("userid");
                //intent.putExtra("userid", user.getId());
                bundle1.putString("id1", id1);
                bundle1.putDouble("longitud", longitud);
       
        FragmentPrincipal fb1= new  FragmentPrincipal();
        fb1.setArguments(bundle1);





        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(fb1, "Inicio ");


        viewPagerAdapter.addFragment(fb, "Publicacion");
      

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));



    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_navigation_items,menu);



        return super.onCreateOptionsMenu(menu);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }




        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.inicio:
                Intent loginIntent = new Intent( InfUsuario.this, MainActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.Notificacion:
                Intent loginIntent1 = new Intent( InfUsuario.this, Notificacion1.class);
                startActivity(loginIntent1);

                break;
            case R.id.report:
                DialogFragment dialogoTipoJuego=new DialogFragment();
                dialogoTipoJuego.show(getSupportFragmentManager(),"Dialogo1");
                break;
            case R.id.reseña:
                Intent loginIntent2 = new Intent( InfUsuario.this, resellaActivity.class);


                loginIntent2.putExtra("userid",userid);
                startActivity(loginIntent2);

                break;
            default:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }





}
