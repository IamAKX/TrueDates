package com.neosao.truedates.screens;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.ChatAdapter;
import com.neosao.truedates.configs.Constants;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.ChatMetadataModel;
import com.neosao.truedates.model.MessageModel;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    ArrayList<Image> images = new ArrayList<>();


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

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .whenPermissionsGranted(new PermissionsGrantedListener() {
                            @Override
                            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                ImagePicker.with(Chat.this)
                                        .setFolderMode(true)
                                        .setFolderTitle("Album")
                                        .setDirectoryName("True Dates")
                                        .setMultipleMode(false)
                                        .setShowNumberIndicator(true)
                                        .setMaxSize(1)
                                        .setBackgroundColor("#ffffff")
                                        .setStatusBarColor("#E0E0E0")
                                        .setToolbarColor("#ffffff")
                                        .setToolbarIconColor("#FF6F8B")
                                        .setToolbarTextColor("#FF6F8B")
                                        .setProgressBarColor("#FF6F8B")
                                        .setIndicatorColor("#FF6F8B")
                                        .setShowCamera(true)
                                        .setDoneTitle("Select")
                                        .setLimitMessage("You can select up to 10 images")
                                        .setSelectedImages(images)
                                        .setRequestCode(100)
                                        .start();
                            }
                        })
                        .whenPermissionsRefused(new PermissionsRefusedListener() {
                            @Override
                            public void onPermissionsRefused(String[] permissions) {
                                Toast.makeText(getBaseContext(), "We need your permission to read the image", Toast.LENGTH_LONG).show();
                            }
                        })
                        .execute(Chat.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            images = ImagePicker.getImages(data);
            Uri imageURI = images.get(0).getUri();

            SweetAlertDialog progressDialog = Utils.getProgress(Chat.this, "Sending image");
            progressDialog.show();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = mStorageRef.child("images/"+System.currentTimeMillis()+".jpg");
            imageRef.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference chatMessage = chatRoom.child("chats").push();
                                    MessageModel messageModel = new MessageModel(uri.toString(),myBasicDetails.getUserID(),myBasicDetails.getUserName(), myBasicDetails.getProfileImage(), new Date(),chatMessage.getKey(), Constants.MESSAGE_TYPE_IMAGE);
                                    chatMessage.setValue(messageModel);
                                    chatRoom.child("lastUpdateTimeStamp").setValue(messageModel.getMessageTimestamp());
                                    chatRoom.child("lastMessage").setValue("Image");
                                    chatRoom.child("lastMessageSentBy").setValue(messageModel.getSenderID());
                                    messageEditText.setText("");
                                    progressDialog.dismissWithAnimation();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressDialog.dismissWithAnimation();
                        }
                    });
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void sendMessage(String message) {
        DatabaseReference chatMessage = chatRoom.child("chats").push();
        MessageModel messageModel = new MessageModel(message,myBasicDetails.getUserID(),myBasicDetails.getUserName(), myBasicDetails.getProfileImage(), new Date(),chatMessage.getKey(), Constants.MESSAGE_TYPE_TEXT);
        chatMessage.setValue(messageModel);
        chatRoom.child("lastUpdateTimeStamp").setValue(messageModel.getMessageTimestamp());
        chatRoom.child("lastMessage").setValue(message);
        chatRoom.child("lastMessageSentBy").setValue(messageModel.getSenderID());
        messageEditText.setText("");
    }
}