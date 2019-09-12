package com.example.malihakhizer.chatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.Activity.MessageChat;
import com.example.malihakhizer.chatapp.Adapter.UserAdapter;
import com.example.malihakhizer.chatapp.Model.User;
import com.example.malihakhizer.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersFragment extends Fragment implements  UserAdapter.OnItemListener {
    View rootView;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private ArrayList<User> mUsers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_users,container,false);

        recyclerView = rootView.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        readUsers();

        return rootView;
    }

    public void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;

                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                    }
                }
                adapter = new UserAdapter(mUsers, getContext(), new UserAdapter.OnItemListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), MessageChat.class);
                        Bundle bundle  = new Bundle();
                        bundle.putString("User_id", mUsers.get(position).getId());
                        intent.putExtras(bundle);

                        Toast.makeText(view.getContext(), "Opening chat with" + mUsers.get(position).getName(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view, int position) {

    }
}
