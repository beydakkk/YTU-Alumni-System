package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Filter extends AppCompatActivity {

    EditText filter ;
    Button cancelBt, filterBt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filter = findViewById(R.id.et_filterBox);
        cancelBt = findViewById(R.id.filter_cancel);
        filterBt = findViewById(R.id.filter_filter);

        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Filter.this,SearchAlumni.class);
                startActivity(intent);
            }
        });

        filterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Filter.this,SearchAlumni.class);
                intent.putExtra("filter",filter.getText().toString());
                startActivity(intent);
            }
        });


    }
}
