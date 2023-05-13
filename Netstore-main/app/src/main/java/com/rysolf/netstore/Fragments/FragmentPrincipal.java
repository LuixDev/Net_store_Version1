package com.rysolf.netstore.Fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rysolf.netstore.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentPrincipal extends Fragment {
    private Button chat;
    private GoogleMap mapa;
    private Bundle bundle;
    private DatabaseReference mDatabase1;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);



        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    mapa.setMyLocationEnabled(true);
                    mapa.getUiSettings().setZoomControlsEnabled(true);
                    mapa.getUiSettings().setCompassEnabled(true);

                }
                else{





                }
                mDatabase1= FirebaseDatabase.getInstance().getReference();
                mDatabase1.child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (Marker marker : realTimeMarkers) {
                            marker.remove();
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            bundle = getArguments();

                            Double latitud = bundle.getDouble("latitud");
                            Double longitud = bundle.getDouble("longitud");

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(latitud, longitud));
                            tmpRealTimeMarkers.add(mapa.addMarker(markerOptions));
                        }

                        realTimeMarkers.clear();


                        realTimeMarkers.addAll(tmpRealTimeMarkers);
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError){

                    }
                });
            }
        });


        return view;
    }
}