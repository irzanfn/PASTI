package com.myapp.android.pasti.mahasiswa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MahasiswaFormulirActivity extends AppCompatActivity {
    Map<String, Object> bundle = new HashMap<>();
    static final String KEY_PRODI = "prodi";
    static final String KEY_NIM = "nim";
    static final String KEY_NAMA = "nama";
    static final String KEY_TTL = "ttl";
    static final String KEY_AGAMA = "agama";
    static final String KEY_TLPN = "telepon";
    static final String KEY_PERIODE = "periode";
    static final String KEY_JUDUL = "judul";
    static final String KEY_PEMBIMBING = "pembimbing";
    static final String KEY_STATUS_DOSEN = "checkStatusDosen";
    static final String KEY_STATUS_STAFF = "checkStatusStaff";

    SessionManager sessionManager;
    FirebaseFirestore firestore;
    String id, prodi, nama, ttl, agama, tlpn, periode, judul, pembimbing;
    TextInputEditText etProdi, etNIM, etNama, etTTL, etAgama, etTlpn, etPeriode, etJudul,
            etProposal, etBukuBimbingan, etBuktiPembayaran;
    Spinner spPembimbing;
    Button btnKirim;
    Uri uriProposal, uriBuku, uriBukti;
    int posisi = 0;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_formulir);
        firestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());
        id = sessionManager.getId();
        etProdi = findViewById(R.id.et_prodi);
        etNIM = findViewById(R.id.et_nim);
        etNama = findViewById(R.id.et_nama);
        etTTL = findViewById(R.id.et_ttl);
        etAgama = findViewById(R.id.et_agama);
        etTlpn = findViewById(R.id.et_tlpn);
        etPeriode = findViewById(R.id.et_periode);
        etJudul = findViewById(R.id.et_judul);
        etProposal = findViewById(R.id.et_proposal);
        etBukuBimbingan = findViewById(R.id.et_buku_bimbingan);
        etBuktiPembayaran = findViewById(R.id.et_bukti);
        btnKirim = findViewById(R.id.btn_formulir_kirim);
        spPembimbing = findViewById(R.id.sp_pembimbing);
        getData();
        storageRef = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        List<Dosen> dosenList = new ArrayList<>();
        Dosen dosen0 = new Dosen("Pilih Dosen Pembimbing", "");
        dosenList.add(dosen0);
        Dosen dosen1 = new Dosen("Noor Falih", "198907152018031000");
        dosenList.add(dosen1);
        Dosen dosen2 = new Dosen("Mayanda Mega Santoni", "199005252018032001");
        dosenList.add(dosen2);
        Dosen dosen3 = new Dosen("Ria Astriatma", "198911242018032001");
        dosenList.add(dosen3);

        ArrayAdapter<Dosen> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, dosenList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPembimbing.setAdapter(adapter);

        spPembimbing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Dosen dosen = (Dosen) parent.getSelectedItem();
                getDataDosen(dosen);
                posisi = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul = etJudul.getText().toString();
                if(judul.isEmpty()){
                    etJudul.setError("Wajib diisi!");
                }else{
                    bundle.put("judul", judul);
                    uploadFile();
                }
            }
        });

        etProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(1);
            }
        });

        etBukuBimbingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(2);
            }
        });

        etBuktiPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(3);
            }
        });
    }

    public void getDataDosen(Dosen dosen) {
        bundle.put("namaDosen", dosen.getNameDosen());
        bundle.put("idDosen", dosen.getIdDosen());
    }

    private void uploadFile() {
        if (posisi == 0) {
            TextView errorText = (TextView)spPembimbing.getSelectedView();
            errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("Pilih Dosen Pembimbing");
        } else {
            if (uriProposal != null && uriBukti != null && uriBuku != null) {
                StorageReference fileReference = storageRef.child(id);
                fileReference.child(System.currentTimeMillis() + "." + getFileName(uriProposal))
                        .putFile(uriProposal).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firestore.collection("Formulir").document(id).update("urlProposal", uri.toString());
                                    }
                                });
                            }
                        }
                    }
                });

                fileReference.child(System.currentTimeMillis() + "." + getFileName(uriBuku))
                        .putFile(uriBuku).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firestore.collection("Formulir").document(id).update("urlBuku", uri.toString());
                                    }
                                });
                            }
                        }
                    }
                });

                fileReference.child(System.currentTimeMillis() + "." + getFileName(uriBukti)).
                        putFile(uriBukti).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firestore.collection("Formulir").document(id).update("urlBukti", uri.toString());
                                    }
                                });
                            }
                        }
                    }
                });

                bundle.put(KEY_STATUS_STAFF, false);
                bundle.put(KEY_STATUS_DOSEN, false);
                firestore.collection("Formulir").document(id).set(bundle)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firestore.collection("Mahasiswa").document(id).update("tahap3", true);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MahasiswaFormulirActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            startActivity(new Intent(getApplicationContext(), MahasiswaMainActivity.class));
            finish();
        }
    }

    private void getData() {
        DocumentReference documentReference = firestore.collection("Mahasiswa").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                prodi = document.getString("prodi");
                nama = document.getString("nama");
                ttl = document.getString("ttl");
                agama = document.getString("agama");
                tlpn = document.getString("telepon");
                periode = document.getString("periode");

                etProdi.setText(prodi);
                etNIM.setText(id);
                etNama.setText(nama);
                etTTL.setText(ttl);
                etAgama.setText(agama);
                etTlpn.setText(tlpn);
                etPeriode.setText(periode);

                bundle.put(KEY_PRODI, prodi);
                bundle.put(KEY_NIM, id);
                bundle.put(KEY_NAMA, nama);
                bundle.put(KEY_TTL, ttl);
                bundle.put(KEY_AGAMA, agama);
                bundle.put(KEY_TLPN, tlpn);
                bundle.put(KEY_PERIODE, periode);
            }
        });
    }

    private void openFileChooser(int request) {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih file PDF"), request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriProposal = data.getData();
            etProposal.setText(getFileName(uriProposal));
        }
        if (requestCode == 2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriBuku = data.getData();
            etBukuBimbingan.setText(getFileName(uriBuku));
        }
        if (requestCode == 3 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriBukti = data.getData();
            etBuktiPembayaran.setText(getFileName(uriBukti));
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
