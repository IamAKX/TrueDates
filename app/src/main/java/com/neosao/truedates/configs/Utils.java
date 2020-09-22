package com.neosao.truedates.configs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.neosao.truedates.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class Utils {

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

    public static SweetAlertDialog getProgress(Context context, String title){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.themePink);
        pDialog.setContentText(title);
        pDialog.setCancelable(false);
        return  pDialog;
    }
}
