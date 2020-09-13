package com.helping.foodservices;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CoordinatorLoginActivity extends AppCompatActivity {

    TextInputEditText coemailphonenumber,coopassword;
    Button coosignin, coosignup;

    FirebaseAuth mAuth;
    private FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_login);

        coemailphonenumber = (TextInputEditText) findViewById(R.id.cooemailphonenumber);
        coopassword = (TextInputEditText) findViewById(R.id.coopassword);

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();

        coosignin = (Button) findViewById(R.id.coosignin);

        coosignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = coemailphonenumber.getText().toString().trim();
                String pass = coopassword.getText().toString().trim();

                if(TextUtils.isEmpty(emailid)){
                    Toast.makeText(getApplicationContext(),"Please enter emailId",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.length()<6){
                    Toast.makeText(getApplicationContext(),"Password too short",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailid,pass).addOnCompleteListener(CoordinatorLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),CooMainActivity.class));
                        }else {
                            Toast.makeText(CoordinatorLoginActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        coosignup = (Button) findViewById(R.id.coosignup);

        coosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CoordinatorLoginActivity.this,CoordinatorRegistrationActivity.class);
                startActivity(i);
            }
        });
    }


}
