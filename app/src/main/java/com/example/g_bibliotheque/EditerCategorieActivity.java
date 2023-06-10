package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditerCategorieActivity extends AppCompatActivity {

    private EditText editTextLibelle;
    private Button buttonEnregistrer;

    private DatabaseReference refCategorieListe;
    private String categorieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editer_categorie);

        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditerCategorieActivity.this,MainActivity.class));
            }
        });

        // Récupérer l'ID de la catégorie à éditer depuis l'Intent
        categorieId = getIntent().getStringExtra("categorieId");
     //   Toast.makeText(this,categorieId,Toast.LENGTH_SHORT).show();

        // Récupérer la référence de la catégorie à éditer dans Firebase
        refCategorieListe = FirebaseDatabase.getInstance().getReference("categorie").child(categorieId);

        // Récupérer les vues de l'activité
        editTextLibelle = findViewById(R.id.editTextLibelle);
        buttonEnregistrer = findViewById(R.id.buttonEnregistrer);

        // Ajouter un écouteur pour les modifications de la catégorie dans Firebase
        refCategorieListe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Récupérer la catégorie depuis Firebase
                Categorie categorie = dataSnapshot.getValue(Categorie.class);

                // Afficher le libellé de la catégorie dans l'EditText
                if (categorie != null) {
                    editTextLibelle.setText(categorie.getLibelle());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Afficher une erreur si la catégorie ne peut pas être récupérée depuis Firebase
                Toast.makeText(getApplicationContext(), "Erreur : impossible de récupérer la catégorie", Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter un écouteur pour le bouton "Enregistrer"
        buttonEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le nouveau libellé de la catégorie depuis l'EditText
                String nouveauLibelle = editTextLibelle.getText().toString().trim();

                // Vérifier que le nouveau libellé n'est pas vide
                if (TextUtils.isEmpty(nouveauLibelle)) {
                    Toast.makeText(getApplicationContext(), "Veuillez entrer un libellé", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mettre à jour le libellé de la catégorie dans Firebase
                refCategorieListe.child("libelle").setValue(nouveauLibelle);

                // Afficher un message de succès
                Toast.makeText(getApplicationContext(), "La catégorie a été mise à jour", Toast.LENGTH_SHORT).show();

                // Fermer l'activité
                finish();
            }
        });
    }
    }
