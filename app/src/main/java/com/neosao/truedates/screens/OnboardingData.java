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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.OnboardingAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.ResponseParser;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.FirebaseUserModel;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.model.options.FieldOfStudy;
import com.neosao.truedates.model.options.Interest;
import com.neosao.truedates.model.options.WorkIndustry;
import com.neosao.truedates.onboardingfragments.Habits;
import com.neosao.truedates.onboardingfragments.Intoduction;
import com.neosao.truedates.onboardingfragments.Others;
import com.neosao.truedates.onboardingfragments.Personal;
import com.neosao.truedates.onboardingfragments.Photos;
import com.neosao.truedates.onboardingfragments.Work;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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


        initializeComponents();
//        dialog = Utils.getProgress(OnboardingData.this, "Loading, please wait ...");

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
//        adapter.addFrag(new Photos());
        viewpager.setAdapter(adapter);

        springDotsIndicator.setViewPager(viewpager);
        viewpager.setOnPageChangeListener(new PageListener());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager.getCurrentItem() < 4)
                {
                    if(validateInputs())
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                }
                else {
                    if (validateInputs())
                        new RegisterUser().execute();
//                    startActivity(new Intent(getBaseContext(), HomeContainer.class));
                }
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

    private boolean validateInputs() {
        // Page 0 validation
        if (null == user.getName() || user.getName().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.name.setError("Enter name");
            return false;
        }
        if (null == user.getEmail() || user.getEmail().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.email.setError("Enter email");
            return false;
        }
        if (!Utils.isValid(user.getEmail())) {
            viewpager.setCurrentItem(0);
            Intoduction.email.setError("Invalid email");
            return false;
        }
        if (null == user.getAboutMe() || user.getAboutMe().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.about.setError("Write about yourself");
            return false;
        }
        if (null == user.getGender() || user.getGender().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.gender.setError("Enter gender");
            return false;
        }
        if (null == user.getBirthDate() || user.getBirthDate().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.dob.setError("Enter date of birth");
            return false;
        }
        if (null == user.getCurrentLocation() || user.getCurrentLocation().isEmpty()) {
            viewpager.setCurrentItem(0);
            Intoduction.location.setError("Enter location");
            return false;
        }

        // Page 1 validation
        if (null == user.getUniversity() || user.getUniversity().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.university.setError("Enter university");
            return false;
        }
        if (null == user.getFieldOfStudy() || user.getFieldOfStudy().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.fieldOfStudy.setError("Enter field of study");
            return false;
        }
        if (null == user.getQualification() || user.getQualification().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.qualification.setError("Enter qualification");
            return false;
        }
        if (null == user.getWorkIndustry() || user.getWorkIndustry().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.workIndustry.setError("Enter work industry");
            return false;
        }
        if (null == user.getExperience() || user.getExperience().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.experience.setError("Enter experience");
            return false;
        }
        if (null == user.getMotherTounge() || user.getMotherTounge().isEmpty()) {
            viewpager.setCurrentItem(1);
            Work.motherTongue.setError("Enter mother tongue");
            return false;
        }

        // Page 2 validation
        if (null == user.getZodiacSign() || user.getZodiacSign().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.zodiac.setError("Enter zodiac sign");
            return false;
        }
        if (null == user.getHeight() || user.getHeight().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.height.setError("Enter height");
            return false;
        }
        if (null == user.getRelationshipStatus() || user.getRelationshipStatus().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.relationshipStatus.setError("Enter relationship status");
            return false;
        }
        if (null == user.getCaste() || user.getCaste().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.caste.setError("Enter caste");
            return false;
        }
        if (null == user.getReligion() || user.getReligion().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.religion.setError("Enter religion");
            return false;
        }
        if (null == user.getShowMe() || user.getShowMe().isEmpty()) {
            viewpager.setCurrentItem(2);
            Personal.showMe.setError("Enter show me");
            return false;
        }

        // Page 3 validation
        if (null == user.getDrink() || user.getDrink().isEmpty()) {
            viewpager.setCurrentItem(3);
            Habits.drinks.setError("Enter drink");
            return false;
        }

        if (null == user.getSmoke() || user.getSmoke().isEmpty()) {
            viewpager.setCurrentItem(3);
            Habits.smoke.setError("Enter smoke");
            return false;
        }

        if (null == user.getDiet() || user.getDiet().isEmpty()) {
            viewpager.setCurrentItem(3);
            Habits.diet.setError("Enter deit");
            return false;
        }

        if (null == user.getPets() || user.getPets().isEmpty()) {
            viewpager.setCurrentItem(3);
            Habits.pets.setError("Enter pets");
            return false;
        }

        if (null == user.getIntrests() || user.getIntrests().isEmpty()) {
            viewpager.setCurrentItem(3);
            Habits.interests.setError("Enter interests");
            return false;
        }

        // Page 4 validation
        if (null == user.getHaveKids() || user.getHaveKids().isEmpty()) {
            viewpager.setCurrentItem(4);
            Others.haveKids.setError("Enter have kids");
            return false;
        }
        if (null == user.getWantKids() || user.getWantKids().isEmpty()) {
            viewpager.setCurrentItem(4);
            Others.wantKids.setError("Enter want kids");
            return false;
        }
        if (null == user.getLookingFor() || user.getLookingFor().isEmpty()) {
            viewpager.setCurrentItem(4);
            Others.lookingFor.setError("Enter looking for");
            return false;
        }
        if (null == user.getBodyType() || user.getBodyType().isEmpty()) {
            viewpager.setCurrentItem(4);
            Others.bodyType.setError("Enter body type");
            return false;
        }

        return true;
    }

    class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            Log.e("check", "current : " + position);
            if (position == 4)
                nextBtn.setText("Save");
            else
                nextBtn.setText("Next");
        }
    }

    private class LoadDynamicOptionLists extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(OnboardingData.this, "Loading, please wait ...");
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

    private class RegisterUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(OnboardingData.this, "Loading, please wait ...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, API.REGISTER_USER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("check",response);
                            try {
                                JSONObject jsonResp = new JSONObject(response);
                                if (jsonResp.has("status")) {
                                    if (jsonResp.getString("status").equals("200")) {
                                        if (jsonResp.has("result") && jsonResp.getJSONObject("result").has("userData")) {
                                            String successMessage = jsonResp.getString("message");
                                            jsonResp = jsonResp.getJSONObject("result").getJSONObject("userData");
                                            if (jsonResp.has("userId")) {
                                                user.setUserId(jsonResp.getString("userId"));
                                                user.setId(jsonResp.getString("id"));
                                                user.setUniqueId(jsonResp.getString("uniqueId"));
                                                user.setCode(jsonResp.getString("code"));

                                                Log.e("check", user.toString());
                                                new LocalPref(getBaseContext()).saveUser(user);
                                                new LocalPref(getBaseContext()).setLoginStatus(true);
                                                startActivity(new Intent(getBaseContext(), UploadProfileImage.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                                Toast.makeText(getBaseContext(), successMessage, Toast.LENGTH_LONG).show();

                                            } else
                                                Toast.makeText(getBaseContext(), "User Id is not generated", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(getBaseContext(), "Response is unparsable", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getBaseContext(), jsonResp.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } else
                                    Toast.makeText(getBaseContext(), "Response is unparsable", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismissWithAnimation();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismissWithAnimation();
                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Log.e("check", new String(networkResponse.data));
                                    Toast.makeText(getBaseContext(),new String(networkResponse.data), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("registerType", user.getRegisterType());
                    params.put("firebaseId", user.getFirebaseId());
                    params.put("name", user.getName());
                    params.put("zodiacSign", user.getZodiacSign());
                    params.put("contactNumber", user.getContactNumber() == null ? "" : user.getContactNumber());
                    params.put("gender", user.getGender());
                    params.put("birthDate", user.getBirthDate());
                    params.put("currentLocation", user.getCurrentLocation());
                    params.put("height", user.getHeight());
                    params.put("motherTounge", user.getMotherTounge());
                    params.put("maritalStatus", user.getMaritalStatus());
                    params.put("caste", user.getCaste());
                    params.put("religion", user.getReligion());
                    params.put("drink", user.getDrink());
                    params.put("smoke", user.getSmoke());
                    params.put("fieldStudyCode", getFieldStudyCode(user.getFieldOfStudy()));
                    params.put("highestQualification", user.getQualification());
                    params.put("industryCode", getWorkIndustryCode(user.getWorkIndustry()));
                    params.put("experienceYears", user.getExperience());
                    params.put("email", user.getEmail());
                    params.put("showMe", user.getShowMe());
                    params.put("longitude", user.getLongitude());
                    params.put("latitude", user.getLatitude());
                    params.put("bodyType", user.getBodyType());
                    params.put("lookingFor", user.getLookingFor());
                    params.put("universityName", user.getUniversity());

                    params.put("about", user.getAboutMe());
                    params.put("relationshipStatus", user.getWorkIndustry());
                    params.put("diet", user.getDiet());
                    params.put("pets", user.getPets());
                    params.put("interests", getInterestCode(user.getIntrests()));
                    params.put("haveKids", user.getHaveKids());
                    params.put("wantKids", user.getWantKids());

                    Log.e("check","Reg req body : "+params.toString());
                    return params;
                }
            };

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

    private String getInterestCode(String intrests) {
        for(Interest i : DynamicOptionConstants.INTEREST_ARRAY_LIST)
            if(i.getInterestName().equalsIgnoreCase(intrests))
                return i.getCode();
        return null;
    }

    private String getWorkIndustryCode(String workIndustry) {
        for (WorkIndustry wim : DynamicOptionConstants.WORK_INDUSTRY_ARRAY_LIST) {
            if (wim.getIndustryName().equalsIgnoreCase(workIndustry))
                return wim.getCode();
        }
        return null;
    }

    private String getFieldStudyCode(String fieldOfStudy) {
        for (FieldOfStudy fos : DynamicOptionConstants.FIELD_OF_STUDY_ARRAY_LIST) {
            if (fos.getFieldName().equalsIgnoreCase(fieldOfStudy))
                return fos.getCode();
        }
        return null;
    }
}