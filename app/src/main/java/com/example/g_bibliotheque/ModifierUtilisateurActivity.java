package com.example.g_bibliotheque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ModifierUtilisateurActivity extends AppCompatActivity {
    private EditText editNom, editEmail, editTelephone, editDateNaissance, editMotDePasse;
    private Button boutonEnregistrer;
    private DatabaseReference refUtilisateurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_utilisateur);

        TextView textView = findViewById(R.id.textView11);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        editNom = findViewById(R.id.edit_nom);
        editEmail = findViewById(R.id.edit_email);
        editTelephone = findViewById(R.id.edit_telephone);
        editDateNaissance = findViewById(R.id.edit_date_naissance);
        editMotDePasse = findViewById(R.id.edit_mot_de_passe);
        boutonEnregistrer = findViewById(R.id.bouton_enregistrer);

        // Récupérer l'utilisateur à modifier depuis l'Intent
       // Intent intent = getIntent();
       // Utilisateur utilisateur = (Utilisateur) intent.getSerializableExtra("Utilisateur");

        // Afficher les informations de l'utilisateur dans les champs EditText correspondants
        refUtilisateurs = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        refUtilisateurs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Utilisateur> utilisateurs = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    if(utilisateur != null && utilisateur.getKey().equals(MyGlobals.getKey())) {
        editNom.setText(utilisateur.getNom());
        editEmail.setText(utilisateur.getEmail());
        editTelephone.setText(utilisateur.getTelephone());
        editDateNaissance.setText(utilisateur.getDateNaissance());
        editMotDePasse.setText(utilisateur.getPassword());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });

        // Référence à la base de données Firebase
     //   refUtilisateurs = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        // Écouter le clic sur le bouton Enregistrer
        boutonEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les nouvelles informations de l'utilisateur depuis les champs EditText
                String nom = editNom.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String telephone = editTelephone.getText().toString().trim();
                String dateNaissance = editDateNaissance.getText().toString().trim();
                String motDePasse = editMotDePasse.getText().toString().trim();

                // Vérifier que les champs ne sont pas vides
                if (TextUtils.isEmpty(nom)) {
                    editNom.setError("Le nom est requis.");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editEmail.setError("L'e-mail est requis.");
                    return;
                }
                if (TextUtils.isEmpty(telephone)) {
                    editTelephone.setError("Le téléphone est requis.");
                    return;
                }
                if (TextUtils.isEmpty(dateNaissance)) {
                    editDateNaissance.setError("La date de naissance est requise.");
                    return;
                }
                if (TextUtils.isEmpty(motDePasse)) {
                    editMotDePasse.setError("Le mot de passe est requis.");
                    return;
                }

                // Mettre à jour les informations de l'utilisateur dans la base de données Firebase
                refUtilisateurs.child(MyGlobals.getKey()).child("nom").setValue(nom);
                refUtilisateurs.child(MyGlobals.getKey()).child("email").setValue(email);
                refUtilisateurs.child(MyGlobals.getKey()).child("telephone").setValue(telephone);
                refUtilisateurs.child(MyGlobals.getKey()).child("dateNaissance").setValue(dateNaissance);
                refUtilisateurs.child(MyGlobals.getKey()).child("password").setValue(motDePasse);

                // Afficher un message de confirmation
                Toast.makeText(ModifierUtilisateurActivity.this, "Les informations ont été mises à jour.", Toast.LENGTH_SHORT).show();

                // Fermer l'activité ModifierUtilisateurActivity et retourner à l'activité FragmentSetting
                finish();
            }
        });
    }
}

