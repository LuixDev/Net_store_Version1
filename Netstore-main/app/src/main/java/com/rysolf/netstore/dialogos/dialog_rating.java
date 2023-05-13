package com.rysolf.netstore.dialogos; //dialog_rating

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rysolf.netstore.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class dialog_rating extends androidx.fragment.app.DialogFragment implements View.OnClickListener {




    private FirebaseFirestore db;
    private MaterialRatingBar mRatingBar;
    private EditText mRatingText;
    private Bundle bundle1;
    private FirebaseAuth mAuth;
    public String userId;
    double total = 0.00;
 public  Double resultado;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_rating, container, false);

        mRatingBar = v.findViewById(R.id.restaurant_form_rating);
        mRatingText = v.findViewById(R.id.restaurant_form_text);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        v.findViewById(R.id.button1).setOnClickListener(this);
        v.findViewById(R.id.restaurant_form_cancel).setOnClickListener(this);

        return v;


        //int i = recyclerViewAdapter.getItemCount();

    }





    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                onSubmitClicked(v);
                break;
            case R.id.restaurant_form_cancel:
                onCancelClicked(v);
                break;
        }
    }

    public void onSubmitClicked(View view) {

        db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");

                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        Object rating =mRatingBar.getRating();
                        Object texto = mRatingText.getText().toString();


                        hashMap.put("rating",rating);
                        hashMap.put("texto",texto);
                        hashMap.put("usuario",name);
                        SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

                        String now = ISO_8601_FORMAT.format(new Date());
                        hashMap.put("tiempo",now);

                            String id = getArguments().getString("userid");
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            hashMap.put("id", id);

                        reference.child("Reseña1").child(id).push().setValue(hashMap);
                        reference.child("Reseña").push().setValue(hashMap);








                    }
                }


            }
        });






        dismiss();

    }

    public void onCancelClicked(View view) {
        dismiss();
    }

}