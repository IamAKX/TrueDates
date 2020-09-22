package com.neosao.truedates.mainfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.CardStackAdapter;
import com.neosao.truedates.configs.DummyProfile;
import com.neosao.truedates.configs.ProfileDiffCallback;
import com.neosao.truedates.model.Profile;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateBrowser extends Fragment implements CardStackListener {
    CardStackView cardStackView;
    CardStackLayoutManager manager;
    CardStackAdapter adapter;
    View rootView;
    FloatingActionButton fabBack, fabDislike,fabLike,fabSuperLike,fabBoost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_date_browser, container, false);

        cardStackView = rootView.findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(getContext(), this);
        adapter = new CardStackAdapter(getContext(), new DummyProfile().getAllProfileList());
        fabBack = rootView.findViewById(R.id.fabBack);
        fabDislike = rootView.findViewById(R.id.fabSkip);
        fabLike = rootView.findViewById(R.id.fabLike);
        fabSuperLike = rootView.findViewById(R.id.fabSuperLike);
        fabBoost = rootView.findViewById(R.id.fabBoost);

        setupNavigation();
        setupCardStackView();

        return rootView;
    }


    private void setupCardStackView() {
        List<Direction> swipeDirection = Arrays.asList(Direction.Left, Direction.Right, Direction.Top);

        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(4);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(swipeDirection);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupNavigation() {
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RewindAnimationSetting setting = new RewindAnimationSetting.Builder()
                        .setDirection(Direction.Bottom)
                        .setDuration(Duration.Normal.duration)
                        .setInterpolator(new DecelerateInterpolator())
                        .build();
                manager.setRewindAnimationSetting(setting);
                cardStackView.rewind();
            }
        });

        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Left)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();

            }
        });

        fabDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Right)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });

        fabSuperLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                        .setDirection(Direction.Top)
                        .setDuration(Duration.Slow.duration)
                        .setInterpolator(new AccelerateInterpolator())
                        .build();
                manager.setSwipeAnimationSetting(setting);
                cardStackView.swipe();
            }
        });
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction");
        // Refill the stack
//        if (manager.getTopPosition() == adapter.getItemCount() - 2) {
//            paginate();
//        }
        switch (direction)
        {
            case Top:
                Toast.makeText(getContext(),"Call Diamond Like API", Toast.LENGTH_SHORT).show();
                break;
            case Left:
                Toast.makeText(getContext(),"Call Like API", Toast.LENGTH_SHORT).show();
                break;
            case Right:
                Toast.makeText(getContext(),"Call Dislike API", Toast.LENGTH_SHORT).show();
                break;
            case Bottom:
                Toast.makeText(getContext(),"Can not do bottom swipe", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void paginate() {
        ArrayList<Profile> oldProfile = adapter.getList();
        ArrayList<Profile> newProfile = new ArrayList<>();
        newProfile.addAll(new DummyProfile().getAllProfileList());
        ProfileDiffCallback callback = new ProfileDiffCallback(oldProfile, newProfile);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        adapter.setList(newProfile);
        result.dispatchUpdatesTo(adapter);
    }

    @Override
    public void onCardRewound() {
        Log.d("CardStackView", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: " + manager.getTopPosition());
    }

    @Override
    public void onCardAppeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_name);
        Log.d("CardStackView", "onCardAppeared: " + position + " " + textView.getText().toString() + "");
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_name);
        Log.d("CardStackView", "onCardDisappeared: " + position + " " + textView.getText().toString() + "");
    }
}