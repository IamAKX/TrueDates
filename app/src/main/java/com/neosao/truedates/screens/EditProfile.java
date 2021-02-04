package com.neosao.truedates.screens;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import androidx.core.content.ContextCompat;

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
import com.neosao.truedates.model.MemberInterests;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.model.options.Interest;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mabbas007.tagsedittext.TagsEditText;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.neosao.truedates.configs.Utils.getIndexOfImageView;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OnMenuItemClickListener<PowerMenuItem> {

    Toolbar toolbar;
    EditText name, email, about, gender, dob, location, university, fieldOfStudy, qualification, workIndustry, experience, motherTongue, zodiac, height, relationshipStatus, maritalStatus, caste, religion, showMe, drinks, smoke, diet, pets, haveKids, wantKids, lookingFor, bodyType;
    TagsEditText interests;
    final Calendar calendar = Calendar.getInstance();
    UserModel user;
    LocalPref localPref;
    ImageView[] profileImageArray;
    ImageView tappedImageView, longTappedImageView;
    ArrayList<Image> images = new ArrayList<>();
    PowerMenu powerMenu;

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
        MemberPhotos[] tempMemberPhotoArray = new MemberPhotos[9];
        for (int i = 0; i < user.getMemberPhotos().length; i++) {
            tempMemberPhotoArray[i] = user.getMemberPhotos()[i];
        }
        user.setMemberPhotos(tempMemberPhotoArray);
        localPref.saveUser(user);
        if(null != user.getHeight() && !user.getHeight().isEmpty())
            user.setHeight(user.getHeight().replace("&quot;","\""));
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
        for (final ImageView iv : profileImageArray) {

            powerMenu = new PowerMenu.Builder(EditProfile.this)
                    .addItem(new PowerMenuItem("Make default image", false)) // add an item.
                    .addItem(new PowerMenuItem("Delete", true)) // aad an item list.
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent))
                    .setTextGravity(Gravity.CENTER)
                    .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(ContextCompat.getColor(getBaseContext(), R.color.white))
                    .setSelectedMenuColor(ContextCompat.getColor(getBaseContext(), R.color.red))
                    .setOnMenuItemClickListener(this)
                    .build();

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longTappedImageView = iv;
                    powerMenu.showAsDropDown(view);
                    return true;
                }
            });
        }


        findViewById(R.id.saveBtn).setOnClickListener(this);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                showInterestOptionPopup("Interests", (TagsEditText) view, DynamicOptionConstants.INTEREST_OPTION);
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
                updateUserObject();
                if (validateInputs()) {
                    new SaveProfile().execute();
                }
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
                                if(null == tappedImageView)
                                {
                                    return;
                                }
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

        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return options.length;
            }

            @Override
            public Object getItem(int i) {
                return options[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View row = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                TextView textView = row.findViewById(android.R.id.text1);
                textView.setText(String.valueOf(getItem(i)));
                if (!editText.getText().toString().isEmpty() && editText.getText().toString().equals(getItem(i))) {
                    textView.setTextColor(getBaseContext().getResources().getColor(R.color.themePink));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                }
                return row;
            }
        };
        listView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfile.this);
        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText(String.valueOf(adapter.getItem(i)));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    private void showInterestOptionPopup(String title, final TagsEditText editText, String[] options) {
        ArrayList<String> interestArrayList;
        if (editText.getText().toString().isEmpty())
            interestArrayList = new ArrayList<>();
        else
            interestArrayList = (ArrayList<String>) editText.getTags();


        LayoutInflater inflater = getLayoutInflater();

        View titleView = inflater.inflate(R.layout.alertdialogbox_title, null);
        TextView titleTextView = titleView.findViewById(R.id.title);
        titleTextView.setText(title);
        ImageButton clear_text = titleView.findViewById(R.id.clear_text);
        View dialogView = inflater.inflate(R.layout.listview_option, null);
        ListView listView = dialogView.findViewById(R.id.select_dialog_listview);
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return options.length;
            }

            @Override
            public Object getItem(int i) {
                return options[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View row = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                TextView textView = row.findViewById(android.R.id.text1);
                textView.setText(String.valueOf(getItem(i)));


                if (interestArrayList.contains(getItem(i))) {
                    textView.setTextColor(getBaseContext().getResources().getColor(R.color.themePink));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                }
                return row;
            }
        };
        listView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfile.this);
        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editText.setTags(interestArrayList.toArray(new String[0]));

                dialogInterface.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (interestArrayList.contains(String.valueOf(adapter.getItem(i))))
                    interestArrayList.remove(String.valueOf(adapter.getItem(i)));
                else
                    interestArrayList.add(String.valueOf(adapter.getItem(i)));

                adapter.notifyDataSetChanged();
            }
        });


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.themePink));
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
        if (diff < 18) {
            Toast.makeText(getBaseContext(), "You should be at least 18 years old", Toast.LENGTH_LONG).show();
        } else {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            dob.setText(format.format(personBirthDate.getTime()));
            user.setAge(String.valueOf(diff));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomeContainer.pagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, 100)) {
            images = ImagePicker.getImages(data);
            UCrop.of(images.get(0).getUri(), Uri.fromFile(new File(getCacheDir(), String.valueOf("TrueDates_edited_" + System.currentTimeMillis() + ".png"))))
                    .withOptions(getCropOption())
                    .withAspectRatio(4, 3)
                    .start(EditProfile.this);
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(getBaseContext())
                    .load(resultUri.getPath())
                    .into(tappedImageView);

            if (null != resultUri) {
                File imageToBeUploaded = new File(resultUri.getPath());
                if (null != imageToBeUploaded)
                    new UploadImageTask(imageToBeUploaded).execute();
            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);

        options.setToolbarColor(ContextCompat.getColor(this, R.color.white));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.grey));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.themePink));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.white));
        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, R.color.themePink));
        return options;
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
                                if(null == addresses || addresses.isEmpty())
                                    return;
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
        interests.setTags(Utils.getInterestNameArray(user));
        haveKids.setText(user.getHaveKids());
        wantKids.setText(user.getWantKids());
        lookingFor.setText(user.getLookingFor());
        bodyType.setText(user.getBodyType());
        for (int i = 0; i < Utils.getPhotoCount(user.getMemberPhotos()) && i < 9; i++) {
            if (null != user.getMemberPhotos()[i] && null != user.getMemberPhotos()[i].getMemberPhoto()) {
                Glide.with(getBaseContext())
                        .load(user.getMemberPhotos()[i].getMemberPhoto())
                        .into(profileImageArray[i]);
                if (user.getMemberPhotos()[i].getIsDefault()==0)
                    profileImageArray[i].setBackgroundColor(getResources().getColor(R.color.white));
                else
                    profileImageArray[i].setBackgroundColor(getResources().getColor(R.color.themePink));
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateUserObject() {

        try {
            user.setAge(Utils.getDiffYears(dob.getText().toString(),calendar));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        ArrayList<String> interestArrayList;
        if (interests.getText().toString().isEmpty())
            interestArrayList = new ArrayList<>();
        else
            interestArrayList = new ArrayList<>(Arrays.asList(interests.getText().toString().split(" ")));

        user.setMemberInterests(new ArrayList<MemberInterests>());
        for (String item : interestArrayList) {
            Interest interest = new Utils().getInterestCode(item);
            if (null != interest) {
                MemberInterests memberInterests = new MemberInterests();
                memberInterests.setInterestCode(interest.getCode());
                memberInterests.setInterestName(interest.getInterestName());

                memberInterests.setMemberInterestValue(interest.getInterestValue());

                user.getMemberInterests().add(memberInterests);

            }
        }
    }

    @Override
    public void onItemClick(int position, PowerMenuItem item) {


        int index = getIndexOfImageView(longTappedImageView);
        if (index > Utils.getPhotoCount(user.getMemberPhotos())) {
            Toast.makeText(getBaseContext(), "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }
        MemberPhotos defImage = user.getMemberPhotos()[index];
        if(null == defImage)
        {
            Toast.makeText(getBaseContext(), "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (position) {
            case 0:

                if (null != defImage && null != defImage.getMemberPhoto()) {

                    defImage.setIsDefault(1);
                    for (int i = 0; i < 9 && i < Utils.getPhotoCount(user.getMemberPhotos()); i++) {

                        if (null != user.getMemberPhotos()[i]) {
                            // set default=1 in local user
                            user.getMemberPhotos()[i].setIsDefault(0);
                            // change default imageview backgroud
                            profileImageArray[i].setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                    user.getMemberPhotos()[index].setIsDefault(1);
                    longTappedImageView.setBackgroundColor(getResources().getColor(R.color.themePink));

                    localPref.saveUser(user);
                    new SaveDefaulImage(user.getMemberPhotos()[index].getPhotoCode()).execute();

                }
                powerMenu.dismiss();
                break;
            case 1:
                powerMenu.dismiss();
                if (user.getDefaultPhoto().equals(defImage.getMemberPhoto())) {
                    Toast.makeText(getBaseContext(), "You cannot delete default image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (null != defImage && null != defImage.getPhotoCode())
                    new DeleteImage(defImage.getPhotoCode(), longTappedImageView, index).execute();
                break;
        }

        if(null != powerMenu && powerMenu.isShowing())
            powerMenu.dismiss();
    }

    private class SaveProfile extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

//                                    Toast.makeText(getBaseContext(), jsonResp.getString("message"), Toast.LENGTH_LONG).show();
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
//                    params.put("age", user.getAge());
                    params.put("zodiacSign", user.getZodiacSign());
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
                    params.put("interestCode", Utils.getInterestCodeList(user));
                    params.put("haveKids", user.getHaveKids());
                    params.put("wantKids", user.getWantKids());


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

    private boolean validateInputs() {
        // Page 0 validation
        if (null == user.getName() || user.getName().isEmpty()) {

            name.setError("Enter name");
            return false;
        }
        if (null == user.getEmail() || user.getEmail().isEmpty()) {

            email.setError("Enter email");
            return false;
        }
        if (!Utils.isValid(user.getEmail())) {

            email.setError("Invalid email");
            return false;
        }
        if (null == user.getAbout() || user.getAbout().isEmpty()) {

            about.setError("Write about yourself");
            return false;
        }
        if (null == user.getGender() || user.getGender().isEmpty()) {

            gender.setError("Enter gender");
            return false;
        }
        if (null == user.getBirthDate() || user.getBirthDate().isEmpty()) {

            dob.setError("Enter date of birth");
            return false;
        }
        if (null == user.getMembersettings().get(0).getCurrentLocation() || user.getMembersettings().get(0).getCurrentLocation().isEmpty()) {

            location.setError("Enter location");
            return false;
        }

        // Page 1 validation
//        if (null == user.getMemberWork().get(0).getUniversityName() || user.getMemberWork().get(0).getUniversityName().isEmpty()) {
//
//            university.setError("Enter university");
//            return false;
//        }
        if (null == user.getMemberWork().get(0).getFieldName() || user.getMemberWork().get(0).getFieldName().isEmpty()) {

            fieldOfStudy.setError("Enter field of study");
            return false;
        }
        if (null == user.getMemberWork().get(0).getHighestQualification() || user.getMemberWork().get(0).getHighestQualification().isEmpty()) {

            qualification.setError("Enter qualification");
            return false;
        }
//        if (null == user.getMemberWork().get(0).getIndustryName() || user.getMemberWork().get(0).getIndustryName().isEmpty()) {
//
//            workIndustry.setError("Enter work industry");
//            return false;
//        }
//        if (null == user.getMemberWork().get(0).getExperienceYears() || user.getMemberWork().get(0).getExperienceYears().isEmpty()) {
//
//            experience.setError("Enter experience");
//            return false;
//        }
        if (null == user.getMotherTounge() || user.getMotherTounge().isEmpty()) {

            motherTongue.setError("Enter mother tongue");
            return false;
        }

        // Page 2 validation
        if (null == user.getZodiacSign() || user.getZodiacSign().isEmpty()) {

            zodiac.setError("Enter zodiac sign");
            return false;
        }
//        if (null == user.getHeight() || user.getHeight().isEmpty()) {
//
//            height.setError("Enter height");
//            return false;
//        }
        if (null == user.getMaritalStatus() || user.getMaritalStatus().isEmpty()) {

            maritalStatus.setError("Enter marital status");
            return false;
        }

        if (null == user.getRelationshipStatus() || user.getRelationshipStatus().isEmpty()) {

            relationshipStatus.setError("Enter relationship status");
            return false;
        }
        if (null == user.getCaste() || user.getCaste().isEmpty()) {

            caste.setError("Enter caste");
            return false;
        }
        if (null == user.getReligion() || user.getReligion().isEmpty()) {

            religion.setError("Enter religion");
            return false;
        }
        if (null == user.getMembersettings().get(0).getShowMe() || user.getMembersettings().get(0).getShowMe().isEmpty()) {

            showMe.setError("Enter show me");
            return false;
        }

        // Page 3 validation
        if (null == user.getDrink() || user.getDrink().isEmpty()) {

            drinks.setError("Enter drink");
            return false;
        }

        if (null == user.getSmoke() || user.getSmoke().isEmpty()) {

            smoke.setError("Enter smoke");
            return false;
        }

        if (null == user.getDiet() || user.getDiet().isEmpty()) {

            diet.setError("Enter deit");
            return false;
        }

        if (null == user.getPets() || user.getPets().isEmpty()) {

            pets.setError("Enter pets");
            return false;
        }

        if (null == user.getMemberInterests() || user.getMemberInterests().isEmpty()) {

            interests.setError("Enter interests");
            return false;
        }

        // Page 4 validation
        if (null == user.getHaveKids() || user.getHaveKids().isEmpty()) {

            haveKids.setError("Enter have kids");
            return false;
        }
        if (null == user.getWantKids() || user.getWantKids().isEmpty()) {

            wantKids.setError("Enter want kids");
            return false;
        }
        if (null == user.getLookingFor() || user.getLookingFor().isEmpty()) {

            lookingFor.setError("Enter looking for");
            return false;
        }
        if (null == user.getBodyType() || user.getBodyType().isEmpty()) {

            bodyType.setError("Enter body type");
            return false;
        }

        return true;
    }

    private class UploadImageTask extends AsyncTask<Void, Void, String> {

        File imageToBeUploaded;
        SweetAlertDialog dialog;

        public UploadImageTask(File imageToBeUploaded) {
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


                // Adding file data to http body
                entity.addPart("file", new FileBody(imageToBeUploaded));

                // Extra parameters if you want to pass to server
                entity.addPart("userId",
                        new StringBody(user.getUserId()));
                entity.addPart("index",
                        new StringBody(String.valueOf(getIndexOfImageView(tappedImageView))));
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
            } finally {
                dialog.dismissWithAnimation();

            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            try {
                JSONObject obj = new JSONObject(responseString);
//                if (null != obj && obj.has("message"))
//                    Toast.makeText(getBaseContext(), obj.getString("message"), Toast.LENGTH_LONG).show();

                if (null != obj && obj.has("status") && obj.getString("status").equals("200")) {

                    if (null != obj && obj.has("result") && obj.getJSONObject("result").has("memberPhoto")) {
                        MemberPhotos photos = new Gson().fromJson(obj.getJSONObject("result").getJSONObject("memberPhoto").toString(), MemberPhotos.class);
                        photos.setIndex(String.valueOf(getIndexOfImageView(tappedImageView)));
                        user.getMemberPhotos()[getIndexOfImageView(tappedImageView)] = photos;
                        Glide.with(getBaseContext())
                                .load(photos.getMemberPhoto())
                                .into(tappedImageView);
                        localPref.saveUser(user);
                    } else {
                        Glide.with(getBaseContext())
                                .load(R.drawable.dashed_border)
                                .into(tappedImageView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(),"Upload response : " + responseString,Toast.LENGTH_LONG).show();
                Glide.with(getBaseContext())
                        .load(R.drawable.dashed_border)
                        .into(tappedImageView);
                e.printStackTrace();
            }
        }
    }

    private class SaveDefaulImage extends AsyncTask<Void, Void, Void> {
        String photoCode;

        public SaveDefaulImage(String photoCode) {
            this.photoCode = photoCode;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.SET_DEFAULT_PHOTO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
//                                if (object.has("message") && null != object.getString("message"))
//                                    Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", user.getUserId());
                    params.put("photoCode", photoCode);
                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class DeleteImage extends AsyncTask<Void, Void, Void> {

        String photoCode;
        ImageView imageView;
        int index;

        public DeleteImage(String photoCode, ImageView imageView, int index) {
            this.photoCode = photoCode;
            this.imageView = imageView;
            this.index = index;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.DELETE_PHOTO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
//                                if (object.has("message") && null != object.getString("message"))
//                                    Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                                if (object.has("status") && !object.getString("status").equals("200")) {
                                    return;
                                }

                                imageView.setBackgroundColor(getResources().getColor(R.color.white));
                                Glide.with(getBaseContext())
                                        .load(R.drawable.dashed_border)
                                        .into(imageView);
                                user.getMemberPhotos()[index] = null;
                                localPref.saveUser(user);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", user.getUserId());
                    params.put("photoCode", photoCode);
                    Log.e("check", "Req body : " + params.toString());
                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }
}