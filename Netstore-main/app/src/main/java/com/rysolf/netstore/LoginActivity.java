package com.rysolf.netstore;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rysolf.netstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private Button loginBtn, registerBtn,passwordrecover;
    private TextInputEditText loginEmailField, loginPasswordField;
    private ProgressBar loginProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        // Declare Fields & Buttons
        loginEmailField =(TextInputEditText) findViewById(R.id.login_email);
        loginPasswordField = (TextInputEditText)findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.login_reg_btn);
        loginProgress = findViewById(R.id.login_progress);
        passwordrecover= findViewById(R.id.login_rcover_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });
        passwordrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,Forgetpassword.class);
                startActivity(intent);
                finish();
            }
        });
        loginBtn.setBackgroundColor(Color.parseColor("#FF1E639A"));
        Window window1 = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window1.setStatusBarColor(ContextCompat.getColor(this,R.color.black));

    }
/*
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            navigateToMain();
        }

    }
*/



    private void startSignIn() {
        String email = loginEmailField.getText().toString();
        String password = loginPasswordField.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            loginProgress.setVisibility(View.VISIBLE);



            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override

                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String uid= task.getResult().getUser().getUid();
                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        firebaseDatabase.getReference().child("Users").child(uid).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int usertype=snapshot.getValue(Integer.class);
                                if(usertype==0){
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                                if(usertype==1){
                                    Intent mainIntent = new Intent(LoginActivity.this, siteAdmin.class);
                                    startActivity(mainIntent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Contraseña o correo Electronico Incorrecto", Toast.LENGTH_LONG).show();
                        ;
                    }
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            });




        } else {
            Toast.makeText(LoginActivity.this, "Los campos no debe estar vacio", Toast.LENGTH_SHORT).show();
        }
    }
}
