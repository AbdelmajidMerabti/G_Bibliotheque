package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Historique extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LivreDemandeAdapter adapter;
    private List<Demande> demandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        demandes = new ArrayList<>();
        adapter = new LivreDemandeAdapter(demandes);
        recyclerView.setAdapter(adapter);

        // Récupérer les demandes de l'utilisateur actuel depuis Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Query query = database.child("Demande").orderByChild("utilisateurId").equalTo(MyGlobals.getKey());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                demandes.clear();
                for (DataSnapshot demandeSnapshot : snapshot.getChildren()) {
                    Demande demande = demandeSnapshot.getValue(Demande.class);
                  //  if (demande.getStatusDemande() != 2) {
                        demandes.add(demande);
                    //}
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "loadDemandes:onCancelled", error.toException());
            }
        });
    }
}