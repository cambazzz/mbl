package com.example.socialuni;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialuni.databinding.ActivitySharePostBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.UUID;

public class SharePostActivity extends AppCompatActivity {


    private Uri SharedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share_post);
        CardView pickPhoto = findViewById(R.id.pickPhoto);
        Button sharePost = findViewById(R.id.uploadpost);
        TextView textView=(TextView) findViewById(R.id.textView2);

        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());



        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });

        sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = new ProgressDialog(SharePostActivity.this);
                progressDialog.setTitle("Posting");
                progressDialog.setMessage("Processing..");
                progressDialog.show();



                String id = UUID.randomUUID().toString();
                EditText postText = (EditText) findViewById(R.id.postText);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("Posts/"+id+"image.png");
                if (SharedImageUri != null){

                    storageReference.putFile(SharedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.cancel();
                                    finish();
                                    Toast.makeText(SharePostActivity.this, "Posted", Toast.LENGTH_SHORT).show();
                                    PostModel postModel = new PostModel(id, FirebaseAuth.getInstance().getUid(),postText.getText().toString(),
                                            uri.toString(),"0","0", Calendar.getInstance().getTimeInMillis());

                                    FirebaseFirestore
                                            .getInstance()
                                            .collection("Posts")
                                            .document(id)
                                            .set(postModel);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    finish();
                                    Toast.makeText(SharePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

                }else{

                    progressDialog.cancel();
                    finish();
                    PostModel postModel = new PostModel(id, FirebaseAuth.getInstance().getUid(),postText.getText().toString(),
                            null,"0","0", Calendar.getInstance().getTimeInMillis());

                    FirebaseFirestore
                            .getInstance()
                            .collection("Posts")
                            .document(id)
                            .set(postModel);



                }

            }
        });


    }
    private void openGallery(){
        Intent intent =new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(data!=null){
                SharedImageUri=data.getData();
                ImageView sharedPhoto = findViewById(R.id.sharedPhoto);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sharedPhoto.post(new Runnable() {
                            @Override
                            public void run() {
                                sharedPhoto.setImageURI(SharedImageUri);
                            }
                        });
                    }
                }).start();

            }else{
                Toast.makeText(this, "Image not picked", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
