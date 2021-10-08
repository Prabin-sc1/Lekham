package com.lekham.blog.app.np.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lekham.blog.app.np.Model.AllUserMember;
import com.lekham.blog.app.np.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {
    private EditText etname, etprofession, etbio;
    private Button createBtn;
    private ImageView profilePicture;
    private ProgressBar progressBar;
    TextView already;

    Uri imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private static final int PICK_IMAGE = 1;


    AllUserMember member;
    String currentUserId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_profile);


        already = findViewById(R.id.alreadyProfile);
        already.setText(Html.fromHtml("<u>Already have a profile?</u>"));
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateProfileActivity.this, AllFragmentActivity.class));
                finish();
            }
        });
        member = new AllUserMember();
        etname = findViewById(R.id.et_name_pp);
        etprofession = findViewById(R.id.et_profession_cp);
        etbio = findViewById(R.id.et_bio_cp);
        createBtn = findViewById(R.id.btn_cp);
        profilePicture = findViewById(R.id.iv_pp);
        progressBar = findViewById(R.id.progressbar_cp);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//
//        if(  != null){
//            startActivity(new Intent(CreateProfileActivity.this, AllFragmentActivity.class));
//            finish();
//        }


        currentUserId = user.getUid();

        documentReference = db.collection("user").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");
        databaseReference = database.getReference("AllUsers");


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData() != null){
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(profilePicture);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver  contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadData() {
        String nameString = etname.getText().toString();
        String profString = etprofession.getText().toString();
        String bioString = etbio.getText().toString();

        if(!TextUtils.isEmpty(nameString) || !TextUtils.isEmpty(profString) || !TextUtils.isEmpty(bioString) || imageUri != null){
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull  Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(task.isSuccessful()){
//                        throw task.getException();
//                        Toast.makeText(CreateProfileActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull  Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();

                        Map<String, String> profile = new HashMap<>();
                        profile.put("name",nameString);
                        profile.put("prof", profString);
                        profile.put("bio",bioString);
                        profile.put("url",downloadUri.toString());
                        profile.put("privacy","Public");
                        profile.put("uid",currentUserId);

                        member.setName(nameString);
                        member.setProf(profString);
                        member.setUid(currentUserId);
                        member.setUrl(downloadUri.toString());

                        databaseReference.child(currentUserId).setValue(member);
                        documentReference.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(CreateProfileActivity.this, "Profile created", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(CreateProfileActivity.this,AllFragmentActivity.class);
                                        startActivity(intent);
                                    }
                                },500);
                            }
                        });
                    }
                }
            });
        }else {
            Toast.makeText(this, "Please fill all the fields properly!", Toast.LENGTH_SHORT).show();
        }
    }


}