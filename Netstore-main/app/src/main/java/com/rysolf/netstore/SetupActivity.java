package com.rysolf.netstore;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.rysolf.netstore.R;
import com.rysolf.netstore.casos_uso.CasosUsoLocalizacion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {
    Switch simpleSwitch1,simpleSwitch2;
    public String image;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;
private String currentUserId;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private String userId;
    private FirebaseFirestore db;
    private boolean isChanged = false;
    private FusedLocationProviderClient fusedLocationClient;

    private TextInputEditText  setupName, setupDireccion,setupCatergoria;

    private Button setupBtn;
    private ProgressBar setupProgress;
    DatabaseReference reference;
    private CasosUsoLocalizacion usoLocalizacion;
    Task<Void> reference1;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;



    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = findViewById(R.id.setup_toolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Configuracion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Firebase Init
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        usoLocalizacion = new CasosUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        simpleSwitch1 = (Switch) findViewById(R.id.simpleSwitch1);
        simpleSwitch2 = (Switch) findViewById(R.id.simpleSwitch2);
        setupImage = findViewById(R.id.setup_image);

        setupName =(TextInputEditText)findViewById(R.id.setup_name);
        setupDireccion =(TextInputEditText) findViewById(R.id.setup_direccion);
        setupCatergoria =(TextInputEditText) findViewById(R.id.setup_categoria);
        setupBtn = findViewById(R.id.setup_btn);


        setupBtn.setEnabled(true);



        simpleSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference completedSurveysRef = rootRef.child("Users").child(userId).child("latitud");
                DatabaseReference longitud = rootRef.child("Users").child(userId).child("longitud");
                completedSurveysRef.setValue(999999999);
                longitud.setValue(999999999);
                Toast.makeText(SetupActivity.this, "Se ha eliminado la ubicacion", Toast.LENGTH_SHORT).show();

            }

        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    public void onSuccess(Location location) {



                        if (location != null) {
                            Log.e("Latitud:",+location.getLatitude()+"Logitud:"+location.getLongitude());
                            Map<String,Object> latlang= new HashMap<>();
                            latlang.put("latitud",location.getLatitude());
                            latlang.put("longitud",location.getLongitude());




                            mAuth = FirebaseAuth.getInstance();
                            if(mAuth.getCurrentUser() != null) {
                                reference1 = FirebaseDatabase.getInstance().getReference("Users")

                                        .child(mAuth.getCurrentUser().getUid())
                                        .updateChildren(latlang);



                            }


                        }

                    }
                });


        simpleSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            usoLocalizacion.activar();



                Toast.makeText(SetupActivity.this, "Se agregos la ubicacion", Toast.LENGTH_SHORT).show();


            }

        });


        setupToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setupIntent = new Intent(SetupActivity.this, MainActivity.class);
                startActivity(setupIntent);

                finish();
            }
        });
        db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String direccion  = task.getResult().getString("direccion");
                        String categoria = task.getResult().getString("categoria");

                        image = task.getResult().getString("image");
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("image", image);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(hashMap);
                        mainImageURI = Uri.parse(image);

                        setupName.setText(name);
                        setupDireccion.setText(direccion);
                        setupCatergoria.setText(categoria);


                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_profile);

                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);

                    }
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Firestore Load Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }


            }
        });



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayName = setupName.getText().toString();
                final String displayDireccion =   setupDireccion.getText().toString();
                final String displayCategoria =   setupCatergoria.getText().toString();
                if (!TextUtils.isEmpty(displayName) && (!TextUtils.isEmpty(displayDireccion)&& (!TextUtils.isEmpty(displayCategoria) && mainImageURI != null))) {


                    // If image is changed
                    if (isChanged) {

                        userId = mAuth.getCurrentUser().getUid();

                        final StorageReference imagePath = storageReference.child("profile_images").child(userId + ".jpg");
                        UploadTask uploadTask = imagePath.putFile(mainImageURI);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return imagePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, displayName,  displayDireccion, displayCategoria);
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "Image Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    setupProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        // Else image UNCHANGED, update name only
                        Toast.makeText(SetupActivity.this, "Se ha guardado la configuracion ", Toast.LENGTH_LONG).show();
                        storeFirestore(null, displayName,  displayDireccion, displayCategoria);
                    }
                }

            }
        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check permissions for Image Upload
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        openImagePicker();
                    }
                } else {
                    // Version lower than M does not require permission (accepted in Play Store)
                    openImagePicker();
                }
            }
        });

    }


            private void storeFirestore(Task<Uri> task, String displayName, String setupDireccion, String setupCatergoria) {
        Uri downloadUri;

        if (task != null) {
            downloadUri = task.getResult();
        } else {
            downloadUri = mainImageURI;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", displayName);
        userMap.put("categoria", setupCatergoria);
        userMap.put("direccion", setupDireccion);
        userMap.put("image", downloadUri.toString());
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .updateChildren(userMap);

        db.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                    startActivity(mainIntent);

                    finish();
                } else {
                    String errorMessage = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Firestore Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void openImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Crop Image Result handler
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
