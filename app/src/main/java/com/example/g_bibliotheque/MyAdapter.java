package com.example.g_bibliotheque;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<Livre> list_livre;

    public MyAdapter(Context context, List<Livre> list_livre) {
        this.context = context;
        this.list_livre = list_livre;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list_livre.get(position).getImage()).into(holder.recImage);
        holder.recTitre.setText(list_livre.get(position).getTitre());
        holder.recDescription.setText(list_livre.get(position).getDescription());
        holder.recCategorie.setText(list_livre.get(position).getCategorie());
        holder.recQte.setText(String.valueOf(list_livre.get(position).getQte()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String imageUrl = list_livre.get(holder.getAdapterPosition()).getImage();
                 String key = list_livre.get(holder.getAdapterPosition()).getKey();
                Toast.makeText(context, key+" : "+imageUrl, Toast.LENGTH_SHORT).show();
                DatabaseReference detailRef = FirebaseDatabase.getInstance().getReference("Details");
                Query query = detailRef.orderByChild("livreId").equalTo(key);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot detailSnapshot: dataSnapshot.getChildren()) {
                            detailSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }
                });


                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete();

                DatabaseReference livreRef = FirebaseDatabase.getInstance().getReference("Livre").child(key);
                livreRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Livre deleted successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error deleting livre", e);
                    }
                });





            }
        });
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailLivreActivity.class);
                intent.putExtra("key",list_livre.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("image",list_livre.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("titre",list_livre.get(holder.getAdapterPosition()).getTitre());
                intent.putExtra("description",list_livre.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("categorie",list_livre.get(holder.getAdapterPosition()).getCategorie());
                intent.putExtra("qte",list_livre.get(holder.getAdapterPosition()).getQte());
                intent.putExtra("qteR",list_livre.get(holder.getAdapterPosition()).getQteR());
                context.startActivity(intent);

            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditLivreActivity.class);
                intent.putExtra("key",list_livre.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("image",list_livre.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("titre",list_livre.get(holder.getAdapterPosition()).getTitre());
                intent.putExtra("description",list_livre.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("categorie",list_livre.get(holder.getAdapterPosition()).getCategorie());
                intent.putExtra("qte",list_livre.get(holder.getAdapterPosition()).getQte());
                intent.putExtra("qteR",list_livre.get(holder.getAdapterPosition()).getQteR());
                context.startActivity(intent);

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


}
     class  MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage,btnEdit,btnDelete,btnDetail;
    TextView recTitre,recDescription,recCategorie,recQte;
    RelativeLayout recRealtive;

         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             btnEdit = itemView.findViewById(R.id.btnEdit);
             btnDelete = itemView.findViewById(R.id.btnDelete);
             btnDetail = itemView.findViewById(R.id.btnDetail);
             recImage = itemView.findViewById(R.id.imageView);
             recTitre = itemView.findViewById(R.id.recTitre);
             recDescription = itemView.findViewById(R.id.recDescription);
             recCategorie = itemView.findViewById(R.id.recCategorie);
             recQte = itemView.findViewById(R.id.recQte);
             recRealtive = itemView.findViewById(R.id.recRelative);
         }
     }

