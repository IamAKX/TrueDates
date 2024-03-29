package com.neosao.truedates.configs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.neosao.truedates.R;
import com.neosao.truedates.model.MemberInterests;
import com.neosao.truedates.model.MemberPhotos;
import com.neosao.truedates.model.UserModel;
import com.neosao.truedates.model.options.FieldOfStudy;
import com.neosao.truedates.model.options.Interest;
import com.neosao.truedates.model.options.WorkIndustry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Utils {

    public static String CURRENCY_SYMBOL = "\u20B9 ";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.horizontal_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int getDiffYears(Calendar dob, Calendar today) {

        LocalDate birthDate = LocalDateTime.ofInstant(dob.toInstant(), dob.getTimeZone().toZoneId()).toLocalDate();
        LocalDate currentDate = LocalDateTime.ofInstant(today.toInstant(), today.getTimeZone().toZoneId()).toLocalDate();
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getDiffYears(String dobStr, Calendar today) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar dob = Calendar.getInstance();
        dob.setTime(format.parse(dobStr));// all done

        LocalDate birthDate = LocalDateTime.ofInstant(dob.toInstant(), dob.getTimeZone().toZoneId()).toLocalDate();
        LocalDate currentDate = LocalDateTime.ofInstant(today.toInstant(), today.getTimeZone().toZoneId()).toLocalDate();
        if ((birthDate != null) && (currentDate != null)) {
            return String.valueOf(Period.between(birthDate, currentDate).getYears());
        } else {
            return "0";
        }
    }

    public static SweetAlertDialog getProgress(Context context, String title) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.themePink);
        pDialog.getProgressHelper().setRimColor(R.color.themePink);
        pDialog.setContentText(title);
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    public Interest getInterestCode(String intrests) {
        for (Interest i : DynamicOptionConstants.INTEREST_ARRAY_LIST)
            if (i.getInterestName().equalsIgnoreCase(intrests))
                return i;
        return null;
    }

    public WorkIndustry getWorkIndustryCode(String workIndustry) {
        for (WorkIndustry wim : DynamicOptionConstants.WORK_INDUSTRY_ARRAY_LIST) {
            if (wim.getIndustryName().equalsIgnoreCase(workIndustry))
                return wim;
        }
        return null;
    }

    public FieldOfStudy getFieldStudyCode(String fieldOfStudy) {
        for (FieldOfStudy fos : DynamicOptionConstants.FIELD_OF_STUDY_ARRAY_LIST) {
            if (fos.getFieldName().equalsIgnoreCase(fieldOfStudy))
                return fos;
        }
        return null;
    }

    public static int getPhotoCount(MemberPhotos[] arr) {
        int counter = 0;
        if (null == arr)
            return counter;
        for (int i = 0; i < arr.length; i++)
            if (arr[i] != null)
                counter++;
        return counter;
    }

    public static int getIndexOfImageView(ImageView view) {
        switch (view.getId()) {
            case R.id.profileImage1:
                return 0;
            case R.id.profileImage2:
                return 1;
            case R.id.profileImage3:
                return 2;
            case R.id.profileImage4:
                return 3;
            case R.id.profileImage5:
                return 4;
            case R.id.profileImage6:
                return 5;
            case R.id.profileImage7:
                return 6;
            case R.id.profileImage8:
                return 7;
            case R.id.profileImage9:
                return 8;
        }
        return 0;
    }

    public static String getInterestCodeList(UserModel userModel) {
        String id = "";
        ArrayList<String> idList = new ArrayList<>();
        if (null == userModel.getMemberInterests() || userModel.getMemberInterests().isEmpty())
            return id;
        for (MemberInterests interest : userModel.getMemberInterests())
            idList.add(interest.getInterestCode());
        id = TextUtils.join(",", idList);
        return id;
    }

    public static String getInterestName(UserModel userModel) {
        String id = "";
        ArrayList<String> idList = new ArrayList<>();
        if (null == userModel.getMemberInterests() || userModel.getMemberInterests().isEmpty())
            return id;
        for (MemberInterests interest : userModel.getMemberInterests())
            idList.add(interest.getInterestName());
        id = TextUtils.join(",", idList);
        return id;
    }

    public static String[] getInterestNameArray(UserModel userModel) {

        ArrayList<String> idList = new ArrayList<>();
        if (null == userModel.getMemberInterests() || userModel.getMemberInterests().isEmpty())
            return new String[]{" "};
        for (MemberInterests interest : userModel.getMemberInterests()) {
            if (null != interest.getInterestName())
                idList.add(interest.getInterestName());
        }

        return idList.toArray(new String[0]);
    }

    public static String buildQueryFromMap(Map<String, String> params) {
        String query = "";
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            list.add(entry.getKey()+"="+entry.getValue());
        }
        query = "?"+TextUtils.join("&",list);
        return query;
    }

    public String generateChatRoomId(String userId1, String userId2){
        String chatRoomId = "";
        ArrayList<Character> charList = new ArrayList<Character>();
        for(char c : userId1.toCharArray())
            charList.add(c);
        for(char c : userId2.toCharArray())
            charList.add(c);
        Collections.sort(charList);
        for(char c : charList)
            chatRoomId += c;
        return chatRoomId;
    }

}
