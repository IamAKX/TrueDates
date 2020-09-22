package com.neosao.truedates.onboardingfragments;

import android.os.Bundle;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.neosao.truedates.R;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.screens.OnboardingData;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Work extends Fragment implements View.OnClickListener {

    View rootView;
    MaterialEditText university, fieldOfStudy, qualification, workIndustry, experience, motherTongue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.onboading_work, container, false);

        university = rootView.findViewById(R.id.university);
        fieldOfStudy = rootView.findViewById(R.id.fieldOfStudy);
        qualification = rootView.findViewById(R.id.qualification);
        workIndustry = rootView.findViewById(R.id.workIndustry);
        experience = rootView.findViewById(R.id.experience);
        motherTongue = rootView.findViewById(R.id.motherTongue);

        setEditTextListners();

        fieldOfStudy.setOnClickListener(this);
        qualification.setOnClickListener(this);
        workIndustry.setOnClickListener(this);
        experience.setOnClickListener(this);
        motherTongue.setOnClickListener(this);


        return rootView;
    }

    private void setEditTextListners() {
        university.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setUniversity(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fieldOfStudy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setFieldOfStudy(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        qualification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setQualification(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        workIndustry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setWorkIndustry(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        experience.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setExperience(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        motherTongue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setMotherTounge(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fieldOfStudy:
                showOptionPopup("Field of Study", (MaterialEditText) view, DynamicOptionConstants.FIELD_OF_STUDY_OPTION);
                break;
            case R.id.qualification:
                showOptionPopup("Qualification", (MaterialEditText) view, OptionContants.QUALIFICATION_OPTIONS);
                break;
            case R.id.workIndustry:
                showOptionPopup("Work Industry", (MaterialEditText) view, DynamicOptionConstants.WORK_INDUSTRY_OPTION);
                break;
            case R.id.experience:
                showOptionPopup("Experience", (MaterialEditText) view, OptionContants.EXPERIENCE_OPTIONS);
                break;
            case R.id.motherTongue:
                showOptionPopup("Mother Tongue", (MaterialEditText) view, DynamicOptionConstants.MOTHER_TONGUE_OPTION);
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