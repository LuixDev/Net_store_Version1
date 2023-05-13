package com.rysolf.netstore.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rysolf.netstore.MainActivity;
import com.rysolf.netstore.R;
import com.rysolf.netstore.Users;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class  FragmentMapa extends Fragment {
    private Button chat;
    private GoogleMap mapa;
    Context context;
    private Marker marker;
    private Bundle bundle;
    private DatabaseReference mDatabase1;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_mapa, container, false);


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapa);


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            public void onMapReady(GoogleMap googleMap) {
                mapa = googleMap;
                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    mapa.setMyLocationEnabled(true);
                    mapa.getUiSettings().setZoomControlsEnabled(true);
                    //  googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());


                    mapa.getUiSettings().setCompassEnabled(true);

                    mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);


                        }
                    });
                } else {


                }


                mDatabase1 = FirebaseDatabase.getInstance().getReference();
                mDatabase1.child("Ubicacion").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (Marker marker : realTimeMarkers) {
                            marker.remove();
                        }

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Users mp = snapshot.getValue(Users.class);
                            Double latitud = mp.getLatitud();
                            Double longitud = mp.getLongitud();

                            MarkerOptions markerOptions = new MarkerOptions();
                            if (latitud != null && (longitud != null)) {
                                markerOptions.position(new LatLng(latitud, longitud));
                            }
                            else {
                                markerOptions.position(new LatLng(999999999, 999999999));

                            }

                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ubicacion));

                            String categoria = mp.getCategoria() + ":ㅤ" + mp.getName();

                            markerOptions.title(categoria);




                            Marker locationMarker;

                            locationMarker = mapa.addMarker(markerOptions);
                            locationMarker.showInfoWindow();
                            tmpRealTimeMarkers.add(mapa.addMarker(markerOptions));



                        }

                        realTimeMarkers.clear();


                        realTimeMarkers.addAll(tmpRealTimeMarkers);
                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        return view;








/*
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;
        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.activity_custom_info_window_adapter,
                    null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.activity_custom_info_window_adapter, null);

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView desc = (TextView) v.findViewById(R.id.desc);
            ImageView image = (ImageView) v.findViewById(R.id.image);


            name.setText("hola");
            desc.setText("hola 2");

            image.setImageResource(R.drawable.category);


            return v;
        }
    }

*/
    }
    }








