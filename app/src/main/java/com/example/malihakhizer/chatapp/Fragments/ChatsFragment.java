package com.example.malihakhizer.chatapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.Activity.MessageChat;
import com.example.malihakhizer.chatapp.Adapter.UserAdapter;
import com.example.malihakhizer.chatapp.Model.ChatList;
import com.example.malihakhizer.chatapp.Model.Message;
import com.example.malihakhizer.chatapp.Model.User;
import com.example.malihakhizer.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

public class ChatsFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private ArrayList<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private ArrayList<ChatList> usersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragments_chats, container, false);

        recyclerView = rootView.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatlist = snapshot.getValue(ChatList.class);
                    usersList.add(chatlist);
                }

                chatListFunc();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;

    }

    private void chatListFunc(){
            mUsers = new ArrayList<>();
            reference = FirebaseDatabase.getInstance().getReference("users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        for (ChatList chatlist : usersList){
                            if (user.getId().equals(chatlist.getId())){
                                mUsers.add(user);
                            }
                        }
                    }
                    userAdapter = new UserAdapter( mUsers,getContext(), new UserAdapter.OnItemListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent intent = new Intent(view.getContext(), MessageChat.class);
                            Bundle bundle  = new Bundle();
                            bundle.putString("User_id", mUsers.get(position).getId());
                            intent.putExtras(bundle);

                            Toast.makeText(view.getContext(), "Opening chat with " + mUsers.get(position).getName(), Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



   /* private void readChats() {
        Log.i("Chats Fragment ----","Entered readChats");
        mUsers = new ArrayList<>();
        for(String user : usersList){
            Log.i("USERLIST ----",user);
        }
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    Log.i("UsersList in readchats","hi");
                    for (String id : usersList){

                        if (user.getId().equals(id)){
                            if( mUsers.size() != 0 ){
                                for (User user1 : mUsers){
                                    if(!user.getId().equals(user1.getId())){
                                        mUsers.add(user);
                                    }
                                }

                            } else {
                                mUsers.add(user);
                            }

                        }
                    }
                }

                for(User user : mUsers){
                    Log.i("mUser ----",user.getName());
                }
                userAdapter = new UserAdapter(mUsers, getContext(), new UserAdapter.OnItemListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), MessageChat.class);
                        Bundle bundle  = new Bundle();
                        bundle.putString("User_id", mUsers.get(position).getId());
                        intent.putExtras(bundle);

                        Toast.makeText(view.getContext(), "Opening chat with " + mUsers.get(position).getName(), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
