package com.neosao.truedates.screens;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appyvet.materialrangebar.RangeBar;
import com.bumptech.glide.Glide;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.SliderAdapterPackage;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.FeatureSliderModel;
import com.neosao.truedates.model.SubscribtionFeatureDataModel;
import com.neosao.truedates.model.SubscribtionPackageModel;
import com.neosao.truedates.model.SubscribtionPriceDataModel;
import com.neosao.truedates.model.UserModel;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private SliderView sliderView;

    CardView locationCard, showMeCard;
    TextView location, showMe, phoneNumber;
    Toolbar toolbar;
    UserModel user;
    LocalPref localPref;
    RangeBar ageRangebar, distanceRangebar;
    TextView distanceText, ageText;
    SwitchCompat instaSwitch;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initializaComponents();
    }

    private void initializaComponents() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        localPref = new LocalPref(getBaseContext());
        user = localPref.getUser();

        if (null == user.getMembersettings().get(0).getMaxAgeFilter())
            user.getMembersettings().get(0).setMaxAgeFilter("30");

        if (null == user.getMembersettings().get(0).getMinAgeFilter())
            user.getMembersettings().get(0).setMinAgeFilter("18");

        if (null == user.getMembersettings().get(0).getMaxDistance())
            user.getMembersettings().get(0).setMaxDistance("5");

        locationCard = findViewById(R.id.locationCard);
        location = findViewById(R.id.location);
        ageRangebar = findViewById(R.id.ageRangebar);
        distanceRangebar = findViewById(R.id.distanceRangebar);
        distanceText = findViewById(R.id.distanceText);
        ageText = findViewById(R.id.ageText);
        showMeCard = findViewById(R.id.showMeCard);
        showMe = findViewById(R.id.showMe);
        instaSwitch = findViewById(R.id.instaSwitch);
        linearLayout = findViewById(R.id.subsPackage);
        phoneNumber = findViewById(R.id.phoneNumber);

        locationCard.setOnClickListener(this);
        showMeCard.setOnClickListener(this);
        findViewById(R.id.phoneCard).setOnClickListener(this);
        findViewById(R.id.logoutBtn).setOnClickListener(this);

        new LoadSubscribtion().execute();
        instaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    showInstaPopup();
                else {
                    user.getMembersettings().get(0).setIsInstagramActive("0");
                    user.getMembersettings().get(0).setInstagramLink("");
                    new UpdateInstagram().execute();
                }
            }
        });


        distanceRangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                distanceText.setText(rightPinValue + " Km");
                user.getMembersettings().get(0).setMaxDistance(rightPinValue);
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                new UpdateMaxDistance().execute();
            }
        });

        ageRangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                ageText.setText(leftPinValue + " - " + rightPinValue);
                user.getMembersettings().get(0).setMinAgeFilter(leftPinValue);
                user.getMembersettings().get(0).setMaxAgeFilter(rightPinValue);
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {
                new UpdateAgeRange().execute();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.locationCard:
                getAddress();
                break;
            case R.id.showMeCard:
                showOptionPopup("Show me", OptionContants.SHOW_ME_OPTIONS);
                break;
            case R.id.phoneCard:
                showPhonenNumberPopup();
                break;
            case R.id.logoutBtn:
                localPref.logOutUser();
                Toast.makeText(getBaseContext(), "You have been logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

        }
    }

    private void showSubscribtionAlertbox(SubscribtionPackageModel subscription) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Settings.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.subs_package, null);
        dialogBuilder.setView(dialogView);

        ArrayList<FeatureSliderModel> list = new ArrayList<>();
        for (SubscribtionFeatureDataModel featureDataModel : subscription.getFeatureData()) {
            list.add(new FeatureSliderModel(featureDataModel.getFeatureCode(), featureDataModel.getFeatureName(), featureDataModel.getFeatureLogo(), featureDataModel.getFeatureName()));
        }

        TextView title = dialogView.findViewById(R.id.title);
        sliderView = dialogView.findViewById(R.id.slider_view);
        final SliderAdapterPackage adapter = new SliderAdapterPackage(getBaseContext(), list);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.startAutoCycle();
        title.setText(subscription.getPackageName());

        // Set price config
        LinearLayout priceDataLinearLayout = dialogView.findViewById(R.id.priceDataLinearLayout);
        priceDataLinearLayout.setWeightSum(subscription.getPriceData().size());

        for (SubscribtionPriceDataModel priceDataModel : subscription.getPriceData()) {

            View priceItemView = LayoutInflater.from(this).inflate(R.layout.subs_package_item, linearLayout, false);

            TextView quantity = priceItemView.findViewById(R.id.quantity);
            TextView unit = priceItemView.findViewById(R.id.unit);
            TextView amountPerUnit = priceItemView.findViewById(R.id.amountPerUnit);
            TextView amount = priceItemView.findViewById(R.id.amount);
            CardView cardView = priceItemView.findViewById(R.id.card);

            quantity.setText(priceDataModel.getQuantity());
            unit.setText(priceDataModel.getUnit());
            amount.setText(Utils.CURRENCY_SYMBOL + priceDataModel.getAmount());
            double amtPerUnit = Double.parseDouble(priceDataModel.getAmount()) / Double.parseDouble(priceDataModel.getQuantity());
            DecimalFormat df2 = new DecimalFormat("#.##");
            amountPerUnit.setText(Utils.CURRENCY_SYMBOL + df2.format(amtPerUnit) + "/" + priceDataModel.getUnit().toLowerCase().substring(0, 2));
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));

            priceItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), priceDataModel.getPackagePriceCode(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < priceDataLinearLayout.getChildCount(); i++) {
                        View child = priceDataLinearLayout.getChildAt(i);
                        CardView cv = child.findViewById(R.id.card);
                        cv.setCardBackgroundColor(getResources().getColor(android.R.color.transparent));
                    }

                    CardView cvv = view.findViewById(R.id.card);
                    cvv.setCardBackgroundColor(getResources().getColor(R.color.themePink));
                }
            });
            priceDataLinearLayout.addView(priceItemView);
        }


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void showOptionPopup(String title, String[] options) {
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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Settings.this);
        dialogBuilder.setCustomTitle(titleView);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMe.setText("");
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMe.setText(adapter.getItem(i));
                user.getMembersettings().get(0).setShowMe(adapter.getItem(i));
                alertDialog.dismiss();
                new UpdateShowMe().execute();
            }
        });


        alertDialog.show();
    }

    private void showInstaPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.alertdialogbox_title, null);
        TextView titleTextView = titleView.findViewById(R.id.title);
        ImageButton clear_text = titleView.findViewById(R.id.clear_text);
        titleTextView.setText("Enter your Instagram link");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Settings.this);
        dialogBuilder.setCustomTitle(titleView);

        final EditText input = new EditText(Settings.this);
        input.setText(user.getMembersettings().get(0).getInstagramLink());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(20, 10, 20, 0);
        input.setLayoutParams(lp);
        dialogBuilder.setView(input);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Enter instagram profile link", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.getMembersettings().get(0).setIsInstagramActive("1");
                user.getMembersettings().get(0).setInstagramLink(input.getText().toString());
                new UpdateInstagram().execute();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMe.setText("");
                instaSwitch.setChecked(false);
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.show();
    }

    private void showPhonenNumberPopup() {
        LayoutInflater inflater = getLayoutInflater();
        View titleView = inflater.inflate(R.layout.alertdialogbox_title, null);
        TextView titleTextView = titleView.findViewById(R.id.title);
        ImageButton clear_text = titleView.findViewById(R.id.clear_text);
        titleTextView.setText("Enter new mobile number");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Settings.this);
        dialogBuilder.setCustomTitle(titleView);

        final EditText input = new EditText(Settings.this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setText(user.getMembersettings().get(0).getInstagramLink());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(20, 10, 20, 0);
        input.setLayoutParams(lp);
        dialogBuilder.setView(input);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().isEmpty() || input.getText().length() != 10) {
                    Toast.makeText(getBaseContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.getMembersettings().get(0).setContactNumber("+91 " + input.getText().toString());
                new UpdateContactNumber().execute();
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

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.TOP;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        alertDialog.show();
    }

    private void getAddress() {
        new Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        // given permissions are granted
                        LocationManager locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
                        Location loc = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                        Geocoder geocoder = new Geocoder(Settings.this, Locale.getDefault());
                        if (loc != null) {
                            try {
                                user.getMembersettings().get(0).setLatitude(String.valueOf(loc.getLatitude()));
                                user.getMembersettings().get(0).setLongitude(String.valueOf(loc.getLongitude()));
                                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                                Address address = addresses.get(0);
                                user.getMembersettings().get(0).setCurrentLocation(address.getLocality() + ", " + address.getCountryName());
                                location.setText(address.getLocality() + ", " + address.getCountryName());

                                new UpdateLocation().execute();
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
                        Toast.makeText(getBaseContext(), "We need you permission to fetch your address", Toast.LENGTH_LONG).show();
                    }
                })
                .execute(Settings.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.setText(user.getMembersettings().get(0).getCurrentLocation());
        ageRangebar.setRangePinsByValue(Float.parseFloat(user.getMembersettings().get(0).getMinAgeFilter()), Float.parseFloat(user.getMembersettings().get(0).getMaxAgeFilter()));
        distanceRangebar.setSeekPinByValue(Float.parseFloat(user.getMembersettings().get(0).getMaxDistance()));
        showMe.setText(user.getMembersettings().get(0).getShowMe());
        if (null != user.getMembersettings().get(0).getContactNumber())
            phoneNumber.setText(user.getMembersettings().get(0).getContactNumber());
    }

    private class UpdateLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_LOCATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
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
                    params.put("longitude", user.getMembersettings().get(0).getLongitude());
                    params.put("latitude", user.getMembersettings().get(0).getLatitude());
                    params.put("currentLocation", user.getMembersettings().get(0).getCurrentLocation());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class UpdateMaxDistance extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_MAX_DISTANCE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
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
                    params.put("maxDistance", user.getMembersettings().get(0).getMaxDistance());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class UpdateAgeRange extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_AGE_RANGE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
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
                    params.put("minAgeFilter", user.getMembersettings().get(0).getMinAgeFilter());
                    params.put("maxAgeFilter", user.getMembersettings().get(0).getMaxAgeFilter());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class UpdateShowMe extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_SHOW_ME,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
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
                    params.put("showMe", user.getMembersettings().get(0).getShowMe());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class UpdateInstagram extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_INSTAGRAM,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
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
                    params.put("isInstagramActive", user.getMembersettings().get(0).getIsInstagramActive());
                    params.put("instagramLink", user.getMembersettings().get(0).getInstagramLink());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class UpdateContactNumber extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_PHONE_NUMBER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUser(user);
                                phoneNumber.setText(user.getMembersettings().get(0).getContactNumber());
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
                    params.put("contactNumber", user.getMembersettings().get(0).getContactNumber());

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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class LoadSubscribtion extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.GET_SUBSCRIBTION_PACKAGES,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("check", "Response " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("result")) {
                                    JSONArray array = object.getJSONArray("result");
                                    for (int i = 0; i < array.length(); i++) {
                                        SubscribtionPackageModel packageModel = new Gson().fromJson(array.getJSONObject(i).toString(), SubscribtionPackageModel.class);
                                        addPackageLayout(packageModel);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e("check", "Error in response catch: " + e.getLocalizedMessage());
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
                                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("packageLevel", "0");
                    params.put("userId", user.getUserId());

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

    private void addPackageLayout(SubscribtionPackageModel subscription) {
        Log.e("check", subscription.toString());
        View view = LayoutInflater.from(this).inflate(R.layout.subscribtion_layout, linearLayout, false);
        TextView name = view.findViewById(R.id.name);
        TextView desc = view.findViewById(R.id.desc);
        ImageView logo = view.findViewById(R.id.logo);

        name.setText(subscription.getPackageName());
        desc.setText(subscription.getPackageDescription());
        Glide.with(getBaseContext())
                .load(subscription.getPackageLogo())
                .into(logo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubscribtionAlertbox(subscription);
            }
        });
        linearLayout.addView(view);

    }
}