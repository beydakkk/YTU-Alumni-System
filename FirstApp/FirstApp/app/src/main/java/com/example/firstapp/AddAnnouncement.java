package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddAnnouncement extends AppCompatActivity {

    EditText title, announcement, date;
    FloatingActionButton share, upload;
    String titleStr, announcementStr, dateStr;
    DatabaseReference ref;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String uid;
    Uri uri;
    FirebaseUser user;
    ImageView announceImage;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        title = findViewById(R.id.et_title);
        announcement = findViewById(R.id.et_announcement);
        date = findViewById(R.id.et_date);
        announceImage = findViewById(R.id.announceImage);

        share = findViewById(R.id.bt_announce);
        upload = findViewById(R.id.bt_uploadPhoto);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddAnnouncement.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadToFirebase(uri);


                titleStr = title.getText().toString().trim();
                announcementStr = announcement.getText().toString().trim();
                dateStr = date.getText().toString().trim();

                database = FirebaseDatabase.getInstance();
                ref = database.getReference("announces");

                if(!TextUtils.isEmpty(titleStr) && !TextUtils.isEmpty(announcementStr) && !TextUtils.isEmpty(dateStr)){
                    ref.child(titleStr).child("title").setValue(titleStr);
                    ref.child(titleStr).child("announce").setValue(announcementStr);
                    ref.child(titleStr).child("date").setValue(dateStr);

                    Intent intent = new Intent(AddAnnouncement.this,MainScreen.class);
                    startActivity(intent);

                    Intent intent2 = new Intent(AddAnnouncement.this,MainScreen.class);
                    startActivity(intent2);
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        Log.d("test",uri.toString());
        announceImage.setImageURI(uri);
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
                        ref.child(titleStr).child("image").setValue(uri.toString());

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());

                        Toast.makeText(AddAnnouncement.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAnnouncement.this, "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}
