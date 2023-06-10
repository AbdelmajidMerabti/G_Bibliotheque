package com.example.g_bibliotheque;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class LoadUtilisateurAdapter extends ArrayAdapter<Utilisateur> {

    public LoadUtilisateurAdapter(Context context, List<Utilisateur> utilisateurs) {
        super(context, 0, utilisateurs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_utilisateur, parent, false);
        }

        Utilisateur utilisateur = getItem(position);

        TextView textNom = convertView.findViewById(R.id.textViewNom);
        TextView textCin = convertView.findViewById(R.id.textViewCin);
        TextView textEmail = convertView.findViewById(R.id.textViewEmail);
        TextView textTelephone = convertView.findViewById(R.id.textViewTelephone);
        TextView textDateNaissance = convertView.findViewById(R.id.textViewDateNaissance);
        TextView textPassword = convertView.findViewById(R.id.textViewMotDePasse);
        Button boutonModifier = convertView.findViewById(R.id.bouton_modifier);
        textCin.setText(utilisateur.getCin());
        textNom.setText(utilisateur.getNom());
        textEmail.setText(utilisateur.getEmail());
        textTelephone.setText(utilisateur.getTelephone());
        textDateNaissance.setText(utilisateur.getDateNaissance());
        textPassword.setText(utilisateur.getPassword());
        boutonModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ModifierUtilisateurActivity.class);
              //  intent.putExtra("utilisateur", utilisateur);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}