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

public class UserLoginActivity extends AppCompatActivity {

    private TextInputEditText emailphonenumber, password;
    Button userlogin,usignup;

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener firebaseAuthListner;
    private  FirebaseUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();


        emailphonenumber = (TextInputEditText) findViewById(R.id.emailidphonenumber);
        password = (TextInputEditText) findViewById(R.id.passwordfield);

        userlogin = (Button) findViewById(R.id.usersignin);
        usignup = (Button) findViewById(R.id.usersignup);

        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = emailphonenumber.getText().toString().trim();
                String userpass = password.getText().toString().trim();

                if(TextUtils.isEmpty(emailid)){
                    Toast.makeText(getApplicationContext(),"Please enter emailId",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userpass)){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(userpass.length()<6){
                    Toast.makeText(getApplicationContext(),"Password too short",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailid,userpass).addOnCompleteListener(UserLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(),UserBottomNav.class));
                        }else {
                            Toast.makeText(UserLoginActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });


        usignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLoginActivity.this,UserRegistrationActivity.class);
                startActivity(i);
            }
        });

    }


}
