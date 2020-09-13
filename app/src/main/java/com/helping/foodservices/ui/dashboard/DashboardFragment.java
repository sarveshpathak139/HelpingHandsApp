package com.helping.foodservices.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helping.foodservices.Post;
import com.helping.foodservices.R;
import com.helping.foodservices.UserPostAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DashboardFragment extends Fragment {


    private RecyclerView recyclerView;
    private UserPostAdapter adapter;
    private DatabaseReference post;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        Toast.makeText(getActivity(),"Email is "+email,Toast.LENGTH_LONG).show();


        post= FirebaseDatabase.getInstance().getReference().child("Post");
        String kedar=email+"_1";
        Query query=post.orderByChild("emailid_approve").equalTo(kedar).limitToFirst(3);

        recyclerView=root.findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Post> options=new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(query,Post.class)
                .build();

        adapter=new UserPostAdapter(options);
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
