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
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.screens.OnboardingData;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Others extends Fragment implements View.OnClickListener {

    View rootView;
    MaterialEditText haveKids, wantKids, lookingFor, bodyType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.onboading_others, container, false);

        haveKids = rootView.findViewById(R.id.haveKids);
        wantKids = rootView.findViewById(R.id.wantKids);
        lookingFor = rootView.findViewById(R.id.lookingFor);
        bodyType = rootView.findViewById(R.id.bodyType);

        setEditTextListner();

        haveKids.setOnClickListener(this);
        wantKids.setOnClickListener(this);
        lookingFor.setOnClickListener(this);
        bodyType.setOnClickListener(this);

        return rootView;
    }

    private void setEditTextListner() {
        haveKids.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setHaveKids(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        wantKids.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setWantKids(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lookingFor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setLookingFor(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bodyType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                OnboardingData.user.setBodyType(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.haveKids:
                showOptionPopup("Have Kids", (MaterialEditText) view, OptionContants.HAVE_KIDS_OPTIONS);
                break;
            case R.id.wantKids:
                showOptionPopup("Want Kids", (MaterialEditText) view, OptionContants.WANT_KIDS_OPTIONS);
                break;
            case R.id.lookingFor:
                showOptionPopup("Looking For", (MaterialEditText) view, OptionContants.LOOKING_FOR_OPTIONS);
                break;
            case R.id.bodyType:
                showOptionPopup("Body Type", (MaterialEditText) view, OptionContants.BODY_TYPE_OPTIONS);
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