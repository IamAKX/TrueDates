package com.neosao.truedates.mainfragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.LikeAdapter;
import com.neosao.truedates.adapters.MessageListAdapter;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.Like;
import com.neosao.truedates.model.MessageItem;
import com.neosao.truedates.model.UserBasicDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class ChatFragment extends Fragment {

    View rootLayout;
    private List<MessageItem> messageList;
    private List<Like> likeList;
    private MessageListAdapter mAdapter;
    private int[] likePictures = {R.drawable.user_woman_1, R.drawable.user_woman_2};
    private String[] likeNames = {"Nebula", "Mantis"};
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private EditText edit_name;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_chat, container, false);
        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view_messages);
        messageList = new ArrayList<>();
        mAdapter = new MessageListAdapter(getContext(), messageList);
        edit_name = rootLayout.findViewById(R.id.edit_name);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

//        prepareMessageList();


        prepareContactList();
        LikeAdapter contactAdapter = new LikeAdapter(getContext(), likeList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewContact = rootLayout.findViewById(R.id.recycler_view_likes);
        recyclerViewContact.setLayoutManager(layoutManager);
        recyclerViewContact.setAdapter(contactAdapter);
        //new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerViewContact));

        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty())
                    prepareMessageList();
                else {

                    Iterator<MessageItem> iter = messageList.iterator();
                    List<MessageItem> temp = new ArrayList();

                    while (iter.hasNext()) {
                        MessageItem mi = iter.next();
                        UserBasicDetails friend = null;
                        for (UserBasicDetails u : mi.getParticipants()) {
                            if (u.getUserID() != new LocalPref(getContext()).getUser().getUserId()) {
                                friend = u;
                                break;
                            }
                        }
                        if (friend.getUserName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            temp.add(mi);

                        }
                        mAdapter.updateList(temp);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootLayout;
    }

    private void prepareMessageList() {
        messageList.clear();
        database.getReference("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (null != snapshot) {
                    MessageItem model = snapshot.getValue(MessageItem.class);
                    Log.e("check", "MessageItem : " + model.toString());
                    messageList.add(model);
                    mAdapter.notifyDataSetChanged();
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

    }


    private void prepareContactList() {
        likeList = new ArrayList<>();
        Random rand = new Random();
        int id = rand.nextInt(100);
        int i;
        for (i = 0; i < 2; i++) {
            Like like = new Like(id, likeNames[i], likePictures[i]);
            likeList.add(like);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareMessageList();
    }
}