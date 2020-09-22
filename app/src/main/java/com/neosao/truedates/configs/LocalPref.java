package com.neosao.truedates.configs;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.neosao.truedates.model.FirebaseUserModel;

public class LocalPref {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public LocalPref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Local_Preference", Activity.MODE_PRIVATE);
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
}
