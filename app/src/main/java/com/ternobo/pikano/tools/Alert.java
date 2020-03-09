package com.ternobo.pikano.tools;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

/**
 * Created by Soroosh on 05/04/2019.
 */

public class Alert {

    public static AlertDialog make(String title, String text, Context context){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(title);
        d.setMessage(text);
        d.setPositiveButton("تایید", null);
        AlertDialog alertDialog = d.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        return alertDialog;
    }

    public static AlertDialog make(String title, String text, Context context, Runnable runnable) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(title);
        d.setMessage(text);
        d.setPositiveButton("تایید", (dialog, which) -> runnable.run());
        AlertDialog alertDialog = d.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            alertDialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        return alertDialog;
    }



}