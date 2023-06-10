package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    EditText n_password,r_password;
    Button bouton_enregistrer;
    private DatabaseReference databaseRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        n_password = findViewById(R.id.n_password);
        r_password = findViewById(R.id.r_password);
        bouton_enregistrer = findViewById(R.id.bouton_enregistrer);

        databaseRef = FirebaseDatabase.getInstance().getReference("Utilisateurs");


        bouton_enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(n_password.getText().toString().trim()) || TextUtils.isEmpty(r_password.getText().toString().trim()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this, R.style.AlertDialogTheme);
                    builder.setTitle("Erreur")
                            .setMessage("Veuillez saisir toutes les informations")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Code à exécuter lorsque l'utilisateur clique sur le bouton OK
                                }
                            }) ;
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return;
                }
                else
                {
                    if(!n_password.getText().toString().trim().equals(r_password.getText().toString().trim())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Erreur")
                                .setMessage("Les deux nouveaux mots de passe que vous avez entrés ne correspondent pas. Veuillez vérifier que vous avez entré le même mot de passe dans les deux champs et réessayer.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Code à exécuter lorsque l'utilisateur clique sur le bouton OK
                                    }
                                }) ;
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        return;
                    }
                    else
                    {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("password", n_password.getText().toString().trim());
                        updates.put("status", 1);

// Mise à jour des valeurs dans la base de données Firebase
                        databaseRef.child(MyGlobals.getKey()).updateChildren(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        n_password.setText("");
                                        r_password.setText("");
                                        startActivity(new Intent(PasswordActivity.this,HomeActivity.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Une erreur s'est produite lors de la mise à jour du mot de passe
                                        Log.w("TAG", "Une erreur s'est produite lors de la mise à jour du mot de passe.", e);
                                    }
                                });

                    }
                }

            }
        });

    }
}