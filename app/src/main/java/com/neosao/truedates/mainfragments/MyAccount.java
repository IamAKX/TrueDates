package com.neosao.truedates.mainfragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.SliderAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.FeatureSliderModel;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.EditProfile;
import com.neosao.truedates.screens.Settings;
import com.neosao.truedates.screens.ViewProfile;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAccount extends Fragment {

    View rootLayout;
    private SliderView sliderView;
    ImageView profile_image;
    LocalPref localPref;
    UserModel user;
    TextView name, workIndustry, university;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_my_account, container, false);

        sliderView = rootLayout.findViewById(R.id.slider_view);
        profile_image = rootLayout.findViewById(R.id.profile_image);
        name = rootLayout.findViewById(R.id.name);
        workIndustry = rootLayout.findViewById(R.id.workIndustry);
        university = rootLayout.findViewById(R.id.university);

        localPref = new LocalPref(getContext());
        user = localPref.getUser();

        new LoadFeatureSlider().execute();

        rootLayout.findViewById(R.id.editProfileButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });

        rootLayout.findViewById(R.id.settingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Settings.class));
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ViewProfile.class));
            }
        });
        new LoadProfile().execute();
        return rootLayout;
    }

    private class LoadProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.GET_USER_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject object = new JSONObject(response);
                                if (object.getString("status").equals("200")) {
                                    user = new Gson().fromJson(object.getJSONObject("result").getJSONObject("member").toString(), UserModel.class);
                                    localPref.saveUser(user);
                                    Glide.with(getContext())
                                            .load(user.getDefaultPhoto())
                                            .placeholder(R.drawable.in_love)
                                            .into(profile_image);
                                    name.setText(user.getName() + ", " + user.getAge());
                                    workIndustry.setText(user.getMemberWork().get(0).getIndustryName());
                                    university.setText(user.getMemberWork().get(0).getUniversityName());
                                } else
                                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Log.e("check", new String(networkResponse.data));
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", user.getUserId());
                    Log.e("check", "Reg req body : " + params.toString());
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

    @Override
    public void onResume() {
        super.onResume();
        user = localPref.getUser();
        Glide.with(getContext())
                .load(user.getDefaultPhoto())
                .placeholder(R.drawable.in_love)
                .into(profile_image);
        name.setText(user.getName() + ", " + user.getAge());
        workIndustry.setText(user.getMemberWork().get(0).getIndustryName());
        university.setText(user.getMemberWork().get(0).getUniversityName());
    }

    private class LoadFeatureSlider extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.GET, API.FEATURE_SLIDER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("check", "LoadFeatureSlider: "+response );
                                JSONObject object = new JSONObject(response);
                                if (object.getString("status").equals("200")) {
                                    ArrayList<FeatureSliderModel> list = new ArrayList<>();
                                    if (object.has("result") && object.getJSONObject("result").has("features")) {
                                        JSONArray array = object.getJSONObject("result").getJSONArray("features");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject item = array.getJSONObject(i);
                                            FeatureSliderModel model = new Gson().fromJson(item.toString(), FeatureSliderModel.class);
                                            list.add(model);
                                        }

                                        final SliderAdapter adapter = new SliderAdapter(getActivity(), list);

                                        sliderView.setSliderAdapter(adapter);

                                        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
                                        sliderView.startAutoCycle();
                                    } else
                                        Toast.makeText(getContext(), "Unable to load feature slider", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Log.e("check", new String(networkResponse.data));
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    Log.e("check", "Reg req body : " + params.toString());
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