package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.OnboardingAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.ResponseParser;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.FirebaseUserModel;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.onboardingfragments.Habits;
import com.neosao.truedates.onboardingfragments.Intoduction;
import com.neosao.truedates.onboardingfragments.Others;
import com.neosao.truedates.onboardingfragments.Personal;
import com.neosao.truedates.onboardingfragments.Photos;
import com.neosao.truedates.onboardingfragments.Work;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OnboardingData extends AppCompatActivity {
    ViewPager viewpager;
    Button nextBtn;
    Button prevBtn;
    SpringDotsIndicator springDotsIndicator;
    public static FirebaseUserModel firebaseUser;
    public static UserModel user;
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_data);

        firebaseUser = new LocalPref(getBaseContext()).getFirebaseUser();
        user = new UserModel();
        user.setUsername(firebaseUser.getName());
        user.setEmail(firebaseUser.getEmail());
        user.setContactNumber(firebaseUser.getPhoneNumber());
        user.setFirebaseId(firebaseUser.getFirebaseUUID());
        user.setRegisterType(firebaseUser.getLoginProvider());
        user.setProfileImage(firebaseUser.getProfileImage());

        initializeComponents();
        dialog = Utils.getProgress(OnboardingData.this, "Loading details...");
        new LoadDynamicOptionLists().execute();

    }

    private void initializeComponents() {
        viewpager = findViewById(R.id.viewpager);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        springDotsIndicator = findViewById(R.id.spring_dots_indicator);

        OnboardingAdapter adapter = new OnboardingAdapter(getSupportFragmentManager());
        adapter.addFrag(new Intoduction());
        adapter.addFrag(new Work());
        adapter.addFrag(new Personal());
        adapter.addFrag(new Habits());
        adapter.addFrag(new Others());
        adapter.addFrag(new Photos());
        viewpager.setAdapter(adapter);

        springDotsIndicator.setViewPager(viewpager);
        viewpager.setOnPageChangeListener(new PageListener());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager.getCurrentItem() < 5)
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                else
                    startActivity(new Intent(getBaseContext(), HomeContainer.class));
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager.getCurrentItem() > 0)
                    viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
            }
        });
    }

    int currentPage;

    class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            Log.e("check", "current : " + position);
            if (position == 5)
                nextBtn.setText("Save");
            else
                nextBtn.setText("Next");
        }
    }

    private class LoadDynamicOptionLists extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, API.GET_ALL_OPTIONS_LIST, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("status").equalsIgnoreCase("200"))
                                    ResponseParser.parseGetAllOption(response);
                                else
                                    Toast.makeText(getBaseContext(), "Faild to load data", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismissWithAnimation();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismissWithAnimation();
                    NetworkResponse networkResponse = error.networkResponse;
                    if (error.networkResponse != null && new String(networkResponse.data) != null) {
                        if (new String(networkResponse.data) != null) {
                            Log.e("check", new String(networkResponse.data));
                        }
                    }
                }
            });

            jsonObjectRequest.setShouldCache(false);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }
}