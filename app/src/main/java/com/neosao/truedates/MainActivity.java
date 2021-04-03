package com.neosao.truedates;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.HomeContainer;
import com.neosao.truedates.screens.Login;
import com.neosao.truedates.screens.UploadProfileImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        Utils.setStatusBarGradiant(this);
        new FetchAdMobData().execute();
        printHashKey(getBaseContext());
        initiateLoginProcess();
    }

    private void initiateLoginProcess() {
        final LocalPref localPref = new LocalPref(getBaseContext());
//        localPref.logOutUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(localPref.getLoginStatus())
                        {
                            UserModel user = localPref.getUser();
                            if(null == user)
                                startActivity(new Intent(getBaseContext(), Login.class));
                            else
                            if (null==user.getMemberPhotos() || Utils.getPhotoCount(user.getMemberPhotos()) <4)
                                startActivity(new Intent(getBaseContext(), UploadProfileImage.class));
                            else
                                startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                        else
                            startActivity(new Intent(getBaseContext(), Login.class));
                        finish();
                    }
                });
            }
        }).start();

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("check", "hashKey: "+hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
    }

    private class FetchAdMobData extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.GET, API.GET_ADMOB_SETTING,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {

                                } else {
                                    if (object.has("message") && null != object.getString("message"))
                                        Toast.makeText(getBaseContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getBaseContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Toast.makeText(getBaseContext(),new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();


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
            RequestQueue requestQueue = RequestQueueSingleton.getInstance(getBaseContext())
                    .getRequestQueue();
            requestQueue.getCache().clear();
            requestQueue.add(stringObjectRequest);
            return null;
        }
    }
}