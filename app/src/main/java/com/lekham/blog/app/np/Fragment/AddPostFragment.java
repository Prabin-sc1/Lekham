package com.lekham.blog.app.np.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lekham.blog.app.np.Activity.AddPostActivity;
import com.lekham.blog.app.np.Activity.AllFragmentActivity;
import com.lekham.blog.app.np.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPostFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;
    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mProgressDialog;
    private Uri mImageUri;
    private static final int GALLERY_CODE = 1;
    private StorageReference mStorage;

    private ImageButton back;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    public AddPostFragment() {
        // Required empty public constructor
    }

    public static AddPostFragment newInstance(String param1, String param2) {
        AddPostFragment fragment = new AddPostFragment();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mPostImage.setImageURI(mImageUri);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mStorage = FirebaseStorage.getInstance().getReference();

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("PBlog");

        mPostImage = getActivity().findViewById(R.id.addImg);
        mPostTitle = getActivity().findViewById(R.id.postTitleET);
        mPostDesc = getActivity().findViewById(R.id.postDescriptionET);
        mSubmitButton = getActivity().findViewById(R.id.postBtn);

        back = getActivity().findViewById(R.id.backButton);

        back.setOnClickListener(this);




        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }

        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //THis is where we putting to database
                startPosting();
//                startActivity(new Intent(getActivity(), HomeFragment.class));
            }
        });
    }



    private void startPosting() {
        mProgressDialog.setMessage("Posting to blog...");
        mProgressDialog.show();

        String titleVal = mPostTitle.getText().toString().trim();
        String descVal = mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mImageUri != null) {
            StorageReference filepath = mStorage.child("MBlog_images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();

                                    //createNewPost(imageUrl);
                                    DatabaseReference newPost = mPostDatabase.push();
                                    Map<String, String> dataToSave = new HashMap<>();


                                    dataToSave.put("title", titleVal);
                                    dataToSave.put("description", descVal);

                                    dataToSave.put("image", imageUrl);
                                    dataToSave.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));
                                    dataToSave.put("userid", mUser.getUid());

                                    newPost.setValue(dataToSave);
                                    mProgressDialog.dismiss();
                                    startActivity(new Intent(getActivity(), AllFragmentActivity.class));
                                }
                            });
                        }
                    }
                }
            });

        } else {
            Toast.makeText(getActivity(), "Please fill all the above first field!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backButton){
            startActivity(new Intent(getActivity(), AllFragmentActivity.class));
        }
    }
}