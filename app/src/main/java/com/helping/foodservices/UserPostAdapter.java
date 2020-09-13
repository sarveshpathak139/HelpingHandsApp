package com.helping.foodservices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class UserPostAdapter extends FirebaseRecyclerAdapter<Post,UserPostAdapter.PastViewHolder> {


    public UserPostAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull PastViewHolder holder, final int position1, @NonNull Post model) {

        holder.name.setText(model.getOwnername());
        holder.amount.setText(model.getAmount());
        holder.mobile.setText(model.getMobilenumber());
        holder.location.setText(model.getLocation());
        holder.aprovedby.setText(model.getApprovedby());


    }
    @NonNull
    @Override
    public UserPostAdapter.PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getpostofuser,parent,false);
        return new PastViewHolder(view);
    }




    public class PastViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,amount,location,mobile,aprovedby;


        public PastViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
            location=itemView.findViewById(R.id.location);
            mobile=itemView.findViewById(R.id.mobile);
            aprovedby=itemView.findViewById(R.id.approvedby);



        }


    }
}
