package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.neosao.truedates.R;
import com.neosao.truedates.model.ChatMetadataModel;

public class Chat extends AppCompatActivity {

    private Toolbar toolbar;
    ChatMetadataModel chatMetadataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatMetadataModel = (ChatMetadataModel)getIntent().getSerializableExtra("chatMetadata");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(chatMetadataModel.getParticipants().getUserName());
    }
}