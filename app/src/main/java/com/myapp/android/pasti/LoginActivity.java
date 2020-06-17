package com.myapp.android.pasti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.android.pasti.dosen.DosenMainActivity;
import com.myapp.android.pasti.mahasiswa.MahasiswaMainActivity;
import com.myapp.android.pasti.staff.StaffMainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout Email, Password;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String s, emailInput, passwordInput;
    String userId;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        cekSession();

        Email = findViewById(R.id.et_email);
        Password = findViewById(R.id.et_pass);
        Button btnMoveActivity = findViewById(R.id.btn_login);
        btnMoveActivity.setOnClickListener(this);
    }

    private void cekSession() {
        if (sessionManager.getLogin()) {
            String level = sessionManager.getLevel();
            if (level.equals("1")) {
                startActivity(new Intent(getApplicationContext(), MahasiswaMainActivity.class));
                finish();
            } else if (level.equals("2")) {
                startActivity(new Intent(getApplicationContext(), StaffMainActivity.class));
                finish();
            } else if (level.equals("3")) {
                startActivity(new Intent(getApplicationContext(), DosenMainActivity.class));
                finish();
            }else {
                return;
            }
        }
    }

    private boolean validateEmail() {
        emailInput = Email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            Email.setError("Tidak boleh kosong");
            return false;
        } else {
            if (!emailInput.contains("@upnvj.com")) {
                Email.setError("Harus menggunakan email UPNVJ");
                return false;
            } else {
                Email.setError(null);
                return true;
            }
        }
    }

    private boolean validatePassword() {
        passwordInput = Password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            Password.setError("Tidak boleh kosong");
            return false;
        } else {
            Password.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (!validateEmail() | !validatePassword()) {
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userId = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firestore.collection("users").document(userId);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            String level = document.getString("level");
                            String id = document.getString("id");

                            if (level.equals("1")) {
                                //Set Session
                                sessionManager.setLogin(true);
                                sessionManager.setLevel(level);
                                sessionManager.setId(id);

                                Toast.makeText(LoginActivity.this, "Login berhasil.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MahasiswaMainActivity.class));
                                finish();
                            } else if (level.equals("2")) {
                                //Set Session
                                sessionManager.setLogin(true);
                                sessionManager.setLevel(level);
                                sessionManager.setId(id);

                                Toast.makeText(LoginActivity.this, "Login berhasil.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, StaffMainActivity.class));
                                finish();
                            } else if(level.equals("3")){
//                                Set Session
                                sessionManager.setLogin(true);
                                sessionManager.setLevel(level);
                                sessionManager.setId(id);

                                Toast.makeText(LoginActivity.this, "Login berhasil.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), DosenMainActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
