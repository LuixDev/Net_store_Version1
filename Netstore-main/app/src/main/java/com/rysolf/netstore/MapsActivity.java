package com.rysolf.netstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.rysolf.netstore.R;
import com.rysolf.netstore.casos_uso.CasosUsoLocalizacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private CasosUsoLocalizacion usoLocalizacion;
    DatabaseReference ref;
    private FusedLocationProviderClient fusedLocationClient;
    Task<Void> reference;
    FirebaseUser fuser;
    String nombre ,categoria;
    private FirebaseAuth mAuth;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Location currentLocation;
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        usoLocalizacion = new CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                nombre =user.getName();
                categoria =user.getCategoria();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });

    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(MapsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.maps_add,menu);



        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addMaps:




                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    turnGPSOn();
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
                                    latlang.put("name",nombre);
                                    latlang.put("categoria",categoria);



                                    mAuth = FirebaseAuth.getInstance();
                                    if(mAuth.getCurrentUser() != null) {
                                        reference = FirebaseDatabase.getInstance().getReference("Ubicacion")

                                                .child(mAuth.getCurrentUser().getUid())
                                                .updateChildren(latlang);



                                    }


                                }

                            }
                        });






                break;
            case R.id.DeletMaps:


                mAuth = FirebaseAuth.getInstance();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference completedSurveysRef = rootRef.child("Ubicacion").child(mAuth.getCurrentUser().getUid()).child("latitud");
                DatabaseReference longitud = rootRef.child("Ubicacion").child(mAuth.getCurrentUser().getUid()).child("longitud");
                DatabaseReference nombre = rootRef.child("Ubicacion").child(mAuth.getCurrentUser().getUid()).child("name");
                DatabaseReference categoria = rootRef.child("Ubicacion").child(mAuth.getCurrentUser().getUid()).child("categoria");
                completedSurveysRef.removeValue();
                longitud.removeValue();
                nombre.removeValue();
                categoria.removeValue();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);


                Toast.makeText(MapsActivity.this, "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intent);

                break;




        }
        return super.onOptionsItemSelected(item);
    }


    @Override protected void onResume() {
           super.onResume();
            usoLocalizacion.activar();
   }


         @Override protected void onPause() {
             super.onPause();
             usoLocalizacion.desactivar();
         }

        }

