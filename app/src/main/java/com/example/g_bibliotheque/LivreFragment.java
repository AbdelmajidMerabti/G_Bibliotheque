package com.example.g_bibliotheque;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LivreFragment extends Fragment {

    private RecyclerView recyclerView;
    private LivreDemandeAdapter adapter;
    private List<Demande> demandes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_livre, container, false);

        if(MyGlobals.getNom().isEmpty())
        {
            startActivity(new Intent(getActivity(),MainActivity3.class));
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        demandes = new ArrayList<>();
        adapter = new LivreDemandeAdapter(demandes);
        recyclerView.setAdapter(adapter);

        // Récupérer les demandes de l'utilisateur actuel depuis Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Query query = database.child("Demande").orderByChild("utilisateurId").equalTo(MyGlobals.getKey());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                demandes.clear();
                for (DataSnapshot demandeSnapshot : snapshot.getChildren()) {
                    Demande demande = demandeSnapshot.getValue(Demande.class);
                    //  if (demande.getStatusDemande() != 2) {
                    demandes.add(demande);
                    //}
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MainActivity", "loadDemandes:onCancelled", error.toException());
            }
        });

        return view;
    }



}