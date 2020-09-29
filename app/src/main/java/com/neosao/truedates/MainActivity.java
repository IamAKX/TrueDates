package com.neosao.truedates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.neosao.truedates.configs.LocalPref;
import com.neosao.truedates.configs.Utils;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.screens.HomeContainer;
import com.neosao.truedates.screens.Login;
import com.neosao.truedates.screens.OnboardingData;
import com.neosao.truedates.screens.Settings;
import com.neosao.truedates.screens.UploadProfileImage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        startActivity(new Intent(getBaseContext(), UploadProfileImage.class));
////        printHashKey(getBaseContext());
//        final LocalPref localPref = new LocalPref(getBaseContext());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                SystemClock.sleep(1500);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(localPref.getLoginStatus())
//                        {
//                            UserModel user = localPref.getUser();
//                            if(null == user)
//                                startActivity(new Intent(getBaseContext(), Login.class));
//                            else
//                                if (null==user.getMemberPhotos() || Utils.getPhotoCount(user.getMemberPhotos()) <4)
//                                    startActivity(new Intent(getBaseContext(), UploadProfileImage.class));
//                                else
//                                    startActivity(new Intent(getBaseContext(), HomeContainer.class));
//                        }
//                        else
//                            startActivity(new Intent(getBaseContext(), Login.class));
//                        finish();
//                    }
//                });
//            }
//        }).start();

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("check", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("check", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("check", "printHashKey()", e);
        }
    }
}