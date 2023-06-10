package com.example.g_bibliotheque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListUser extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private ArrayList<Utilisateur> utilisateursList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        listView = findViewById(R.id.listView);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListUser.this,DashbordUser.class));
            }
        });
        utilisateursList = new ArrayList();

        remplirListView();
    }

    private void remplirListView() {
        // Récupérer la référence de la base de données Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Utilisateurs");

        ListView listView = findViewById(R.id.listView);

        // Créer un écouteur pour la base de données
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Effacer les données précédentes dans la liste
                utilisateursList.clear();

                // Parcourir les données de la base de données et les ajouter à la liste
                for (DataSnapshot utilisateurSnapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = utilisateurSnapshot.getValue(Utilisateur.class);
                    utilisateursList.add(utilisateur);
                }

                // Créer un adaptateur pour la liste et l'attacher au ListView
                UtilisateurAdapter adapter = new UtilisateurAdapter(ListUser.this, utilisateursList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur de la base de données ici
            }
        });
    }
}