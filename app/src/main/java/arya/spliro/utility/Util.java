package arya.spliro.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.location.Location;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arya.lib.init.Env;
import com.arya.lib.logger.Logger;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.custom.CustomTypefaceSpan;
import arya.spliro.dao.CreateData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.database.TableType;
import arya.spliro.viewController.HomeActivity;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Ramnivas Singh on 7/23/2015.
 */
public class Util {
    private static final String TAG = "spliro";
    private static ProgressDialog progressDialog;
    private static Dialog confirmation;

    public static void startActAnimation(Activity ctx) {
        ctx.overridePendingTransition(R.anim.right_in, R.anim.left_out);

    }

    public static void endActAnimation(Activity act) {
        act.overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /**
     * Check network is avalable or not
     * Created by Ramnivas Singh on 04-08-2015
     *
     * @return
     */
    public static boolean isDeviceOnline(boolean showErroDialg) {
        boolean isDeviceOnLine = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Env.appContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isDeviceOnLine = netInfo.isConnected();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        if (!isDeviceOnLine && showErroDialg) {

            showOkDialog(null, Env.appContext.getString(R.string.noInternetMsg));
        }
        return isDeviceOnLine;
    }

    /**
     * Show common progress dialog
     * Created by Ramnivas Singh on 04-08-2015
     *
     * @param msg
     * @return
     */
    public static Dialog showProDialog(String msg) {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(Env.currentActivity);
        progressDialog.setCancelable(false);
        if (msg == null) {
            msg = Env.currentActivity.getString(R.string.wait);
        }
        SpannableStringBuilder s = new SpannableStringBuilder(msg);
        s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontRegular()), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        progressDialog.setMessage(s);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
        return progressDialog;
    }

    public static Dialog showOkDialog(View.OnClickListener yesClick, String message) {
        if (Env.currentActivity != null) {
            if (confirmation != null && confirmation.isShowing()) {
                confirmation.hide();
            }
            confirmation = new Dialog(Env.currentActivity,
                    android.R.style.Theme_Translucent_NoTitleBar);
            confirmation.setContentView(R.layout.spl_show_ok_dialog);
            TextView txtDescription_auth, txtOk_auth;
            txtOk_auth = (TextView) confirmation.findViewById(R.id.txtOk_auth);
            txtDescription_auth = (TextView) confirmation.findViewById(R.id.txtDescription_auth);
            txtDescription_auth.setTypeface(SpliroApp.getFontLight());
            txtOk_auth.setTypeface(SpliroApp.getFontLight());
            txtDescription_auth.setText(message);
            if (yesClick != null) {
                txtOk_auth.setOnClickListener(yesClick);
            } else {
                txtOk_auth.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        confirmation.hide();
                        confirmation = null;
                    }
                });
                confirmation.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        confirmation = null;
                    }
                });

                confirmation.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        confirmation = null;
                    }
                });
            }
            confirmation.show();
            return confirmation;
        } else {
            return null;
        }
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String text = dest.toString();
            String secondChar = "";
            boolean isPhoneNumber = false;
            if (text.trim().length() > 1) {
                secondChar = text.substring(1, 2);
                if (text.startsWith("(") && secondChar.matches("\\d")) {
                    isPhoneNumber = true;
                }
            } else if (text.trim().length() == 0 && source.length() == 1) {
                secondChar = "" + source.charAt(0);
                if (secondChar.matches("\\d")) {
                    isPhoneNumber = true;
                }
            }


            if (source.length() > 0 && Character.isDigit(source.charAt(0)) && secondChar.matches("\\d") && isPhoneNumber) {
                if (!Character.isDigit(source.charAt(0)))
                    return "";
                else {
                    if (dend == 3) {
                        return source + ") ";
                    } else if (dend == 0) {
                        return "(" + source;
                    } else if ((dend == 5) || (dend == 9))
                        return " " + source;
                    else if (dend >= 14)
                        return "";
                }

            } else {
                if (text.trim().length() > 1 && text.substring(1, 2).matches("\\d") && isPhoneNumber) {
                    return "";
                }
            }

            return null;

        }
    };

    public static Dialog dimissProDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }

    public static boolean checkStringsContainsOnlySpecialChar(String inputString) {
        boolean found = false;
        String splChrs = "/^[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$/";
        found = inputString.matches("[" + splChrs + "]+");
        return found;
    }

    public static String getDeviceMacAddres(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        if (macAddress == null) {
            manager.setWifiEnabled(true);
            final WifiInfo wifiInfo = manager.getConnectionInfo();
            macAddress = wifiInfo.getMacAddress();
        }
        return macAddress;
    }

    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Integer getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * get formatted phone number as (---) --- ----.
     *
     * @param text   : phoneNumber
     * @param before : start from
     * @param start: end from
     */
    public static String setPhoneNumberFormat(String text, int before, int start) {
        StringBuffer s = null;
        s = new StringBuffer(text);
        if (s.length() != 0) {
            if (s.length() == 1 || (s.length() > 1 && s.length() <= 3)) {
                s.insert(0, '(');
            } else if (s.length() == 4 || (s.length() > 4 && s.length() <= 6)) {
                s.insert(0, '(');
                s.insert(4, ") ");
            } else if (s.length() == 6 || (s.length() > 6 && s.length() <= 10)) {
                s.insert(0, '(');
                s.insert(4, ") ");
                s.insert(9, ' ');
            }
        }
        return s.toString();
    }

    public static void openKeyBoard(View textView) {
        InputMethodManager imm = (InputMethodManager) Env.currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static boolean passwordPatternValidation(String pass) {
        boolean b = true;
        String PASSWORD_PATTERN = "^.*[a-zA-Z].*$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);
        if (!matcher.matches() || !pass.matches(".*\\d.*")) {
            b = false;
        }
        return b;
    }

    public static void showOkErrorDialog(String string, EditText eTxt, View btn) {
        btn.setEnabled(true);
        Util.showOkDialog(null, string);
        eTxt.requestFocus();
    }

    public static void gotoHomeScreen(Context con) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        con.startActivity(intent);
    }


    public static String getRealPathFromURI(Context ctx, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static boolean checKBackCameraSupport() {
        boolean b = false;
        int backCameraId = -1;
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = i;
            }
        }
        b = (backCameraId > -1);
        Log.d(TAG, "back camera exists ? " + b);
        Log.d(TAG, "back camera id  :" + backCameraId);
        return b;
    }

    public static boolean isSamsung() {
        String deviceManufacturer = Build.MANUFACTURER;

        boolean b = false;
        if (Build.MANUFACTURER.equalsIgnoreCase(Constants.SAMSUNGDEVICENAME)) {
            b = true;
        }
        return b;
    }

    public static void deleteFolderFromSDCard(File filePath, boolean flag) {
        // TODO Auto-generated method stub
        File[] files = filePath.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
        if (flag) {
            filePath.delete();
        }
    }

    public static JSONObject getJsonFromUrl(String url) {
        InputStream is = null;
        String result = "";
        JSONObject jObject = null;
        if (isDeviceOnline(true)) {

            try {

                URL requestUrl = new URL(url);
                URLConnection con = requestUrl.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb = new StringBuilder();
                int cp;

                while ((cp = in.read()) != -1) {
                    sb.append((char) cp);
                }
                String json = sb.toString();
                if (json.isEmpty()) {
                    jObject = new JSONObject(json);
                }
//            try {
//
//
//                HttpClient httpClient = HttpClientBuilder.create().build();
//                HttpPost httppost = new HttpPost(url);
//                HttpResponse response = httpclient.execute(httppost);
//                HttpEntity entity = response.getEntity();
//                is = entity.getContent();
//                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
//                StringBuilder stringBuilder=new StringBuilder();
//                String line;
//                while((line=bufferedReader.readLine())!=null)
//                {
//                    stringBuilder.append(line+"\n");
//                }
//                is.close();
//                result=stringBuilder.toString();
//                jObject=new JSONObject(result);
//
//            } catch (Exception e) {
//                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jObject;
    }

    @SuppressWarnings("deprecation")
    public static String compressImage(Context ctx, String imageUri) {
        Bitmap bmp = null;
        File projectNameFolder1 = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.AppFolderName);
//        File createFolder = new File(projectNameFolder1.getPath() +File.separator+ Constants.createFolder);

//        String toFileName = null;
        String filePath = getRealPathFromURI(ctx, imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        // max Height and width values of the compressed image is taken as
        // 816x612
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        // width and height values are set maintaining the aspect ratio of the
        // image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSizeForImageCompress(options, actualWidth, actualHeight);
        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String imagePath = saveFileToFolder(ctx, scaledBitmap, 85);
        scaledBitmap.recycle();
        bmp.recycle();
        scaledBitmap = null;
        bmp = null;
        return imagePath;

    }

    private static String getRealPathFromURI(Context ctx, String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = ctx.getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String uriname = cursor.getString(index);
            cursor.close();
            return uriname;
        }
    }

    public static int calculateInSampleSizeForImageCompress(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static String saveFileToFolder(Context ctx, Bitmap mBitmap, int compression) {
        FileOutputStream fos = null;
        String imageName = Constants.profileImge + System.currentTimeMillis() + Constants.imageFormat;
        File createdImagePath = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + Constants.AppFolderName + File.separator + Constants.createFolder + File.separator + imageName);
        try {
            fos = new FileOutputStream(createdImagePath);
            if (compression != 0) {
                mBitmap.compress(Bitmap.CompressFormat.JPEG, compression, fos);
            }
            fos.flush();
            fos.close();
            String savedImagePath = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), mBitmap, createdImagePath.getPath(), imageName);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return createdImagePath.getPath();
    }

    public static FragmentTransaction addFragments(FragmentManager fm, Fragment frg) {
        FragmentTransaction ft1 = fm.beginTransaction();
//        ft1.setCustomAnimations(R.anim.right_in, R.anim.left_out,R.anim.left_in,R.anim.right_out);
//        ft1.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        ft1.replace(R.id.createFrameContainer, frg);
        ft1.addToBackStack(null);
        ft1.commit();
        return ft1;
    }

    public static FragmentTransaction addFragment(FragmentManager fm, Fragment frg, int resource, boolean addToBackStack) {
        FragmentTransaction ft1 = fm.beginTransaction();
        ft1.replace(resource, frg);
        if (addToBackStack)
            ft1.addToBackStack(null);

        ft1.commit();
        return ft1;
    }

    public static void openContactList(Activity act) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        act.startActivityForResult(pickContactIntent, Constants.PICK_CONTACTS);
        Util.startActAnimation(act);
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || TextUtils.isEmpty(target)
                || target.toString().trim().length() < 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static String getOnlyPhoneNumber(String displayPNumber) {
        StringBuffer sb = new StringBuffer(displayPNumber);
        String s = sb.reverse().substring(0, 10);
        return new StringBuffer(s).reverse().toString();
    }

    public static String[] getSelectedContactFromNative(Context ctx, Uri contactDataUri) {
        String[] ContactArray = new String[2];
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        Cursor cursor = ctx.getContentResolver().query(contactDataUri, projection, null, null, null);
        cursor.moveToFirst();
        ContactArray[0] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        ContactArray[1] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        return ContactArray;
    }

    public static Dialog showYesNoMessageDialog(Context ctx, String msg, String title, View.OnClickListener yesClick, View.OnClickListener noClick,
                                                String rightString, String leftString) {
        TextView txt_container_dialog, titleTxtMessage, txtRightClick, txtLeftClick;
        final Dialog mDialog = new Dialog(ctx);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.common_yes_no_dialog);
        txt_container_dialog = (TextView) mDialog
                .findViewById(R.id.txt_container_dialog);
        titleTxtMessage = (TextView) mDialog.findViewById(R.id.titleTxtMessage);
        txtRightClick = (TextView) mDialog.findViewById(R.id.txtRightClick);
        txtLeftClick = (TextView) mDialog.findViewById(R.id.txtLeftClick);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (rightString == null) {
            rightString = ctx.getString(R.string.yes);
            leftString = ctx.getString(R.string.no);
        }
        if (title.trim().length() == 0) {
            titleTxtMessage.setVisibility(View.GONE);
        }
        txtRightClick.setText(rightString);
        txtLeftClick.setText(leftString);
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(lp);
        txt_container_dialog.setText(msg);
        titleTxtMessage.setText(title);
        txtRightClick.setOnClickListener(yesClick);
        if (noClick == null) {
            txtLeftClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                }
            });
        } else {
            txtLeftClick.setOnClickListener(noClick);
        }
        txt_container_dialog.setTypeface(SpliroApp.getFontLight());
        titleTxtMessage.setTypeface(SpliroApp.getFontSemiBold());
        txtRightClick.setTypeface(SpliroApp.getFontSemiBold());
        txtLeftClick.setTypeface(SpliroApp.getFontSemiBold());

        mDialog.show();
        return mDialog;
    }

    public static double toDouble(String str) {
        double d = 0;
        try {
            d = Double.parseDouble(str);
        } catch (Exception e) {

        }
        return d;
    }

    public static void hideKeyBoardMethod(Context con, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatTwoDecimalPlaces(double value) {
        String s = "";
        try {
            s = String.format("%.2f", value);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    public static SpannableStringBuilder setDistanseSpan(Context context, String str, int startIndex, int endIndex) {
        SpannableStringBuilder s = new SpannableStringBuilder(str);
        s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontSemiBold()), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontLight()), endIndex, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan((int) context.getResources().getDimension(R.dimen.add_fav_txt_size_my_share)), endIndex + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    public static String calculatePerSharePrice(double total_price, double no_of_shares) {
        String s = String.format("%.2f", total_price / no_of_shares);
        return s;
    }

    public static void showImageInDefaultImageViewer(Context ctx, String path)//22/8/2015 phoosaram
    {
        if (path != null && !path.isEmpty()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            ctx.startActivity(intent);
        }
    }

    public static boolean validationPassword(Context context, EditText editPassword, View singUpBtn) {
        boolean b = false;
        String pass = editPassword.getText().toString().trim();
        if (pass.trim().length() < Constants.MINIMUMLENGTHOFPASS) {
            Util.showOkErrorDialog(context.getString(R.string.password_must_error_msg), editPassword, singUpBtn);

        } else if (!Util.passwordPatternValidation(pass)) {
            Util.showOkErrorDialog(context.getString(R.string.msg_password_must_contain_numeric_special_character), editPassword, singUpBtn);
        } else {
            b = true;
        }
        return b;
    }


    public static boolean validationFirstName(Context context, EditText editFirstName, View singUpBtn) {
        boolean b = false;
        String fName = editFirstName.getText().toString().trim();
        if (fName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
            Util.showOkErrorDialog(context.getString(R.string.first_name_error_msg), editFirstName, singUpBtn);

        } else if (Util.checkStringsContainsOnlySpecialChar(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.first_name_only_special_msg), editFirstName, singUpBtn);
        } else if (Util.isNumeric(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.first_name_only_numeric_msg), editFirstName, singUpBtn);
        } else if (fName.trim().length() > Constants.MAXLENGTHOFFIRSTNAME) {
            Util.showOkErrorDialog(context.getString(R.string.first_name_error_msg_max), editFirstName, singUpBtn);
        } else {
            b = true;
        }
        return b;
    }

    public static boolean validationDisplayName(Context context, EditText editFirstName, View logoutBtn) {
        boolean b = false;
        String fName = editFirstName.getText().toString().trim();
        if (fName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
            Util.showOkErrorDialog(context.getString(R.string.display_name_error_msg), editFirstName, logoutBtn);
        } else if (Util.checkStringsContainsOnlySpecialChar(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.display_name_only_special_msg), editFirstName, logoutBtn);
        } else if (Util.isNumeric(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.display_name_only_numeric_msg), editFirstName, logoutBtn);
        } else if (fName.trim().length() > 26) {
            Util.showOkErrorDialog(context.getString(R.string.display_name_error_msg_max), editFirstName, logoutBtn);
        } else {
            b = true;
        }
        return b;
    }

    public static boolean validationLastName(Context context, EditText editLastName, View singUpBtn) {
        boolean flag = false;
        String lName = editLastName.getText().toString().trim();
        if (lName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
            Util.showOkErrorDialog(context.getString(R.string.last_name_error_msg), editLastName, singUpBtn);
        } else if (Util.checkStringsContainsOnlySpecialChar(lName)) {
            Util.showOkErrorDialog(context.getString(R.string.last_name_only_special_msg), editLastName, singUpBtn);
        } else if (Util.isNumeric(lName)) {
            Util.showOkErrorDialog(context.getString(R.string.last_name_only_numeric_msg), editLastName, singUpBtn);
        } else if (lName.trim().length() > Constants.MAXLENGTHOFLASTNAME) {
            Util.showOkErrorDialog(context.getString(R.string.last_name_error_msg_max), editLastName, singUpBtn);
        } else {
            flag = true;
        }
        return flag;
    }

    public static boolean validationPhoneEmail(Context context, EditText editPhoneNo, View singUpBtn) {
        boolean flag = false;
        String phone = editPhoneNo.getText().toString().trim();
        String eMailId = "";
        String phoneNumber = phone;
        if (phone.trim().length() >= 4) {
            phoneNumber = phone.substring(1, 4);
        }
        if (Util.isNumeric(phoneNumber)) {
            eMailId = "";
            phone = phone.replaceAll("\\D", "");
        } else {
            eMailId = phone;
        }


        if (phone.trim().length() == 0) {
            Util.showOkErrorDialog(context.getString(R.string.enter_valid_email_phone), editPhoneNo, singUpBtn);
        } else if (phone.trim().length() != 0) {
            if (Util.isNumeric(phone) && phone.trim().length() != Constants.MAXLENGTHOFPHONENUMBER) {
                Util.showOkErrorDialog(context.getString(R.string.phone_num_error_msg), editPhoneNo, singUpBtn);
            } else if (eMailId.trim().length() != 0 && (!Util.isEmailValid(phone))) {
                Util.showOkErrorDialog(context.getString(R.string.email_error_msg), editPhoneNo, singUpBtn);
            } else {
                flag = true;
            }
        }

        return flag;
    }

    public static boolean validationAboutYourSelf(Context context, EditText editText, View singUpBtn) {
        boolean b = false;
        String fName = editText.getText().toString().trim();
        if (fName.trim().length() < 80) {
            Util.showOkErrorDialog(context.getString(R.string.about_self_error_msg), editText, singUpBtn);

        } else if (Util.checkStringsContainsOnlySpecialChar(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.about_self_only_special_msg), editText, singUpBtn);
        } else if (Util.isNumeric(fName)) {
            Util.showOkErrorDialog(context.getString(R.string.about_self_only_numeric_msg), editText, singUpBtn);
        } else if (fName.trim().length() > 1000) {
            Util.showOkErrorDialog(context.getString(R.string.about_self_error_msg_max), editText, singUpBtn);
        } else {
            b = true;
        }
        return b;
    }

    public static SpannableStringBuilder setShareEndDate(String shareEndDate) {

        SpannableStringBuilder s = null;
        try {
            Date date = new SimpleDateFormat(Constants.ShareEndDateFormat_create_send).parse(shareEndDate);
            String newFormat = new SimpleDateFormat(Constants.ShareEndDateFormat_create_show).format(date.getTime() + TimeZone.getDefault().getRawOffset());
            s = new SpannableStringBuilder(newFormat);
            String[] shareEndDateArray = newFormat.split(",");
            String first = shareEndDateArray[0];
            s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontSemiBold()), 0, first.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontLight()), first.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;

    }

    public static void logOutMethod(Context ctx, boolean setAnimation) {

        Config.removeOrClearPerferance(null);
//        dbhelHelper.clearDataBase();
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Intent intent = new Intent(ctx.getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();

        if (setAnimation) {
            endActAnimation(((Activity) ctx));
        }

    }

    /**
     * download file from parse server to sdCard
     *
     * @param imgUrl: file url for download file from parse server
     */
    public static String downloadFileUsingUrl(String imgUrl) {
        String[] imageUrl = imgUrl.split("/");
        String filename = imageUrl[imageUrl.length - 1];
        File SDCardRoot = checkAppFoledIsExist();
        if (!SDCardRoot.exists()) {
            SDCardRoot.mkdirs();
        }
        File file = new File(SDCardRoot, filename);
        String filepath = null;

        if (!file.exists()) {
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(imgUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                inputStream = conn.getInputStream();
                int sizeInBytes = conn.getContentLength();
                filepath = SDCardRoot + "/" + filename;
                File pdfFile = new File(filepath);
                float lengthOfFileInMB = ((float) Math.round((sizeInBytes / (1024 * 1024)) * 10) / 10);
                if (lengthOfFileInMB < 9) {
                    writeFileUsingStream(inputStream, filename, SDCardRoot);
                    if (pdfFile.exists() && pdfFile.length() < sizeInBytes) {
                        filepath = null;
                        pdfFile.delete();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                filepath = "";
                e.printStackTrace();
            }

            Log.d("filepath:", " " + filepath);
        } else {
            filepath = SDCardRoot + "/" + filename;
        }
        return filepath;

    }


    public static void writeFileUsingStream(InputStream inputStream, String filename, File SDCardRoot) throws IOException {
        // TODO Auto-generated method stub
        FileOutputStream fileOutput = null;
        File file = new File(SDCardRoot, filename);
        byte data[] = new byte[1024];
        fileOutput = new FileOutputStream(file);

        int count;
        while ((count = inputStream.read(data, 0, 1024)) != -1) {
            fileOutput.write(data, 0, count);
        }

        fileOutput.flush();
        fileOutput.close();
        inputStream.close();
    }

    public static void writeDBfileOnSdcard(Context context) {
        File f = new File(context.getDatabasePath(Constants.DB_NAME).getAbsolutePath());
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(f);
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "spliro.sqlite");
            while (true) {
                int i = fis.read();
                if (i != -1) {
                    fos.write(i);
                } else {
                    break;
                }
            }
            fos.flush();
            Log.v("DB file Exported", "Success");
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("DB file Exported", "Failed");
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException ioe) {
            }
        }
    }

    public static String getGUID(String postGUID) {
        if (postGUID == null || postGUID.isEmpty()) {
            postGUID = Config.deviceInfo.getDeviceId() + "-" + System.currentTimeMillis();
        }
        return postGUID;
    }

    /**
     * set first latter capital in given string.
     *
     * @param str :pass any string of characters
     */
    public static String setFirstLatterCap(String str) {
        String nameValue = "";
        if (str.trim().length() != 0) {
            String[] nameString = str.split("");
            String name = nameString[1];
            name = name.toUpperCase();
            SpannableStringBuilder builder = new SpannableStringBuilder();

            builder.append(str);
            builder.replace(0, 1, name);

            nameValue = builder.toString();
        }
        return nameValue;

    }

    public static String uploadImageToServer(String imagePath, String imageName) throws JSONException {
        NetworkResponse response = NetworkMgr.uploadImageToServer(Constants.UPLOAD_URL, imagePath, imageName);
        try {
            if (response != null && response.isSuccess())
                imageName = response.getJsonObject().getJSONObject(Constants.FLD_KEY_DATA).getString(Constants.FLD_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
        return imageName;
    }

    public static String getLocationDistance(double lat1, double long1, double lat2, double long2) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(lat1);
        selected_location.setLongitude(long1);
        Location near_locations = new Location("locationB");
        near_locations.setLatitude(lat2);
        near_locations.setLongitude(long2);
        double distance = (selected_location.distanceTo(near_locations)) * 0.000621371;
        return formatTwoDecimalPlaces(distance);
    }

    public static SpannableStringBuilder getSpannedString(String str, int textSize) {
        SpannableStringBuilder s = new SpannableStringBuilder(str);
        s.setSpan(new CustomTypefaceSpan("", SpliroApp.getFontSemiBold()), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(textSize), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;

    }

    public static SpannedString getTimeLeft(String endDate, String currentDate) {
        SpannedString spannedString = new SpannedString("0" + Constants.TIME_LEFT_TEXT);
        try {
            Date oldDate = (Date) new SimpleDateFormat(Constants.ShareEndDateFormat_create_send).parse(currentDate);
            Date newDate = (Date) new SimpleDateFormat(Constants.ShareEndDateFormat_create_send).parse(endDate);
            if (oldDate.before(newDate)) {
                long diff = newDate.getTime() - oldDate.getTime();
                long minutes = diff / 60000;
                long hoursleft = minutes / 60;
                long minsLeft = minutes % 60;
                Spannable hrs = (Spannable) getSpannedString("" + hoursleft, (int) Env.appContext.getResources().getDimension(R.dimen.spannable_txt_size));
                Spannable mins = (Spannable) getSpannedString("" + minsLeft, (int) Env.appContext.getResources().getDimension(R.dimen.spannable_txt_size));
                if (hoursleft > 0) {
                    spannedString = (SpannedString) TextUtils.concat(hrs, "hr", mins, Constants.TIME_LEFT_TEXT);
                } else {
                    spannedString = (SpannedString) TextUtils.concat(mins, Constants.TIME_LEFT_TEXT);
                }
            } else {
                spannedString = (SpannedString) TextUtils.concat((Spannable) getSpannedString("0", (int) Env.appContext.getResources().getDimension(R.dimen.spannable_txt_size)), Constants.TIME_LEFT_TEXT);
                ;
            }

        } catch (Exception e) {

            e.printStackTrace();
            Logger.error("Util", "getTimeLeft()+[" + e + "]");
        }


        return spannedString;
    }

    public static boolean renameFile(String old_file_name, String old_file_path, String newfileName) {
        File fromFile = new File(old_file_path);
        File toFile = new File(old_file_path.replace(old_file_name, newfileName));
        return fromFile.renameTo(toFile);

    }

    public static void showCenteredToast(String msg) {

        Toast toast = Toast.makeText(Env.currentActivity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String[] cVForSelectedContactFromNative(String[] contactInformation) {
        if (contactInformation[1].trim().length() < 10) {
            Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.phone_num_error_msg));
            contactInformation = null;
        } else {
            String DisplayPNumber = Util.getOnlyPhoneNumber(contactInformation[1].replaceAll("\\D", ""));
            String display_name = contactInformation[0];
            if (display_name.isEmpty()) {
                Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.contact_name_error));
                contactInformation = null;
            } else if (Util.checkStringsContainsOnlySpecialChar(display_name)) {
                Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.first_name_only_special_msg));
                contactInformation = null;
            } else if (Util.isNumeric(display_name)) {
                Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.first_name_only_numeric_msg));
                contactInformation = null;
            } else if (display_name.trim().length() > Constants.MAXLENGTHOFFIRSTNAME) {
                Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.first_name_error_msg_max));
                contactInformation = null;
            } else if (!Util.isValidPhoneNumber(contactInformation[1])) {
                Util.showOkDialog(null, Env.currentActivity.getResources().getString(R.string.phone_num_error_msg));
                contactInformation = null;
            } else {
                contactInformation[0] = display_name;
                contactInformation[1] = DisplayPNumber;
            }
        }
        return contactInformation;
    }

    public static File checkAppFoledIsExist() {
        File SDCardRoot = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/" + Constants.AppFolderName + File.separator + Constants.createFolder);
        if (!SDCardRoot.exists()) {
            SDCardRoot.mkdirs();
        }
        return SDCardRoot;
    }

    public static String getChangeDateFormat(String sendedDate, String oldPattern, String applyPattern, boolean retunrnWithTime) {
        String showingPate = "yyyy" + applyPattern + "MM" + applyPattern + "dd";
        String oldPat = "yyyy" + oldPattern + "MM" + oldPattern + "dd";
        if (sendedDate.contains(":")) {
            oldPat = "yyyy" + oldPattern + "MM" + oldPattern + "dd"
                    + " hh:mm:ss";
        }
        if (retunrnWithTime) {
            showingPate = "yyyy" + applyPattern + "MM" + applyPattern + "dd"
                    + " hh:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(oldPat);

        try {
            Date date = sdf.parse(sendedDate);
            sdf.applyPattern(showingPate);
            sendedDate = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendedDate;
    }

    public static String getCurrentTimeInUTC() {
        DateFormat df = new SimpleDateFormat(Constants.ShareEndDateFormat_create_send);
        df.setTimeZone(TimeZone.getTimeZone(Constants.UTC_TIMEZONE));
        String utc_time = df.format(new Date());

        return utc_time;
    }


    public static View setColorToShape(View view, int colorCode) {
        GradientDrawable bgShape = (GradientDrawable) view.getBackground();
        bgShape.setColor(colorCode);


        return view;
    }

    public static void deleteCreatePostRow(TableType tableName, CreateData createDataObj) {
        DatabaseMgrSpliro.deleteTableRow(tableName, CreateData.FLD_POST_GUID + "=?", new String[]{createDataObj.post_guid});
    }

    public static void loadImage(Context context, ImageView imgView, String url, int defaultResource) {
        if (url != null&&!url.isEmpty()) {
            Picasso.with(context).load(url)
                    .fit()
                    .centerCrop().error(R.drawable.ic_launcher).fit().placeholder(R.drawable.ic_launcher).fit()
                    .into(imgView);
        }
    }

    public static void loadImageFromSDcard(Context context, ImageView imgView, String imagePath, int defaultResource) {
        Picasso.with(context).load(new File(imagePath))
                .fit()
                .centerCrop().error(R.drawable.ic_launcher).fit().placeholder(defaultResource).fit()
                .into(imgView);

    }

    public static int[] getViewHeightWidth(final ImageView imgView) {
        final int arr[] = new int[2];
        ViewTreeObserver vto = imgView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imgView.getViewTreeObserver().removeOnPreDrawListener(this);
                arr[0] = imgView.getMeasuredWidth();
                arr[1] = imgView.getMeasuredHeight();
                return true;
            }
        });
        return arr;

    }

    public static boolean isGoogleMapsInstalled(Context ctx) {
        try {
            ApplicationInfo info = ctx.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void shareFacebookLink(Context ctx, String urlToShare) {
        try {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ctx.startActivity(Intent.createChooser(intent, "Share Link!"));
        } catch (Exception e) {
            e.printStackTrace();
            com.arya.lib.utils.Util.showCenteredToast(ctx, "No app available");
        }
    }

    public static void shareTwitterLink(Context cntxt, String title, String link) {
        try {

            String sharerUrl = "https://twitter.com/intent/tweet?text=" + title + "&url" + link;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            cntxt.startActivity(Intent.createChooser(intent, "Share Link!"));

        } catch (Exception e) {
            e.printStackTrace();
            com.arya.lib.utils.Util.showCenteredToast(cntxt, "No app available");
        }
    }

    public static void shareGooglePlusLink(Context ctx, String urlToShare) {
        try {
            String sharerUrl = "https://plus.google.com/share?url=" + urlToShare;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            ctx.startActivity(Intent.createChooser(intent, "Share Link!"));
        } catch (Exception e) {
            e.printStackTrace();
            com.arya.lib.utils.Util.showCenteredToast(ctx, "No app available");
        }
    }

    public static void shareMailLink(Context ctx, String title, String link) {
        try {
            Intent intent2 = new Intent();
            intent2.setAction(Intent.ACTION_SEND);
            intent2.setType("message/rfc822");
            intent2.putExtra(Intent.EXTRA_SUBJECT, title);
            intent2.putExtra(Intent.EXTRA_TEXT, link);
            ctx.startActivity(intent2);
        } catch (Exception e) {
            e.printStackTrace();
            com.arya.lib.utils.Util.showCenteredToast(ctx, "No app available");
        }
    }

    public static String getExpirationTimeInUTC()
    {
        DateFormat df =new SimpleDateFormat(Constants.ShareEndDateFormat_create_send);
        df.setTimeZone(TimeZone.getTimeZone(Constants.UTC_TIMEZONE));
        Calendar c = Calendar.getInstance();
        Date dt=new Date();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        String utc_time = df.format(dt);
        return utc_time;
    }

}
