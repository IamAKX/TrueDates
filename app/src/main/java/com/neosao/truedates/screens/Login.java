package com.neosao.truedates.screens;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.API;
import com.neosao.truedates.configs.FirebaseLoginProvider;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.RequestQueueSingleton;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.FirebaseUserModel;
import com.neosao.truedates.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    Button facebookLoginButton;
    Button phoneLoginButton;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setUpFirebaseAuth();
        initializeComponents();
        loadVideoBackground();

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("check", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.e("check", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("check", "facebook:onError", error);
                        if (error instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Toast.makeText(getBaseContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        phoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), Otp.class));
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        Log.e("check", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("check", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUserModel userModel = new FirebaseUserModel(FirebaseLoginProvider.facebook.name(), true, user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), user.getPhotoUrl().toString(), user.getUid());
                            new LocalPref(getBaseContext()).saveFirebaseUser(userModel);
//                            startActivity(new Intent(getBaseContext(), OnboardingData.class));
                            new CheckForNewUser().execute();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("check", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void loadVideoBackground() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels + 500;

        videoView.setLayoutParams(params);

        Uri uri = Uri.parse("android.resource://"
                + getPackageName()
                + "/" // add a slash,
                + R.raw.background_video);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                mMediaPlayer.setLooping(true);
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });
    }

    private void initializeComponents() {
        videoView = findViewById(R.id.videoView);
        phoneLoginButton = findViewById(R.id.phoneLoginButton);
        facebookLoginButton = findViewById(R.id.facebookLoginButton);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    private class CheckForNewUser extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(Login.this, "Please wait...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            StringRequest stringObjectRequest = new StringRequest(Request.Method.POST, API.LOGIN_PROCESS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismissWithAnimation();
                            try {
                                Log.e("check", "onResponse: "+response);
                                JSONObject object = new JSONObject(response);
                                if (object.has("status") && object.getString("status").equals("200")) {
                                    if (object.has("result") && object.getJSONObject("result").has("member")){
                                        UserModel userModel = new Gson().fromJson(object.getJSONObject("result").getJSONObject("member").toString(),UserModel.class);
                                        if(null != userModel)
                                        {
                                            new LocalPref(getBaseContext()).saveUser(userModel);
                                            startActivity(new Intent(getBaseContext(), HomeContainer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        }
                                        else
                                        {
                                            startActivity(new Intent(getBaseContext(), OnboardingData.class));
                                        }
                                    }
                                    else
                                    {
                                        startActivity(new Intent(getBaseContext(), OnboardingData.class));
                                    }

                                } else {
//                                    if (object.has("message") && null != object.getString("message"))
//                                        Toast.makeText(getBaseContext(),object.getString("message"), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getBaseContext(), OnboardingData.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getBaseContext(),e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e("check","Error in response catch: "+e.getLocalizedMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismissWithAnimation();
                            NetworkResponse networkResponse = error.networkResponse;
                            if (error.networkResponse != null && new String(networkResponse.data) != null) {
                                if (new String(networkResponse.data) != null) {
                                    Log.e("check", new String(networkResponse.data));
                                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firebaseId",new LocalPref(getBaseContext()).getFirebaseUser().getFirebaseUUID());
//                    params.put("type","facebook");
                    Log.e("check", "Req body : " + params.toString());
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