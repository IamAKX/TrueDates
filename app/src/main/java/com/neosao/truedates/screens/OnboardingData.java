package com.neosao.truedates.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.OnboardingAdapter;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.FirebaseUserModel;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.onboardingfragments.Habits;
import com.neosao.truedates.onboardingfragments.Intoduction;
import com.neosao.truedates.onboardingfragments.Others;
import com.neosao.truedates.onboardingfragments.Personal;
import com.neosao.truedates.onboardingfragments.Photos;
import com.neosao.truedates.onboardingfragments.Work;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class OnboardingData extends AppCompatActivity {
    ViewPager viewpager;
    Button nextBtn;
    Button prevBtn;
    SpringDotsIndicator springDotsIndicator;
    public static FirebaseUserModel firebaseUser;
    public static UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_data);

        firebaseUser = new LocalPref(getBaseContext()).getFirebaseUser();
        user = new UserModel();
        user.setUsername(firebaseUser.getName());
        user.setEmail(firebaseUser.getEmail());
        user.setContactNumber(firebaseUser.getPhoneNumber());
        user.setFirebaseId(firebaseUser.getFirebaseUUID());
        user.setRegisterType(firebaseUser.getLoginProvider());
        user.setProfileImage(firebaseUser.getProfileImage());

        initializeComponents();

    }

    private void initializeComponents() {
        viewpager = findViewById(R.id.viewpager);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        springDotsIndicator = findViewById(R.id.spring_dots_indicator);

        OnboardingAdapter adapter = new OnboardingAdapter(getSupportFragmentManager());
        adapter.addFrag(new Intoduction());
        adapter.addFrag(new Work());
        adapter.addFrag(new Personal());
        adapter.addFrag(new Habits());
        adapter.addFrag(new Others());
        adapter.addFrag(new Photos());
        viewpager.setAdapter(adapter);

        springDotsIndicator.setViewPager(viewpager);
        viewpager.setOnPageChangeListener(new PageListener());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager.getCurrentItem() < 5)
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                else
                    startActivity(new Intent(getBaseContext(), HomeContainer.class));
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewpager.getCurrentItem() > 0)
                    viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
            }
        });
    }

    int currentPage;

    class PageListener extends ViewPager.SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            Log.e("check", "current : " + position);
            if (position == 5)
                nextBtn.setText("Save");
            else
                nextBtn.setText("Next");
        }
    }

}