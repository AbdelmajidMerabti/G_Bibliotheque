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

public class DemandeStatus3 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DemandeAdapter2 demandeAdapter;
    ValueEventListener eventListener;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_status3);

        recyclerView = findViewById(R.id.recyclerview1);
        searchView = findViewById(R.id.search1);
        TextView textView = findViewById(R.id.textView1);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DemandeStatus3.this,ListDemandeActivity.class));
            }
        });
        // searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(DemandeStatus3.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        demandeAdapter = new DemandeAdapter2(DemandeStatus3.this,GlobalList.list_demandeLivre);
        recyclerView.setAdapter(demandeAdapter);



        DatabaseReference demandeRef = FirebaseDatabase.getInstance().getReference("Demande");
        DatabaseReference livreRef = FirebaseDatabase.getInstance().getReference("Livre");
        DatabaseReference utilisateurRef = FirebaseDatabase.getInstance().getReference("Utilisateurs");

        GlobalList.list_demandeLivre.clear(); // initialisez l'objet ici
        eventListener = demandeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot demandeSnapshot : dataSnapshot.getChildren()) {
                    try{
                    String livreId = demandeSnapshot.child("livreId").getValue(String.class);
                    String utilisateurId = demandeSnapshot.child("utilisateurId").getValue(String.class);
                    DemandeLivre demandLiv = new DemandeLivre(); // initialisez l'objet ici pour chaque demande
                    demandLiv.setDateDemande(demandeSnapshot.child("dateDemande").getValue(String.class));
                    Integer status = demandeSnapshot.child("statusDemande").getValue(Integer.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date dateDemandeObj = null;
                    dateDemandeObj = dateFormat.parse(demandeSnapshot.child("dateDemande").getValue(String.class));

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateDemandeObj);
                    cal.add(Calendar.DATE, 4);
                    Date limite = cal.getTime();
                    Date maintenant = new Date();
                    if (status == 1 && maintenant.after(limite)) {
                        livreRef.child(livreId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot livreSnapshot) {
                                demandLiv.setTitre(livreSnapshot.child("titre").getValue(String.class));
                                demandLiv.setCategorie(livreSnapshot.child("categorie").getValue(String.class));

                                utilisateurRef.child(utilisateurId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot utilisateurSnapshot) {
                                        demandLiv.setNom(utilisateurSnapshot.child("nom").getValue(String.class));
                                        demandLiv.setCin(utilisateurSnapshot.child("cin").getValue(String.class));
                                        demandLiv.setTelephone(utilisateurSnapshot.child("telephone").getValue(String.class));
                                        demandLiv.setEmail(utilisateurSnapshot.child("email").getValue(String.class));

                                        //  list_demandeLivre.add(demandLiv);
                                        GlobalList.list_demandeLivre.add(demandLiv);
                                        demandeAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle error
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle error
                            }
                        });
                    }
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    /*    TextView textView2 = findViewById(R.id.test);

        for(int i =0; i <GlobalList.list_demandeLivre.size(); i++)
        {

            textView2.setText(GlobalList.list_demandeLivre.get(0).getDateDemande());
        }
*/


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



    }


    public void searchList(String text)
    {
        ArrayList<DemandeLivre> searchDemandeLivre = new ArrayList<>();
        for (DemandeLivre demandeLivre : GlobalList.list_demandeLivre) {
            if (demandeLivre.getCin().toLowerCase().contains(text.toLowerCase())) {
                searchDemandeLivre.add(demandeLivre);
            }
        }

        demandeAdapter.searchLivreListe(searchDemandeLivre);
    }







}