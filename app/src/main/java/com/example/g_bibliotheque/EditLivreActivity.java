package com.example.g_bibliotheque;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EditLivreActivity extends AppCompatActivity {

    EditText Titre, Description;
    AutoCompleteTextView Categorie;
    String livreKey;
    String title, desc, cat,oldImageURL;
    int qte, qteR;
    Button buttonModifier;
    DatabaseReference databaseRef;
    String villeSelectionnee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_livre);
        Titre = findViewById(R.id.Titre);
        Description = findViewById(R.id.Description);
        Categorie = findViewById(R.id.Categorie);
        buttonModifier = findViewById(R.id.Modifiere);
        TextView textView1 = findViewById(R.id.textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditLivreActivity.this,MainActivity4.class));
            }
        });
        fillSpinnerWithCategories(Categorie);
        Categorie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                villeSelectionnee = (String) parent.getItemAtPosition(position);
            }
        });

        databaseRef = FirebaseDatabase.getInstance().getReference("Livre");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            livreKey = bundle.getString("key");
            oldImageURL = bundle.getString("image");
            Titre.setText(bundle.getString("titre"));
            qte = bundle.getInt("qte");
            qteR = bundle.getInt("qteR");
            Description.setText(bundle.getString("description"));
            cat = bundle.getString("categorie");
        }
        villeSelectionnee = cat;

        buttonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titre = Titre.getText().toString().trim();
                String description = Description.getText().toString().trim();
                if(titre == "" || description == "") {

                }
                else
                {
                        modifierLivre(livreKey, oldImageURL, titre, description, villeSelectionnee, qte, qteR);

                }
            }
        });


    }

    private void fillSpinnerWithCategories(AutoCompleteTextView spinner) {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categorie");

        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> categoriesList = new ArrayList<>();

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("libelle").getValue(String.class);
                    categoriesList.add(categoryName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditLivreActivity.this, android.R.layout.simple_spinner_item, categoriesList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read categories from database.", error.toException());
            }
        });
    }


    private void modifierLivre(String key, String image, String titre, String description, String categorie, int qte, int qteR) {
        Livre livre = new Livre(key, image, titre, description, categorie, qte, qteR);
        databaseRef.child(key).setValue(livre)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditLivreActivity.this, "Livre modifié avec succès", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditLivreActivity.this, "Erreur lors de la modification du livre : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}