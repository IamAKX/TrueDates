package com.neosao.truedates.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.InstagramMediaAdapter;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.InstagramMediaModel;
import com.neosao.truedates.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ViewProfile extends AppCompatActivity {

    private static int PROGRESS_COUNT = 1;

    private ImageView image;
    View reverse;
    View skip;

    TextView name, age, workIndustry, education, showMe, about, instagramImageCount;
    UserModel userModel;
    private int counter = 0;
    private String[] resources;
    LinearLayout storyProgressContainer, instagramLayout;
    RecyclerView instagramRecyclerView;
    ArrayList<InstagramMediaModel> instagramMediaArrayList = new ArrayList<>();
    long pressTime = 0L;
    long limit = 500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_profile);


        initializeComponents();
    }

    private void initializeComponents() {
        userModel = new LocalPref(getBaseContext()).getUser();
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        workIndustry = findViewById(R.id.workIndustry);
        education = findViewById(R.id.education);
        showMe = findViewById(R.id.showMe);
        about = findViewById(R.id.about);
        storyProgressContainer = findViewById(R.id.storyProgressContainer);
        instagramImageCount = findViewById(R.id.instagramImageCount);
        instagramLayout = findViewById(R.id.instagramLayout);
        instagramRecyclerView = findViewById(R.id.instagramRecyclerView);

        instagramLayout.setVisibility(View.GONE);
        // bind reverse view
        reverse = findViewById(R.id.reverse);
        // bind skip view
        skip = findViewById(R.id.skip);

        initStory();

        name.setText(userModel.getName() + ",");
        age.setText(userModel.getAge());
        workIndustry.setText(userModel.getMemberWork().get(0).getIndustryName());
        education.setText(userModel.getMemberWork().get(0).getUniversityName());
        showMe.setText(userModel.getGender());
        about.setText(userModel.getAbout());

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), EditProfile.class));
            }
        });

        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reverse();
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });

        // Instagram Logic
        if(null != userModel.getMembersettings().get(0).getIsInstagramActive() && null != userModel.getMembersettings().get(0).getInstagramDetails() && userModel.getMembersettings().get(0).getIsInstagramActive().equals("1"))
        {
            instagramMediaArrayList.clear();
            instagramLayout.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(userModel.getMembersettings().get(0).getInstagramDetails());
                String user_id = jsonObject.getString("user_id");
                String longLivedToken = jsonObject.getString("longLivedToken");

                getUserDetails(user_id, longLivedToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void skip() {
        ++counter;
        if(counter == resources.length)
            counter = 0;
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);
        updateProgessInidicator();
    }

    private void reverse() {
        --counter;
       if(counter < 0)
           counter = resources.length-1;
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);
        updateProgessInidicator();
    }

    private void initStory() {
        resources = new String[userModel.getMemberPhotos().length];
        for (int i = 0; i < userModel.getMemberPhotos().length; i++) {
            if(null != userModel.getMemberPhotos()[i] && null != userModel.getMemberPhotos()[i].getMemberPhoto())
                resources[i] = (userModel.getMemberPhotos()[i].getMemberPhoto());
        }
        PROGRESS_COUNT = userModel.getMemberPhotos().length;
        storyProgressContainer.setWeightSum(PROGRESS_COUNT);
        for (int i = 0; i < PROGRESS_COUNT; i++) {
            View priceItemView = LayoutInflater.from(this).inflate(R.layout.story_progress_view, storyProgressContainer, false);
            TextView view = priceItemView.findViewById(R.id.tv);
            view.setBackgroundColor(getResources().getColor(R.color.hintColor));
            storyProgressContainer.addView(priceItemView);
        }
        counter = 0;
        updateProgessInidicator();
        image = (ImageView) findViewById(R.id.image);
        Glide.with(getBaseContext())
                .load(resources[counter])
                .into(image);

    }

    private void updateProgessInidicator() {
        for (int i = 0; i < PROGRESS_COUNT; i++) {
            View priceItemView = storyProgressContainer.getChildAt(i);
            TextView view = priceItemView.findViewById(R.id.tv);
            if(i == counter)
                view.setBackgroundColor(getResources().getColor(R.color.themePink));
            else
            view.setBackgroundColor(getResources().getColor(R.color.hintColor));
        }
    }


    private void getUserDetails(String user_id, String access_token) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        String userDataUrl = "https://graph.instagram.com/me/media?fields=id,caption&access_token=" + access_token;

        String userMediaUrl = "graph.facebook.com/" + user_id + "/media";//?access_token=" + access_token;



        Volley.newRequestQueue(this).add(
                new StringRequest(
                        Request.Method.GET,
                        userDataUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jsonData = null;
                                try {
                                    jsonData = new JSONObject(response);

                                    Log.e("media", jsonData.toString(2));
                                    JSONArray data = jsonData.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                        getInstaImage(data.getJSONObject(i).getString("id"), user_id, access_token);
                                    }
                                    pd.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                )

        );

    }

    private void getInstaImage(String media_id, String user_id, String access_token) {

        String userMediaUrl = "https://graph.instagram.com/"+media_id+"?fields=id,media_type,media_url,username,timestamp&access_token="+access_token;


        Volley.newRequestQueue(this).add(
                new StringRequest(
                        Request.Method.GET,
                        userMediaUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONObject jsonData = null;
                                try {
                                    jsonData = new JSONObject(response);
                                    Log.e("check","data : "+jsonData.toString(2));

                                    InstagramMediaModel mediaModel = new Gson().fromJson(jsonData.toString(),InstagramMediaModel.class);
                                    if(mediaModel.getMedia_type().equals("IMAGE") || mediaModel.getMedia_type().equals("CAROUSEL_ALBUM"))
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                instagramMediaArrayList.add(mediaModel);
                                                instagramImageCount.setText(instagramMediaArrayList.size()+" Instagram Images");
//                                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
//                                                instagramRecyclerView.setLayoutManager(mLayoutManager);
                                                instagramRecyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(),3));
                                                InstagramMediaAdapter mediaAdapter = new InstagramMediaAdapter(getBaseContext(),instagramMediaArrayList);
                                                instagramRecyclerView.setAdapter(mediaAdapter);
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                )

        );
    }
}