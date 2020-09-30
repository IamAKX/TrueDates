package com.neosao.truedates.configs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.neosao.truedates.model.FirebaseUserModel;
import com.neosao.truedates.model.UserModel;

public class LocalPref {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public LocalPref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Local_Preference", Activity.MODE_PRIVATE);
    }


    public boolean logOutUser()
    {
        FirebaseAuth.getInstance().signOut();
        editor.clear();
        return editor.commit();
    }

    public boolean saveFirebaseUser(FirebaseUserModel user)
    {
        editor = preferences.edit();
        editor.putString(LocalPrefKey.FIREBASE_USER,new Gson().toJson(user));
        return editor.commit();
    }

    public FirebaseUserModel getFirebaseUser()
    {
        FirebaseUserModel user = new Gson().fromJson(preferences.getString(LocalPrefKey.FIREBASE_USER,null),FirebaseUserModel.class);
        return user;
    }

    public boolean saveUser(UserModel user)
    {
        editor = preferences.edit();
        editor.putString(LocalPrefKey.CURRENT_USER,new Gson().toJson(user));
        return editor.commit();
    }

    public UserModel getUser()
    {
        UserModel user = new Gson().fromJson(preferences.getString(LocalPrefKey.CURRENT_USER,null),UserModel.class);
        return user;
    }

    public boolean setLoginStatus(boolean status)
    {
        editor = preferences.edit();
        editor.putBoolean(LocalPrefKey.LOGIN_STATUS,status);
        return editor.commit();
    }

    public boolean getLoginStatus()
    {
        return preferences.getBoolean(LocalPrefKey.LOGIN_STATUS, false);
    }
}
