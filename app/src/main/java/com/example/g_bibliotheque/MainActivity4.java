package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity4 extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<Livre> list_livre;
    DatabaseReference reference;
    ValueEventListener eventListener;
    SearchView searchView;
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        fab = findViewById(R.id.floatfab1);
        recyclerView = findViewById(R.id.recyclerview1);
        searchView = findViewById(R.id.search1);
        TextView textView = findViewById(R.id.textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity4.this,MainActivity5.class));
            }
        });
        // searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity4.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder bulider = new AlertDialog.Builder(MainActivity4.this);
        bulider.setCancelable(false);
        bulider.setView(R.layout.progress_layout);
        AlertDialog dialog = bulider.create();
        dialog.show();
        list_livre = new ArrayList<>();
        // list_livre.clear();
        myAdapter = new MyAdapter(MainActivity4.this,list_livre);
        recyclerView.setAdapter(myAdapter);
        reference = FirebaseDatabase.getInstance().getReference().child("Livre");
        dialog.show();
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_livre.clear();
                for (DataSnapshot item : snapshot.getChildren())
                {
                    Livre dataLivre = item.getValue(Livre.class);
                    list_livre.add(dataLivre);
                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this,UploadActivity.class);
                startActivity(intent);
            }
        });

    }




    public void searchList(String text)
    {
        ArrayList<Livre> searchLivre = new ArrayList<>();
        for (Livre livre : list_livre) {
            if (livre.getTitre().toLowerCase().contains(text.toLowerCase())) {
                searchLivre.add(livre);
            }
        }

        myAdapter.searchLivreListe(searchLivre);
    }



}