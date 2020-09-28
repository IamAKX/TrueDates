package com.neosao.truedates.screens;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.AndroidMultiPartEntity;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.neosao.truedates.configs.Utils.getIndexOfImageView;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    EditText name, email, about, gender, dob, location, university, fieldOfStudy, qualification, workIndustry, experience, motherTongue, zodiac, height, relationshipStatus, maritalStatus, caste, religion, showMe, drinks, smoke, diet, pets, interests, haveKids, wantKids, lookingFor, bodyType;
    final Calendar calendar = Calendar.getInstance();
    UserModel user;
    LocalPref localPref;
    ImageView[] profileImageArray;
    ImageView tappedImageView;
    ArrayList<Image> images = new ArrayList<>();

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
        profileImageArray = new ImageView[]{
                findViewById(R.id.profileImage1),
                findViewById(R.id.profileImage2),
                findViewById(R.id.profileImage3),
                findViewById(R.id.profileImage4),
                findViewById(R.id.profileImage5),
                findViewById(R.id.profileImage6),
                findViewById(R.id.profileImage7),
                findViewById(R.id.profileImage8),
                findViewById(R.id.profileImage9)
        };

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
        for (ImageView iv : profileImageArray)
            iv.setOnClickListener(this);


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
            case R.id.profileImage1:
            case R.id.profileImage2:
            case R.id.profileImage3:
            case R.id.profileImage4:
            case R.id.profileImage5:
            case R.id.profileImage6:
            case R.id.profileImage7:
            case R.id.profileImage8:
            case R.id.profileImage9:
                tappedImageView = (ImageView) view;
                new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .whenPermissionsGranted(new PermissionsGrantedListener() {
                            @Override
                            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                ImagePicker.with(EditProfile.this)
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
                        .execute(EditProfile.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            images = ImagePicker.getImages(data);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Glide.with(getBaseContext())
                        .load(images.get(0).getUri())
                        .into(tappedImageView);
            } else {
                Glide.with(getBaseContext())
                        .load(images.get(0).getPath())
                        .into(tappedImageView);
            }
            if(null != images.get(0))
            {
                new UploadImageTask(images.get(0)).execute();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                                user.getMembersettings().get(0).setLatitude(String.valueOf(loc.getLatitude()));
                                user.getMembersettings().get(0).setLongitude(String.valueOf(loc.getLongitude()));
                                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                                Address address = addresses.get(0);
                                user.getMembersettings().get(0).setCurrentLocation(address.getLocality() + ", " + address.getCountryName());
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
        about.setText(user.getAbout());
        gender.setText(user.getGender());
        dob.setText(user.getBirthDate());
        location.setText(user.getMembersettings().get(0).getCurrentLocation());
        university.setText(user.getMemberWork().get(0).getUniversityName());
        fieldOfStudy.setText(user.getMemberWork().get(0).getFieldName());
        qualification.setText(user.getMemberWork().get(0).getHighestQualification());
        workIndustry.setText(user.getMemberWork().get(0).getIndustryName());
        experience.setText(user.getMemberWork().get(0).getExperienceYears());
        motherTongue.setText(user.getMotherTounge());
        zodiac.setText(user.getZodiacSign());
        height.setText(user.getHeight());
        relationshipStatus.setText(user.getRelationshipStatus());
        maritalStatus.setText(user.getMaritalStatus());
        caste.setText(user.getCaste());
        religion.setText(user.getReligion());
        showMe.setText(user.getMembersettings().get(0).getShowMe());
        drinks.setText(user.getDrink());
        smoke.setText(user.getSmoke());
        diet.setText(user.getDiet());
        pets.setText(user.getPets());
        interests.setText(user.getMemberInterests().get(0).getInterestName());
        haveKids.setText(user.getHaveKids());
        wantKids.setText(user.getWantKids());
        lookingFor.setText(user.getLookingFor());
        bodyType.setText(user.getBodyType());
        for (int i = 0; i < Utils.getPhotoCount(user.getMemberPhotos()) && i<9; i++) {
            if(null != user.getMemberPhotos()[i] && null != user.getMemberPhotos()[i].getMemberPhoto())
            Glide.with(getBaseContext())
                    .load(user.getMemberPhotos()[i].getMemberPhoto())
                    .into(profileImageArray[i]);
        }

    }

    private class SaveProfile extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user.setName(name.getText().toString());
            user.setEmail(email.getText().toString());
            user.setAbout(about.getText().toString());
            user.setGender(gender.getText().toString());
            user.setBirthDate(dob.getText().toString());
            user.getMembersettings().get(0).setCurrentLocation(location.getText().toString());
            user.getMemberWork().get(0).setUniversityName(university.getText().toString());
            user.getMemberWork().get(0).setFieldName(fieldOfStudy.getText().toString());
            user.getMemberWork().get(0).setHighestQualification(qualification.getText().toString());
            user.getMemberWork().get(0).setIndustryName(workIndustry.getText().toString());
            user.getMemberWork().get(0).setExperienceYears(experience.getText().toString());
            user.setMotherTounge(motherTongue.getText().toString());
            user.setZodiacSign(zodiac.getText().toString());
            user.setHeight(height.getText().toString());
            user.setRelationshipStatus(relationshipStatus.getText().toString());
            user.setMaritalStatus(maritalStatus.getText().toString());
            user.setCaste(caste.getText().toString());
            user.setReligion(religion.getText().toString());
            user.getMembersettings().get(0).setShowMe(showMe.getText().toString());
            user.setDrink(drinks.getText().toString());
            user.setSmoke(smoke.getText().toString());
            user.setDiet(diet.getText().toString());
            user.setPets(pets.getText().toString());
            user.getMemberInterests().get(0).setInterestName(interests.getText().toString());
            user.setHaveKids(haveKids.getText().toString());
            user.setWantKids(wantKids.getText().toString());
            user.setLookingFor(lookingFor.getText().toString());
            user.setBodyType(bodyType.getText().toString());

            user.getMemberWork().get(0).setFieldStudyCode(new Utils().getFieldStudyCode(user.getMemberWork().get(0).getFieldName()).getCode());
            user.getMemberWork().get(0).setIndustryCode(new Utils().getWorkIndustryCode(user.getMemberWork().get(0).getIndustryName()).getCode());
            user.getMemberInterests().get(0).setInterestName(new Utils().getInterestCode(user.getMemberInterests().get(0).getInterestName()).getCode());
            user.getMemberInterests().get(0).setMemberInterestValue(new Utils().getInterestCode(user.getMemberInterests().get(0).getInterestName()).getInterestValue());

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
                    params.put("userId", user.getUserId());
                    params.put("registerType", user.getRegisterType());
                    params.put("firebaseId", user.getFirebaseId());
                    params.put("name", user.getName());
                    params.put("zodiacSign", user.getZodiacSign());
                    params.put("contactNumber", user.getContactNumber() == null ? "" : user.getContactNumber());
                    params.put("gender", user.getGender());
                    params.put("birthDate", user.getBirthDate());
                    params.put("currentLocation", user.getMembersettings().get(0).getCurrentLocation());
                    params.put("height", user.getHeight());
                    params.put("motherTounge", user.getMotherTounge());
                    params.put("maritalStatus", user.getMaritalStatus());
                    params.put("caste", user.getCaste());
                    params.put("religion", user.getReligion());
                    params.put("drink", user.getDrink());
                    params.put("smoke", user.getSmoke());
                    params.put("fieldStudyCode", user.getMemberWork().get(0).getFieldStudyCode());
                    params.put("highestQualification", user.getMemberWork().get(0).getHighestQualification());
                    params.put("industryCode", user.getMemberWork().get(0).getIndustryCode());
                    params.put("experienceYears", user.getMemberWork().get(0).getExperienceYears());
                    params.put("email", user.getEmail());
                    params.put("showMe", user.getMembersettings().get(0).getShowMe());
                    params.put("longitude", user.getMembersettings().get(0).getLongitude());
                    params.put("latitude", user.getMembersettings().get(0).getLatitude());
                    params.put("bodyType", user.getBodyType());
                    params.put("lookingFor", user.getLookingFor());
                    params.put("universityName", user.getMemberWork().get(0).getUniversityName());

                    params.put("about", user.getAbout());
                    params.put("relationshipStatus", user.getRelationshipStatus());
                    params.put("diet", user.getDiet());
                    params.put("pets", user.getPets());
                    params.put("interestCode", user.getMemberInterests().get(0).getInterestName());
                    params.put("haveKids", user.getHaveKids());
                    params.put("wantKids", user.getWantKids());


                    Log.e("check", "Reg req body : " + params.toString());
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

    private class UploadImageTask extends AsyncTask<Void,Void,String>{

        Image imageToBeUploaded;
        SweetAlertDialog dialog;
        public UploadImageTask(Image imageToBeUploaded) {
            this.imageToBeUploaded = imageToBeUploaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(EditProfile.this, "Uploading image...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(API.UPLOAD_IMAGE);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });


                File sourceFile = new File(imageToBeUploaded.getPath());

                // Adding file data to http body
                entity.addPart("file", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("userId",
                        new StringBody(user.getUserId()));

                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            finally {
                dialog.dismissWithAnimation();
                try {
                    JSONObject obj = new JSONObject(responseString);
                    if(null != obj && obj.has("message"))
                        Toast.makeText(getBaseContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("check","upload response : "+ responseString);

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
                if(null != obj && obj.has("message"))
                    Toast.makeText(getBaseContext(),obj.getString("message"),Toast.LENGTH_LONG).show();

                if(null != obj && obj.has("status") && !obj.getString("status").equals("200"))
                {
                    Glide.with(getBaseContext())
                            .load(R.drawable.dashed_border)
                            .into(tappedImageView);
                    if(null != obj && obj.has("result") && obj.getJSONObject("result").has("memberPhoto"))
                    {
                        MemberPhotos photos = new Gson().fromJson(obj.getJSONObject("result").getJSONObject("result").toString(), MemberPhotos.class);
                        if(null == user.getMemberPhotos())
                            user.setMemberPhotos( new MemberPhotos[9]);

                        user.getMemberPhotos()[getIndexOfImageView(tappedImageView)] = photos;

                        localPref.saveUser(user);
                    }
                    else
                    {
                        Glide.with(getBaseContext())
                                .load(R.drawable.dashed_border)
                                .into(tappedImageView);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}