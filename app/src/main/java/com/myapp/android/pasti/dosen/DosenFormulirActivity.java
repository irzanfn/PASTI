package com.myapp.android.pasti.dosen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.myapp.android.pasti.staff.StaffFormulirActivity;
import com.myapp.android.pasti.staff.StaffListFormulirActivity;
import com.myapp.android.pasti.staff.StaffListSyaratActivity;

import java.util.HashMap;
import java.util.Map;

public class DosenFormulirActivity extends AppCompatActivity {
    private static final String TAG = "StaffFormulirActivity";
    TextView nim,nama,prodi,periode,nmr_tlp,judul,dospem,bkt_pmbyaran,bk_bimbingan,bk_proposal;
    String id;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    Button btnTerima, btnTolak;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_formulir);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulir");

        sessionManager = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        id = intent.getStringExtra(DosenMainActivity.EXTRA_ID);

        nim = findViewById(R.id.nim_form);
        nama = findViewById(R.id.nama_form);
        prodi = findViewById(R.id.prodi_form);
        periode = findViewById(R.id.periode_form);
        nmr_tlp = findViewById(R.id.hp_form);
        judul = findViewById(R.id.judul_form);
        dospem = findViewById(R.id.dosen_form);
        bkt_pmbyaran = findViewById(R.id.butki_form);
        bk_bimbingan = findViewById(R.id.buku_form);
        bk_proposal = findViewById(R.id.proposal_form);
        setDisplay();

        btnTerima = findViewById(R.id.btnTerima);
        btnTolak = findViewById(R.id.btnTolak);

        btnTerima.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("Mahasiswa")
                        .document(id);

                Map<String,Object> map = new HashMap<>();
                map.put("tahap4",true);
                docRef.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSucces : Updated");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onSucces : Failed");
                            }
                        });
                firestore.collection("Formulir").document(id).update("checkStatusDosen", true);
                Toast.makeText(getApplicationContext(),"Formulir Diterima", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DosenFormulirActivity.this, DosenMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Formulir")
                        .document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSucces : Success Deleting Document");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onFailure : Failed Deleting Document");
                            }
                        });
                DocumentReference docRef = FirebaseFirestore.getInstance()
                        .collection("Mahasiswa")
                        .document(id);

                Map<String,Object> map = new HashMap<>();
                map.put("tahap3",false);
                map.put("tahap4",false);
                docRef.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"onSucces : Success Updating Collection");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"onFailure : Failed Updating Collection");
                            }
                        });
                Toast.makeText(getApplicationContext(),"Ditolak", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DosenFormulirActivity.this, DosenMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setDisplay() {
        DocumentReference documentReference = firestore.collection("Formulir").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                nama.setText("= " + document.getString("nama").toString());
                nim.setText("= " + document.getString("nim").toString());
                prodi.setText("= " + document.getString("prodi").toString());
                periode.setText("= " + document.getString("periode").toString());
                nmr_tlp.setText("= " + document.getString("telepon").toString());
                judul.setText("= " + document.getString("judul").toString());
                dospem.setText("= " + document.getString("namaDosen").toString());
                bkt_pmbyaran.setMovementMethod(LinkMovementMethod.getInstance());
                bkt_pmbyaran.setText(Html.fromHtml("= " + "<a href="+ document.getString("urlBukti").toString() +" >Click Here</a>"));
                bk_bimbingan.setMovementMethod(LinkMovementMethod.getInstance());
                bk_bimbingan.setText(Html.fromHtml("= " + "<a href="+ document.getString("urlBuku").toString() +" >Click Here</a>"));
                bk_proposal.setMovementMethod(LinkMovementMethod.getInstance());
                bk_proposal.setText(Html.fromHtml("= " + "<a href="+ document.getString("urlProposal").toString() +" >Click Here</a>"));
            }
        });
    }
}