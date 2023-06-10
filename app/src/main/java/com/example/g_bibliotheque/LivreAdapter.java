package com.example.g_bibliotheque;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LivreAdapter extends RecyclerView.Adapter<MyViewHolder2> {

    private Context context;
    private List<Livre> list_livre;

    public LivreAdapter(Context context, List<Livre> list_livre) {
        this.context = context;
        this.list_livre = list_livre;
    }


    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livre_item,parent,false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder2 holder, int position) {
        holder.titreTextView.setText("Titre : " + list_livre.get(position).getTitre());
        holder.description_textview.setText("Description : " + list_livre.get(position).getDescription());
        holder.categorieTextView.setText("Categorie : " + list_livre.get(position).getCategorie());
        holder.qteTextView.setText("Qte : " + list_livre.get(position).getQteR() + "/" + list_livre.get(position).getQte());
        String keyLivre = list_livre.get(position).getKey();
        int qteR = list_livre.get(position).getQteR();
        holder.demanderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifierDemandeDeLivre(keyLivre,qteR);


                /**/


            }
        });
    }


            /*    DatabaseReference demandeRef = FirebaseDatabase.getInstance().getReference().child("Demander");
                String utilisateurKey = MyGlobals.getKey();
                String demandeKey = demandeRef.push().getKey();
                DateTimeFormatter dtf = null;

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
                String date = sdf.format(now);


                // Ajouter la demande dans la table "Demander"
                HashMap<String, Object> demandeData = new HashMap<>();
                demandeData.put("keyLivre", keyLivre);
                demandeData.put("keyUtilisateur", utilisateurKey);
                demandeData.put("qte", 1);
                demandeData.put("date", date);
                demandeData.put("status", 0);
                demandeRef.child(demandeKey).setValue(demandeData);


                // Mettre à jour la quantité de livre dans la table "Livres"
                DatabaseReference livreRef = FirebaseDatabase.getInstance().getReference().child("Livre").child(keyLivre);
                livreRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Livre livre = mutableData.getValue(Livre.class);
                        if (livre == null) {
                            return Transaction.abort();
                        }

                        livre.setQteR(livre.getQteR() - 1);
                        mutableData.setValue(livre);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        // Rien à faire ici
                    }
                });
*/

    private void verifierDemandeDeLivre(String KeyLivre,int qteR) {
        DatabaseReference demandeRef = FirebaseDatabase.getInstance().getReference().child("Demande");
        Query query = demandeRef.orderByChild("utilisateurId").equalTo(MyGlobals.getKey());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot demandeSnapshot : snapshot.getChildren()) {
                        Demande demande = demandeSnapshot.getValue(Demande.class);
                        if (demande != null) {
                            if (demande.getStatusDemande() == 0 || demande.getStatusDemande() == 1) {
                                // L'utilisateur a déjà demandé le livre
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Déjà demandé")
                                        .setMessage("Vous avez déjà fait une demande pour ce livre et elle est en attente de traitement.")
                                        .setPositiveButton("OK", null)
                                        .show();
                                return;
                            }
                            else
                            {
                                // L'utilisateur n'a pas encore demandé le livre, vous pouvez enregistrer une nouvelle demande

                            }
                        }
                    }
                }
                enregistrerDemandeDeLivre(KeyLivre,qteR);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("BookViewHolder", "Erreur lors de la vérification de la demande de livre", error.toException());
            }
        });
    }


    public void enregistrerDemandeDeLivre(String keyLivre, int qteR) {
        DatabaseReference demandeRef = FirebaseDatabase.getInstance().getReference().child("Demande");
        DatabaseReference livresRef = FirebaseDatabase.getInstance().getReference("Livre");
        String nouvelleDemandeId = demandeRef.push().getKey();
        String dateDemande = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
        Demande nouvelleDemande = new Demande(nouvelleDemandeId, keyLivre, MyGlobals.getKey(), 1 , dateDemande, 0);
        demandeRef.child(nouvelleDemandeId).setValue(nouvelleDemande);
        int qteDispo = qteR - 1;
        livresRef.child(keyLivre).child("qteR").setValue(qteDispo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Demande ajoutée avec succès")
                        .setMessage("Votre demande a été ajoutée avec succès!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return list_livre.size();
    }

    public void searchLivreListe(ArrayList<Livre> search_livre)
    {
        list_livre = search_livre;
        notifyDataSetChanged();
    }

    private void afficherAlertDialog(final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Fermer l'alerte et retourner à l'activité précédente
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }




}
class  MyViewHolder2 extends RecyclerView.ViewHolder{

    TextView titreTextView,description_textview,categorieTextView,qteTextView;
    Button demanderButton;
    public MyViewHolder2(@NonNull View itemView) {
        super(itemView);

         titreTextView = itemView.findViewById(R.id.titre_textview);
        description_textview = itemView.findViewById(R.id.description_textview);
         categorieTextView = itemView.findViewById(R.id.categorie_textview);
         qteTextView = itemView.findViewById(R.id.qte_textview);
        demanderButton = itemView.findViewById(R.id.demanderButton);
    }
}

