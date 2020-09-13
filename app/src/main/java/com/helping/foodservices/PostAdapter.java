package com.helping.foodservices;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostAdapter extends FirebaseRecyclerAdapter<Post,PostAdapter.PastViewHolder> {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email1 = user.getEmail();
    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int position, @NonNull Post model) {
        holder.name.setText(model.getOwnername());
        holder.amount.setText(model.getAmount());
        holder.mobile.setText(model.getMobilenumber());
        holder.location.setText(model.getLocation());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("Post")
                       .child(getRef(position).getKey())


                       .addValueEventListener(new ValueEventListener() {
                            @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try {


                                    String email = (String) dataSnapshot.child("emailid").getValue();
                                    String name = email + "_1";
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Post")
                                            .child(getRef(position).getKey());
                                    ref.child("emailid_approve").setValue(name);


                                    ref.child("approve").setValue(1);
                                    ref.child("approvedby").setValue(email1);
                                }
                                catch (Exception e)

                                {
                                    Log.e("kedka","Sorry to inturpt");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            //.child("approve").setValue(1);
//
//
//                FirebaseDatabase.getInstance().getReference().child("Post")
//                        .child(getRef(position).getKey()).child("email").getDatabase();


            }
        });
    }


    @NonNull
    @Override
    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getpost,parent,false);
        return new PastViewHolder(view);
    }

    public class PastViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,amount,location,mobile;
        ImageView delete;
        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
            location=itemView.findViewById(R.id.location);
            mobile=itemView.findViewById(R.id.mobile);
            delete=itemView.findViewById(R.id.delete);



        }


    }
}
