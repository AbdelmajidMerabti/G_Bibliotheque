package com.example.g_bibliotheque;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UtilisateurAdapter extends ArrayAdapter<Utilisateur> {

    public UtilisateurAdapter(Context context, ArrayList<Utilisateur> utilisateurs) {
        super(context, 0, utilisateurs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Récupérer l'élément de la ListView correspondant à la position spécifiée
        Utilisateur utilisateur = getItem(position);

        // Vérifier si une vue existante est réutilisée, sinon créer une nouvelle vue
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_utilisateur, parent, false);
        }

        // Récupérer les vues de la vue mise à jour
        TextView textViewNom = convertView.findViewById(R.id.textViewNom);
        TextView textViewCin = convertView.findViewById(R.id.textViewCin);
        TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
        TextView textViewDateNaissance = convertView.findViewById(R.id.textViewDateNaissance);
        TextView textViewType = convertView.findViewById(R.id.textViewType);
        TextView textViewPassword = convertView.findViewById(R.id.textViewPassword);
        TextView textViewTelephone = convertView.findViewById(R.id.textViewTelephone);
        ImageView editButton = convertView.findViewById(R.id.buttonEdit);
        ImageView deleteButton = convertView.findViewById(R.id.buttonDelete);

        // Afficher les informations de l'utilisateur dans les vues
        textViewNom.setText("Nom: " + utilisateur.getNom());
        textViewCin.setText("CIN: " + utilisateur.getCin());
        textViewEmail.setText("Email: " + utilisateur.getEmail());
        textViewDateNaissance.setText("Date de naissance: " + utilisateur.getDateNaissance());
        textViewType.setText("Type: " + utilisateur.getType());
        textViewPassword.setText("Password: " + utilisateur.getPassword());
        textViewTelephone.setText("Telephone: " + utilisateur.getTelephone());

        // Définir les actions à effectuer lorsque les boutons "Edit" et "Delete" sont cliqués
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir une nouvelle activité pour éditer les données de l'utilisateur
               Intent intent = new Intent(getContext(), EditUtilisateurActivity.class);
                intent.putExtra("utilisateurId", utilisateur.getKey());
                getContext().startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Supprimer l'utilisateur de la base de données Firebase
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Utilisateurs").child(utilisateur.getKey());
                databaseRef.removeValue();
            }
        });

        // Retourner la vue mise à jour
        return convertView;
    }
}
