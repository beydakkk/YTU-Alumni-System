package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchAlumni extends AppCompatActivity implements RC_Interface{

    DatabaseReference ref;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser,firebaseUser2;
    RecyclerView recyclerView;
    HashMap users;
    ArrayList<SearchRecyclerModel> arrayList;
    SearchRcAdapter rcAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_alumni);

        String tmp = getIntent().getStringExtra("filter");


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        ref=FirebaseDatabase.getInstance().getReference();


        recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        arrayList=new ArrayList<>();
        rcAdapter=new SearchRcAdapter(this,arrayList,this);
        recyclerView.setAdapter(rcAdapter);

        users = new HashMap();

        ref.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot s : snapshot.getChildren()){
                    HelperClass user = s.getValue(HelperClass.class);
                    if(tmp == null)
                        users.put(s.getKey().toString(),user);
                    else{
                        if(tmp.equals(user.getCountry()))
                            users.put(s.getKey().toString(),user);
                    }

                }
                users.forEach((k,v) -> insertRCModel((String) k, (HelperClass) v,arrayList));
                rcAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
    public void insertRCModel (String key, HelperClass u,ArrayList<SearchRecyclerModel> arrayList ){
        SearchRecyclerModel rcModel = new SearchRecyclerModel(u.getName(),u.getSurname(), u.getphotoUri());
        rcModel.setUid(key);
        arrayList.add(rcModel);
    }
    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(SearchAlumni.this,SelectedProfile.class);
        SearchRecyclerModel rcModel1 = arrayList.get(position);
        intent.putExtra("transfer",rcModel1.uid.toString());
        startActivity(intent);
    }
}
