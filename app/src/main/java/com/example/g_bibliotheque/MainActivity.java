package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
  /*  ListView listViewCategorie;
    ArrayList<Categorie> categorieListe;
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        DatabaseReference refCategorie = database.getReference("categorie");

        // Récupérer les vues
        EditText editTextLibelle = findViewById(R.id.categorie);
        Button buttonAjouter = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity5.class));
            }
        });
      /*listViewCategorie = findViewById(R.id.listView);
        categorieListe = new ArrayList<>();
*/

        buttonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le libellé de la catégorie à partir de l'EditText
                String libelle = editTextLibelle.getText().toString();

                // Créer un nouvel ID pour la catégorie
                String id = refCategorie.push().getKey();

                // Créer un nouvel objet catégorie
                Categorie categorie = new Categorie(id, libelle);

                // Ajouter la catégorie dans Firebase
                refCategorie.child(id).setValue(categorie);

                // Effacer le contenu de l'EditText
                editTextLibelle.setText("");

            }
        });

        remplirListView();

    }


    private void remplirListView() {
        // Récupérer une référence de Firebase pour la liste des catégories
        DatabaseReference refCategorieListe = FirebaseDatabase.getInstance().getReference("categorie");

        // Récupérer la ListView
        ListView listViewCategorie = findViewById(R.id.listView);

        // Créer un ArrayList pour stocker les catégories
        ArrayList<Categorie> categorieListe = new ArrayList<>();

        // Créer un adaptateur personnalisé pour la ListView
        ArrayAdapter<Categorie> adapter = new ArrayAdapter<Categorie>(this, R.layout.row_categorie, R.id.textViewLibelle, categorieListe) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Récupérer l'élément de la liste à la position donnée
                Categorie categorie = getItem(position);

                // Vérifier si la vue existe déjà, sinon la créer
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_categorie, parent, false);
                }

                // Récupérer les vues de l'élément de la liste
                TextView textViewLibelle = convertView.findViewById(R.id.textViewLibelle);
                ImageView buttonEditer = convertView.findViewById(R.id.buttonEditer);
                ImageView buttonSupprimer = convertView.findViewById(R.id.buttonSupprimer);

                // Afficher le libellé de la catégorie
                textViewLibelle.setText(categorie.getLibelle());

                // Ajouter un écouteur pour le bouton Editer
                buttonEditer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EditerCategorieActivity.class);

                        // Ajouter l'ID de la catégorie à l'Intent
                        intent.putExtra("categorieId", categorie.getId());

                        // Ouvrir l'activité d'édition de la catégorie
                        startActivity(intent);
                    }
                });

                // Ajouter un écouteur pour le bouton Supprimer
                buttonSupprimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refCategorieListe.child(categorie.getId()).removeValue();
                    }
                });

                // Retourner la vue de l'élément de la liste
                return convertView;
            }
        };

// Configurer la ListView pour utiliser l'adaptateur
        listViewCategorie.setAdapter(adapter);

// Ajouter un écouteur pour les modifications de la liste des catégories dans Firebase
        refCategorieListe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Effacer la liste des catégories
                categorieListe.clear();

                // Parcourir toutes les catégories dans Firebase pour les ajouter à l'ArrayList
                for (DataSnapshot categorieSnapshot : dataSnapshot.getChildren()) {
                    Categorie categorie = categorieSnapshot.getValue(Categorie.class);
                    categorieListe.add(categorie);
                }

                // Mettre à jour l'adaptateur de la ListView pour afficher les nouvelles catégories
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Afficher un message d'erreur si la récupération des données a échoué
                Toast.makeText(getApplicationContext(), "Erreur : " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    }
