package com.myapp.android.pasti.mahasiswa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.android.pasti.LoginActivity;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;



/**
 * A simple {@link Fragment} subclass.
 */
public class MahasiswaProfileFragment extends Fragment implements View.OnClickListener {
    TextView name_text,nim_text,email_text,telp_text,sex_text,ttl_text;
    ImageView profile, email_img,telp_img,sex_img,ttl_img;
    String nim;
    private FirebaseFirestore firestore;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mahasiswa_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getActivity().getApplicationContext());
//        idUser = sessionManager.getIdUser();
        nim = sessionManager.getId();

        name_text = view.findViewById(R.id.name_text);
        nim_text = view.findViewById(R.id.nim_text);
        email_text = view.findViewById(R.id.email_text);
        telp_text = view.findViewById(R.id.telp_text);
        sex_text = view.findViewById(R.id.sex_text);
        ttl_text = view.findViewById(R.id.ttl_text);
        setDisplay();

        profile = view.findViewById(R.id.profile);
        email_img = view.findViewById(R.id.email_img);
        telp_img = view.findViewById(R.id.telp_img);
        sex_img = view.findViewById(R.id.sex_img);
        ttl_img = view.findViewById(R.id.ttl_img);

        Button btnDetailCategory = view.findViewById(R.id.btn_logout);
        btnDetailCategory.setOnClickListener(this);

    }

    public void setDisplay() {
        DocumentReference documentReference = firestore.collection("Mahasiswa").document(nim);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                name_text.setText(document.getString("nama").toString());
                nim_text.setText(nim);
                email_text.setText(document.getString("email").toString());
                telp_text.setText(document.getString("telepon").toString());
                sex_text.setText(document.getString("sex").toString());
                ttl_text.setText(document.getString("ttl").toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        sessionManager.setLogin(false);
        sessionManager.setLevel("");
        sessionManager.setId("");
        startActivity(new Intent(getActivity(), LoginActivity.class));
        ((Activity) getActivity()).overridePendingTransition(0, 0);
    }
}