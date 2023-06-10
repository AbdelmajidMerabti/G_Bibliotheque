package com.example.g_bibliotheque;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LivreDemandeAdapter extends RecyclerView.Adapter<LivreDemandeAdapter.ViewHolder> {

    private List<Demande> demandes;
    private DatabaseReference database;
    String keyLivre;
    int qteR;

    public LivreDemandeAdapter(List<Demande> demandes) {
        this.demandes = demandes;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_livre_demande, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Demande demande = demandes.get(position);

        database = FirebaseDatabase.getInstance().getReference().child("Livre");
        Query query = database.orderByChild("key").equalTo(demande.getLivreId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot demandeSnapshot : snapshot.getChildren()) {
                        Livre livre = demandeSnapshot.getValue(Livre.class);
                        if (demande != null) {
                            holder.titreTextView.setText("Titre : "+livre.getTitre());
                     //       holder.descriptionTextView.setText("Description : "+livre.getDescription());
                            holder.categorieTextView.setText("Categorie : "+livre.getCategorie());
                            qteR = livre.getQteR();
                        } else {
                            Log.d("TAG", "Livre n'existe pas dans la base de données.");
                        } }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BookViewHolder", "Erreur lors de la vérification de la demande de livre", error.toException());
            }
        });

        // Récupérer les informations du livre associé à cette demande
      /*  database.child("Livre").child(demande.getLivreId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Livre livre = snapshot.getValue(Livre.class);

                // Afficher les informations dans l'interface utilisateur
                if (livre != null) {
                    holder.titreTextView.setText(livre.getTitre());
                    holder.descriptionTextView.setText(livre.getDescription());
                    holder.categorieTextView.setText(livre.getCategorie());
                    holder.qteTextView.setText(String.valueOf(livre.getQte()));
                } else {
                    Log.d("TAG", "Livre n'existe pas dans la base de données.");
                }
            }
*/


        // Afficher la date de la demande et le statut dans l'interface utilisateur
       // holder.qteTextView.setText(String.valueOf(demande.getQteDemande()));
        holder.dateDemandeTextView.setText("Date Demande : "+demande.getDateDemande());
       // holder.statusTextView.setText(String.valueOf(demande.getStatusDemande()));
        keyLivre = demande.getLivreId();


        // Si le statut est "0" ou "1", afficher un bouton "Supprimer"
        if (demande.getStatusDemande() == 0) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                // Supprimer la demande de la base de données Firebase
                //    Toast.makeText(v.getContext(), demande.getDemandeId(), Toast.LENGTH_SHORT).show();
                    // Récupération de la référence de la demande à supprimer
                    DatabaseReference demandeRef = FirebaseDatabase.getInstance().getReference("Demande").child(demande.getDemandeId());

// Suppression de la demande de la table Firebase Realtime Database
                    demandeRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // La demande a été supprimée avec succès
                                    DatabaseReference livresRef = FirebaseDatabase.getInstance().getReference("Livre");
                                    int qteDispo = qteR+1;
                                    livresRef.child(keyLivre).child("qteR").setValue(qteDispo);
                                    Toast.makeText(v.getContext(), "La demande a été supprimée avec succès", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // La suppression de la demande a échoué
                                    Toast.makeText(v.getContext(), "La suppression de la demande a échoué", Toast.LENGTH_SHORT).show();
                                }
                            });

                    //   database.child("Demande").child(demande.getDemandeId()).removeValue();
            }
        });
    } else {
        holder.deleteButton.setVisibility(View.GONE);
    }
}

    @Override
    public int getItemCount() {
        return demandes.size();
    }

public static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView titreTextView;
    //public TextView descriptionTextView;
    public TextView categorieTextView;
 //   public TextView qteTextView;
    public TextView dateDemandeTextView;
  //  public TextView statusTextView;
    public ImageView deleteButton;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        titreTextView = itemView.findViewById(R.id.titreTextView);
      //  descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        categorieTextView = itemView.findViewById(R.id.categorieTextView);
       // qteTextView = itemView.findViewById(R.id.qteTextView);
        dateDemandeTextView = itemView.findViewById(R.id.dateDemandeTextView);
       // statusTextView = itemView.findViewById(R.id.statusTextView);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
}

