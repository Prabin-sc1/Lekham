package com.lekham.blog.app.np.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.lekham.blog.app.np.Fragment.ProfileFragment;
import com.lekham.blog.app.np.R;

public class UpdateProfileActivity extends AppCompatActivity {
    EditText etname, etprof, etbio;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentuid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();
        documentReference =db.collection("user").document(currentuid);

        etname = findViewById(R.id.et_name_up);
        etprof = findViewById(R.id.et_profession_up);
        etbio = findViewById(R.id.et_bio_up);
        button = findViewById(R.id.btn_up);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_back){
            startActivity(new Intent(UpdateProfileActivity.this, AllFragmentActivity.class));
            finish();
        }
//        switch (item.getItemId()){
//            case R.id.action_add:
//                if(mUser != null && mAuth != null) {
//                    startActivity(new Intent(PostListActivity.this, AddPostActivity.class));
//                    finish();
//                }
//                break;
//            case R.id.action_signout:
//                if(mUser != null && mAuth != null) {
//                    mAuth.signOut();
//                    startActivity(new Intent(PostListActivity.this, MainActivity.class));
//                    finish();
//                }
//
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();


        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<DocumentSnapshot> task) {
                        if(task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String profResult = task.getResult().getString("prof");
                            String url = task.getResult().getString("url");

//                        Picasso.get().load(url).into(imageView);
                            etname.setText(nameResult);
                            etbio.setText(bioResult);
                            etprof.setText(profResult);
                        }else{
                            Toast.makeText(UpdateProfileActivity.this, "No profile exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateProfile() {
        String name = etname.getText().toString();
        String prof = etprof.getText().toString();
        String bio = etbio.getText().toString();

        final DocumentReference sDoc = db.collection("user").document(currentuid);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
//                DocumentSnapshot snapshot = transaction.get(sfDocRef);


                transaction.update(sDoc, "name",name);
                transaction.update(sDoc, "prof",prof);
                transaction.update(sDoc, "bio",bio);


                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Transaction success!");
                Toast.makeText(UpdateProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProfileActivity.this, AllFragmentActivity.class));
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Transaction failure.", e);
                        Toast.makeText(UpdateProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}