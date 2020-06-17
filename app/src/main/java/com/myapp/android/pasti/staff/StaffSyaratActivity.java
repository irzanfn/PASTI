package com.myapp.android.pasti.staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class StaffSyaratActivity extends AppCompatActivity {
    private static final String TAG = "StaffSyaratActivity";
    TextView nim, nama, sks, ipk, cvalue, toefl;
    public String img;
    boolean isNilai;
    ImageView imageView;
    String id;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    Button btnTerima, btnTolak;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_syarat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Persyaratan");

        Intent intent = getIntent();
        id = intent.getStringExtra(StaffListSyaratActivity.EXTRA_ID);

        sessionManager = new SessionManager(getApplicationContext());

        nim = findViewById(R.id.nim_syarat);
        nama = findViewById(R.id.nama_syarat);
        sks = findViewById(R.id.sks_syarat);
        ipk = findViewById(R.id.ipk_syarat);
        cvalue = findViewById(R.id.cvalue_syarat);
        toefl = findViewById(R.id.toefl_syarat);
        imageView = findViewById(R.id.iv_toefl);
        setDisplay();

        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);

        btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("Mahasiswa")
                        .document(id);

                Map<String, Object> map = new HashMap<>();
                map.put("tahap2", true);
                docRef.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSucces : Updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onSucces : Failed");
                            }
                        });

                firestore.collection("Persyaratan").document(id).update("checkStatus", true);

                Toast.makeText(getApplicationContext(), "Diterima", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffSyaratActivity.this, StaffListSyaratActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Persyaratan")
                        .document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSucces : Success Deleting Document");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure : Failed Deleting Document");
                            }
                        });
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("Mahasiswa")
                        .document(id);

                Map<String, Object> map = new HashMap<>();
                map.put("tahap1", false);
                map.put("tahap2", false);
                docRef.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSucces : Success Updating Collection");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure : Failed Updating Collection");
                            }
                        });
                Toast.makeText(getApplicationContext(), "Ditolak", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffSyaratActivity.this, StaffListSyaratActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setDisplay() {
        DocumentReference documentReference = firestore.collection("Persyaratan").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                nama.setText("= " + document.getString("nama").toString());
                nim.setText("= " + id);
                sks.setText("= " + String.format("%d", document.getLong("sks").intValue()));
                ipk.setText("= " + String.format("%.2f", document.getLong("ipk").floatValue()));
                isNilai = document.getBoolean("nilaiC");
                if (!isNilai) {
                    cvalue.setText("= Tidak ada");
                } else {
                    cvalue.setText("= Ada");
                }
                toefl.setText("= " + document.getString("toeflNilai").toString());

                img = document.getString("toeflImageUrl").toString();
                Glide.with(StaffSyaratActivity.this).load(img).into(imageView);
            }
        });
    }
}