package com.rysolf.netstore;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;


import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.rysolf.netstore.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;


import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;


import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    public Object NotifCount;


    private Chip num11,num22,num33,num44,num55,num66;
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    private String currentUserId;

    Task<Void> reference;
    private DrawerLayout mainDrawerNav;
    private ActionBarDrawerToggle mainDrawerToggle;
    private NavigationView navigationView;




    DatabaseReference ref;
    ArrayList<Users> list;

    SearchView searchView;
    LinearLayoutManager lm;
    Adapter adapter;
    Context mContext;
    Location currentLocation;
    MenuItem menuItem;
    // badge text view
    TextView badgeCounter;

    int pendingNotifications;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://maps-netstore.vercel.app/web.html");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
  Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        num11 = findViewById(R.id.num1);
        num22 = findViewById(R.id.num2);
        num33 = findViewById(R.id.num3);
        num44 = findViewById(R.id.num4);
        num55 = findViewById(R.id.num5);
        num66 = findViewById(R.id.num6);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Net store");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, busqueda.class);
                startActivity(loginIntent);
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list = new ArrayList<>();


        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.downbutton);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.inicio:
                                mainDrawerNav.openDrawer(GravityCompat.START);
                                break;
                            case R.id.cerca:
                                Intent setupIntent = new Intent(MainActivity.this, MainPublication.class);
                                startActivity(setupIntent);
                                finish();
                                break;
                            case R.id.configuracion:


                                Intent setupIntent1 = new Intent(MainActivity.this, Notificacion1.class);
                                startActivity(setupIntent1);
                                finish();
                                break;
                        }
                        return false;
                    }
                });








        // Init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Init Toolbar & Navbar


        if (mAuth.getCurrentUser() != null) {

            // Init Fragments





            mainDrawerNav = findViewById(R.id.main_drawer_layout);
            mainDrawerToggle = new ActionBarDrawerToggle(this, mainDrawerNav, mainToolbar, R.string.drawer_open, R.string.drawer_close);
            mainDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));

            mainDrawerNav.addDrawerListener(mainDrawerToggle);

            mainDrawerToggle.syncState();

            navigationView = findViewById(R.id.mainNavView);

            navigationView.setNavigationItemSelectedListener(this);

            navigationView.setCheckedItem(R.id.nav_home);


        }
       /* ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(String.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
        this.setTaskDescription(taskDesc);
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
      fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Log.e("Latitud:",+location.getLatitude()+"Logitud:"+location.getLongitude());
                            Map<String,Object> latlang= new HashMap<>();
                            latlang.put("latitud",location.getLatitude());
                            latlang.put("longitud",location.getLongitude());





                            if(mAuth.getCurrentUser() != null) {
                                reference = FirebaseDatabase.getInstance().getReference("Users")

                                        .child(mAuth.getCurrentUser().getUid())
                                        .updateChildren(latlang);


                            }


                        }

                    }
                });

*/

        num11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
        num22.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
        num33.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
        num44.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
        num55.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
        num66.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), busqueda.class);
                String str1 = ("Tienda");
                intent.putExtra("dato", str1);
                startActivity(intent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.buscad, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search_btn);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Buscar...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), busqueda.class);

                intent.putExtra("dato", searchView.getQuery().toString());
                startActivity(intent);
                finish();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);
                return true;
            }
        });

        return true;
    }

    void buscar(String s) {
        ArrayList<Users> milista = new ArrayList<>();
        for (Users obj : list) {
            if (obj.getName().toLowerCase().contains(s.toLowerCase())) {

                milista.add(obj);
            } else if (obj.getCategoria().toLowerCase().contains(s.toLowerCase())) {


                milista.add(obj);
            }


        }
        Adapter adapter = new Adapter(mContext, milista);


    }


    @Override
    protected void onStart() {
        super.onStart();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Users ms = snapshot.getValue(Users.class);



                        list.add(ms);

                    }

                    adapter = new Adapter(mContext, list);


                    adapter.notifyDataSetChanged();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            navigateToLogin();

        } else {

            currentUserId = mAuth.getCurrentUser().getUid();
            db.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        if (!task.getResult().exists()) {

                        }

                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }


    public void onBackPressed() {
        if (mainDrawerNav.isDrawerOpen(GravityCompat.START)) {
            mainDrawerNav.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

        switch (item.getItemId()) {
            case R.id.nav_home:

                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.nav_account:

                Intent Intent = new Intent(MainActivity.this, BuscadorUser.class);
                startActivity(Intent);

                break;
            case R.id.nav_googlemaps:
                Intent setupIntent = new Intent(MainActivity.this, Cercademi.class);
                startActivity(setupIntent);
                finish();
                break;

            case R.id.clo_account:
                logout();
                return true;

            default:
                return false;

        }
        mainDrawerNav.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        mAuth.signOut();
        navigateToLogin();
    }


    private void navigateToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


    // @Override protected void onResume() {
    //    super.onResume();
    //     usoLocalizacion.activar();
//    }


    // @Override protected void onPause() {
    //super.onPause();
    //usoLocalizacion.desactivar();

    }









