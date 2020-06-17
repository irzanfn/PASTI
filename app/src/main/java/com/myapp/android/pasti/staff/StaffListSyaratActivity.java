package com.myapp.android.pasti.staff;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.myapp.android.pasti.Note;
import com.myapp.android.pasti.NoteAdapter;
import com.myapp.android.pasti.R;

public class StaffListSyaratActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.myapp.android.pasti.staff.EXTRA_ID";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Persyaratan");

    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list_syarat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Persyaratan");

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = notebookRef.whereEqualTo("checkStatus", false); //.orderBy("priority", Query.Direction.ASCENDING);
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
                Intent intent = new Intent(StaffListSyaratActivity.this, StaffSyaratActivity.class);
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