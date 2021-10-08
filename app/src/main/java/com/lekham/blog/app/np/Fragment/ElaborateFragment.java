package com.lekham.blog.app.np.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lekham.blog.app.np.R;


public class ElaborateFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private String title, description, image;

    public ElaborateFragment() {

    }

    public ElaborateFragment(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }


    public static ElaborateFragment newInstance(String param1, String param2) {
        ElaborateFragment fragment = new ElaborateFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_elaborate, container, false);
        ImageView imageholder = view.findViewById(R.id.ea_imageview);
        TextView titleholder = view.findViewById(R.id.ea_title);
        TextView descholder = view.findViewById(R.id.ea_description);

        titleholder.setText(title);
        descholder.setText(description);

        Glide.with(getContext()).load(image).into(imageholder);

        return view;
    }
    public void onBackPressed(){
        AppCompatActivity activity = (AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HomeFragment())
                .addToBackStack(null).commit();
    }
}