package com.neosao.truedates.onboardingfragments;

import android.graphics.Typeface;
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
import android.widget.BaseAdapter;
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
    public static MaterialEditText zodiac, height, relationshipStatus, caste, religion, showMe,maritalStatus;

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
        maritalStatus = rootView.findViewById(R.id.maritalStatus);

        setEditTextListners();

        zodiac.setOnClickListener(this);
        height.setOnClickListener(this);
        relationshipStatus.setOnClickListener(this);
        caste.setOnClickListener(this);
        religion.setOnClickListener(this);
        showMe.setOnClickListener(this);
        maritalStatus.setOnClickListener(this);

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
                OnboardingData.user.getMembersettings().get(0).setShowMe(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        maritalStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setMaritalStatus(charSequence.toString());
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
                showOptionPopup("Zodiac Sign", (MaterialEditText) view, OptionContants.ZODIAC_OPTIONS);
                break;
            case R.id.height:
                showOptionPopup("Height", (MaterialEditText) view, OptionContants.HEIGHT_OPTIONS);
                break;
            case R.id.relationshipStatus:
                showOptionPopup("Relationship Status", (MaterialEditText) view, OptionContants.RELATIONSHIP_OPTIONS);
                break;
            case R.id.maritalStatus:
                showOptionPopup("Marital Status", (MaterialEditText) view, OptionContants.MARITAL_OPTIONS);
                break;
            case R.id.caste:
                showOptionPopup("Caste", (MaterialEditText) view, DynamicOptionConstants.CASTE_OPTION);
                break;
            case R.id.religion:
                showOptionPopup("Religion", (MaterialEditText) view, DynamicOptionConstants.RELIGION_OPTION);
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
                TextView textView = row.findViewById( android.R.id.text1);
                textView.setText(String.valueOf(getItem(i)));
                if(!editText.getText().toString().isEmpty() && editText.getText().toString().equals(getItem(i)))
                {
                    textView.setTextColor(getContext().getResources().getColor(R.color.themePink));
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                }
                return row;
            }
        };
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
                editText.setText(String.valueOf(adapter.getItem(i)));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}