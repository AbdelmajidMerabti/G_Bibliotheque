package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PageHome extends AppCompatActivity {

    TextView nom,item;
    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_home);

        nom = findViewById(R.id.nom);
        item = findViewById(R.id.item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PageHome.this,Historique.class));
            }
        });
        listView = findViewById(R.id.categories_listview);

        Intent intent = getIntent();
        String valeur = intent.getStringExtra("nom");
        String key = intent.getStringExtra("key");
        if(MyGlobals.getNom() == "" && MyGlobals.getKey() == "") {
            MyGlobals.setNom(valeur);
            MyGlobals.setKey(key);
        }
        nom.setText(MyGlobals.getNom());

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categorie");
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Categorie> categories = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Categorie category = categorySnapshot.getValue(Categorie.class);
                    categories.add(category);
                }

                CategoryAdapter adapter = new CategoryAdapter(PageHome.this, categories);

                // Passer l'adaptateur à la ListView

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Erreur lors de la récupération des catégories.", databaseError.toException());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categorie selectedCategory = (Categorie) parent.getItemAtPosition(position);
                String label = selectedCategory.getLibelle();
                Intent intent = new Intent(PageHome.this,ListLivreparCategorie.class);
                intent.putExtra("libelle", label);
                startActivity(intent);
            }
        });




    }
}