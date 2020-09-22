package com.neosao.truedates.mainfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neosao.truedates.R;
import com.neosao.truedates.adapters.LikeAdapter;
import com.neosao.truedates.adapters.MessageListAdapter;
import com.neosao.truedates.model.Like;
import com.neosao.truedates.model.MessageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ChatFragment extends Fragment {

    View rootLayout;
    private List<MessageItem> messageList;
    private List<Like> likeList;
    private MessageListAdapter mAdapter;
    private String[] messages = {"Ah ok", "Just out of habit anyway", "Hey!", "6946743263", "Give me your number, I will call you"};
    private int[] counts = {0, 3, 0, 0, 1};
    private int[] messagePictures = {R.drawable.user_woman_3, R.drawable.user_woman_4, R.drawable.user_woman_5, R.drawable.user_woman_6 , R.drawable.user_woman_7};
    private int[] likePictures = {R.drawable.user_woman_1, R.drawable.user_woman_2};
    private String[] messageNames = {"Wonder Women", "Black Widow", "Gamora", "Scarlet Witch", "Captain Marvel"};
    private String[] likeNames = {"Nebula", "Mantis"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout =  inflater.inflate(R.layout.fragment_chat, container, false);
        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view_messages);
        messageList = new ArrayList<>();
        mAdapter = new MessageListAdapter(getContext(), messageList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        prepareMessageList();


        prepareContactList();
        LikeAdapter contactAdapter = new LikeAdapter(getContext(), likeList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewContact =  rootLayout.findViewById(R.id.recycler_view_likes);
        recyclerViewContact.setLayoutManager(layoutManager);
        recyclerViewContact.setAdapter(contactAdapter);
        //new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerViewContact));


        return rootLayout;
    }

    private void prepareMessageList(){

        Random rand = new Random();
        int id = rand.nextInt(100);
        int i;
        for(i=0; i<5; i++) {
            MessageItem message = new MessageItem(id, messageNames[i], messages[i], counts[i], messagePictures[i]);
            messageList.add(message);
        }
    }

    private void prepareContactList(){
        likeList = new ArrayList<>();
        Random rand = new Random();
        int id = rand.nextInt(100);
        int i;
        for(i=0; i<2; i++) {
            Like like = new Like(id, likeNames[i], likePictures[i]);
            likeList.add(like);
        }
    }
}