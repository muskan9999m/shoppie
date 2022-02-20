package com.example.shoppie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class
MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = auth.getCurrentUser();
        if (currentuser == null) {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
            finish();
        } else {
            FirebaseFirestore.getInstance().collection("Users").document(currentuser.getUid()).update("Last Seen", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(i);
                        finish();
                    } else {
                        String e = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(), e, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();


    }
}