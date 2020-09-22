package com.neosao.truedates.onboardingfragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.neosao.truedates.R;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.screens.OnboardingData;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Habits extends Fragment implements View.OnClickListener {

    View rootView;
    public static MaterialEditText drinks, smoke, diet, pets, interests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.onboading_habits, container, false);

        drinks = rootView.findViewById(R.id.drinks);
        smoke = rootView.findViewById(R.id.smoke);
        diet = rootView.findViewById(R.id.diet);
        pets = rootView.findViewById(R.id.pets);
        interests = rootView.findViewById(R.id.interests);

        setEditTextListners();

        drinks.setOnClickListener(this);
        smoke.setOnClickListener(this);
        diet.setOnClickListener(this);
        pets.setOnClickListener(this);
        interests.setOnClickListener(this);


        return rootView;
    }

    private void setEditTextListners() {
        drinks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setDrink(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        smoke.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setSmoke(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        diet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setDiet(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pets.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setPets(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        interests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setIntrests(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.drinks:
                showOptionPopup("Drinks", (MaterialEditText) view, OptionContants.DRINKS_OPTIONS);
                break;
            case R.id.smoke:
                showOptionPopup("Smoke", (MaterialEditText) view, OptionContants.SMOKE_OPTIONS);
                break;
            case R.id.diet:
                showOptionPopup("Diet", (MaterialEditText) view, OptionContants.DIET_OPTIONS);
                break;
            case R.id.pets:
                showOptionPopup("Pets", (MaterialEditText) view, OptionContants.PET_OPTIONS);
                break;
            case R.id.interests:
                showOptionPopup("Interests", (MaterialEditText) view, DynamicOptionConstants.INTEREST_OPTION);
                break;
        }
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
}