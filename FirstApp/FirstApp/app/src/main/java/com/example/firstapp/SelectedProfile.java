package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SelectedProfile extends AppCompatActivity {

    TextView nameSurname, email, entry, grad, phone, social, country, city, company;
    DatabaseReference mDatabase;
    Button back,contactButton;
    RadioGroup radio;
    ImageView ppImg;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_profile);
        String uid = getIntent().getStringExtra("transfer");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        radio = findViewById(R.id.s_radioGroup);
        nameSurname=findViewById(R.id.s_p_et_name_surname);
        email=findViewById(R.id.s_p_et_email);
        entry=findViewById(R.id.s_p_et_entryYear);
        grad=findViewById(R.id.s_p_et_gradYear);
        phone=findViewById(R.id.s_p_et_phone);
        social=findViewById(R.id.s_p_et_socialMedia);
        country=findViewById(R.id.s_p_et_country);
        city=findViewById(R.id.s_p_et_city);
        company=findViewById(R.id.s_p_et_company);
        ppImg = findViewById(R.id.s_profilePhoto);
        contactButton = findViewById(R.id.bt_contact);


        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HelperClass profileUser = new HelperClass();
                profileUser = snapshot.getValue(HelperClass.class);
                if(profileUser.getphotoUri() != null)
                    Glide.with(SelectedProfile.this).load(profileUser.getphotoUri()).into(ppImg);
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
                        RadioButton rb1 =findViewById(R.id.s_radio_bachelor);
                        rb1.setChecked(true);
                    }else if(profileUser.getDegree().equals("Master")){
                        RadioButton rb2 =findViewById(R.id.s_radio_master);
                        rb2.setChecked(true);
                    }else if(profileUser.getDegree().equals("PhD")){
                        RadioButton rb3 =findViewById(R.id.s_radio_phd);
                        rb3.setChecked(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        contactButton.setOnClickListener(view -> {
            Intent intent = new Intent(SelectedProfile.this,SendMail.class);
            intent.putExtra("mail",email.getText().toString());
            startActivity(intent);
        });

    }
}
