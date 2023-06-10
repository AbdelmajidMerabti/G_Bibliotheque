package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DemandeStatus2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DemandeAdapter1 demandeAdapter;
    List<Demande> list_demande;
    DatabaseReference reference;
    ValueEventListener eventListener;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_status2);

        recyclerView = findViewById(R.id.recyclerview1);
        searchView = findViewById(R.id.search1);
        TextView textView = findViewById(R.id.textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DemandeStatus2.this,ListDemandeActivity.class));
            }
        });
        // searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DemandeStatus2.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder bulider = new AlertDialog.Builder(DemandeStatus2.this);
        bulider.setCancelable(false);
        bulider.setView(R.layout.progress_layout);
        AlertDialog dialog = bulider.create();
        dialog.show();
        list_demande = new ArrayList<>();
        // list_livre.clear();
        demandeAdapter = new DemandeAdapter1(DemandeStatus2.this,list_demande);
        recyclerView.setAdapter(demandeAdapter);
        reference = FirebaseDatabase.getInstance().getReference().child("Demande");
        dialog.show();
        eventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_demande.clear();
                for (DataSnapshot item : snapshot.getChildren())
                {

                    Demande dataDemande = item.getValue(Demande.class);
                    if(dataDemande!=null) {

                        if(dataDemande.getStatusDemande() == 1) {
                            list_demande.add(dataDemande);
                        }
                    }

                }
                demandeAdapter.notifyDataSetChanged();
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
                if(!newText.isEmpty()) {
                    demandeAdapter.searchDemandeList(newText);
                }
                else {
                    demandeAdapter = new DemandeAdapter1(DemandeStatus2.this, list_demande);
                    recyclerView.setAdapter(demandeAdapter);
                }
                return false;
            }
        });



    }







}