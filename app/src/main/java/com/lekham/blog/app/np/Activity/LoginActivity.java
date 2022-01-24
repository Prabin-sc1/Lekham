package com.lekham.blog.app.np.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.lekham.blog.app.np.Model.AllUserMember;
import com.lekham.blog.app.np.R;

public class LoginActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 101;
    private TextView forgot, register;

    private TextInputEditText email, password;
    private Button login;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);
        FirebaseDatabase.getInstance().getReference("message").setValue("aple");



        forgot = findViewById(R.id.forgotId);
        register = findViewById(R.id.registerNowId);

        forgot.setText(Html.fromHtml("<u>Forgot password?</u>"));
        register.setText(Html.fromHtml("<u>Register now</u>"));


        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordId);
        login = findViewById(R.id.loginBtnId);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( @NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //databaseReference.push().setValue("apple");
                    updateUI(firebaseUser);

                    //startActivity(new Intent(getApplicationContext(), BlogsActivity.class));
                } else {
                    updateUI(null);

                }
            }
        };



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null ) {
                    Toast.makeText(LoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    String emailString = email.getText().toString();
                    String pwd = password.getText().toString();

                    login(emailString, pwd);
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill above field", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void login(String emailString, String passwordString) {
        mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Please register first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signout) {
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), CreateProfileActivity.class));
            finish();

        }
    }


}