package com.example.g_bibliotheque;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Details extends AppCompatActivity {

    EditText prix,qte;
    Button button;

    String key,imageR;
    String ti,des,cat;
    int qt,qtR;
    DatabaseReference referenceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        prix = findViewById(R.id.prix1);
        qte = findViewById(R.id.qte1);
        button = findViewById(R.id.buttonDetail);

        TextView textView1 = findViewById(R.id.textView1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Details.this,MainActivity4.class));
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            key = bundle.getString("key");
            imageR = bundle.getString("image");
            ti = bundle.getString("titre");
            des = bundle.getString("description");
            cat = bundle.getString("categorie");
            qt = bundle.getInt("qte");
            qtR = bundle.getInt("qteR");
        }

        Toast.makeText(Details.this, cat+", "+String.valueOf(qt)+","+String.valueOf(qtR), Toast.LENGTH_SHORT).show();




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


           //     Toast.makeText(Details.this, String.valueOf(qt), Toast.LENGTH_SHORT).show();

                        FirebaseDatabase reference = FirebaseDatabase.getInstance();
                        String detailId;
                        referenceDetail = reference.getReference("Details");
                        DatabaseReference newChildRef1 = referenceDetail.push();
                        detailId = newChildRef1.getKey();
                        DateTimeFormatter dtf = null;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            LocalDateTime now = LocalDateTime.now();
                            dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
                            String customFormat = dtf.format(now);
                            Detail detail = new Detail(detailId, customFormat, Integer.parseInt(qte.getText().toString()), key, Double.parseDouble(prix.getText().toString()));
                            // String detailId = newChildRef1.getKey();
                            referenceDetail.child(detailId).setValue(detail);

                        }
                      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Livre").child(key);
                        //qte.setText(Integer.parseInt(qte.getText().toString()) + Integer.parseInt(donnee1));
                        Livre dataClass = new Livre(key, imageR, ti, des, cat, qt + Integer.parseInt(qte.getText().toString()),qtR + Integer.parseInt(qte.getText().toString()));
                        databaseReference.setValue(dataClass);
                        startActivity(new Intent(Details.this,MainActivity4.class));

                    }
                });

    }
}