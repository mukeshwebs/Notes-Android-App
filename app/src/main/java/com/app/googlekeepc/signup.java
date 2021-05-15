package com.app.googlekeepc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignupemail, msignuppass;
    private RelativeLayout msignup;
    private TextView mgotologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        msignupemail = findViewById(R.id.signupemail);
        msignuppass = findViewById(R.id.signuppass);
        msignup = findViewById(R.id.signup);
        mgotologin = findViewById(R.id.gotologin);


        firebaseAuth = FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = msignupemail.getText().toString().trim();
                String password = msignuppass.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 4) {
                    Toast.makeText(getApplicationContext(), "Password should be Greater then 7 Digit", Toast.LENGTH_SHORT).show();
                } else {
                    //register the user to the firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration Succesful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                                //it will check the email entered by the user is correct or not
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
            //send email verification
            private void sendEmailVerification() {

                //taking the instance of the current user
                FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();

                if (firebaseuser != null) {
                    firebaseuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "verification email is sent verifiy it and login again", Toast.LENGTH_SHORT).show();
                            //we have to signout the user and start the new activity of the login after registration
                            firebaseAuth.signOut();
                            finish();
                            startActivity(new Intent(signup.this, MainActivity.class));

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to send the verification mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}