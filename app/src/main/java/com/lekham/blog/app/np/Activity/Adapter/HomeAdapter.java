package com.lekham.blog.app.np.Activity.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lekham.blog.app.np.Activity.CreateProfileActivity;
import com.lekham.blog.app.np.Fragment.ElaborateFragment;
import com.lekham.blog.app.np.Model.Blog;
import com.lekham.blog.app.np.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class HomeAdapter extends FirebaseRecyclerAdapter<Blog, HomeAdapter.PostViewHolder> {
    private FirebaseFirestore firebaseFirestore;

    public HomeAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Blog> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull PostViewHolder holder, int position, @NonNull @NotNull Blog blog) {
        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDescription());
        String imageUrl = blog.getImage();


        String userId= blog.getUserid();
//        firebaseFirestore.collection("user").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
//             if(task.isSuccessful()){
//                 String userProfileImage = task.getResult().getString("image");
//                 Glide.with(holder.imageView.getContext()).load(userProfileImage).into(holder.ppImageView);
//             } else{
//
//             }
//            }
//        });

        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        holder.timestamp.setText(formattedDate);


        Glide.with(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentid = user.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference;
        reference = firestore.collection("user").document(userId);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){


                    String url = task.getResult().getString("url");
                    Picasso.get().load(url).into(holder.ppImageView);
                    String uname = task.getResult().getString("name");

                    holder.name.setText(uname);

                }else {
                }

            }
        });




        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,
                        new ElaborateFragment(blog.getTitle(),blog.getDescription(),blog.getImage()))
                        .addToBackStack(null).commit();
            }
        });


    }




    @NonNull
    @NotNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row_profile, parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new PostViewHolder(view);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView title,name;
        public TextView desc;
        public TextView timestamp;
        public ImageView imageView, ppImageView;
        String userId;

        public PostViewHolder(@NonNull @NotNull View view) {
            super(view);

            title = view.findViewById(R.id.titleTextView);
            desc = view.findViewById(R.id.descriptionTextView);
            imageView = view.findViewById(R.id.imageViewList);
            timestamp = view.findViewById(R.id.timestampList);
            ppImageView = view.findViewById(R.id.iv_pp_home);
            name  = view.findViewById(R.id.name);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}


