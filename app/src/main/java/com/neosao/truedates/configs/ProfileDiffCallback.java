package com.neosao.truedates.configs;

import androidx.recyclerview.widget.DiffUtil;

import com.neosao.truedates.model.Profile;

import java.util.ArrayList;

public class ProfileDiffCallback extends DiffUtil.Callback {
    ArrayList<Profile> oldProfile;
    ArrayList<Profile> newProfile;

    public ProfileDiffCallback(ArrayList<Profile> oldProfile, ArrayList<Profile> newProfile) {
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
        return oldProfile.get(oldItemPosition).getId() == newProfile.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProfile.get(oldItemPosition).equals(newProfile.get(newItemPosition));
    }
}
