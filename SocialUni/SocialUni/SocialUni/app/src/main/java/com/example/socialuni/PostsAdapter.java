package com.example.socialuni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    private Context context;
    private List<PostModel> postModelList;


    public PostsAdapter(Context context){
        this.context = context;
        postModelList = new ArrayList<>();
    }

    public void addPost(PostModel postModel){
        postModelList.add(postModel);
        notifyDataSetChanged();
    }
    public void clearPosts(){
        postModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel postModel = postModelList.get(position);
        if (postModel.getPostImage()!=null){
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(postModel.getPostImage()).into(holder.postImage);
        }else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.postText.setText(postModel.getPostText());

        String uid = postModel.getUserId();
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        if (userModel.getUserProfile()!=null){
                            Glide.with(context).load(userModel.getUserProfile()).into(holder.userProfile);
                        }
                        holder.userName.setText(userModel.getUserName());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView userName, postText;
        private ImageView userProfile, postImage,like,comments;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            userName= itemView.findViewById(R.id.userNamePW);
            postText= itemView.findViewById(R.id.postTextPW);
            userProfile= itemView.findViewById(R.id.userProfilePW);
            postImage= itemView.findViewById(R.id.postImagePW);
            like= itemView.findViewById(R.id.LikePW);
            comments= itemView.findViewById(R.id.CommentPW);


        }


    }
}