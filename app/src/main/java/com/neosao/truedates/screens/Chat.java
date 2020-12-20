package com.neosao.truedates.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.ChatAdapter;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.ChatMetadataModel;
import com.neosao.truedates.model.MessageModel;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Chat extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference chatRoom;
    private Toolbar toolbar;
    private ChatMetadataModel chatMetadataModel;
    private EditText messageEditText;
    private FloatingActionButton picture, send;
    private UserModel user;
    private UserBasicDetails myBasicDetails;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ChatAdapter chatAdapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = new LocalPref(getBaseContext()).getUser();
        chatMetadataModel = (ChatMetadataModel)getIntent().getSerializableExtra("chatMetadata");
        chatRoom = database.getReference("message").child(chatMetadataModel.getChatRoomId());

        myBasicDetails = new UserBasicDetails(user.getUserId(),user.getName(),user.getDefaultPhoto(),user.getEmail());
        ArrayList<UserBasicDetails> participants = new ArrayList<>();
        participants.add(chatMetadataModel.getParticipants());
        participants.add(myBasicDetails);

        chatRoom.child("chatRoomId").setValue(chatMetadataModel.getChatRoomId());
        chatRoom.child("participants").setValue(participants);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(chatMetadataModel.getParticipants().getUserName());

        initViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        messageEditText = findViewById(R.id.messageEditText);
        send = findViewById(R.id.send);
        picture = findViewById(R.id.picture);
        send.setEnabled(false);



        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0)
                    send.setEnabled(false);
                else
                    send.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageEditText.getText().toString().length() > 0)
                    sendMessage(messageEditText.getText().toString());
            }
        });

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(getBaseContext(),messageList);

        chatRoom.child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue() != null) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    Log.e("checking",messageModel.toString());

                    messageList.add(messageModel);
                    chatAdapter.notifyDataSetChanged();
                    linearLayoutManager.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(chatAdapter);
    }



    private void sendMessage(String message) {
        DatabaseReference chatMessage = chatRoom.child("chats").push();
        MessageModel messageModel = new MessageModel(message,myBasicDetails.getUserID(),myBasicDetails.getUserName(), myBasicDetails.getProfileImage(), new Date(),chatMessage.getKey());
        chatMessage.setValue(messageModel);
        chatRoom.child("lastUpdateTimeStamp").setValue(messageModel.getMessageTimestamp());
        chatRoom.child("lastMessage").setValue(message);
        chatRoom.child("lastMessageSentBy").setValue(messageModel.getSenderID());
        messageEditText.setText("");
    }
}