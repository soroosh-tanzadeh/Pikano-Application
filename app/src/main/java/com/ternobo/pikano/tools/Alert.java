package com.ternobo.pikano.tools;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Soroosh on 05/04/2019.
 */

public class Alert {

    public static AlertDialog make(String title, String text, Context context){
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle(title);
        d.setMessage(text);
        d.setPositiveButton("تایید", null);
        return d.create();
    }

}