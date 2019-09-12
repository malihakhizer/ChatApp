package com.example.malihakhizer.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.R;
import com.example.malihakhizer.chatapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText emailED;
    private EditText passwordED;
    private EditText nameED;
    private Button signupButton;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);

        initViews();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("users");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });

    }

    public void initViews() {
        emailED = findViewById(R.id.email);
        passwordED = findViewById(R.id.password);
        nameED = findViewById(R.id.name);
        signupButton = findViewById(R.id.signupbutton);
    }

    public void createNewUser(){
        final String name = nameED.getText().toString().trim();
        final String email = emailED.getText().toString().trim();
        final String password = passwordED.getText().toString().trim();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUp.this, "User created!", Toast.LENGTH_SHORT).show();


                            FirebaseUser fuser = auth.getCurrentUser();
                            String key = fuser.getUid();
                            User user = new User(email,password,name,"",key);
                            dbRef.child(key).setValue(user);

                            SupdateUI();
                        } else {
                            Toast.makeText(SignUp.this, "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void SupdateUI() {
        // and since user has been logged in so we can access the current user from any activity
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            // Toast.makeText(this, "User not Logged in", Toast.LENGTH_SHORT).show();
            return;
        } else {
            auth.signOut();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
