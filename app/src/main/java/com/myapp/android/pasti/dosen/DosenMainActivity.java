package com.myapp.android.pasti.dosen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.myapp.android.pasti.LoginActivity;
import com.myapp.android.pasti.Note;
import com.myapp.android.pasti.NoteAdapter;
import com.myapp.android.pasti.R;
import com.myapp.android.pasti.SessionManager;
import com.myapp.android.pasti.staff.StaffFormulirActivity;
import com.myapp.android.pasti.staff.StaffListFormulirActivity;
import com.myapp.android.pasti.staff.StaffMainActivity;

public class DosenMainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.myapp.android.pasti.dosen.EXTRA_ID";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Formulir");

    private NoteAdapter adapter;
    Button btnLogout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Formulir");

        btnLogout = findViewById(R.id.btn_logout);
        setUpRecyclerView();
        sessionManager = new SessionManager(getApplicationContext());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setLogin(false);
                sessionManager.setLevel("");
                sessionManager.setId("");
                startActivity(new Intent(DosenMainActivity.this, LoginActivity.class));
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.whereEqualTo("checkStatusStaff", true)
                .whereEqualTo("checkStatusDosen", false); //.orderBy("priority", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        adapter = new NoteAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
//                Toast.makeText(StaffListSyaratActivity.this,
//                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DosenMainActivity.this, DosenFormulirActivity.class);
                intent.putExtra(EXTRA_ID, id);
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
