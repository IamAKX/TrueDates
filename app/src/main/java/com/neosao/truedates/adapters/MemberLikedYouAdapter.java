package com.neosao.truedates.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MemberLikedYouAdapter extends RecyclerView.Adapter<MemberLikedYouAdapter.LikeViewHolder> {

    private Context context;
    private ArrayList<UserModel> likeList;

    public MemberLikedYouAdapter(Context context, ArrayList<UserModel> likeList) {
        this.context = context;
        this.likeList = likeList;
    }

    @NonNull
    @Override
    public MemberLikedYouAdapter.LikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_like_card, parent, false);
        return new MemberLikedYouAdapter.LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberLikedYouAdapter.LikeViewHolder holder, int position) {
        UserModel userModel = likeList.get(position);

        if (null != userModel.getDefaultPhoto()) {
//            Glide.with(context)
//                    .load(userModel.getDefaultPhoto())
//                    .into(holder.item_image);


        }


        holder.likeName.setText(userModel.getName());
        holder.item_city.setText(userModel.getAge() + " years old");

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buySubscriptionPrompt();
//                new FeatureDeductionOnSwipe(FEATURE_LIKE, userModel.getUserId()).execute();
            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buySubscriptionPrompt();
//                new FeatureDeductionOnSwipe(FEATURE_DISLIKE, userModel.getUserId()).execute();
            }
        });
    }

    private void buySubscriptionPrompt() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Buy subscription")
                .setContentText("Purchase subscription to see who liked you, like or dislike them.")
                .setConfirmText("okay")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        context.startActivity(new Intent(context, Settings.class));
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }

    public class LikeViewHolder extends RecyclerView.ViewHolder {

        TextView likeName;
        TextView item_city;
        ImageView item_image, like, dislike;

        public LikeViewHolder(@NonNull View itemView) {
            super(itemView);
            likeName = itemView.findViewById(R.id.item_name);
            item_image = itemView.findViewById(R.id.item_image);
            item_city = itemView.findViewById(R.id.item_city);
            like = itemView.findViewById(R.id.like);
            dislike = itemView.findViewById(R.id.dislike);
        }
    }

    class FeatureDeductionOnSwipe extends AsyncTask<Void, Void, Void> {

        String featureType;
        String toUserId;
        String userId;

        public FeatureDeductionOnSwipe(String featureType, String toUserId) {
            this.featureType = featureType;
            this.toUserId = toUserId;
            this.userId = new LocalPref(context).getUser().getUserId();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.FEATURE_DEDUCTION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
//                                if (object.has("message") && null != object.getString("message"))
//                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
//                                    Toast.makeText(context, new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", userId);
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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(context)
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }
}
