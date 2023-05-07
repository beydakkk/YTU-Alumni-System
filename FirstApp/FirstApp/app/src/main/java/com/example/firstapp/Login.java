package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText email, password;
    String emailStr, passwordStr,uid;
    Button login, register;
    TextView forgotPassword;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    FirebaseDatabase database;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.btn_register);
        forgotPassword = findViewById(R.id.forgot_password);
        ref = FirebaseDatabase.getInstance().getReference();


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

        register.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),Register.class);
            startActivity(intent);
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();

                if(!emailStr.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                    if(!passwordStr.isEmpty()){
                        auth.signInWithEmailAndPassword(emailStr,passwordStr)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, MainScreen.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        password.setError("Password can not be empty!");
                    }
                }else if(emailStr.isEmpty())
                    email.setError("E-mail can not be empty!");
                else
                    email.setError("Not a valid e-mail!");
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.filter_filter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();

                        if(TextUtils.isEmpty(userEmail) | !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this, "Enter your registered e-mail", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Check your e-mail!", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(Login.this, "Unable to send verification mail!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                });
                dialogView.findViewById(R.id.filter_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();
            }
        });


    }

    public Boolean validateEmail(){
        String val = email.getText().toString();
        if(val.isEmpty()){
            email.setError("E-mail can not be empty!");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = password.getText().toString();
        if(val.isEmpty()){
            password.setError("Password can not be empty!");
            return false;
        }  else{
            password.setError(null);
            return true;
        }
    }

    public void checkUser(){
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(emailStr);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    email.setError(null);
                    String passwordFromDb = snapshot.child("password").getValue(String.class);
                    Log.d("a",passwordFromDb);
                    if(!Objects.equals(passwordFromDb,passwordStr)){
                        email.setError(null);
                        Intent intent = new Intent(Login.this, MainScreen.class);
                        startActivity(intent);
                    }else{
                        password.setError("Invalid Credential!");
                        password.requestFocus();
                    }
                }else{
                    email.setError("User doesn't exist!");
                    email.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}