package com.helping.foodservices;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CooProfileActivity extends AppCompatActivity {

    private Button updateinfo;
    private TextInputEditText cooname, coophonenumber, coolocation;
    private CircleImageView userphoto;
    private String currentuserId;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userref;

    private StorageReference CooProfileImageRef;

    private ProgressDialog loadingbar;

    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coo_profile);

        updateinfo = (Button) findViewById(R.id.cooupdate);
        cooname = (TextInputEditText) findViewById(R.id.coordinatorname);
        coophonenumber = (TextInputEditText) findViewById(R.id.coordinatorphoneno);
        coolocation = (TextInputEditText) findViewById(R.id.cooarealocation);
        userphoto = (CircleImageView) findViewById(R.id.cooprofileimage);

        mAuth = FirebaseAuth.getInstance();
        currentuserId = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference();
        CooProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        firebaseDatabase = FirebaseDatabase.getInstance();


        updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesettings();
            }
        });

        retrievecooinfo();

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.coobottomnav);

        bottomNavigationView.setSelectedItemId(R.id.coo_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.coo_home:
                        startActivity(new Intent(getApplicationContext(),CooMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.coo_dashboard:
                        startActivity(new Intent(getApplicationContext(),CooDashBoardActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.coo_profile:

                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode== RESULT_OK && data!=null){
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait, your profile image is updating...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                Uri resultUri = result.getUri();

                final StorageReference filepath = CooProfileImageRef.child(currentuserId + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final  String downloadUrl = uri.toString();

                                userref.child("Users").child("Coordinator").child(currentuserId).child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(CooProfileActivity.this,"Profile Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                                    loadingbar.dismiss();
                                                }
                                                else {
                                                    String message = task.getException().toString();
                                                    Toast.makeText(CooProfileActivity.this,"Error:"+message,Toast.LENGTH_SHORT).show();
                                                    loadingbar.dismiss();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
            }

        }
    }

    private void retrievecooinfo() {

        userref.child("Users").child("Coordinator").child(currentuserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("cooname") && (dataSnapshot.hasChild("image")))){

                    String retrievecooname = dataSnapshot.child("cooname").getValue().toString();
                    String retrievecoonumber = dataSnapshot.child("coophonenumber").getValue().toString();
                    String retrievecoolocation = dataSnapshot.child("coolocation").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();

                    cooname.setText(retrievecooname);
                    coophonenumber.setText(retrievecoonumber);
                    coolocation.setText(retrievecoolocation);
                    Picasso.get().load(retrieveprofileimage).into(userphoto);
                }
                else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("cooname"))){
                    String retrievecooname = dataSnapshot.child("cooname").getValue().toString();
                    String retrievecoonumber = dataSnapshot.child("coophonenumber").getValue().toString();
                    String retrievecoolocation = dataSnapshot.child("coolocation").getValue().toString();

                    cooname.setText(retrievecooname);
                    coophonenumber.setText(retrievecoonumber);
                    coolocation.setText(retrievecoolocation);

                }else {
                    Toast.makeText(CooProfileActivity.this,"Please set & update profile information",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updatesettings() {

        String coordinatorname = cooname.getText().toString();
        String coordinatorphoneno = coophonenumber.getText().toString();
        String coordinatorlocation = coolocation.getText().toString();

        if(TextUtils.isEmpty(coordinatorname)){
            Toast.makeText(CooProfileActivity.this,"Please Enter Name",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(coordinatorphoneno)){
            Toast.makeText(CooProfileActivity.this,"Please Enter Phone Number",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(coordinatorlocation)){
            Toast.makeText(CooProfileActivity.this,"Please Enter Location",Toast.LENGTH_SHORT).show();
        }

        else {
            HashMap<String, Object> profilemap = new HashMap<>();
            profilemap.put("uid",currentuserId);
            profilemap.put("cooname",coordinatorname);
            profilemap.put("coophonenumber",coordinatorphoneno);
            profilemap.put("coolocation",coordinatorlocation);

            userref.child("Users").child("Coordinator").child(currentuserId).updateChildren(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CooProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                    }
                    String message = task.getException().toString();
                    Toast.makeText(CooProfileActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.userlogout){
            mAuth.signOut();
            finish();
            Toast.makeText(CooProfileActivity.this,"Logged Out Successfully!",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(CooProfileActivity.this, MainActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }

}
