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

public class Personal extends Fragment implements View.OnClickListener {

    View rootView;
    public static MaterialEditText zodiac, height, relationshipStatus, caste, religion, showMe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.onboading_personal, container, false);

        zodiac = rootView.findViewById(R.id.zodiac);
        height = rootView.findViewById(R.id.height);
        relationshipStatus = rootView.findViewById(R.id.relationshipStatus);
        caste = rootView.findViewById(R.id.caste);
        religion = rootView.findViewById(R.id.religion);
        showMe = rootView.findViewById(R.id.showMe);

        setEditTextListners();

        zodiac.setOnClickListener(this);
        height.setOnClickListener(this);
        relationshipStatus.setOnClickListener(this);
        caste.setOnClickListener(this);
        religion.setOnClickListener(this);
        showMe.setOnClickListener(this);

        return rootView;
    }

    private void setEditTextListners() {
        zodiac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setZodiacSign(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setHeight(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        relationshipStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setRelationshipStatus(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        caste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setCaste(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        religion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setReligion(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        showMe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setShowMe(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.zodiac:
                showOptionPopup("Zodiac Sign", (MaterialEditText) view, DynamicOptionConstants.ZODIAC_SIGNS_OPTION);
                break;
            case R.id.height:
                showOptionPopup("Height", (MaterialEditText) view, OptionContants.HEIGHT_OPTIONS);
                break;
            case R.id.relationshipStatus:
                showOptionPopup("Relationship Status", (MaterialEditText) view, OptionContants.RELATIONSHIP_OPTIONS);
                break;
            case R.id.caste:
                showOptionPopup("Caste", (MaterialEditText) view, DynamicOptionConstants.CASTE_OPTION);
                break;
            case R.id.religion:
                showOptionPopup("Religion", (MaterialEditText) view, OptionContants.RELATIONSHIP_OPTIONS);
                break;
            case R.id.showMe:
                showOptionPopup("Show me", (MaterialEditText) view, OptionContants.SHOW_ME_OPTIONS);
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