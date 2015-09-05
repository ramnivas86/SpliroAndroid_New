package com.arya.lib.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.arya.lib.R;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public class Util {
    public static final String VAL_OK ="OK" ;
    private static ProgressDialog progressDialog = null;
    public static String FLD_STATUS="status";

    public static void showProDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    public static void dimissProDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void showCenteredToast(Context ctx, String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
