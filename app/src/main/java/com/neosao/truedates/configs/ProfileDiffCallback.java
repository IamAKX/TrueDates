package com.neosao.truedates.configs;

import androidx.recyclerview.widget.DiffUtil;

import com.neosao.truedates.model.UserModel;

import java.util.ArrayList;

public class ProfileDiffCallback extends DiffUtil.Callback {
    ArrayList<UserModel> oldProfile;
    ArrayList<UserModel> newProfile;

    public ProfileDiffCallback(ArrayList<UserModel> oldProfile, ArrayList<UserModel> newProfile) {
        this.oldProfile = oldProfile;
        this.newProfile = newProfile;
    }

    @Override
    public int getOldListSize() {
        return oldProfile.size();
    }

    @Override
    public int getNewListSize() {
        return newProfile.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProfile.get(oldItemPosition).getUserId() == newProfile.get(newItemPosition).getUserId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProfile.get(oldItemPosition).equals(newProfile.get(newItemPosition));
    }
}
