package com.lekham.blog.app.np.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lekham.blog.app.np.Activity.AllFragmentActivity;
import com.lekham.blog.app.np.Activity.LoginActivity;
import com.lekham.blog.app.np.R;

public class SettingFragment extends Fragment {
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Button button;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SettingFragment() {

    }


    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        button = getActivity().findViewById(R.id.darkModeBtn);


        SharedPreferences sharedPreferences  = getActivity().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn",false);

        if(isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            button.setText("Disable Dark Mode");
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            button.setText("Enable Dark Mode");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDarkModeOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("isDarkModeOn", false);
                    editor.apply();
                    button.setText("Enable Dark Mode");
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("isDarkModeOn", true);
                    editor.apply();
                    button.setText("Disable Dark Mode");
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}