package com.example.g_bibliotheque;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DemandeAdapter2 extends RecyclerView.Adapter<MyViewHolder9> {

    private Context context;
    private List<DemandeLivre> list_demande;




    public DemandeAdapter2(Context context, List<DemandeLivre> list_demande) {
        this.context = context;
        this.list_demande = list_demande;
    }


    @NonNull
    @Override
    public MyViewHolder9 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_demande2,parent,false);
        return new MyViewHolder9(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder9 holder, int position) {
        try {
        DemandeLivre demandeLivre = list_demande.get(position);

        holder.nomUtilisateurTextView.setText("Nom : "+demandeLivre.getNom());
        holder.cinTextView.setText("CIN : "+demandeLivre.getCin());
        holder.titreLivreTextView.setText("Titre : "+demandeLivre.getTitre());
        holder.categorieTextView.setText("Categorie : "+demandeLivre.getCategorie());
        holder.telephoneTextView.setText("Telephone : "+demandeLivre.getTelephone());
        holder.dateDemandeTextView.setText("Date Demande : "+demandeLivre.getDateDemande());

            holder.date_jour_text_view.setText("Jour : "+getDaysBetweenDates(demandeLivre.getDateDemande())+" Jour");

        holder.statusTextView.setText("Status : en attend");

        holder.EnvoiMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendEmailTask(demandeLivre.getEmail(),
                        "Récupérer un mot de passe",
                        "Bonjour Mr,\n" +demandeLivre.getNom()+
                                "\n" +
                                "Je voulais te rappeler que tu as maintenant plus de 4 jours de retard pour rendre le livre que tu as emprunté. Nous avons besoin de récupérer le livre pour qu'il soit disponible pour les autres étudiants qui en ont besoin."+
                                "\n" +
                                "Cordialement,").execute();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Information")
                        .setMessage("Email envoi avec success !!!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        holder.EnvoiSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String phoneNumber = "0615798659";
                    String message = "Bonjour \n Je voulais te rappeler que tu as maintenant plus de 4 jours de retard pour rendre le livre que tu as emprunté. Nous avons besoin de récupérer le livre pour qu'il soit disponible pour les autres étudiants qui en ont besoin.";
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phoneNumber, null, message, null, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Information")
                            .setMessage("SMS envoi avec success !!!\n" + phoneNumber + "\n" + message)
                            .setPositiveButton("OK", null)
                            .show();
            }
        });

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return list_demande.size();
    }

    public void searchLivreListe(ArrayList<DemandeLivre> search_livre)
   {
        list_demande = search_livre;
        notifyDataSetChanged();
    }


    public int getDaysBetweenDates(String date1Str) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date1 = dateFormat.parse(date1Str);
        Calendar calendar = Calendar.getInstance();
        Date date2 = calendar.getTime();
        long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
        return (int) diffInDays;
    }



}


class MyViewHolder9 extends RecyclerView.ViewHolder {
    public TextView nomUtilisateurTextView, titreLivreTextView, dateDemandeTextView,
            cinTextView, statusTextView, categorieTextView,telephoneTextView,date_jour_text_view;
    Button EnvoiSMS, EnvoiMail;

    public MyViewHolder9(@NonNull View itemView) {
        super(itemView);
        nomUtilisateurTextView = itemView.findViewById(R.id.nom_utilisateur_text_view);
        titreLivreTextView = itemView.findViewById(R.id.titre_livre_text_view);
        dateDemandeTextView = itemView.findViewById(R.id.date_demande_text_view);
        statusTextView = itemView.findViewById(R.id.status_text_view);
        EnvoiSMS = itemView.findViewById(R.id.EnvoiSMS);
        EnvoiMail = itemView.findViewById(R.id.EnvoiMail);
        date_jour_text_view = itemView.findViewById(R.id.date_jour_text_view);
        categorieTextView = itemView.findViewById(R.id.categorieTextView);
        cinTextView = itemView.findViewById(R.id.cin_text_view);
        telephoneTextView = itemView.findViewById(R.id.telephoneTextView);
    }
}

