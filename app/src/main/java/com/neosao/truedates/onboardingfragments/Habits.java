package com.neosao.truedates.onboardingfragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.neosao.truedates.R;
import com.neosao.truedates.configs.DynamicOptionConstants;
import com.neosao.truedates.configs.OptionContants;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.MemberInterests;
import com.neosao.truedates.model.options.Interest;
import com.neosao.truedates.screens.OnboardingData;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;

import mabbas007.tagsedittext.TagsEditText;

public class Habits extends Fragment implements View.OnClickListener {

    View rootView;
    public static MaterialEditText drinks, smoke, diet, pets;
    public static TagsEditText interests;

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


                ArrayList<String> interestArrayList;
                if(charSequence.toString().isEmpty())
                    interestArrayList = new ArrayList<>();
                else
                    interestArrayList = new ArrayList<>(Arrays.asList(charSequence.toString().split(" ")));

                OnboardingData.user.setMemberInterests(new ArrayList<MemberInterests>());
                for(String item : interestArrayList)
                {
                    Interest interest = new Utils().getInterestCode(item);
                    if(null != interest)
                    {
                        MemberInterests memberInterests = new MemberInterests();
                        memberInterests.setInterestCode(interest.getCode());
                        memberInterests.setInterestName(interest.getInterestName());

                        memberInterests.setMemberInterestValue(interest.getInterestValue());

                        OnboardingData.user.getMemberInterests().add(memberInterests);

                    }
                }


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
                showInterestOptionPopup("Interests", (TagsEditText) view, DynamicOptionConstants.INTEREST_OPTION);
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
        if(editText.getText().toString().isEmpty())
            interestArrayList = new ArrayList<>();
        else
            interestArrayList = (ArrayList<String>) editText.getTags();


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


                if(interestArrayList.contains(getItem(i)))
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

                if(interestArrayList.contains(String.valueOf(adapter.getItem(i))))
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
}