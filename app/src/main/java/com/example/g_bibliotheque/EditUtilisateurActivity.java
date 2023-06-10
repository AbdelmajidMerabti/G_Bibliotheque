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

public class EditUtilisateurActivity extends AppCompatActivity {

    private EditText editTextNom;
    private EditText editTextCin;
    private EditText editTextEmail;
    private EditText editTextDateNaissance;
    private EditText editTextType;
    private EditText editTextPassword,editTextTelephone;
    private Button buttonSave;
    private DatabaseReference databaseRef;
    private String utilisateurId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_utilisateur);

        // Récupérer l'identifiant de l'utilisateur à modifier
        utilisateurId = getIntent().getStringExtra("utilisateurId");

        // Récupérer les vues de l'activité
        editTextNom = findViewById(R.id.editTextNom);
        editTextCin = findViewById(R.id.editTextCin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextDateNaissance = findViewById(R.id.editTextDateNaissance);
        editTextType = findViewById(R.id.editTextType);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextTelephone = findViewById(R.id.editTextTelephone);
        buttonSave = findViewById(R.id.buttonSave);

        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditUtilisateurActivity.this,ListUser.class));
            }
        });

        // Récupérer la référence de la base de données Firebase pour l'utilisateur à modifier
        databaseRef = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(utilisateurId);

        // Récupérer les informations de l'utilisateur actuel
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                if (utilisateur != null) {
                    editTextNom.setText(utilisateur.getNom());
                    editTextCin.setText(utilisateur.getCin());
                    editTextEmail.setText(utilisateur.getEmail());
                    editTextDateNaissance.setText(utilisateur.getDateNaissance());
                    editTextType.setText(utilisateur.getType());
                    editTextPassword.setText(utilisateur.getPassword());
                    editTextTelephone.setText(utilisateur.getTelephone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gérer l'erreur de lecture des données
            }
        });

        // Définir l'action à effectuer lorsque le bouton "Save" est cliqué
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUtilisateur();
            }
        });
    }

    private void updateUtilisateur() {
        // Récupérer les nouvelles informations de l'utilisateur
        String nom = editTextNom.getText().toString().trim();
        String cin = editTextCin.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String dateNaissance = editTextDateNaissance.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String telephone = editTextTelephone.getText().toString().trim();

        // Vérifier que toutes les informations ont été saisies
        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(cin) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(dateNaissance) || TextUtils.isEmpty(type) || TextUtils.isEmpty(password) || TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "Veuillez saisir toutes les informations", Toast.LENGTH_LONG).show();
            return;
        }

        // Mettre à jour l'utilisateur dans la base de données Firebase
        Utilisateur utilisateur = new Utilisateur(utilisateurId, nom, cin, email, dateNaissance, type, password, telephone);
        databaseRef.setValue(utilisateur);

        // Afficher un message de confirmation
        Toast.makeText(this, "Utilisateur mis à jour avec succès", Toast.LENGTH_LONG).show();

        // Fermer l'activité
        finish();

    }
}