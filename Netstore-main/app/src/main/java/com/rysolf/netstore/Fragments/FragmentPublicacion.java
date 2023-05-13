package com.rysolf.netstore.Fragments;


import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.rysolf.netstore.NewPostActivity;
import com.rysolf.netstore.Post;
import com.rysolf.netstore.PostRecyclerAdapter;
import com.rysolf.netstore.R;
import com.rysolf.netstore.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class FragmentPublicacion extends Fragment {
    private FloatingActionButton addPostBtn;
    RecyclerView postListView;
    private List<Post> postList;
    private List<Users> userList;
    String nameTxt;
    private Bundle bundle;
    LinearLayoutManager lm;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUserId;
    SearchView searchView;
    private PostRecyclerAdapter postRecyclerAdapter;
    private DocumentSnapshot lastVisible;
    private Boolean firstLoad = true;
    TextView textview123;

    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publicacion, container, false);

        addPostBtn = view.findViewById(R.id.add_post_btn);
        textview123 = view.findViewById(R.id.text123);

        postList = new ArrayList<>();
        userList = new ArrayList<>();
        postListView = view.findViewById(R.id.post_list_view1);
        searchView = view.findViewById(R.id.filter1);
        mAuth = FirebaseAuth.getInstance();

        postRecyclerAdapter = new PostRecyclerAdapter(postList, userList);
        postListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        postListView.setAdapter(postRecyclerAdapter);
        lm = new LinearLayoutManager(getContext());
        postListView.setLayoutManager(lm);

        bundle = getArguments();
        String idTxt = bundle.getString("id");

                currentUserId = mAuth.getCurrentUser().getUid();
                if (idTxt.equals(currentUserId)) {
                    addPostBtn.setVisibility(View.VISIBLE);
                } else {
                    addPostBtn.setVisibility(View.GONE);


        }

        if (mAuth.getCurrentUser() != null) {

            db = FirebaseFirestore.getInstance();

            postListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if (reachedBottom) {
                        // Toast.makeText(container.getContext(), "Reached bottom", Toast.LENGTH_SHORT).show();
                        loadPost();
                    }
                }
            });

            // Sort results by descending (latest posts first)
            Query firstQuery = db.collection("Posts").limit(10);

            // Set real time database listener
            firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {

                            if (firstLoad) {
                                lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                                postList.clear();
                                userList.clear();
                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String newPostId = doc.getDocument().getId();
                                    final Post newPost = doc.getDocument().toObject(Post.class).withId(newPostId);

                                    String postUserId = doc.getDocument().getString("user_id");

                                    db.collection("Users").document(postUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                Users user = task.getResult().toObject(Users.class);

                                                if (firstLoad) {
                                                    userList.add(user);
                                                    postList.add(newPost);
                                                } else {
                                                    userList.add(0, user);
                                                    postList.add(0, newPost);
                                                }

                                            }

                                            postRecyclerAdapter.notifyDataSetChanged();
                                        }
                                    });


                                }
                            }

                            firstLoad = false;
                        }
                    }
                }
            });
        }




        new Handler().postDelayed(new Runnable(){

            public void run() {

                searchView.setOnQueryTextListener(queryTextListener);
                if (getArguments() != null) {
                    bundle = getArguments();
                    nameTxt = bundle.getString("name");




                    searchView.setQuery(nameTxt, false);
                }

            }

        }, 800);





               







        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPostIntent = new Intent(getContext(), NewPostActivity.class);
                if (getArguments() != null) {
                    bundle = getArguments();
                    nameTxt = bundle.getString("name");
                    addPostIntent.putExtra("name",nameTxt);
                }
                startActivity(addPostIntent);

            }
        });

        return view;




    }

    void buscar(String s) {
        ArrayList<Users> milista = new ArrayList<>();
        ArrayList<Post> milista1 = new ArrayList<>();

            for (Post obj1 : postList) {
                for (Users obj : userList) {
                    if(obj.getName()!=null){
                if (obj.getName().toLowerCase().contains(s.toLowerCase())) {
                    milista.add(obj);
                }



                }


                }
                if(obj1.getName()!=null){
                if (obj1.getName().toLowerCase().contains(s.toLowerCase())) {
                    milista1.add(obj1);

                }

                else if (milista1.isEmpty()) {

                    textview123.setVisibility(View.VISIBLE);
                }
                else{
                    textview123.setVisibility(View.VISIBLE);
                }

                }

            }

        postRecyclerAdapter = new PostRecyclerAdapter(milista1, milista);

        postListView.setAdapter(postRecyclerAdapter);
        postRecyclerAdapter.notifyDataSetChanged();
    }


    public void loadPost() {
        if (mAuth.getCurrentUser() != null) {
            // Sort results by descending (latest posts first)
            Query nextQuery = db.collection("Posts")
                    .startAfter(lastVisible)
                    .limit(10);

            // Set real time database listener
            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshots != null) {
                        if (!documentSnapshots.isEmpty()) {
                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String newPostId = doc.getDocument().getId();
                                    final Post newPost = doc.getDocument().toObject(Post.class).withId(newPostId);

                                    String postUserId = doc.getDocument().getString("user_id");

                                    db.collection("Users").document(postUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {

                                                Users user = task.getResult().toObject(Users.class);

                                                userList.add(user);
                                                postList.add(newPost);

                                            }

                                            postRecyclerAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            buscar(s);
            return true;
        }
    };
}


