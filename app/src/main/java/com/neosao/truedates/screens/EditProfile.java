package com.neosao.truedates.screens;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.model.options.FieldOfStudy;
import com.neosao.truedates.model.options.WorkIndustry;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    EditText name, email, about, gender, dob, location, university, fieldOfStudy, qualification, workIndustry, experience, motherTongue, zodiac, height, relationshipStatus, maritalStatus, caste, religion, showMe, drinks, smoke, diet, pets, interests, haveKids, wantKids, lookingFor, bodyType;
    final Calendar calendar = Calendar.getInstance();
    UserModel user;
    LocalPref localPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initializaComponents();
    }

    private void initializaComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        localPref = new LocalPref(getBaseContext());
        user = localPref.getUser();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        about = findViewById(R.id.about);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        location = findViewById(R.id.location);
        university = findViewById(R.id.university);
        fieldOfStudy = findViewById(R.id.fieldOfStudy);
        qualification = findViewById(R.id.qualification);
        workIndustry = findViewById(R.id.workIndustry);
        experience = findViewById(R.id.experience);
        motherTongue = findViewById(R.id.motherTongue);
        zodiac = findViewById(R.id.zodiac);
        height = findViewById(R.id.height);
        relationshipStatus = findViewById(R.id.relationshipStatus);
        maritalStatus = findViewById(R.id.maritalStatus);
        caste = findViewById(R.id.caste);
        religion = findViewById(R.id.religion);
        showMe = findViewById(R.id.showMe);
        drinks = findViewById(R.id.drinks);
        smoke = findViewById(R.id.smoke);
        diet = findViewById(R.id.diet);
        pets = findViewById(R.id.pets);
        interests = findViewById(R.id.interests);
        haveKids = findViewById(R.id.haveKids);
        wantKids = findViewById(R.id.wantKids);
        lookingFor = findViewById(R.id.lookingFor);
        bodyType = findViewById(R.id.bodyType);

        gender.setOnClickListener(this);
        dob.setOnClickListener(this);
        location.setOnClickListener(this);
        fieldOfStudy.setOnClickListener(this);
        qualification.setOnClickListener(this);
        workIndustry.setOnClickListener(this);
        experience.setOnClickListener(this);
        motherTongue.setOnClickListener(this);
        zodiac.setOnClickListener(this);
        height.setOnClickListener(this);
        relationshipStatus.setOnClickListener(this);
        maritalStatus.setOnClickListener(this);
        caste.setOnClickListener(this);
        religion.setOnClickListener(this);
        showMe.setOnClickListener(this);
        drinks.setOnClickListener(this);
        smoke.setOnClickListener(this);
        diet.setOnClickListener(this);
        pets.setOnClickListener(this);
        interests.setOnClickListener(this);
        haveKids.setOnClickListener(this);
        wantKids.setOnClickListener(this);
        lookingFor.setOnClickListener(this);
        bodyType.setOnClickListener(this);

        findViewById(R.id.saveBtn).setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.gender:
                showOptionPopup("Gender", (MaterialEditText) view, OptionContants.GENDER_OPTIONS);
                break;
            case R.id.dob:
                new DatePickerDialog(EditProfile.this, R.style.DialogTheme, this, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.location:
                getAddress();
                break;
            case R.id.fieldOfStudy:
                showOptionPopup("Field of Study", (EditText) view, DynamicOptionConstants.FIELD_OF_STUDY_OPTION);
                break;
            case R.id.qualification:
                showOptionPopup("Qualification", (EditText) view, OptionContants.QUALIFICATION_OPTIONS);
                break;
            case R.id.workIndustry:
                showOptionPopup("Work Industry", (EditText) view, DynamicOptionConstants.WORK_INDUSTRY_OPTION);
                break;
            case R.id.experience:
                showOptionPopup("Experience", (EditText) view, OptionContants.EXPERIENCE_OPTIONS);
                break;
            case R.id.motherTongue:
                showOptionPopup("Mother Tongue", (EditText) view, DynamicOptionConstants.MOTHER_TONGUE_OPTION);
                break;
            case R.id.zodiac:
                showOptionPopup("Zodiac Sign", (EditText) view, OptionContants.ZODIAC_OPTIONS);
                break;
            case R.id.height:
                showOptionPopup("Height", (EditText) view, OptionContants.HEIGHT_OPTIONS);
                break;
            case R.id.relationshipStatus:
                showOptionPopup("Relationship Status", (EditText) view, OptionContants.RELATIONSHIP_OPTIONS);
                break;
            case R.id.maritalStatus:
                showOptionPopup("Marital Status", (EditText) view, OptionContants.MARITAL_OPTIONS);
                break;
            case R.id.caste:
                showOptionPopup("Caste", (EditText) view, DynamicOptionConstants.CASTE_OPTION);
                break;
            case R.id.religion:
                showOptionPopup("Religion", (EditText) view, DynamicOptionConstants.RELIGION_OPTION);
                break;
            case R.id.showMe:
                showOptionPopup("Show me", (EditText) view, OptionContants.SHOW_ME_OPTIONS);
                break;
            case R.id.drinks:
                showOptionPopup("Drinks", (EditText) view, OptionContants.DRINKS_OPTIONS);
                break;
            case R.id.smoke:
                showOptionPopup("Smoke", (EditText) view, OptionContants.SMOKE_OPTIONS);
                break;
            case R.id.diet:
                showOptionPopup("Diet", (EditText) view, OptionContants.DIET_OPTIONS);
                break;
            case R.id.pets:
                showOptionPopup("Pets", (EditText) view, OptionContants.PET_OPTIONS);
                break;
            case R.id.interests:
                showOptionPopup("Interests", (EditText) view, DynamicOptionConstants.INTEREST_OPTION);
                break;
            case R.id.haveKids:
                showOptionPopup("Have Kids", (EditText) view, OptionContants.HAVE_KIDS_OPTIONS);
                break;
            case R.id.wantKids:
                showOptionPopup("Want Kids", (EditText) view, OptionContants.WANT_KIDS_OPTIONS);
                break;
            case R.id.lookingFor:
                showOptionPopup("Looking For", (EditText) view, OptionContants.LOOKING_FOR_OPTIONS);
                break;
            case R.id.bodyType:
                showOptionPopup("Body Type", (EditText) view, OptionContants.BODY_TYPE_OPTIONS);
                break;
            case R.id.saveBtn:
                new SaveProfile().execute();
                break;
        }

    }

    private void showOptionPopup(String title, final EditText editText, String[] options) {
        LayoutInflater inflater = getLayoutInflater();

        View titleView = inflater.inflate(R.layout.alertdialogbox_title, null);
        TextView titleTextView = titleView.findViewById(R.id.title);
        titleTextView.setText(title);
        ImageButton clear_text = titleView.findViewById(R.id.clear_text);
        View dialogView = inflater.inflate(R.layout.listview_option, null);
        ListView listView = dialogView.findViewById(R.id.select_dialog_listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, options);
        listView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfile.this);
        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText(adapter.getItem(i));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar personBirthDate = Calendar.getInstance();
        personBirthDate.set(Calendar.YEAR, year);
        personBirthDate.set(Calendar.MONTH, month);
        personBirthDate.set(Calendar.DAY_OF_MONTH, day);

        int diff = Utils.getDiffYears(personBirthDate, calendar);
        Log.e("check", "Age : " + diff);
        if (diff < 18) {
            Toast.makeText(getBaseContext(), "You should be at least 18 years old", Toast.LENGTH_LONG).show();
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            dob.setText(format.format(personBirthDate.getTime()));
            user.setAge(String.valueOf(diff));
        }
    }

    private void getAddress() {
        new Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        // given permissions are granted
                        LocationManager locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
                        Location loc = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                        Geocoder geocoder = new Geocoder(EditProfile.this, Locale.getDefault());
                        if (loc != null) {
                            try {
                                user.setLatitude(String.valueOf(loc.getLatitude()));
                                user.setLongitude(String.valueOf(loc.getLongitude()));
                                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                                Address address = addresses.get(0);
                                user.setCurrentLocation(address.getLocality());
                                user.setCurrentCountry(address.getCountryName());
                                location.setText(address.getLocality() + ", " + address.getCountryName());
                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Unable to fetch location", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .whenPermissionsRefused(new PermissionsRefusedListener() {
                    @Override
                    public void onPermissionsRefused(String[] permissions) {
                        Toast.makeText(getBaseContext(), "We need you parmission to fetch your address", Toast.LENGTH_LONG).show();
                    }
                })
                .execute(EditProfile.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.setText(user.getName());
        email.setText(user.getEmail());
        about.setText(user.getAboutMe());
        gender.setText(user.getGender());
        dob.setText(user.getBirthDate());
        location.setText(user.getCurrentLocation()+", "+user.getCurrentCountry());
        university.setText(user.getUniversity());
        fieldOfStudy.setText(user.getFieldOfStudy());
        qualification.setText(user.getQualification());
        workIndustry.setText(user.getWorkIndustry());
        experience.setText(user.getExperience());
        motherTongue.setText(user.getMotherTounge());
        zodiac.setText(user.getZodiacSign());
        height.setText(user.getHeight());
        relationshipStatus.setText(user.getRelationshipStatus());
        maritalStatus.setText(user.getMaritalStatus());
        caste.setText(user.getCaste());
        religion.setText(user.getReligion());
        showMe.setText(user.getShowMe());
        drinks.setText(user.getDrink());
        smoke.setText(user.getSmoke());
        diet.setText(user.getDiet());
        pets.setText(user.getPets());
        interests.setText(user.getIntrests());
        haveKids.setText(user.getHaveKids());
        wantKids.setText(user.getWantKids());
        lookingFor.setText(user.getLookingFor());
        bodyType.setText(user.getBodyType());
    }

    private class SaveProfile extends AsyncTask<Void,Void,Void> {
        SweetAlertDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setAboutMe(about.getText().toString());
            user.setGender(gender.getText().toString());
            user.setBirthDate(dob.getText().toString());
            user.setUniversity(university.getText().toString());
            user.setFieldOfStudy(fieldOfStudy.getText().toString());
            user.setQualification(qualification.getText().toString());
            user.setWorkIndustry(workIndustry.getText().toString());
            user.setExperience(experience.getText().toString());
            user.setMotherTounge(motherTongue.getText().toString());
            user.setZodiacSign(zodiac.getText().toString());
            user.setHeight(height.getText().toString());
            user.setRelationshipStatus(relationshipStatus.getText().toString());
            user.setMaritalStatus(maritalStatus.getText().toString());
            user.setCaste(caste.getText().toString());
            user.setReligion(religion.getText().toString());
            user.setShowMe(showMe.getText().toString());
            user.setDrink(drinks.getText().toString());
            user.setShowMe(smoke.getText().toString());
            user.setDiet(diet.getText().toString());
            user.setPets(pets.getText().toString());
            user.setIntrests(interests.getText().toString());
            user.setHaveKids(haveKids.getText().toString());
            user.setWantKids(wantKids.getText().toString());
            user.setLookingFor(lookingFor.getText().toString());
            user.setBodyType(bodyType.getText().toString());

            dialog = Utils.getProgress(EditProfile.this, "Updating, please wait ...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_USER_PROFILE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResp = new JSONObject(response);
                                if (jsonResp.has("message")) {

                                        Toast.makeText(getBaseContext(), jsonResp.getString("message"), Toast.LENGTH_LONG).show();
                                        localPref.saveUser(user);
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
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId",user.getUserId());
                    params.put("name", user.getName());
                    params.put("zodiacSign", user.getZodiacSign());
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
                    params.put("workIndustry", user.getWorkIndustry());
                    params.put("relationshipStatus", user.getWorkIndustry());
                    params.put("diet", user.getDiet());
                    params.put("pets", user.getPets());
                    params.put("interests", user.getIntrests());
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