package com.helping.foodservices;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CoordinatorRegistrationActivity extends AppCompatActivity {

    TextInputEditText cooemailid,coopassword,cooconfirmpass;
    Button coosignup;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_registration);

        cooemailid = (TextInputEditText) findViewById(R.id.cooemailid);
        coopassword = (TextInputEditText) findViewById(R.id.coopassword);
        cooconfirmpass = (TextInputEditText) findViewById(R.id.cooconfirmpassword);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        coosignup = (Button) findViewById(R.id.coosignup);
        coosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = cooemailid.getText().toString().trim();
                String password = coopassword.getText().toString().trim();
                String confirmpass = cooconfirmpass.getText().toString().trim();

                if(TextUtils.isEmpty(emailid)){
                    Toast.makeText(CoordinatorRegistrationActivity.this,"Please enter emailId",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(CoordinatorRegistrationActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmpass)){
                    Toast.makeText(CoordinatorRegistrationActivity.this,"Please re-enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(CoordinatorRegistrationActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                }

                if(password.equals(confirmpass)){
                    progressDialog.setTitle("Creating New Account...");
                    progressDialog.setMessage("Please wait, while we are creating account for you!");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(CoordinatorRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(CoordinatorRegistrationActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else {
                                String userid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentuser_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Coordinator").child(userid);
                                currentuser_db.setValue(true);
                                Toast.makeText(CoordinatorRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(CoordinatorRegistrationActivity.this,CooMainActivity.class);
                                startActivity(intent);
                            }

                        }
                    });
                }


            }
        });



    }
}
