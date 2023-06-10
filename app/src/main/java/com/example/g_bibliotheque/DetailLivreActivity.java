package com.example.g_bibliotheque;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DetailLivreActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView image;
    TextView titre,description,categorie,qte;
    String key,imageR,ti,des,cat;
    int qt,qtR;
    RecyclerView recyclerView;
    List<Detail> list_detail;
    DatabaseReference reference;
    ValueEventListener eventListener;
    AdapterDetail myAdapter;
    DatabaseReference referenceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_livre);
        image = findViewById(R.id.my_image);
        titre = findViewById(R.id.edit_text_titre);
        description = findViewById(R.id.edit_text_description);
        categorie = findViewById(R.id.edit_text_Categorie);
        qte = findViewById(R.id.edit_text_Qte);
        fab = findViewById(R.id.fab);
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailLivreActivity.this,MainActivity4.class));
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            key = bundle.getString("key");
            imageR = bundle.getString("image");
            Glide.with(this).load(bundle.getString("image")).into(image);
            ti = bundle.getString("titre");
            titre.setText(ti);
            des = bundle.getString("description");
            description.setText(des);
            cat = bundle.getString("categorie");
            categorie.setText(cat);
            qt = bundle.getInt("qte");
            qte.setText(String.valueOf(qt));
            qtR = bundle.getInt("qteR");
        }


        recyclerView = findViewById(R.id.my_list_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder bulider = new AlertDialog.Builder(this);
        bulider.setCancelable(false);
        bulider.setView(R.layout.progress_layout);
        AlertDialog dialog = bulider.create();
        dialog.show();
        list_detail = new ArrayList<>();
        myAdapter = new AdapterDetail(this,list_detail);
        recyclerView.setAdapter(myAdapter);
        reference = FirebaseDatabase.getInstance().getReference().child("Details");
        dialog.show();
        reference.orderByChild("livreId").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_detail.clear();
                for (DataSnapshot item : snapshot.getChildren())
                {
                    Detail dataDetail = item.getValue(Detail.class);
                    list_detail.add(dataDetail);
                }
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailLivreActivity.this,Details.class);
                intent.putExtra("key",key);
                intent.putExtra("image",imageR);
                intent.putExtra("titre",ti);
                intent.putExtra("description",des);
                intent.putExtra("categorie",cat);
                intent.putExtra("qte",qt);
                intent.putExtra("qteR",qtR);
                startActivity(intent);
            }
        });
    }
}