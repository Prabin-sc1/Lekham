package com.lekham.blog.app.np.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lekham.blog.app.np.Activity.Adapter.HomeAdapter;
import com.lekham.blog.app.np.Activity.LoginActivity;
import com.lekham.blog.app.np.Activity.UpdateProfileActivity;
import com.lekham.blog.app.np.Model.Blog;
import com.lekham.blog.app.np.R;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment implements View.OnClickListener{
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    LinearLayoutManager mLayoutManager;
    ImageButton ib_signout;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("PBlog");
        mDatabaseReference.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);

        recyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(mLayoutManager);

        ib_signout = getActivity().findViewById(R.id.ibSignout);
        ib_signout.setOnClickListener(this);


        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PBlog"), Blog.class)
                        .build();
        adapter = new HomeAdapter(options);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibSignout:
                if (mUser != null && mAuth != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    break;
                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}