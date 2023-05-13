package com.rysolf.netstore;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.rysolf.netstore.R;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.annotations.NonNull;

public class Forgetpassword extends AppCompatActivity {
    TextInputEditText recover_email;
    Button Recover_btn;
    ImageButton imageButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        recover_email = findViewById(R.id.recover_email1);
        Recover_btn = findViewById(R.id.Recover_btn1);

        Toolbar toolbar = findViewById(R.id.forgettoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recuperar contraseña");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Forgetpassword.this, MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });


        Recover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });
        Recover_btn.setBackgroundColor(Color.parseColor("#FF1E639A"));
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.greySecondary));
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Forgetpassword.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void validar() {
        String email = recover_email.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            recover_email.setError("Correo invalido");
            return;
        }
        sendEmail(email);
    }

    public void onBlackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Forgetpassword.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email;

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgetpassword.this, "Correo enviado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Forgetpassword.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Forgetpassword.this, "Correo Invalido", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
    }
}
