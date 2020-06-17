package com.myapp.android.pasti.mahasiswa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class MahasiswaPersyaratanActivity extends AppCompatActivity {

    private static final String TAG = "PersyaratanActivity";
    private static final String KEY_TITLE = "toeflNilai";
    private static final String KEY_DESCRIPTION = "toeflImageUrl";
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    CheckBox cb1, cb2, cb3, cb4;
    TextView tvIPK, tvSKS;
    String toefl, id, imageUrl, nama;
    SessionManager sessionManager;
    float IPK;
    int SKS, intToefl;
    boolean checkIPK, checkSKS, checkNilai, checkToefl = false;
    boolean isNilai;
    Button btnImage, btnKirim;
    ImageView imageView;
    ProgressBar pbUpload;
    Uri imageUri;
    TextInputEditText etToefl;
    LinearLayout lytToefl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mahasiswa_persyaratan);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Persyaratan");

        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        sessionManager = new SessionManager(getApplicationContext());
        id = sessionManager.getId();

        tvIPK = findViewById(R.id.tv_persyaratan_ipk);
        tvSKS = findViewById(R.id.tv_persyaratan_sks);
        cb1 = findViewById(R.id.cb_persyaratan_2);
        cb2 = findViewById(R.id.cb_persyaratan_1);
        cb3 = findViewById(R.id.cb_persyaratan_3);
        setDisplay();

        btnImage = findViewById(R.id.btn_persyaratan_toefl);
        imageView = findViewById(R.id.iv_toefl);
        pbUpload = findViewById(R.id.pb_upload);
        etToefl = findViewById(R.id.et_persyaratan_toefl);
        btnKirim = findViewById(R.id.btn_persyaratan_kirim);
        cb4 = findViewById(R.id.cb_persyaratan_4);
        lytToefl = findViewById(R.id.lyt_toefl);
        etToefl.addTextChangedListener(textWatcher);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnKirim.isFocusable()) {
                    uploadFile();
                } else {
                    Toast.makeText(MahasiswaPersyaratanActivity.this, "Belum memenuhi syarat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setDisplay() {
        DocumentReference documentReference = firestore.collection("Mahasiswa").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                IPK = document.getLong("ipk").floatValue();
                if (IPK >= 3) {
                    checkIPK = true;
                }
                tvIPK.setText(String.format("%.2f", IPK));
                cb1.setChecked(checkIPK);

                SKS = document.getLong("sks").intValue();
                if (SKS >= 120) {
                    checkSKS = true;
                }
                tvSKS.setText(String.format("%d", SKS));
                cb2.setChecked(checkSKS);

                isNilai = document.getBoolean("nilaiC");
                if (!isNilai) {
                    checkNilai = true;
                }
                cb3.setChecked(checkNilai);

                nama = document.getString("nama");
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            toefl = etToefl.getText().toString().trim();
            if (!toefl.isEmpty()) {
                intToefl = Integer.parseInt(toefl);
                if (intToefl >= 450 && imageUri != null) {
                    checkToefl = true;
                    cb4.setChecked(checkToefl);
                }
            }

            if (checkIPK && checkToefl && checkSKS && checkNilai) {
                btnKirim.setFocusable(true);
            } else {
                btnKirim.setFocusable(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (intToefl < 450) {
                etToefl.setError("Minimal toefl 450");
            }
        }
    };


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            lytToefl.setVisibility(LinearLayout.VISIBLE);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (imageUri != null) {
            pbUpload.setVisibility(View.VISIBLE);
            StorageReference fileReference = storageRef.child(id).child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pbUpload.setProgress(0);
                                }
                            }, 5000);

                            Toast.makeText(MahasiswaPersyaratanActivity.this, "syaratUpload successful", Toast.LENGTH_LONG).show();

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            imageUrl = uri.toString();

                                            Map<String, Object> note = new HashMap<>();
                                            note.put(KEY_DESCRIPTION, imageUrl);
                                            note.put(KEY_TITLE, toefl);
                                            note.put("ipk", IPK);
                                            note.put("sks", SKS);
                                            note.put("nilaiC", false);
                                            note.put("checkStatus", false);
                                            note.put("nim", id);
                                            note.put("nama", nama);

                                            firestore.collection("Persyaratan").document(id).set(note)
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MahasiswaPersyaratanActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                            Map<String, Object> data = new HashMap<>();
                                            data.put("tahap1", true);
                                            data.put(KEY_DESCRIPTION, imageUrl);
                                            data.put(KEY_TITLE, toefl);
                                            firestore.collection("Mahasiswa").document(id).update(data);
                                        }
                                    });
                                }
                            }

                            startActivity(new Intent(getApplicationContext(), MahasiswaMainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MahasiswaPersyaratanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pbUpload.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Masukkan nilai toefl dan bukti nilai toefl", Toast.LENGTH_SHORT).show();
        }
    }
}
