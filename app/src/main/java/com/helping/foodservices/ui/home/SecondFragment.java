package com.helping.foodservices.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.helping.foodservices.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SecondFragment extends Fragment {
    private EditText downername,dhotelname,dphonenumber,duserlocation;
    private Button dupdateinfo;
    private  ProgressDialog dloadingbar;
    String kedar="Kedar";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_second, container, false);
        downername = (EditText) view.findViewById(R.id.dmessownername);
        dhotelname = (EditText) view.findViewById(R.id.dmessname);
        dphonenumber = (EditText) view.findViewById(R.id.dmessphonenumber);
        duserlocation = (EditText) view.findViewById(R.id.dmesslocation);
        dupdateinfo = (Button) view.findViewById(R.id.duserrequest);
        dloadingbar = new ProgressDialog(getContext());
        dupdateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putrequest();
            }
        });












        return  view;


    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void putrequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String owname = downername.getText().toString();
        String hotname = dhotelname.getText().toString();
        String phoneno = dphonenumber.getText().toString();
        String ulocation = duserlocation.getText().toString();

        if(TextUtils.isEmpty(owname)){
            Toast.makeText(getContext(),"Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(hotname)){
            Toast.makeText(getContext(),"Mess/Hotel Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(ulocation)){
            Toast.makeText(getContext(),"Location Must be entered",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(phoneno)){
            Toast.makeText(getContext(),"Phone number should not be empty",Toast.LENGTH_SHORT).show();
        }
        else {

            HashMap<String,Object> map=new HashMap<>();
            map.put("Amount",owname);
            map.put("ownername",hotname);
            map.put("mobilenumber",phoneno);
            map.put("location",ulocation);
            map.put("emailid",email);
            map.put("approve",0);
            map.put("emailid_approve",email+ "_0");
            map.put("approvedby","Not Yet Approved");

            FirebaseDatabase.getInstance().getReference().child("Post").push()
                    .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("kedar","Complete");
//                    Fragment fragment = new DashboardFragment();
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.constraint, fragment);
//
//                    fragmentTransaction.commit();

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
                            Toast.makeText(getContext(),"Request stored",Toast.LENGTH_LONG).show();
                            HomeFragment newGamefragment = new HomeFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.redirect, newGamefragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();



//                    Intent i=new Intent(getApplicationContext(), DashboardFragment.class);
//                    startActivity(i);

                        }
                    });







        }

    }
}
