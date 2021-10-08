package com.lekham.blog.app.np.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lekham.blog.app.np.Activity.Adapter.ProfileAdapter;
import com.lekham.blog.app.np.Activity.AddPostActivity;
import com.lekham.blog.app.np.Activity.CreateProfileActivity;
import com.lekham.blog.app.np.Activity.UpdateProfileActivity;
import com.lekham.blog.app.np.Model.Blog;
import com.lekham.blog.app.np.R;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener{
    ImageView imageView;
    TextView nameEt, profEt, bioEt;
    RecyclerView profRecyclerView;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;


    ProfileAdapter adapter ;

    ImageButton ib_edit;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {

    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();





        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable  Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.profRecViewId);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("PBlog");
        mDatabaseReference.keepSynced(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);

//        recyclerView = getActivity().findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String userid = mAuth.getUid();
        FirebaseRecyclerOptions<Blog> options =
                new FirebaseRecyclerOptions.Builder<Blog>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PBlog").orderByChild("userid").startAt(userid).endAt(userid+"\uf8ff"), Blog.class)
                        .build();
        adapter = new ProfileAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        imageView = getActivity().findViewById(R.id.iv_pf);
        nameEt = getActivity().findViewById(R.id.tv_name_pf);
        bioEt = getActivity().findViewById(R.id.tv_bio_pf);
        profEt = getActivity().findViewById(R.id.tv_prof_pf);
        ib_edit = getActivity().findViewById(R.id.ib_edit_pf);
        ib_edit.setOnClickListener(this);
//        profRecyclerView = getActivity().findViewById(R.id.profRecViewId);

        floatingActionButton = getActivity().findViewById(R.id.floatingActionButtonProfile);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser != null && mAuth != null) {
                    startActivity(new Intent(getActivity(), AddPostActivity.class));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);


}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_edit_pf:
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
                break;
            case R.id.ib_menu_pf:

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
//        Toast.makeText(getContext(), currentid, Toast.LENGTH_SHORT).show();
        DocumentReference reference;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String nameResult = task.getResult().getString("name");
                    String bioResult = task.getResult().getString("bio");
                    String profResult = task.getResult().getString("prof");
                    String url = task.getResult().getString("url");

                    Picasso.get().load(url).into(imageView);
                    nameEt.setText(nameResult);
                    bioEt.setText(bioResult);
                    profEt.setText(profResult);

                }else {
                    Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}