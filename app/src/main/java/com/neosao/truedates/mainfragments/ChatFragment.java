package com.neosao.truedates.mainfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.LikeAdapter;
import com.neosao.truedates.adapters.MessageListAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.model.Match;
import com.neosao.truedates.model.MessageItem;
import com.neosao.truedates.model.UserBasicDetails;
import com.neosao.truedates.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ChatFragment extends Fragment {

    View rootLayout;
    private List<MessageItem> messageList = new ArrayList<>();
    private MessageListAdapter mAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private EditText edit_name;
    public LikeAdapter contactAdapter;
    UserModel user;
    RelativeLayout messageLoader,contactLoader;
    private List<Match> matchList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_chat, container, false);
        RecyclerView recyclerView = rootLayout.findViewById(R.id.recycler_view_messages);
        messageList = new ArrayList<>();
        mAdapter = new MessageListAdapter(getContext(), messageList);
        edit_name = rootLayout.findViewById(R.id.edit_name);
        contactLoader = rootLayout.findViewById(R.id.contactLoader);
        messageLoader = rootLayout.findViewById(R.id.messageLoader);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);



        contactAdapter = new LikeAdapter(getContext(), matchList);

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
        messageLoader.setVisibility(View.VISIBLE);
        database.getReference("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (null != snapshot) {
                    MessageItem model = snapshot.getValue(MessageItem.class);
                    messageList.add(model);
                    mAdapter.notifyDataSetChanged();
                    messageLoader.setVisibility(View.GONE);
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
//        new LoadMatchedProfiles().execute();

    }



    @Override
    public void onResume() {
        super.onResume();
        prepareMessageList();
        user = new LocalPref(getContext()).getUser();
        matchList.clear();
        new LoadMatchedProfiles().execute();
        contactLoader.setVisibility(View.VISIBLE);
    }

    private class LoadMatchedProfiles extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.GET_RECENT_MATCHED_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject object = new JSONObject(response);

                                if (object.has("status") && object.getString("status").equals("200")) {
                                    if (object.has("result") && object.getJSONObject("result").has("matchingProfiles")) {
                                        matchList.clear();
                                        JSONArray array = object.getJSONObject("result").getJSONArray("matchingProfiles");
                                        if(null != array && array.length() > 0) {
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject item = array.getJSONObject(i);
                                                item = item.getJSONObject("member");
                                                Match match = new Gson().fromJson(item.toString(), Match.class);
                                                matchList.add(match);
                                                contactAdapter.notifyDataSetChanged();
                                            }
                                            Log.i("check", "onResponse: "+array.length());

                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(),"No matching profile found", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    if (object.has("message") && null != object.getString("message"))
                                        Toast.makeText(getContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                            contactLoader.setVisibility(View.GONE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            contactLoader.setVisibility(View.GONE);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getContext(),new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId",user.getUserId());

                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }


}