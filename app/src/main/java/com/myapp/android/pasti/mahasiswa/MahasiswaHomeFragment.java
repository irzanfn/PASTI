package com.myapp.android.pasti.mahasiswa;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MahasiswaHomeFragment extends Fragment {

    FirebaseFirestore db;
    Button btnSyarat, btnPeriksa, btnFormulir, btnPersetujuan, btnLampiran;
    String id;
    SessionManager sessionManager;
    private boolean isTahap1, isTahap2, isTahap3, isTahap4, isTahap5;
    int green = Color.parseColor("#03AE00");
    int yellow = Color.parseColor("#FEB72B");

    public MahasiswaHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mahasiswa_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSyarat = view.findViewById(R.id.btn_mhs_persyaratan);
        btnPeriksa = view.findViewById(R.id.btn_mhs_periksa);
        btnFormulir = view.findViewById(R.id.btn_mhs_formulir);
        btnPersetujuan = view.findViewById(R.id.btn_mhs_persetujuan);
        btnLampiran = view.findViewById(R.id.btn_mhs_lampiran);

        sessionManager = new SessionManager(getActivity().getBaseContext());
        id = sessionManager.getId();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Mahasiswa").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                isTahap1 = document.getBoolean("tahap1");
                isTahap2 = document.getBoolean("tahap2");
                isTahap3 = document.getBoolean("tahap3");
                isTahap4 = document.getBoolean("tahap4");
                isTahap5 = document.getBoolean("tahap5");

                btnSyarat.setBackgroundColor(green);
                if(isTahap1){
                    btnSyarat.setBackgroundColor(yellow);
                    btnPeriksa.setBackgroundColor(green);
                }
                if(isTahap2){
                    btnPeriksa.setBackgroundColor(yellow);
                    btnFormulir.setBackgroundColor(green);
                }
                if(isTahap3){
                    btnFormulir.setBackgroundColor(yellow);
                    btnPersetujuan.setBackgroundColor(green);
                }
                if(isTahap4){
                    btnPersetujuan.setBackgroundColor(yellow);
                    btnLampiran.setBackgroundColor(green);
                }
            }
        });

        btnSyarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTahap1) {
                    Toast.makeText(getActivity().getBaseContext(), "Anda sudah isi. Lanjut ke tahap berikutnya.", Toast.LENGTH_LONG).show();
                } else if (!isTahap1) {
                    startActivity(new Intent(getActivity(), MahasiswaPersyaratanActivity.class));
                }
            }
        });

        btnPeriksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTahap1 && !isTahap2) {
                    Toast.makeText(getActivity().getBaseContext(), "Data Anda dalam proses pemeriksaan", Toast.LENGTH_LONG).show();
                } else if (!isTahap1 && !isTahap2) {
                    Toast.makeText(getActivity().getBaseContext(), "Penuhi syarat-syarat terlebih dahulu, pada tahap sebelumnya", Toast.LENGTH_LONG).show();
                } else if (isTahap1 && isTahap2) {
                    Toast.makeText(getActivity().getBaseContext(), "Data Anda sudah di terima. Lanjut ke tahap berikutnya.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnFormulir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTahap1 && isTahap2 && !isTahap3) {
                    startActivity(new Intent(getActivity(), MahasiswaFormulirActivity.class));
                } else if (isTahap1 && isTahap2 && isTahap3) {
                    Toast.makeText(getActivity().getBaseContext(), "Data Anda sudah di terima. Lanjut ke tahap berikutnya.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Penuhi syarat-syarat terlebih dahulu, pada tahap sebelumnya", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPersetujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTahap1 && isTahap2 && isTahap3 && !isTahap4) {
                    Toast.makeText(getActivity().getBaseContext(), "Data Anda dalam proses persetujuan", Toast.LENGTH_LONG).show();
                } else if (isTahap1 && isTahap2 && isTahap3 && isTahap4) {
                    Toast.makeText(getActivity().getBaseContext(), "Data Anda sudah di terima. Lanjut ke tahap berikutnya.", Toast.LENGTH_LONG).show();
                } else if (isTahap1 && isTahap2) {
                    Toast.makeText(getActivity().getBaseContext(), "Penuhi syarat-syarat terlebih dahulu, pada tahap sebelumnya", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnLampiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTahap1 && isTahap2 && isTahap3 && isTahap4) {
                    DocumentReference docRef = FirebaseFirestore.getInstance()
                            .collection("Mahasiswa")
                            .document(id);
                    docRef.update("tahap5", true);
                    startActivity(new Intent(getActivity(), MahasiswaLampiranActivity.class));
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Penuhi syarat-syarat terlebih dahulu, pada tahap sebelumnya", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
