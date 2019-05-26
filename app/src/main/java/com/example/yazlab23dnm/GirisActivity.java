package com.example.yazlab23dnm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GirisActivity extends AppCompatActivity {


    EditText edt_email_giris, edt_sifre_giris;
    TextView txt_sifresayfasinagit;

    Button btn_girisyap;
    FirebaseAuth girisyetkisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        edt_email_giris = findViewById(R.id.email_giris);
        edt_sifre_giris = findViewById(R.id.password_giris);
        btn_girisyap = findViewById(R.id.btn_giris_activity);

        girisyetkisi = FirebaseAuth.getInstance();

        txt_sifresayfasinagit = findViewById(R.id.sifre_yonlendirme);

        txt_sifresayfasinagit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GirisActivity.this,SifreDegistirActivity.class));
            }
        });


        btn_girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog pdGiris = new ProgressDialog(GirisActivity.this);
                pdGiris.setMessage("Giris Yapılıyor");
                pdGiris.show();

                String str_emailgiris = edt_email_giris.getText().toString();
                String str_sifregiris = edt_sifre_giris.getText().toString();

                if(TextUtils.isEmpty(str_emailgiris) || TextUtils.isEmpty(str_sifregiris))
                {
                    Toast.makeText(GirisActivity.this, "Gerekli alanları doldurunuz!!!", Toast.LENGTH_SHORT).show();
                }

                else
                {
                     //Giris Yapma Kodları
                    girisyetkisi.signInWithEmailAndPassword(str_emailgiris,str_sifregiris)
                            .addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                        DatabaseReference yolGiris = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar")
                                                .child(girisyetkisi.getCurrentUser().getUid());
                                        yolGiris.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pdGiris.dismiss();

                                                Intent intent = new Intent(GirisActivity.this, MapsActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pdGiris.dismiss();

                                            }
                                        });
                                    }

                                    else
                                    {
                                        pdGiris.dismiss();
                                        Toast.makeText(GirisActivity.this, "Giris Basarisiz.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }



            }
        });
    }
}
