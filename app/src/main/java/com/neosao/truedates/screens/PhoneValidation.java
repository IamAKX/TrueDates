package com.neosao.truedates.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.neosao.truedates.R;
import com.neosao.truedates.configs.FirebaseLoginProvider;
import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.model.FirebaseUserModel;

import java.util.concurrent.TimeUnit;

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
                            Log.e("check", "signInWithCredential:success");
                            otp_view.showSuccess();
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseUserModel userModel = new FirebaseUserModel(FirebaseLoginProvider.MOBILE_NUMBER.name(), true, user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), null == user.getPhotoUrl() ? "" : user.getPhotoUrl().toString(), user.getUid());
                            Log.e("check", userModel.toString());
                            new LocalPref(getBaseContext()).saveFirebaseUser(userModel);
                            startActivity(new Intent(getBaseContext(), OnboardingData.class));

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e("check", "signInWithCredential:failure", task.getException());
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
                        Log.e("check", "onVerificationCompleted:" + phoneAuthCredential.getSmsCode());
                        otp_view.setOTP(phoneAuthCredential.getSmsCode());

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e("check", "onVerificationFailed:" + e.getLocalizedMessage());
                        Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        otp_view.showError();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Log.e("check", "onCodeSent:" + verificationId);
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
}