package com.example.g_bibliotheque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Categorie> {
    public CategoryAdapter(Context context, List<Categorie> categories) {
        super(context, 0, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Categorie category = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false);
        }

        TextView labelTextView = convertView.findViewById(R.id.category_label);
        labelTextView.setText(category.getLibelle());

        return convertView;
    }
}
