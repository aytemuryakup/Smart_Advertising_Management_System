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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {

    EditText edt_adi, edt_sifre,edt_mail;

    Button btn_kaydol;

    FirebaseAuth yetki;
    DatabaseReference yol;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        edt_adi = findViewById(R.id.username);
        edt_mail = findViewById(R.id.email);
        edt_sifre = findViewById(R.id.password);
        btn_kaydol = findViewById(R.id.btn_kayit_activity);

        yetki = FirebaseAuth.getInstance();

        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(KayitActivity.this);
                pd.setMessage("Lütfen bekleyin");
                pd.show();

                String str_kullanici = edt_adi.getText().toString();
                String str_mail = edt_mail.getText().toString();
                String str_sifre = edt_sifre.getText().toString();

                if(TextUtils.isEmpty(str_kullanici)|| TextUtils.isEmpty(str_mail)||TextUtils.isEmpty(str_sifre)){

                    Toast.makeText(KayitActivity.this, "Lütfen Bütün alanları doldurun.", Toast.LENGTH_SHORT).show();
                }
                else {

                    //Yeni Kullanıcı kaydetme kodlarını classdan çağıracağız.
                    kaydet(str_kullanici,str_mail,str_sifre);
                }

            }
        });
    }

    private void kaydet(final String kullaniciadi, final String emailadresi , String sifresi)
    {

        yetki.createUserWithEmailAndPassword(emailadresi,sifresi)
                .addOnCompleteListener(KayitActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebasekullanici = yetki.getCurrentUser();
                            String kullaniciid = firebasekullanici.getUid();
                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar").child(kullaniciid);

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",kullaniciid);
                            hashMap.put("kullaniciadi",kullaniciadi.toLowerCase());
                            hashMap.put("bio","");
                            hashMap.put("resim_url","");

                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    pd.dismiss();

                                    Intent intent = new Intent(KayitActivity.this, MapsActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                                }
                            });

                        }

                        else
                        {
                            pd.dismiss();
                            Toast.makeText(KayitActivity.this, "Bu mail ile kayit basarisiz", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
