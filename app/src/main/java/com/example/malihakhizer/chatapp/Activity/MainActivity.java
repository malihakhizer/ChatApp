package com.example.malihakhizer.chatapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malihakhizer.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailED;
    EditText passwordED;
    TextView createAccount;
    Button loginButton;


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        initViews();
        auth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignUp.class);
                startActivity(i);
            }
        });

    }
    public void initViews() {
        emailED = findViewById(R.id.email);
        passwordED = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginbutton);
        createAccount = findViewById(R.id.create_account);

    }

    public void Login() {

        String email = emailED.getText().toString().trim();
        String password = passwordED.getText().toString().trim();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "User logged in", Toast.LENGTH_SHORT).show();

                            // A whole user line in firebase db, represents a Firebase User
                            updateUI();


                        } else {

                            Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



    public void updateUI() {
        // and since user has been logged in so we can access the current user from any activity
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            //  Toast.makeText(this, "User not Logged in", Toast.LENGTH_SHORT).show();
            return;
        } else {

            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }
}
