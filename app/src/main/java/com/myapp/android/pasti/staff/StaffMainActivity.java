package com.myapp.android.pasti.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.myapp.android.pasti.LoginActivity;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;
import com.myapp.android.pasti.mahasiswa.MahasiswaMainActivity;

public class StaffMainActivity extends AppCompatActivity {
    Button btnLogout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        btnLogout = findViewById(R.id.btn_logout);
        sessionManager = new SessionManager(getApplicationContext());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLogin(false);
                sessionManager.setLevel("");
                sessionManager.setId("");
                startActivity(new Intent(StaffMainActivity.this, LoginActivity.class));
            }
        });
    }

    public void goToPersyaratan(View view) {
        startActivity(new Intent(StaffMainActivity.this, StaffListSyaratActivity.class));
    }

    public void goToFormulir(View view) {
        startActivity(new Intent(StaffMainActivity.this, StaffListFormulirActivity.class));
    }
}
