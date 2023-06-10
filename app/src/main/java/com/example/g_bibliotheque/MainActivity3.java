package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity3 extends AppCompatActivity {

    private EditText editTextIdentifier;
    private EditText editTextPassword;
    private EditText mEditText,mCinText;
    private TextView ForgetPassword;
    private static final int REQUEST_SMS_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        ForgetPassword = findViewById(R.id.ForgetPassword);
        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  String toEmail = "abdelmajid.merabti@gmail.com";
                String subject = "Sujet de l'e-mail";
                String body = "Contenu de l'e-mail";
                new SendEmailTask(toEmail, subject, body).execute();*/

                // Créer le dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                builder.setTitle("Récupérer un mot de passe");

                // Créer le layout pour le EditText
                LayoutInflater inflater = LayoutInflater.from(MainActivity3.this);
                View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                mEditText = dialogLayout.findViewById(R.id.edit_text);
                mCinText = dialogLayout.findViewById(R.id.editText_cin);

                builder.setView(dialogLayout);

                // Ajouter les boutons "OK" et "Annuler"
                builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String toEmail = mEditText.getText().toString();
                        String subject = "Sujet de l'e-mail";
                        String body = "Contenu de l'e-mail";
                        boolean isEmailValid = isEmailValid(mEditText);
                        if (isEmailValid) {

                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Utilisateurs");
                            Query query = usersRef.orderByChild("cin").equalTo(mCinText.getText().toString().trim());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                        Utilisateur utilisateur = userSnapshot.getValue(Utilisateur.class);
                                        if (utilisateur != null && utilisateur.getEmail().equals(mEditText.getText().toString())) {
                                            String nvpassword = generatePassword();
                                           new SendEmailTask(utilisateur.getEmail(),
                                                    "Récupérer un mot de passe",
                                                    "Bonjour Mr,\n" +utilisateur.getNom()+
                                                            "\n" +
                                                            "Nous avons réinitialisé votre mot de passe et nous vous envoyons un nouveau mot de passe temporaire. Veuillez utiliser les informations ci-dessous pour vous connecter à votre compte :\n" +
                                                            "\n" +
                                                            "Nouveau mot de passe : "+nvpassword+"\n" +
                                                            "\n" +
                                                            "Nous vous recommandons de changer votre mot de passe dès que possible pour des raisons de sécurité. Si vous avez des questions ou des préoccupations, n'hésitez pas à nous contacter.\n" +
                                                            "\n" +
                                                            "Cordialement,").execute();
                                          //  DatabaseReference userRef = usersRef.child("Utilisateurs").child(utilisateur.getKey());
                                            DatabaseReference utilisateurRef = FirebaseDatabase.getInstance().getReference().child("Utilisateurs").child(utilisateur.getKey());
                                            HashMap<String, Object> updates = new HashMap<>();
                                            updates.put("password", nvpassword);
                                            updates.put("status", 0);
                                            utilisateurRef.updateChildren(updates)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this, R.style.AlertDialogTheme);
                                                            builder.setTitle("Information")
                                                                    .setMessage("Mot de passe envoi sur email")
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
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Une erreur s'est produite lors de la mise à jour
                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity3.this, "Email inconnu", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("TAG", "Erreur lors de la recherche de l'utilisateur", error.toException());
                                }
                            });


                        } else {
                            Toast.makeText(MainActivity3.this, "entrez mail svp !!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Annuler", null);

                // Afficher le dialogue
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        editTextIdentifier = findViewById(R.id.editTextIdentifier);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String identifier = editTextIdentifier.getText().toString();
                String password = editTextPassword.getText().toString();

                ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if (!isConnected) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this, R.style.AlertDialogTheme);
                    builder.setTitle("Erreur")
                            .setMessage("Pas de connexion internet")
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
                else{
                DatabaseReference usersRef1 = FirebaseDatabase.getInstance().getReference("Utilisateurs");
                Query query = usersRef1.orderByChild("cin").equalTo(identifier);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Utilisateur utilisateur = userSnapshot.getValue(Utilisateur.class);
                            if (utilisateur.getPassword().equals(password)) {
                                if (utilisateur.getType().equals("admin")) {
                                    if (ContextCompat.checkSelfPermission(MainActivity3.this, android.Manifest.permission.SEND_SMS)
                                            != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(MainActivity3.this,
                                                new String[]{android.Manifest.permission.SEND_SMS},
                                                REQUEST_SMS_PERMISSION);
                                    } else {
                                        // La permission est déjà accordée, démarrer l'activité "Home"
                                        startActivity(new Intent(MainActivity3.this, MainActivity5.class));
                                    }
                                    //  s
                                } else {
                                    if(utilisateur.getStatus() == 0) {
                                        startActivity(new Intent(MainActivity3.this, PasswordActivity.class));
                                        MyGlobals.setNom(utilisateur.getNom());
                                        MyGlobals.setKey(utilisateur.getKey());
                                    }
                                    else{
                                        startActivity(new Intent(MainActivity3.this,HomeActivity.class));
                                    MyGlobals.setNom(utilisateur.getNom());
                                    MyGlobals.setKey(utilisateur.getKey());}


                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "Erreur lors de la recherche de l'utilisateur", error.toException());
                    }
                });
            }

            }
        });
    }

    public boolean isEmailValid(EditText editText) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty()) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        while (password.length() < 8) {
            int index = (int) (rnd.nextFloat() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(MainActivity3.this, MainActivity5.class));

            } else {
                // Permission refusée, afficher un message d'erreur
                Toast.makeText(this, "Permission to send SMS was not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }




}