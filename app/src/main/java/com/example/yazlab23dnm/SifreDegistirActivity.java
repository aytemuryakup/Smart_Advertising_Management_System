package com.example.yazlab23dnm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class SifreDegistirActivity extends AppCompatActivity {

    Button sifredegis;
    EditText email_sıfırla;

    FirebaseAuth firebaseAuth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);

        email_sıfırla = findViewById(R.id.sifirla_email);
        sifredegis = findViewById(R.id.sifre_degistir);
        firebaseAuth1 = FirebaseAuth.getInstance();

        sifredegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mailsıfırla = email_sıfırla.getText().toString();

                if(mailsıfırla.equals("")){

                    Toast.makeText(SifreDegistirActivity.this, "Email alanını doldurmak zorunludur.", Toast.LENGTH_SHORT).show();
                }
                else{

                    firebaseAuth1.sendPasswordResetEmail(mailsıfırla).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(SifreDegistirActivity.this, "E-mail Adresinizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SifreDegistirActivity.this,GirisActivity.class));
                            }
                            else
                            {
                                String error = task.getException().getMessage();
                                Toast.makeText(SifreDegistirActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


}


