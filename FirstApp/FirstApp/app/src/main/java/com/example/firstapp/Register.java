package com.example.firstapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText name, surname, regPassword, confPassword, entryYear, graduateYear, email;
    String nameStr, surnameStr, regPasswordStr, confPasswordStr, emailStr, entryYearStr, graduateYearStr,uid;
    Button submitButton;
    FloatingActionButton uploadButton;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    Uri uri;
    ImageView captureImage;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @SuppressLint({"MissingInflatedId", "SuspiciousIndentation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.et_name);
        surname = findViewById(R.id.et_surname);
        entryYear = findViewById(R.id.et_entryYear);
        graduateYear = findViewById(R.id.et_gradYear);
        email = findViewById(R.id.et_email);
        regPassword = findViewById(R.id.et_password);
        confPassword = findViewById(R.id.et_confirmPassword);

        submitButton = findViewById(R.id.submitButton);
        uploadButton = findViewById(R.id.uploadButton);

        ref = FirebaseDatabase.getInstance().getReference();
        captureImage = findViewById(R.id.imageView);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(Register.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = name.getText().toString().trim();
                surnameStr = surname.getText().toString().trim();
                emailStr = email.getText().toString().trim();
                regPasswordStr = regPassword.getText().toString().trim();
                confPasswordStr = confPassword.getText().toString().trim();
                entryYearStr = entryYear.getText().toString().trim();
                graduateYearStr = graduateYear.getText().toString().trim();


                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                ref = database.getReference("users");



                if(!TextUtils.isEmpty(nameStr) && !TextUtils.isEmpty(surnameStr) && !TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(regPasswordStr) &&
                        !TextUtils.isEmpty(confPasswordStr) && entryYearStr!=null && graduateYearStr!=null && regPasswordStr.equals(confPasswordStr)){
                    if(uri != null){
                        uploadToFirebase(uri);
                        Log.d("test",uri.toString());
                        auth.createUserWithEmailAndPassword(emailStr,regPasswordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user = auth.getCurrentUser();
                                    uid = user.getUid();

                                    HelperClass helperClass = new HelperClass(nameStr, surnameStr, emailStr, entryYearStr, graduateYearStr);
                                    helperClass.setphotoUri(uri.toString());
                                    ref.child(uid).setValue(helperClass);
                                    Toast.makeText(Register.this, "Registration Successfull!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this,Login.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    }else
                        Toast.makeText(Register.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Register.this, "Fill the blanks!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        Log.d("test",uri.toString());
        captureImage.setImageURI(uri);
    }

    private void uploadToFirebase(Uri uri){
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        user = auth.getCurrentUser();
                        uid = user.getUid();
                        ref.child(uid).child("photoUri").setValue(uri.toString());

                        Toast.makeText(Register.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}

