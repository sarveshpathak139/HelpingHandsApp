package com.helping.foodservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button helper,coordinator;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;
    private DatabaseReference rootref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        rootref = FirebaseDatabase.getInstance().getReference();
        helper = (Button) findViewById(R.id.messhotelowner);
        coordinator = (Button) findViewById(R.id.coordinator);

        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,UserLoginActivity.class);
                startActivity(i);
            }
        });

        coordinator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CoordinatorLoginActivity.class);
                startActivity(i);
            }
        });
    }




}
