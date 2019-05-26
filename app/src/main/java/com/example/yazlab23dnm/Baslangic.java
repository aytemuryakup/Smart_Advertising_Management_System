package com.example.yazlab23dnm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Baslangic extends AppCompatActivity {

    Button btn_baslangicgiris, btn_baslangickaydol;

    FirebaseUser baslangickullanici;

    @Override
    protected void onStart() {
        super.onStart();

        baslangickullanici = FirebaseAuth.getInstance().getCurrentUser();

        //Eğer Kullanıcı Veri tabanında varsa direk anasayfaya gönder.
       if(baslangickullanici !=  null)
        {
            startActivity(new Intent(Baslangic.this, MapsActivity.class ));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baslangic);

        btn_baslangicgiris = findViewById(R.id.btn_giris);
        btn_baslangickaydol = findViewById(R.id.btn_kayit);

        btn_baslangicgiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Baslangic.this, GirisActivity.class));
            }
        });

        btn_baslangickaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Baslangic.this, KayitActivity.class));
            }
        });
    }
}
