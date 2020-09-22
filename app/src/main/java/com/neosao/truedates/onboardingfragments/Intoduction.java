package com.neosao.truedates.onboardingfragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.screens.OnboardingData;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import static android.location.LocationManager.NETWORK_PROVIDER;


public class Intoduction extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public static MaterialEditText name, email, about, gender, dob, location;
    View rootView;
    final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.onboading_intoduction, container, false);

        name = rootView.findViewById(R.id.name);
        email = rootView.findViewById(R.id.email);
        about = rootView.findViewById(R.id.about);
        gender = rootView.findViewById(R.id.gender);
        dob = rootView.findViewById(R.id.dob);
        location = rootView.findViewById(R.id.location);

        setEditTextListners();
        name.setText(OnboardingData.firebaseUser.getName());
        email.setText(OnboardingData.firebaseUser.getEmail());

        gender.setOnClickListener(this);
        dob.setOnClickListener(this);
        location.setOnClickListener(this);

        return rootView;
    }

    private void setEditTextListners() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        about.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setAboutMe(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setGender(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setBirthDate(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setCurrentLocation(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gender:
                 showOptionPopup("Gender", (MaterialEditText) view, OptionContants.GENDER_OPTIONS);
                break;
            case R.id.dob:
                new DatePickerDialog(getActivity(), R.style.DialogTheme, this,  calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.location:
                getAddress();
                break;
        }
    }

    private void getAddress() {
        new Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        // given permissions are granted
                        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        Location loc = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                            Address address = addresses.get(0);
//                            Log.e("check",address.getAddressLine(0)); //271/1, Gangapuri, Block B and C, New Tollygunge, Aurobindo Park, South Kolkata, West Bengal 700093, India
//                            Log.e("check",address.getAdminArea()); // West Bengal
//                            Log.e("check",address.getLocality()); // South Kolkata
//                            Log.e("check",address.getPremises()); // 271/1
//                            Log.e("check",address.getSubLocality()); // Aurobindo Park
//                            Log.e("check",address.getSubAdminArea()); // Kolkata
//                            Log.e("check",address.getCountryName()); //India

                            location.setText(address.getLocality()+", "+address.getCountryName());
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                })
                .whenPermissionsRefused(new PermissionsRefusedListener() {
                    @Override
                    public void onPermissionsRefused(String[] permissions) {
                        Toast.makeText(getContext(),"We need you parmission to fetch your address", Toast.LENGTH_LONG).show();
                    }
                })
                .execute(getActivity());
    }

    private void showOptionPopup(String title, final MaterialEditText editText, String[] options) {
        LayoutInflater inflater = getLayoutInflater();

        View titleView =  inflater.inflate(R.layout.alertdialogbox_title, null);
        TextView titleTextView = titleView.findViewById(R.id.title);
        titleTextView.setText(title);
        ImageButton clear_text = titleView.findViewById(R.id.clear_text);
        View dialogView = inflater.inflate(R.layout.listview_option, null);
        ListView listView = dialogView.findViewById(R.id.select_dialog_listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, options);
        listView.setAdapter(adapter);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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
        Log.e("check","Age : "+diff);
        if(diff >= 18)
        {
            Toast.makeText(getContext(),"You should be atleast 18 years old", Toast.LENGTH_LONG).show();
        }
        else
        {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            dob.setText(format.format(personBirthDate.getTime()));
        }

    }
}