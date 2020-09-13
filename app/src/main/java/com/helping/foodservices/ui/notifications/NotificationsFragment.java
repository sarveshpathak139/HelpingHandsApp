package com.helping.foodservices.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.helping.foodservices.MainActivity;
import com.helping.foodservices.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {

    private Button updateinfo;
    private TextInputEditText ownername,hotelname,phonenumber,userlocation;
    private CircleImageView userphoto;
    private String currentuserId;

    private FirebaseAuth mAuth;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference userref;

    private StorageReference UserProfileImageRef;

    private ProgressDialog loadingbar;

    private static final int GalleryPick = 1;

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        ownername = (TextInputEditText) root.findViewById(R.id.messownername);
        hotelname = (TextInputEditText) root.findViewById(R.id.messname);
        phonenumber = (TextInputEditText) root.findViewById(R.id.messphonenumber);
        userlocation = (TextInputEditText) root.findViewById(R.id.messlocation);
        userphoto = (CircleImageView) root.findViewById(R.id.profile_image);
        updateinfo = (Button) root.findViewById(R.id.userupdate);
        loadingbar = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentuserId = mAuth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        firebaseDatabase = FirebaseDatabase.getInstance();



        updateinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatesettings();
            }
        });

        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });
        retrieveuserinfo();


        return root;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPick && resultCode== RESULT_OK && data!=null){
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getContext(),this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("Please wait, your profile image is updating...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                Uri resultUri = result.getUri();

                final StorageReference filepath = UserProfileImageRef.child(currentuserId + ".jpg");

                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final  String downloadUrl = uri.toString();

                                userref.child("Users").child("MessOwner").child(currentuserId).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(),"Profile Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                                loadingbar.dismiss();
                                            }
                                            else {
                                                String message = task.getException().toString();
                                                Toast.makeText(getActivity(),"Error:"+message,Toast.LENGTH_SHORT).show();
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

    private void updatesettings() {

        String owname = ownername.getText().toString();
        String hotname = hotelname.getText().toString();
        String phoneno = phonenumber.getText().toString();
        String ulocation = userlocation.getText().toString();

        if(TextUtils.isEmpty(owname)){
            Toast.makeText(getActivity(),"Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(hotname)){
            Toast.makeText(getActivity(),"Mess/Hotel Name couldnt be empty",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(ulocation)){
            Toast.makeText(getActivity(),"Location Must be entered",Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(phoneno)){
            Toast.makeText(getActivity(),"Phone number should not be empty",Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> profilemap = new HashMap<>();
            profilemap.put("uid",currentuserId);
            profilemap.put("name",owname);
            profilemap.put("hotelname",hotname);
            profilemap.put("userphone",phoneno);
            profilemap.put("userlocation",ulocation);

            userref.child("Users").child("MessOwner").child(currentuserId).updateChildren(profilemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Profile updated suceessfully",Toast.LENGTH_SHORT).show();

                    }else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void retrieveuserinfo() {
        userref.child("Users").child("MessOwner").child(currentuserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name") && (dataSnapshot.hasChild("image")))){

                    String retrievehotelname = dataSnapshot.child("hotelname").getValue().toString();
                    String retrievename = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserphone = dataSnapshot.child("userphone").getValue().toString();
                    String retrieveuserlocation = dataSnapshot.child("userlocation").getValue().toString();
                    String retrieveprofileimage = dataSnapshot.child("image").getValue().toString();

                    hotelname.setText(retrievehotelname);
                    ownername.setText(retrievename);
                    phonenumber.setText(retrieveuserphone);
                    userlocation.setText(retrieveuserlocation);
                    Picasso.get().load(retrieveprofileimage).into(userphoto);


                }else if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                    String retrievehotelname = dataSnapshot.child("hotelname").getValue().toString();
                    String retrievename = dataSnapshot.child("name").getValue().toString();
                    String retrieveuserphone = dataSnapshot.child("userphone").getValue().toString();
                    String retrieveuserlocation = dataSnapshot.child("userlocation").getValue().toString();


                    hotelname.setText(retrievehotelname);
                    ownername.setText(retrievename);
                    phonenumber.setText(retrieveuserphone);
                    userlocation.setText(retrieveuserlocation);

                }else {
                    Toast.makeText(getActivity(),"Please set & update profile information",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.user_profile_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.userlogout){
            mAuth.signOut();
            getActivity().finish();
            Toast.makeText(getActivity(),"Logged Out Successfully!",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}
