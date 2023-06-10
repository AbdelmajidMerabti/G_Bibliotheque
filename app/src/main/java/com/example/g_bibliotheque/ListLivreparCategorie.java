package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListLivreparCategorie extends AppCompatActivity {

    FirebaseDatabase database;
  //  DatabaseReference livresRef;
    String libelle;
    RecyclerView listView;
    List<Livre> livres;
    SearchView searchView;
    LivreAdapter adapter1;
    DatabaseReference reference;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_livrepar_categorie);

        Intent intent = getIntent();
        libelle = intent.getStringExtra("libelle");

        listView = findViewById(R.id.livres_listview);
        searchView = findViewById(R.id.search1);

        TextView textView = findViewById(R.id.retourUser);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListLivreparCategorie.this,HomeActivity.class));
            }
        });

        /*database = FirebaseDatabase.getInstance();
        livresRef = database.getReference("Livre");

        livresRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 livres = new ArrayList<>();

                for (DataSnapshot livreSnapshot : dataSnapshot.getChildren()) {
                    Livre livre = livreSnapshot.getValue(Livre.class);
                    if (livre.getCategorie().equals(libelle)) {
                        livres.add(livre);
                    }
                }

                // Créer un adaptateur pour la liste de livres
                adapter1 = new LivreAdapter(ListLivreparCategorie.this, livres);

                // Passer l'adaptateur à la ListView

                listView.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Erreur lors de la récupération des livres.", databaseError.toException());
            }
        });*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListLivreparCategorie.this,1);
        listView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder bulider = new AlertDialog.Builder(ListLivreparCategorie.this);
        bulider.setCancelable(false);
        bulider.setView(R.layout.progress_layout);
        AlertDialog dialog = bulider.create();
        dialog.show();
        livres = new ArrayList<>();
        // list_livre.clear();
        adapter1 = new LivreAdapter(ListLivreparCategorie.this,livres);
        listView.setAdapter(adapter1);
        reference = FirebaseDatabase.getInstance().getReference().child("Livre");
        dialog.show();
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                livres.clear();
                for (DataSnapshot item : snapshot.getChildren())
                {
                    Livre dataLivre = item.getValue(Livre.class);
                    if(dataLivre.getCategorie().equals(libelle))
                    livres.add(dataLivre);
                }
                adapter1.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });



    }

    public void searchList(String text)
    {
        ArrayList<Livre> searchLivre = new ArrayList<>();
        for (Livre livre : livres) {
            if (livre.getTitre().toLowerCase().contains(text.toLowerCase())) {
                searchLivre.add(livre);
            }
        }

        adapter1.searchLivreListe(searchLivre);
    }

}