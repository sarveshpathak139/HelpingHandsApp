package com.helping.foodservices;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.helping.foodservices.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

public class messdata extends AppCompatActivity {
    private EditText downername,dhotelname,dphonenumber,duserlocation;
    private Button dupdateinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messdata);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ProgressDialog dloadingbar;

         downername = (EditText) findViewById(R.id.dmessownername);
         dhotelname = (EditText) findViewById(R.id.dmessname);
         dphonenumber = (EditText) findViewById(R.id.dmessphonenumber);
         duserlocation = (EditText) findViewById(R.id.dmesslocation);

        dupdateinfo = (Button) findViewById(R.id.duserrequest);
        dloadingbar = new ProgressDialog(this);
        dupdateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putrequest();
            }
        });





    }

    private void putrequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String owname = downername.getText().toString();
        String hotname = dhotelname.getText().toString();
        String phoneno = dphonenumber.getText().toString();
        String ulocation = duserlocation.getText().toString();

        if(TextUtils.isEmpty(owname)){
            Toast.makeText(getApplicationContext(),"Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(hotname)){
            Toast.makeText(getApplicationContext(),"Mess/Hotel Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(ulocation)){
            Toast.makeText(getApplicationContext(),"Location Must be entered",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(phoneno)){
            Toast.makeText(getApplicationContext(),"Phone number should not be empty",Toast.LENGTH_SHORT).show();
        }
        else {

            HashMap<String,Object> map=new HashMap<>();
            map.put("Amount",owname);
            map.put("ownername",hotname);
            map.put("mobilenumber",phoneno);
            map.put("location",ulocation);
            map.put("emailid",email);
            map.put("approve",0);

            FirebaseDatabase.getInstance().getReference().child("Post").push()
                    .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("kedar","Complete");
                    Fragment fragment = new DashboardFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.constraint, fragment);

                    fragmentTransaction.commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("keda",e.toString());
                }
})
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Request stored",Toast.LENGTH_LONG).show();




//                    Intent i=new Intent(getApplicationContext(), DashboardFragment.class);
//                    startActivity(i);

                }
            });







        }

    }


}
