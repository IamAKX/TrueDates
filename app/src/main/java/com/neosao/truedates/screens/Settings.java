package com.neosao.truedates.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.LinearLayout;
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
import com.appyvet.materialrangebar.RangeBar;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.model.SettingsModel;
import com.neosao.truedates.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    CardView locationCard, showMeCard;
    TextView location, showMe;
    Toolbar toolbar;
    UserModel user;
    LocalPref localPref;
    RangeBar ageRangebar, distanceRangebar;
    TextView distanceText, ageText;
    SettingsModel userSettings;
    SwitchCompat instaSwitch;

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
        userSettings = localPref.getUserSettings();
        if (null == userSettings) {
            userSettings = new SettingsModel("5", "20", "30", "0", "");
            localPref.saveUserSettings(userSettings);
        }

        locationCard = findViewById(R.id.locationCard);
        location = findViewById(R.id.location);
        ageRangebar = findViewById(R.id.ageRangebar);
        distanceRangebar = findViewById(R.id.distanceRangebar);
        distanceText = findViewById(R.id.distanceText);
        ageText = findViewById(R.id.ageText);
        showMeCard = findViewById(R.id.showMeCard);
        showMe = findViewById(R.id.showMe);
        instaSwitch = findViewById(R.id.instaSwitch);

        locationCard.setOnClickListener(this);
        showMeCard.setOnClickListener(this);

        instaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    showInstaPopup();
                else
                {
                    userSettings.setIsInstagramActive("0");
                    userSettings.setInstagramLink("");
                    new UpdateInstagram().execute();
                }
            }
        });


        distanceRangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                distanceText.setText(rightPinValue + " Km");
                userSettings.setMaxDistance(rightPinValue);
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
                userSettings.setMinAgeFilter(leftPinValue);
                userSettings.setMaxAgeFilter(rightPinValue);
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
        }
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
                user.setShowMe(adapter.getItem(i));
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
        titleTextView.setText("Enter you Instagram link");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Settings.this);
        dialogBuilder.setCustomTitle(titleView);

        final EditText input = new EditText(Settings.this);
        input.setText(userSettings.getInstagramLink());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(20,10,20,0);
        input.setLayoutParams(lp);
        dialogBuilder.setView(input);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Enter instagram profile link", Toast.LENGTH_SHORT).show();
                    return;
                }
                userSettings.setIsInstagramActive("1");
                userSettings.setInstagramLink(input.getText().toString());
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
                                user.setLatitude(String.valueOf(loc.getLatitude()));
                                user.setLongitude(String.valueOf(loc.getLongitude()));
                                List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                                Address address = addresses.get(0);
                                user.setCurrentLocation(address.getLocality());
                                user.setCurrentCountry(address.getCountryName());
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
        location.setText(user.getCurrentLocation() + ", " + user.getCurrentCountry());
        ageRangebar.setRangePinsByValue(Float.parseFloat(userSettings.getMinAgeFilter()), Float.parseFloat(userSettings.getMaxAgeFilter()));
        distanceRangebar.setSeekPinByValue(Float.parseFloat(userSettings.getMaxDistance()));
        showMe.setText(user.getShowMe());
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
                    params.put("longitude", user.getLongitude());
                    params.put("latitude", user.getLatitude());
                    params.put("currentLocation", user.getCurrentLocation() + ", " + user.getCurrentCountry());

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
                                localPref.saveUserSettings(userSettings);
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
                    params.put("maxDistance", userSettings.getMaxDistance());

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
                                localPref.saveUserSettings(userSettings);
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
                    params.put("minAgeFilter", userSettings.getMinAgeFilter());
                    params.put("maxAgeFilter", userSettings.getMaxAgeFilter());

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
                    params.put("showMe", user.getShowMe());

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

    private class UpdateInstagram extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.UPDATE_INSTAGRAM,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                Toast.makeText(getBaseContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                localPref.saveUserSettings(userSettings);
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
                    params.put("isInstagramActive", userSettings.getIsInstagramActive());
                    params.put("instagramLink", userSettings.getInstagramLink());

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
}