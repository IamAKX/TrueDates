package com.neosao.truedates.mainfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.MatchListAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.Match;
import com.neosao.truedates.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeedFragment extends Fragment {

    View rootLayout;
    private List<Match> matchList;
    private MatchListAdapter mAdapter;
    UserModel user;
    RecyclerView recyclerView;
    LinearLayout notfound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout =  inflater.inflate(R.layout.fragment_feed, container, false);


        recyclerView = rootLayout.findViewById(R.id.recycler_view_matchs);
        notfound = rootLayout.findViewById(R.id.notfound);
        matchList = new ArrayList<>();

        notfound.setVisibility(View.GONE);
        return rootLayout;
    }

    private void setRecyclerView() {
        mAdapter = new MatchListAdapter(getContext(), matchList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        user = new LocalPref(getContext()).getUser();
        new LoadMatchedProfiles().execute();

    }

    private class LoadMatchedProfiles extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notfound.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.GET_MATCHED_PROFILE,
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
                                            }
                                            Log.i("check", "onResponse: "+array.length());
                                            setRecyclerView();

                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(),"No matching profile found", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    notfound.setVisibility(View.GONE);
                                } else {
                                    notfound.setVisibility(View.VISIBLE);
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                notfound.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            notfound.setVisibility(View.VISIBLE);
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
                    params.put("age",user.getAge());
                    params.put("gender",user.getGender());
                    params.put("lookingFor",user.getLookingFor());
                    params.put("motherTounge",user.getMotherTounge());
                    params.put("interestCode", Utils.getInterestCodeList(user));
                    params.put("latitude", user.getMembersettings().get(0).getLatitude());
                    params.put("longitude", user.getMembersettings().get(0).getLongitude());
                    params.put("currentLocation", user.getMembersettings().get(0).getCurrentLocation());
                    params.put("maxDistance", (user.getMembersettings().get(0).getMaxDistance() == null )? "100":user.getMembersettings().get(0).getMaxDistance());

                    params.put("offset","0");

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