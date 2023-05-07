package com.example.firstapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.databinding.ActivityMainBinding;

public class SendMail extends AppCompatActivity {

    EditText subject,msg;
    String tmp;
    TextView to;

    Button send;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        send =findViewById(R.id.sendMailButton);
        subject = findViewById(R.id.edit_subject);
        msg = findViewById(R.id.edit_message);
        to = findViewById(R.id.edit_to);

        tmp = getIntent().getStringExtra("mail");
        to.setText(tmp);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dest = to.getText().toString();
                String sub = subject.getText().toString();
                String message = msg.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, dest);
                intent.putExtra(Intent.EXTRA_SUBJECT, sub);
                intent.putExtra(Intent.EXTRA_TEXT, message);

                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent,"Choose an e-mail client"));
            }
        });
    }

}
