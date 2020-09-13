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

public class UserRegistrationActivity extends AppCompatActivity {

    TextInputEditText useremailid,userpassword,confirmpassword1;
    Button userregister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mAuth = FirebaseAuth.getInstance();

        /*firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(UserRegistrationActivity.this,UserBottomNav.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };*/

        useremailid = (TextInputEditText) findViewById(R.id.emaiilid);
        userpassword = (TextInputEditText) findViewById(R.id.userpassword);
        confirmpassword1 = (TextInputEditText) findViewById(R.id.confirmpassword);
        progressDialog = new ProgressDialog(this);
        userregister = (Button) findViewById(R.id.userregistration);
        userregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailid = useremailid.getText().toString().trim();
                String password = userpassword.getText().toString().trim();
                String confirmpass = confirmpassword1.getText().toString().trim();

                if(TextUtils.isEmpty(emailid)){
                    Toast.makeText(UserRegistrationActivity.this,"Please enter emailId",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(UserRegistrationActivity.this,"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmpass)){
                    Toast.makeText(UserRegistrationActivity.this,"Please re-enter password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){
                    Toast.makeText(UserRegistrationActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                }

                if(password.equals(confirmpass)){
                    progressDialog.setTitle("Creating New Account...");
                    progressDialog.setMessage("Please wait, while we are creating account for you!");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(UserRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(UserRegistrationActivity.this,"Registration failed",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else {
                                String userid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentuser_db = FirebaseDatabase.getInstance().getReference().child("Users").child("MessOwner").child(userid);
                                currentuser_db.setValue(true);
                                Toast.makeText(UserRegistrationActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(UserRegistrationActivity.this,UserBottomNav.class);
                                startActivity(intent);
                            }

                        }
                    });
                }

            }
        });


    }
}
