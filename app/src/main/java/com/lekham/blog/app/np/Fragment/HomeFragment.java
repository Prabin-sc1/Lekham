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
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lekham.blog.app.np.Activity.Adapter.HomeAdapter;
import com.lekham.blog.app.np.Activity.LoginActivity;
import com.lekham.blog.app.np.Model.Blog;
import com.lekham.blog.app.np.R;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    LinearLayoutManager mLayoutManager;
//    EditText searchtxt;
//    ImageView search;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
//        search = getActivity().findViewById(R.id.searchBlogId);

//        searchtxt = getActivity().findViewById(R.id.search_txt);
//        search = getActivity().findViewById(R.id.search);
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
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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