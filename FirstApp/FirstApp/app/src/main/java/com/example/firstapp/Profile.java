package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    Button saveButton;
    EditText nameSurname, email, entry, grad, phone, social, country, city, company;
    String nameStr, surnameStr, emailStr, entryStr, gradStr, phoneStr, socialStr, countryStr, cityStr, companyStr,degree;
    RadioGroup radio;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    ImageView imgView;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameSurname =findViewById(R.id.p_et_name_surname);
        email = findViewById(R.id.p_et_email);
        entry = findViewById(R.id.p_et_entryYear);
        grad = findViewById(R.id.p_et_gradYear);
        phone=findViewById(R.id.p_et_phone);
        social = findViewById(R.id.p_et_socialMedia);
        country = findViewById(R.id.p_et_country);
        city = findViewById(R.id.p_et_city);
        company = findViewById(R.id.p_et_company);
        saveButton = findViewById(R.id.bt_save);
        radio = findViewById(R.id.radioGroup);
        imgView = findViewById(R.id.profilePhoto);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radio_bachelor:
                        degree = "Bachelor";
                        break;
                    case R.id.radio_master:
                        degree = "Master";
                        break;
                    case R.id.radio_phd:
                        degree = "PhD";
                        break;
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ref.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HelperClass profileUser = new HelperClass();
                profileUser = snapshot.getValue(HelperClass.class);
                if(profileUser.getphotoUri() != null)
                    Glide.with(Profile.this).load(profileUser.getphotoUri()).into(imgView);
                nameSurname.setText(profileUser.getName()+" "+profileUser.getSurname());
                email.setText(profileUser.getEmail());
                entry.setText(profileUser.getEntryDate());
                grad.setText(profileUser.getGraduateDate());
                phone.setText(profileUser.getPhone());
                social.setText(profileUser.getSocial());
                country.setText(profileUser.getCountry());
                city.setText(profileUser.getCity());
                company.setText(profileUser.getCompany());

                if(profileUser.getDegree() != null){
                    if(profileUser.getDegree().equals("Bachelor")){
                        RadioButton rb1 =findViewById(R.id.radio_bachelor);
                        rb1.setChecked(true);
                    }else if(profileUser.getDegree().equals("Master")){
                        RadioButton rb2 =findViewById(R.id.radio_master);
                        rb2.setChecked(true);
                    }else if(profileUser.getDegree().equals("PhD")){
                        RadioButton rb3 =findViewById(R.id.radio_phd);
                        rb3.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] tmp = nameSurname.getText().toString().trim().split("\\s+");
                nameStr = tmp[0];
                surnameStr = tmp[1];
                emailStr = email.getText().toString().trim();
                entryStr = entry.getText().toString().trim();
                gradStr = grad.getText().toString().trim();
                phoneStr = phone.getText().toString().trim();
                socialStr = social.getText().toString().trim();
                countryStr = country.getText().toString().trim();
                cityStr = city.getText().toString().trim();
                companyStr = company.getText().toString().trim();

                HashMap data = new HashMap();
                data.put("name",nameStr);
                data.put("surname",surnameStr);
                data.put("email",emailStr);
                data.put("entryDate",entryStr);
                data.put("graduateDate",gradStr);
                data.put("phone",phoneStr);
                data.put("social",socialStr);
                data.put("country",countryStr);
                data.put("city",cityStr);
                data.put("company",companyStr);
                data.put("degree",degree);
                ref.child("users").child(user.getUid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                            Toast.makeText(Profile.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(Profile.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(Profile.this,MainScreen.class);
                startActivity(intent);
            }
        });

    }
}
