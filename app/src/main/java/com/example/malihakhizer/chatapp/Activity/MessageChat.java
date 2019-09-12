package com.example.malihakhizer.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.Adapter.MessageAdapter;
import com.example.malihakhizer.chatapp.Model.Message;
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

public class MessageChat extends AppCompatActivity {
    private static final String TAG = "Message Chat ---";
    ImageButton send_button;
    EditText messageET;

    MessageAdapter adapter;
    ArrayList<Message> mChat;
    RecyclerView recyclerView;

    FirebaseUser fUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        recyclerView = findViewById(R.id.messages_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Bundle bundle = getIntent().getExtras();
        final String receiverid = bundle.getString("User_id");

        messageET = findViewById(R.id.message_text);
        send_button = findViewById(R.id.send_button);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagebody = messageET.getText().toString().trim();
                if (!messagebody.equals("")){
                    sendMessage(fUser.getUid(), receiverid, messagebody);
                } else {
                    Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                messageET.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("users").child(receiverid);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                readMesagges(fUser.getUid(), receiverid, "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Message msg = new Message(sender, receiver, message);
        ref.child("Chats").push().setValue(msg);

        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(sender)
                .child(receiver);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message chat = snapshot.getValue(Message.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }

                    adapter = new MessageAdapter(mChat, getBaseContext(), imageurl);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
