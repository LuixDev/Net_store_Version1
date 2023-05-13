package com.rysolf.netstore;

import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rysolf.netstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;



public class
RegisterActivity extends AppCompatActivity {

    private TextInputEditText password,password1,username,email,telefono,ciudad,direccion;
    String[] items =  {"Usuario","Empresa","Tienda","Bar","Restaurante"," Discoteca","Farmancia","Hotel","Discoteca","Gasolinera","Otro"};
    AutoCompleteTextView categoria;
    ArrayAdapter<String> adapterItems;
    Button btn_register;
    private FirebaseAuth mAuth;
  String userid,userId;
    private FirebaseFirestore db;
    DatabaseReference reference;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        telefono =(TextInputEditText)findViewById(R.id.telefono1);
        ciudad =(TextInputEditText)findViewById(R.id.ciudad1);

        categoria=findViewById(R.id.categoria);

        username =(TextInputEditText)findViewById(R.id.username1);

        email =(TextInputEditText)findViewById(R.id.email1);
        password =(TextInputEditText)findViewById(R.id.password1);
        password1 =(TextInputEditText)findViewById(R.id.password3);
        btn_register =findViewById(R.id.btn_register);
        direccion =(TextInputEditText)findViewById(R.id.direccion1);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registrar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,items);
categoria.setAdapter(adapterItems);


        categoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }
        });





        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username=username.getText().toString();
                String txt_email=email.getText().toString();
                String txt_telefono=telefono.getText().toString();
                String txt_ciudad=ciudad.getText().toString();

                String txt_password1=password1.getText().toString();
                String txt_password=password.getText().toString();
                String txt_categoria=categoria.getText().toString();
                String txt_direccion=direccion.getText().toString();


                if(TextUtils.isEmpty(txt_username)|| TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password)|| TextUtils.isEmpty(txt_telefono)|| TextUtils.isEmpty(txt_ciudad)|| TextUtils.isEmpty(txt_direccion)|| TextUtils.isEmpty(txt_categoria))
                {
                    Toast.makeText(RegisterActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else if(txt_password.length()<6) {
                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();


                }else if(!txt_password.equals(txt_password1)) {
                    Toast.makeText(RegisterActivity.this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                }

                else{
                    check();


                }

            }
        });

        btn_register.setBackgroundColor(Color.parseColor("#FF1E639A"));
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }
public  void check(){

    String username1=username.getText().toString();
    String txt_email=email.getText().toString();
    String txt_telefono=telefono.getText().toString();
    String txt_ciudad=ciudad.getText().toString();


    String txt_password=password.getText().toString();
    String txt_categoria=categoria.getText().toString();
    String txt_direccion=direccion.getText().toString();

    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
    ref.orderByChild("name").equalTo(username1).addValueEventListener(new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {



            }else{
                Toast.makeText(RegisterActivity.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                 register(username1,txt_email,txt_password,txt_categoria,txt_ciudad,txt_telefono,txt_direccion);

            }

        }


        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(RegisterActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }
    });
}

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return "";
    }



        private void register(final String username, String email, String password,String categoria,String ciudad,String telefono,String direccion)
    {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);


                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", userid);
                                hashMap.put("name", username);
                            hashMap.put("categoria", categoria);

                                hashMap.put("ciudad", ciudad);

                                hashMap.put("email", email);
                            hashMap.put("contraseña", password);
                                hashMap.put("direccion", direccion);



                            hashMap.put("image", "https://firebasestorage.googleapis.com/v0/b/netstore-daae2.appspot.com/o/585e4beacb11b227491c3399%20(3).png?alt=media&token=0d5754e8-a8e8-4e71-a48c-b650c3fba97d");


                            hashMap.put("telefono", telefono);
                                hashMap.put("Address_Mac", getMacAddress());

                            hashMap.put("usertype", 0);
                            db.collection("Users").document(userid).set(hashMap);
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent setupIntent = new Intent(RegisterActivity.this, LoginActivity.class);

                                            finish();
                                            startActivity(setupIntent);
                                        }

                                    }
                                });

                        } else{
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }

                    }


                });













    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            navigateToMain();
        }
    }

    private void navigateToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
