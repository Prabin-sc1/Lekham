package com.lekham.blog.app.np.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lekham.blog.app.np.Fragment.AddPostFragment;
import com.lekham.blog.app.np.Fragment.HomeFragment;
import com.lekham.blog.app.np.Fragment.ProfileFragment;
import com.lekham.blog.app.np.Fragment.SettingFragment;
import com.lekham.blog.app.np.R;

public class AllFragmentActivity extends AppCompatActivity {
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    FrameLayout frameLayout;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_all_fragment);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HomeFragment()).commit();
        bnv = findViewById(R.id.bottomNavigationId);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        temp = new HomeFragment();
                        break;
                    case R.id.add:
                        temp = new AddPostFragment();
                        break;
                    case R.id.profile:
                        temp = new ProfileFragment();
                        break;
                    case R.id.setting:
                        temp = new SettingFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, temp).commit();
                return true;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                if (mUser != null && mAuth != null) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AllFragmentActivity.this, LoginActivity.class));
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}