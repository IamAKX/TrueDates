package com.neosao.truedates.mainfragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.CardStackAdapter;
import com.neosao.truedates.adapters.MemberLikedYouAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.UserModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.RewindAnimationSetting;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.ghyeok.stickyswitch.widget.StickySwitch;

import static com.neosao.truedates.configs.Constants.FEATURE_BOOST;
import static com.neosao.truedates.configs.Constants.FEATURE_DIAMOND;
import static com.neosao.truedates.configs.Constants.FEATURE_DISLIKE;
import static com.neosao.truedates.configs.Constants.FEATURE_LIKE;
import static com.neosao.truedates.configs.Constants.FEATURE_REWIND;

public class DateBrowser extends Fragment implements CardStackListener {
    CardStackView cardStackView;
    CardStackLayoutManager manager;
    CardStackAdapter adapter;
    View rootView;
    LinearLayout progressView, button_container,notfound;
    FloatingActionButton fabBack, fabDislike, fabLike, fabSuperLike, fabBoost;
    UserModel user;
    ArrayList<Object> memberProfileList = new ArrayList<>();
    ArrayList<UserModel> memberLikedYouList = new ArrayList<>();
    int profileOffset = 0;
    StickySwitch feedSwitch;
    RecyclerView likeRecyclerView;
    RelativeLayout likedView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_date_browser, container, false);

        cardStackView = rootView.findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(getContext(), this);
        fabBack = rootView.findViewById(R.id.fabBack);
        fabDislike = rootView.findViewById(R.id.fabSkip);
        fabLike = rootView.findViewById(R.id.fabLike);
        fabSuperLike = rootView.findViewById(R.id.fabSuperLike);
        fabBoost = rootView.findViewById(R.id.fabBoost);
        progressView = rootView.findViewById(R.id.progressView);
        feedSwitch = rootView.findViewById(R.id.feedSwitch);
        button_container = rootView.findViewById(R.id.button_container);
        likeRecyclerView = rootView.findViewById(R.id.likeRecyclerView);
        likedView = rootView.findViewById(R.id.likedView);
        user = new LocalPref(getContext()).getUser();
        notfound = rootView.findViewById(R.id.notfound);
        notfound.setVisibility(View.GONE);

        button_container.setVisibility(View.VISIBLE);

        setupNavigation();
//        setupCardStackView();
        cardStackView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        new LoadAllMemberProfile().execute();
        new LoadWhoLikedMe().execute();

        likedView.setVisibility(View.GONE);

        feedSwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String s) {
                if (direction == StickySwitch.Direction.RIGHT) {
                    cardStackView.setVisibility(View.GONE);
                    notfound.setVisibility(View.GONE);
                    button_container.setVisibility(View.GONE);
                    likedView.setVisibility(View.VISIBLE);
                    if(memberLikedYouList.size() == 0)

                        notfound.setVisibility(View.VISIBLE);
                    else
                        notfound.setVisibility(View.GONE);


                } else {
                    notfound.setVisibility(View.GONE);
                    cardStackView.setVisibility(View.VISIBLE);
//                    if(memberProfileList.size() > 0)

                    likedView.setVisibility(View.GONE);
                    if(memberProfileList.size() == 0)

                    {
                        notfound.setVisibility(View.VISIBLE);
                        button_container.setVisibility(View.GONE);
                    }
                    else
                    {
                        notfound.setVisibility(View.GONE);
                        button_container.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        Log.e("check", "onCreateView: "+memberProfileList.size() );

        return rootView;
    }




    private void setupCardStackView() {
        List<Direction> swipeDirection = Arrays.asList(Direction.Left, Direction.Right, Direction.Top);
        adapter = new CardStackAdapter(getContext(), memberProfileList);

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
                if( memberProfileList.get(manager.getTopPosition() ) instanceof UserModel) {
                    new FeatureDeductionOnSwipe(FEATURE_REWIND).execute();
                }
                else
                    Toast.makeText(getContext(),"Swipe the ad card", Toast.LENGTH_SHORT).show();
            }
        });

        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( memberProfileList.get(manager.getTopPosition() ) instanceof UserModel) {
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Right)
                            .setDuration(Duration.Slow.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    cardStackView.swipe();
                    new FeatureDeductionOnSwipe(FEATURE_LIKE).execute();
                }
                else
                {
//                    Toast.makeText(getContext(),"Swipe the ad card", Toast.LENGTH_SHORT).show();
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Right)
                            .setDuration(Duration.Slow.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    cardStackView.swipe();
                }
            }
        });

        fabDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( memberProfileList.get(manager.getTopPosition() ) instanceof UserModel) {
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Left)
                            .setDuration(Duration.Slow.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    cardStackView.swipe();
                    new FeatureDeductionOnSwipe(FEATURE_DISLIKE).execute();
                }
                else
                {
                    //                    Toast.makeText(getContext(),"Swipe the ad card", Toast.LENGTH_SHORT).show();
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Left)
                            .setDuration(Duration.Slow.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    cardStackView.swipe();
                }
            }
        });

        fabSuperLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( memberProfileList.get(manager.getTopPosition() ) instanceof UserModel) {
                    SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                            .setDirection(Direction.Top)
                            .setDuration(Duration.Slow.duration)
                            .setInterpolator(new AccelerateInterpolator())
                            .build();
                    manager.setSwipeAnimationSetting(setting);
                    cardStackView.swipe();
                    new FeatureDeduction(FEATURE_DIAMOND).execute();
                }
                else
                    Toast.makeText(getContext(),"Swipe the ad card", Toast.LENGTH_SHORT).show();
            }
        });

        fabBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( memberProfileList.get(manager.getTopPosition() ) instanceof UserModel) {
                    new FeatureDeduction(FEATURE_BOOST).execute();
                }
                else
                    Toast.makeText(getContext(),"Swipe the ad card", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
//        Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
    }

    @Override
    public void onCardSwiped(Direction direction) {
//        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction");
        // Refill the stack
//        if (manager.getTopPosition() == adapter.getItemCount() - 2) {
//            paginate();
//        }
        switch (direction) {
            case Top:
                if (memberProfileList.get(manager.getTopPosition() - 1) instanceof UserModel)
                    new FeatureDeductionOnSwipe(FEATURE_DIAMOND).execute();
                break;
            case Right:
                if (memberProfileList.get(manager.getTopPosition() - 1) instanceof UserModel)
                    new FeatureDeductionOnSwipe(FEATURE_LIKE).execute();
                break;
            case Left:
                if (memberProfileList.get(manager.getTopPosition() - 1) instanceof UserModel)
                    new FeatureDeductionOnSwipe(FEATURE_DISLIKE).execute();
                break;
            case Bottom:

                break;
        }
    }

//    private void paginate() {
//        ArrayList<UserModel> oldProfile = adapter.getList();
//        ArrayList<UserModel> newProfile = new ArrayList<>();
//        newProfile.addAll(memberProfileList);
//        ProfileDiffCallback callback = new ProfileDiffCallback(oldProfile, newProfile);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
//        adapter.setList(newProfile);
//        result.dispatchUpdatesTo(adapter);
//    }

    @Override
    public void onCardRewound() {
//        Log.e("CardStackView", "onCardRewound: " + manager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
//        Log.e("CardStackView", "onCardCanceled: " + manager.getTopPosition());
    }

    @Override
    public void onCardAppeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_name);
//        Log.e("CardStackView", "onCardAppeared: " + position + " " + textView.getText().toString() + "");
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        TextView textView = view.findViewById(R.id.item_name);
//        Log.e("CardStackView", "onCardDisappeared: " + position + " " + textView.getText().toString() + "");
        if (position == memberProfileList.size() - 1) {
            profileOffset = 0;
            new LoadAllMemberProfile().execute();
        }
    }

    class FeatureDeduction extends AsyncTask<Void, Void, Void> {

        String featureType;
        String toUserId;

        public FeatureDeduction(String featureType) {
            this.featureType = featureType;
            if (manager.getTopPosition() > 0 && memberProfileList.get(manager.getTopPosition() - 1) instanceof UserModel) {
                UserModel temp = (UserModel) (memberProfileList.get(manager.getTopPosition() - 1));
                this.toUserId = temp.getUserId();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.FEATURE_DEDUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                    SwipeAnimationSetting setting;
                                    switch (featureType) {
                                        case FEATURE_LIKE:
                                            setting = new SwipeAnimationSetting.Builder()
                                                    .setDirection(Direction.Left)
                                                    .setDuration(Duration.Slow.duration)
                                                    .setInterpolator(new AccelerateInterpolator())
                                                    .build();
                                            manager.setSwipeAnimationSetting(setting);
                                            cardStackView.swipe();
                                            break;
                                        case FEATURE_DISLIKE:
                                            setting = new SwipeAnimationSetting.Builder()
                                                    .setDirection(Direction.Right)
                                                    .setDuration(Duration.Slow.duration)
                                                    .setInterpolator(new AccelerateInterpolator())
                                                    .build();
                                            manager.setSwipeAnimationSetting(setting);
                                            cardStackView.swipe();
                                            break;
                                        case FEATURE_DIAMOND:
                                            setting = new SwipeAnimationSetting.Builder()
                                                    .setDirection(Direction.Top)
                                                    .setDuration(Duration.Slow.duration)
                                                    .setInterpolator(new AccelerateInterpolator())
                                                    .build();
                                            manager.setSwipeAnimationSetting(setting);
                                            cardStackView.swipe();
                                            break;
                                        case FEATURE_REWIND:
                                            RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder()
                                                    .setDirection(Direction.Bottom)
                                                    .setDuration(Duration.Normal.duration)
                                                    .setInterpolator(new DecelerateInterpolator())
                                                    .build();
                                            manager.setRewindAnimationSetting(rewindSetting);
                                            cardStackView.rewind();
                                            break;
                                        case FEATURE_BOOST:
                                            break;
                                    }
                                } else {
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", user.getUserId());
                    params.put("toUserId", toUserId);
                    params.put("featureType", featureType);

                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    class FeatureDeductionOnSwipe extends AsyncTask<Void, Void, Void> {

        String featureType;
        String toUserId;

        public FeatureDeductionOnSwipe(String featureType) {
            this.featureType = featureType;
            if (manager.getTopPosition() > 0 && memberProfileList.get(manager.getTopPosition() - 1) instanceof UserModel) {
                UserModel temp = (UserModel) (memberProfileList.get(manager.getTopPosition() - 1));
                this.toUserId = temp.getUserId();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.FEATURE_DEDUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                    if (featureType.equals(FEATURE_REWIND)) {
                                        RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder()
                                                .setDirection(Direction.Bottom)
                                                .setDuration(Duration.Normal.duration)
                                                .setInterpolator(new DecelerateInterpolator())
                                                .build();
                                        manager.setRewindAnimationSetting(rewindSetting);
                                        cardStackView.rewind();
                                    }
                                } else {
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder()
                                        .setDirection(Direction.Bottom)
                                        .setDuration(Duration.Normal.duration)
                                        .setInterpolator(new DecelerateInterpolator())
                                        .build();
                                manager.setRewindAnimationSetting(rewindSetting);
                                cardStackView.rewind();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", user.getUserId());
                    params.put("toUserId", toUserId);
                    params.put("featureType", featureType);

                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private class LoadAllMemberProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardStackView.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            notfound.setVisibility(View.GONE);
            button_container.setVisibility(View.VISIBLE);
//            loadAdInMemberList();
//            setupCardStackView();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", user.getUserId());
            params.put("latitude", user.getMembersettings().get(0).getLatitude());
            params.put("longitude", user.getMembersettings().get(0).getLongitude());
            params.put("showMe", user.getMembersettings().get(0).getShowMe());
            params.put("maxDistance", user.getMembersettings().get(0).getMaxDistance());
            params.put("minAgeFilter", user.getMembersettings().get(0).getMinAgeFilter());
            params.put("maxAgeFilter", user.getMembersettings().get(0).getMaxAgeFilter());
            params.put("offset", String.valueOf(profileOffset));


            StringRequest stringObjectRequest = new StringRequest(Request.Method.GET, API.GET_PROFILE_LIST + Utils.buildQueryFromMap(params),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject object = new JSONObject(response);

                                if (object.has("status") && object.getString("status").equals("200")) {

                                    if (object.has("result") && object.getJSONObject("result").has("membersData")) {
                                        JSONArray array = object.getJSONObject("result").getJSONArray("membersData");
                                        if (null == array || array.length() == 0) {
                                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                                    .setContentText("No dates near your location. Try changing your location or increasing distance range in settings.")
                                                    .setConfirmText("Okay")
                                                    .show();
                                            notfound.setVisibility(View.VISIBLE);
                                            button_container.setVisibility(View.GONE);


                                        } else {
                                            int addFreq = new LocalPref(getContext()).getAppSettings().getAddAfterProfile();
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject memberObject = array.getJSONObject(i);
                                                memberObject = memberObject.getJSONObject("member");
                                                UserModel memberModel = new Gson().fromJson(memberObject.toString(), UserModel.class);
                                                memberProfileList.add(memberModel);
                                                if(i!=0 && addFreq !=0 && i%addFreq==0)
                                                    loadAdInMemberList();
                                            }
                                            setupCardStackView();
                                            button_container.setVisibility(View.VISIBLE);

                                        }
                                    }

                                } else {
                                    button_container.setVisibility(View.GONE);
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                button_container.setVisibility(View.GONE);
                            }
                            cardStackView.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.GONE);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            cardStackView.setVisibility(View.VISIBLE);
                            progressView.setVisibility(View.GONE);
                            notfound.setVisibility(View.VISIBLE);
                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }

    private void loadAdInMemberList() {
        final AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.WIDE_SKYSCRAPER);
        adView.setAdUnitId("ca-app-pub-7095480517399381/5987791915");
        memberProfileList.add(adView);
    }

    private class LoadWhoLikedMe extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", user.getUserId());
//            params.put("userId", "DT20YKLXARAAC");
            params.put("offset", String.valueOf(profileOffset));

            Log.i("check", "API: " + API.GET_PROFILE_WHO_LIKED_ME_LIST + Utils.buildQueryFromMap(params));

            StringRequest stringObjectRequest = new StringRequest(Request.Method.GET, API.GET_PROFILE_WHO_LIKED_ME_LIST + Utils.buildQueryFromMap(params),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                    if (object.has("result") && object.getJSONObject("result").has("likesData")) {
                                        JSONArray array = object.getJSONObject("result").getJSONArray("likesData");
                                        if (null == array || array.length() == 0) {

                                        } else {
                                            memberLikedYouList.clear();
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject memberObject = array.getJSONObject(i);
                                                memberObject = memberObject.getJSONObject("member");
                                                UserModel memberModel = new Gson().fromJson(memberObject.toString(), UserModel.class);
                                                memberLikedYouList.add(memberModel);
                                            }
                                            MemberLikedYouAdapter adapter = new MemberLikedYouAdapter(getContext(), memberLikedYouList);
                                            likeRecyclerView.setHasFixedSize(true);
                                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                                            likeRecyclerView.setLayoutManager(mLayoutManager);
                                            likeRecyclerView.setAdapter(adapter);
                                        }
                                    }

                                } else {
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


                    return params;
                }
            };

            stringObjectRequest.setShouldCache(false);
            stringObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }



}