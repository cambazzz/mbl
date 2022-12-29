package com.example.socialuni;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainPageFragment extends Fragment {


    private PostsAdapter postsAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main_page, container,false);
        RecyclerView postRecyclerView = (RecyclerView) view.findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        postsAdapter = new PostsAdapter(getActivity());

        postRecyclerView.setAdapter(postsAdapter);



        return view;

    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {


        showPosts();

        CardView goSharePost = view.findViewById(R.id.goSharePost);
        goSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SharePostActivity.class);
                startActivity(intent);

            }
        });

    }
    private void showPosts(){
        FirebaseFirestore.getInstance()
                .collection("Posts")
                .orderBy("postingTİme", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        postsAdapter.clearPosts();
                        List<DocumentSnapshot> dsList = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot ds:dsList){
                            PostModel postModel = ds.toObject(PostModel.class);
                            postsAdapter.addPost(postModel);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}