package com.neosao.truedates.screens;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class PhoneValidation extends AppCompatActivity {
    ImageView editPhone;
    OtpTextView otp_view;
    TextView resendOTP, mobileNumber;

    final int OTP_TIME_OUT = 60;
    private String verificationCodeId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_validation);

        initializeComponent();
        setupFirebaseAuth();

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                if (otp == null || otp.isEmpty())
                    return;
                if (verificationCodeId == null)
                    verificationCodeId = "111111";
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeId, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            otp_view.showSuccess();
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseUserModel userModel = new FirebaseUserModel(FirebaseLoginProvider.mobile.name(), true, user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), null == user.getPhotoUrl() ? "" : user.getPhotoUrl().toString(), user.getUid());
                            new LocalPref(getBaseContext()).saveFirebaseUser(userModel);
//                            startActivity(new Intent(getBaseContext(), OnboardingData.class));
                            new CheckForNewUser().execute();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otp_view.showError();
                                Toast.makeText(getBaseContext(), "OTP is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + getIntent().getStringExtra("phone").trim().replace(" ", ""),        // Phone number to verify
                OTP_TIME_OUT,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        otp_view.setOTP(phoneAuthCredential.getSmsCode());

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        otp_view.showError();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        verificationCodeId = verificationId;
                    }
                });
    }

    private void initializeComponent() {
        editPhone = findViewById(R.id.editPhone);
        otp_view = findViewById(R.id.otp_view);
        resendOTP = findViewById(R.id.resendOTP);
        mobileNumber = findViewById(R.id.mobileNumber);

        mobileNumber.setText("+91 " + getIntent().getStringExtra("phone"));
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTimerForResendOTP();
            }
        });
        runTimerForResendOTP();
    }

    private void runTimerForResendOTP() {
        resendOTP.setText("Resend OTP in " + OTP_TIME_OUT + " sec");
        resendOTP.setClickable(false);
        resendOTP.setTextColor(getResources().getColor(R.color.hintColor));
        Thread thread = new Thread() {
            int timeOut = OTP_TIME_OUT;

            @Override
            public void run() {
                try {
                    while (timeOut >= 0) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (timeOut > 0) {
                                    resendOTP.setText("Resend OTP in " + timeOut + (timeOut > 1 ? " seconds" : " second"));
                                    resendOTP.setClickable(false);
                                    resendOTP.setTextColor(getResources().getColor(R.color.hintColor));
                                } else {
                                    resendOTP.setText("Resend OTP");
                                    resendOTP.setClickable(true);
                                    resendOTP.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                                timeOut--;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    private class CheckForNewUser extends AsyncTask<Void, Void, Void> {
        SweetAlertDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Utils.getProgress(PhoneValidation.this, "Please wait...");
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
                                    Toast.makeText(getBaseContext(), new String(networkResponse.data), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("firebaseId",new LocalPref(getBaseContext()).getFirebaseUser().getFirebaseUUID());
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