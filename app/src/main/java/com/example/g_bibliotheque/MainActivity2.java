package com.example.g_bibliotheque;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private EditText editNom, editCin, editEmail, editDateNaissance, editType, editPassword, editTelephone;
    private Button btnAjouter;
    private ListView listView;
    private DatabaseReference databaseReference;
   private ArrayList<Utilisateur> utilisateursList;
    boolean emailExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



        // Initialisation des vues
        editNom = findViewById(R.id.nom);
        editCin = findViewById(R.id.cin);
        editEmail = findViewById(R.id.email);
        editDateNaissance = findViewById(R.id.dateNaissance);
        editType = findViewById(R.id.type);
        editPassword = findViewById(R.id.password);
        editTelephone = findViewById(R.id.telephone);
        btnAjouter = findViewById(R.id.button);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity2.this,DashbordUser.class));
            }
        });

        // Référence à la base de données Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        String id = databaseReference.push().getKey();

        // Écouteur pour le bouton d'ajout
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération des valeurs des champs EditText
             //   String key = editKey.getText().toString().trim();
                String nom = editNom.getText().toString().trim();
                String cin = editCin.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String dateNaissance = editDateNaissance.getText().toString().trim();
                String type = editType.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String telephone = editTelephone.getText().toString().trim();

                // Vérification des champs obligatoires
                if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(cin)
                        || TextUtils.isEmpty(email) || TextUtils.isEmpty(dateNaissance)
                        || TextUtils.isEmpty(type) || TextUtils.isEmpty(password)  || TextUtils.isEmpty(telephone)) {
                    Toast.makeText(MainActivity2.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
                } else {
                    // Création d'un objet Utilisateur
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Utilisateurs");
                    Query query = databaseReference.orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(MainActivity2.this, "Email deja exists", Toast.LENGTH_LONG).show();
                            }else{
                                Utilisateur utilisateur = new Utilisateur(id, nom, cin, email, dateNaissance, type, password, telephone);

                                // Ajout de l'utilisateur à Firebase
                                databaseReference.child(id).setValue(utilisateur);

                                // Effacement des champs EditText
                                //   editKey.setText("");
                                editNom.setText("");
                                editCin.setText("");
                                editEmail.setText("");
                                editDateNaissance.setText("");
                                editType.setText("");
                                editPassword.setText("");
                                editTelephone.setText("");

                                Toast.makeText(MainActivity2.this, "Utilisateur ajouté avec succès", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            emailExists = false;
                        }
                    });


                }
            }
        });

    }

  /*  public boolean isEmailExists(String email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emailExists = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                emailExists = false;
            }
        });
        return emailExists;
    }
*/


}