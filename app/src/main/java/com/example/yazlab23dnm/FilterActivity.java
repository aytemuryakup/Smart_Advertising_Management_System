package com.example.yazlab23dnm;

import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    Button filtrele;
    TextView filtreciktisi;
    ArrayList<Firma_Bilgileri> firmaBilgileriArrayList = new ArrayList<>();


    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("Firma_Bilgileri");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        filtrele = findViewById(R.id.button);
        filtreciktisi = findViewById(R.id.textView2);


        final String[] cikti = {""};

        filtrele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            Firma_Bilgileri fb = new Firma_Bilgileri();
                            fb = singleSnapshot.getValue(Firma_Bilgileri.class);
                            cikti[0] +=(fb.FirmaID+" "+fb.FirmaAdi+" "+fb.Latitude_en+" "+fb.Longitude_boy+" "+
                                    fb.Turu+" "+fb.KampanyaIcerik+" "+fb.KampanyaZaman + "\n");
                            firmaBilgileriArrayList.add(fb);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                filtreciktisi.setText(cikti[0]);


            }
        });
    }
}