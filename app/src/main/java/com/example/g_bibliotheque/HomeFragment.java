package com.example.g_bibliotheque;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    TextView nom,item;
    ListView listView;

    @SuppressLint("MissingInflatedId")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        if(MyGlobals.getNom().isEmpty())
        {
            startActivity(new Intent(getActivity(),MainActivity3.class));
        }

        nom = view.findViewById(R.id.nom);

        listView = view.findViewById(R.id.categories_listview);

        Intent intent = getActivity().getIntent();
        String valeur = intent.getStringExtra("nom");
        String key = intent.getStringExtra("key");
        if(MyGlobals.getNom() == "" && MyGlobals.getKey() == "") {
            MyGlobals.setNom(valeur);
            MyGlobals.setKey(key);
        }
        nom.setText(MyGlobals.getNom());

        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("categorie");
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Categorie> categories = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Categorie category = categorySnapshot.getValue(Categorie.class);
                    categories.add(category);
                }

                CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories);

                // Passer l'adaptateur à la ListView

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Erreur lors de la récupération des catégories.", databaseError.toException());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Categorie selectedCategory = (Categorie) parent.getItemAtPosition(position);
                String label = selectedCategory.getLibelle();
                Intent intent = new Intent(getActivity(),ListLivreparCategorie.class);
                intent.putExtra("libelle", label);
                startActivity(intent);
            }
        });


        return view;
    }
}