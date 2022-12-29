package com.example.socialuni;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder>{

    private Context context;
    private List<PostModel> postModelList;
    private Uri SharedImageUri;

    public PostsAdapter(Context context) {
        this.context = context;
        postModelList=new ArrayList<>();
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel postModel=postModelList.get(position);
        if(postModel.getPostImage()!=null){
            holder.postImage.setVisibility(View.VISIBLE);
            /*Glide.with(context)
                    .load(postModel.getPostImage()).into(holder.postImage);*/

        }else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.postText.setText(postModel.getPostText());

        String uid=postModel.getUserId();
        FirebaseFirestore
                .getInstance()
                .collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel=documentSnapshot.toObject(UserModel.class);
                        if(userModel.getUserProfile()!=null){
                            /*Glide.with(context).load(userModel.getUserProfile()).into(holder.userProfile);*/
                        }
                        holder.username.setText(userModel.getUserName());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView username,postText;
        private ImageView userProfile,postImage,like,comment;

        public MyViewHolder(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.userName);
            userProfile=itemView.findViewById(R.id.userProfile);
            postText=itemView.findViewById(R.id.postText);
            postImage=itemView.findViewById(R.id.postImage);
            like=itemView.findViewById(R.id.Like);
            comment=itemView.findViewById(R.id.Comment);



        }
    }
}
