package com.neosao.truedates.mainfragments;

import android.os.AsyncTask;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neosao.truedates.R;
import com.neosao.truedates.adapters.CardStackAdapter;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.DummyProfile;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.ProfileDiffCallback;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.model.Profile;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    FloatingActionButton fabBack, fabDislike,fabLike,fabSuperLike,fabBoost;
    UserModel user;

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
        user = new LocalPref(getContext()).getUser();

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

                new FeatureDeductionOnSwipe(FEATURE_REWIND,"DT203KO9FIYV1").execute();
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
                new FeatureDeductionOnSwipe(FEATURE_LIKE,"DT203KO9FIYV1").execute();
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
                new FeatureDeductionOnSwipe(FEATURE_DISLIKE,"DT203KO9FIYV1").execute();
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
                new FeatureDeduction(FEATURE_DIAMOND,"DT203KO9FIYV1").execute();
            }
        });

        fabBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FeatureDeduction(FEATURE_BOOST,"DT203KO9FIYV1").execute();
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
                new FeatureDeductionOnSwipe(FEATURE_DIAMOND,"DT203KO9FIYV1").execute();
                break;
            case Left:
                new FeatureDeductionOnSwipe(FEATURE_LIKE,"DT203KO9FIYV1").execute();
                break;
            case Right:
                new FeatureDeductionOnSwipe(FEATURE_DISLIKE,"DT203KO9FIYV1").execute();
                break;
            case Bottom:

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

    class FeatureDeduction extends AsyncTask<Void,Void,Void>{

        String featureType;
        String toUserId;

        public FeatureDeduction(String featureType, String toUserId) {
            this.featureType = featureType;
            this.toUserId = toUserId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.FEATURE_DEDUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("check","Response : "+response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                    SwipeAnimationSetting setting;
                                    switch (featureType){
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
                                    if (object.has("message") && null != object.getString("message"))
                                        Toast.makeText(getContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e("check","Error in response catch: "+e.getLocalizedMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Log.e("check", new String(networkResponse.data));
                                    Toast.makeText(getContext(),new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId",user.getUserId());
                    params.put("toUserId",toUserId);
                    params.put("featureType",featureType);

                    Log.e("check","Req body : "+params.toString());
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

    class FeatureDeductionOnSwipe extends AsyncTask<Void,Void,Void>{

        String featureType;
        String toUserId;

        public FeatureDeductionOnSwipe(String featureType, String toUserId) {
            this.featureType = featureType;
            this.toUserId = toUserId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.FEATURE_DEDUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("check","Response : "+response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                } else {
                                    if (object.has("message") && null != object.getString("message"))
                                        Toast.makeText(getContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                    RewindAnimationSetting rewindSetting = new RewindAnimationSetting.Builder()
                                            .setDirection(Direction.Bottom)
                                            .setDuration(Duration.Normal.duration)
                                            .setInterpolator(new DecelerateInterpolator())
                                            .build();
                                    manager.setRewindAnimationSetting(rewindSetting);
                                    cardStackView.rewind();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e("check","Error in response catch: "+e.getLocalizedMessage());
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
                                    Log.e("check", new String(networkResponse.data));
                                    Toast.makeText(getContext(),new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId",user.getUserId());
                    params.put("toUserId",toUserId);
                    params.put("featureType",featureType);

                    Log.e("check","Req body : "+params.toString());
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