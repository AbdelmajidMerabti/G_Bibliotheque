package com.example.g_bibliotheque;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadActivity extends AppCompatActivity {

    ImageView UploadImage;
    String imageurl;
    Uri uri;
    EditText Titre,Description,Prix,Qte; //Categorie
    Button save;
    FirebaseDatabase database;
    DatabaseReference referenceLivre,referenceDetail;
    AutoCompleteTextView search;
    String villeSelectionnee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        TextView textView1 = findViewById(R.id.textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadActivity.this,MainActivity4.class));
            }
        });

        UploadImage = findViewById(R.id.UploadImage);
        Titre = findViewById(R.id.Titre);
        Description = findViewById(R.id.Description);
        search = findViewById(R.id.Categorie);
        Prix = findViewById(R.id.Prix);
        Qte = findViewById(R.id.Qte);
        save = findViewById(R.id.save);
        fillSpinnerWithCategories(search);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                villeSelectionnee = (String) parent.getItemAtPosition(position);
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            UploadImage.setImageURI(uri);
                        }else{
                            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData()
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Livre").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());

                Uri uriImage = uriTask.getResult();
                imageurl = uriImage.toString();
                uploadData();
                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData()
    {
        try {
            String livreId;
            String titre = Titre.getText().toString();
            String description = Description.getText().toString();
            //  String categorie = Categorie.getText().toString();
            double prix = Double.parseDouble(String.valueOf(Prix.getText()));
            int qte = Integer.parseInt(String.valueOf(Qte.getText()));
            // Toast.makeText(this, titre+","+description+","+categorie, Toast.LENGTH_SHORT).show();
            database = FirebaseDatabase.getInstance();
            referenceLivre = database.getReference("Livre");
            DatabaseReference newChildRef = referenceLivre.push();
            livreId = newChildRef.getKey();
            Livre livre = new Livre(livreId,imageurl, titre, description, villeSelectionnee, qte, qte);

            referenceLivre.child(livreId).setValue(livre).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String detailId;
                    referenceDetail = database.getReference("Details");
                    DatabaseReference newChildRef1 = referenceDetail.push();
                    detailId = newChildRef1.getKey();
                    DateTimeFormatter dtf = null;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime now = LocalDateTime.now();
                        dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                        String customFormat = dtf.format(now);
                        Detail detail = new Detail(detailId, customFormat, qte, livreId, prix);
                        // String detailId = newChildRef1.getKey();
                        referenceDetail.child(detailId).setValue(detail);
                        startActivity(new Intent(UploadActivity.this,MainActivity4.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


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

                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadActivity.this, android.R.layout.simple_spinner_item, categoriesList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read categories from database.", error.toException());
            }
        });
    }

}