package com.example.g_bibliotheque;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.List;

public class AdapterDetail extends RecyclerView.Adapter<MyViewHolder1> {

    private Context context;
    private List<Detail> listDetail;
    DatabaseReference mDatabase;
    public static int valeur = 0;
    int qteG = 0;
    public AdapterDetail(Context context, List<Detail> listDetail) {
        this.context = context;
        this.listDetail = listDetail;
    }


    @NonNull
    @Override
    public MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detaillivre_item,parent,false);
        return new MyViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder1 holder, int position) {
        holder.prix.setText(String.valueOf((listDetail.get(position).getPrix())));
        holder.qte.setText(String.valueOf(listDetail.get(position).getQte()));
        holder.date.setText(listDetail.get(position).getDate());
        int qte = listDetail.get(position).getQte();
        String detailKey = listDetail.get(position).getKey();
        String livreKey = listDetail.get(position).getLivreId();

        holder.btnDetailDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Récupérer la position de l'élément dans la liste
                int itemPosition = holder.getAdapterPosition();

                // Récupérer la clé du détail à supprimer

          //      Toast.makeText(context, String.valueOf(qte), Toast.LENGTH_SHORT).show();

                // Supprimer le détail de la base de données Firebase
               mDatabase = FirebaseDatabase.getInstance().getReference("Details");
                mDatabase.child(detailKey).removeValue();

                // Mettre à jour la quantité de la table "livre"
            //    String livreKey = getItem(itemPosition).getLivreKey();
              //  int qte = getItem(itemPosition).getQte();
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query query = mDatabase.child("Livre").orderByKey().equalTo(livreKey);

// Ajout d'un écouteur pour obtenir les résultats de la recherche
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // La clé (key) existe dans la table "livre"
                            DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                            int currentqte = snapshot.child("qte").getValue(Integer.class);
                            int currentqteR = snapshot.child("qteR").getValue(Integer.class);
                            int updatedQte = currentqte - qte;
                            int updatedQteR = currentqteR - qte;
                           // Toast.makeText(context, String.valueOf(currentqte)+"-"+String.valueOf(qte)+" = "+String.valueOf(updatedQte), Toast.LENGTH_SHORT).show();
                            mDatabase.child("Livre").child(livreKey).child("qte").setValue(updatedQte);
                            mDatabase.child("Livre").child(livreKey).child("qteR").setValue(updatedQteR);
                            context.startActivity(new Intent(context,MainActivity4.class));
                        } else {
                            // La clé (key) n'existe pas dans la table "livre"
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Erreur lors de la récupération des données
                    }
                });
               /* mDatabase.child(livreKey).child("qte").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Livre currentQte = dataSnapshot.getValue(Livre.class);
                        Toast.makeText(context, String.valueOf(currentQte.getQte()), Toast.LENGTH_SHORT).show();
                     //   int updatedQte = currentQte.getQte() - qte;

                    //    mDatabase.child("Livre").child(livreKey).child("qte").setValue(updatedQte);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "Error updating livre quantity", databaseError.toException());
                    }
                });*/
            }
        });


// ...




  /*      holder.btnDetailAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de la AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Ajouter dans Firebase");

                // Création des deux EditText pour entrer les données
                final EditText editText1 = new EditText(v.getContext());
                editText1.setHint("Entrez la première donnée");
                final EditText editText2 = new EditText(v.getContext());
                editText2.setHint("Entrez la deuxième donnée");

                // Ajout des EditText dans la AlertDialog
                LinearLayout linearLayout = new LinearLayout(v.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText1);
                linearLayout.addView(editText2);
                builder.setView(linearLayout);

                // Ajout du bouton pour ajouter dans Firebase
                builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Récupération des données entrées dans les EditText
                        String donnee1 = editText1.getText().toString();
                        String donnee2 = editText2.getText().toString();
                        Toast.makeText(context,donnee1+","+donnee2,Toast.LENGTH_SHORT).show();
                        // Ajout des données dans Firebase
                       /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference newReference = databaseReference.push();
                        newReference.child("donnee1").setValue(donnee1);
                        newReference.child("donnee2").setValue(donnee2);
                    }
                });

                // Ajout du bouton pour annuler
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Affichage de la AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
*/



    }



    @Override
    public int getItemCount() {
        return listDetail.size();
    }


}
class  MyViewHolder1 extends RecyclerView.ViewHolder{

    TextView prix,qte,date;
    RelativeLayout cardview;
    ImageView btnDetailDelete;

    public MyViewHolder1(@NonNull View itemView) {
        super(itemView);
        prix = itemView.findViewById(R.id.prix);
        qte = itemView.findViewById(R.id.qte);
        date = itemView.findViewById(R.id.date);
        cardview = itemView.findViewById(R.id.cardview);
        btnDetailDelete = itemView.findViewById(R.id.btnDetailDelete);
    }
}


