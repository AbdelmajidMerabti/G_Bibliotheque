package com.example.g_bibliotheque;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DemandeAdapter1 extends RecyclerView.Adapter<MyViewHolder8> {

    private Context context;
    private List<Demande> list_demande;




    public DemandeAdapter1(Context context, List<Demande> list_demande) {
        this.context = context;
        this.list_demande = list_demande;
    }


    @NonNull
    @Override
    public MyViewHolder8 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande1,parent,false);
        return new MyViewHolder8(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder8 holder, int position) {

        DatabaseReference utilisateurRef = FirebaseDatabase.getInstance().getReference().child("Utilisateurs").child(list_demande.get(position).getUtilisateurId());
        utilisateurRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Utilisateur utilisateur = dataSnapshot.getValue(Utilisateur.class);
                if(utilisateur!=null) {
                    holder.nomUtilisateurTextView.setText("Nom : "+utilisateur.getNom());
                    holder.cinTextView.setText("CIN : "+utilisateur.getCin());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        DatabaseReference livreRef = FirebaseDatabase.getInstance().getReference().child("Livre").child(list_demande.get(position).getLivreId());
        livreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Livre livre = dataSnapshot.getValue(Livre.class);
                if(livre!=null)
                {
                    holder.titreLivreTextView.setText("Titre : "+livre.getTitre());
                    holder.categorieTextView.setText("Categorie : "+livre.getCategorie());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


        holder.dateDemandeTextView.setText("Date Demande : "+list_demande.get(position).getDateDemande());
        holder.statusTextView.setText("Status : en attend");


        String key = list_demande.get(position).getDemandeId();

        DatabaseReference demandeRef =  FirebaseDatabase.getInstance().getReference("Demande");
        holder.recupere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demandeRef.child(key).child("statusDemande").setValue(2);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list_demande.size();
    }

   /* public void searchLivreListe(ArrayList<Demande> search_livre)
   {
        list_demande = search_livre;
        notifyDataSetChanged();
    }*/

    public void searchDemandeList(String text) {
        ArrayList<Demande> searchDemande = new ArrayList<>();
        for (Demande demande : list_demande) {
            String utilisateurId = demande.getUtilisateurId();
            if (utilisateurId != null) {
                DatabaseReference livreRef = FirebaseDatabase.getInstance().getReference().child("Utilisateurs").child(utilisateurId);
                livreRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                        if (utilisateur != null && utilisateur.getCin().toLowerCase().contains(text.toLowerCase())) {
                            searchDemande.add(demande);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // handle error
                    }
                });
            }
        }
        list_demande = searchDemande;
        notifyDataSetChanged();
    }



}

class MyViewHolder8 extends RecyclerView.ViewHolder {
    public TextView nomUtilisateurTextView, titreLivreTextView, dateDemandeTextView,
            cinTextView, statusTextView, categorieTextView;
    Button recupere;

    public MyViewHolder8(@NonNull View itemView) {
        super(itemView);
        nomUtilisateurTextView = itemView.findViewById(R.id.nom_utilisateur_text_view);
        titreLivreTextView = itemView.findViewById(R.id.titre_livre_text_view);
        dateDemandeTextView = itemView.findViewById(R.id.date_demande_text_view);
        statusTextView = itemView.findViewById(R.id.status_text_view);
        recupere = itemView.findViewById(R.id.recupere);
        categorieTextView = itemView.findViewById(R.id.categorieTextView);
        cinTextView = itemView.findViewById(R.id.cin_text_view);
    }
}

