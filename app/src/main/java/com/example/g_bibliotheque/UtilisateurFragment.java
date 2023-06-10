package com.example.g_bibliotheque;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UtilisateurFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_utilisateur, container, false);
         listView = view.findViewById(R.id.liste_utilisateurs);
        afficherListeUtilisateurs();
        return view;
    }


    private void afficherListeUtilisateurs() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Utilisateurs");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Utilisateur> utilisateurs = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                    if(utilisateur != null && utilisateur.getKey().equals(MyGlobals.getKey())) {
                        utilisateurs.add(utilisateur);
                    }
                }
                LoadUtilisateurAdapter adapter = new LoadUtilisateurAdapter(getContext(), utilisateurs);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gérer l'erreur
            }
        });
    }

}