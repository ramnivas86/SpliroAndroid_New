package arya.spliro.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import org.json.JSONObject;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.SignupData;
import arya.spliro.dao.UserProfileData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.utility.Constants;
import arya.spliro.utility.ObjectSerializer;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/5/2015.
 */
public class Config {
    public static Integer width = null;
    public static Integer height = null;
    public static final String APP_VERSION = "vd00-01-023";
    public static final String APP_TOKEN = "AIzaSyDOm6g5fMoukRBjNEyeSzQ6U_ulE-D0aPU"; // From google api account ageless-impulse-617 ,543129283963
    public static final String GPLACE_API_KEY="AIzaSyAvbBjQQ58mw9hTqnKjyNRuAU1MF1oO370";
    public static final String APP_TOKEN_IN_NUMBERS = "774537942330 ";
    private static final String CONFIG_FILE = "prefs";
    private static SharedPreferences preference = null;
    private static SharedPreferences.Editor editor;
    public static final String KEY_USERFNAME = "firstName";
    public static final String KEY_USERLNAME = "lastName";
    public static final String KEY_USERPHONE = "userphone";
    public static final String KEY_USEREMAIL = "useremail";
    public static final String KEY_USERPASS = "password";
    public static final String KEY_OLDUDID = "oldudid";
    public static String KEY_USERLOGIN="userLogin";
    public static String KEYCREATEMSGARRAY="createMsgArray";
    public static String KEY_AUTHORIZED = "authorized";
    public static final String KEY_USER_TOKEN ="user_token" ;
    public static String KEY_USER_ID="user_id";
    public static String KEY_LANG_CODE="lang_code";
    public static String KEY_USER_DEVICE_ID="user_device_id";
    public static final String KEY_CURRNENT_LOCATIN="current_location";
    public static final String KEY_CURRNENT_LOCATION_ZIP="zip_code";
    public static String KEY_ABOUTME="aboutme";
    public static String KEY_DISPLAYNAME="displayname";
    private static String KEY_PROFILE_IMAGE="profilePic";
    private static String KEY_CATEGORY_ARRAY="categoryArray";
    private static Context mContext;
    private static String deviceType;
    private static String screenType;

    public static  DeviceInfo deviceInfo;

    public static void init(Context mContext) {
       Config.mContext = mContext;
        Config.preference = mContext.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
        Config.editor = preference.edit();
        try{
            deviceInfo = getDeviceInfo();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void removeOrClearPerferance(String key) {
        if(key!=null&&key.trim().length()!=0)
            editor.remove(key);
        else
            editor.clear();
        editor.commit();
    }
    public static void savePreferencese() {
        editor.commit();
    }

    public static ArrayList<CreateData> getCreateCardArray() {
        ArrayList<CreateData> createArrayList=new ArrayList<CreateData>();
        try {if(preference.getString(KEYCREATEMSGARRAY, "")!=null)
            createArrayList= (ArrayList<CreateData>) ObjectSerializer.deserialize(preference.getString(KEYCREATEMSGARRAY,  ObjectSerializer.serialize(new ArrayList<CreateData>())));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return createArrayList;
    }
    public static void setCreateCardArray(ArrayList<CreateData> createCardList) {
        try {
            if(createCardList==null||createCardList.size()==0)
            {
                editor.putString(KEYCREATEMSGARRAY, null );
            }
            else
            {
                editor.putString(KEYCREATEMSGARRAY, ObjectSerializer.serialize(createCardList) );
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String getUserDeviceId() {
        return preference.getString(KEY_USER_DEVICE_ID, Constants.INVALID_ID_STRING);
    }

    public static void setUserDeviceId(String usrDeviceId) {
        editor.putString(KEY_USER_DEVICE_ID, usrDeviceId);
    }
    public static String getLangCode() {
        return preference.getString(KEY_LANG_CODE, "en_us");
    }
    public static void setLangCode(String langCode) {
        editor.putString(KEY_LANG_CODE, langCode);
    }
    public static void setUserfname(String firstName)
    {
        editor.putString(KEY_USERFNAME, firstName);
    }
public static String getUserfname()
{
    return preference.getString(KEY_USERFNAME, "");
}
    public static String getUserToken() {
        return preference.getString(KEY_USER_TOKEN, "");
    }
    public static void setUserToken(String userToken) {
        editor.putString(KEY_USER_TOKEN, userToken);
    }
    public static long getUserId() {
        return preference.getLong(KEY_USER_ID,Constants.INVALID_ID);
    }
    public static void setUserId(long userId) {
        editor.putLong(KEY_USER_ID, userId);
    }
    public static void setAUTHORIZED(String lName)
    {
        editor.putString(KEY_AUTHORIZED, lName);
    }
    public static String getAUTHORIZED()
    {
        return preference.getString(KEY_AUTHORIZED, "");
    }
    public static void setUserlname(String lName)
  {
      editor.putString(KEY_USERLNAME, lName);
  }
    public static String getUserlname()
    {
        return preference.getString(KEY_USERLNAME, "");
    }
   public static void setUserphone(String userphone)
   {
       editor.putString(KEY_USERPHONE, userphone);
   }
    public static String getUserphone()
    {
        return preference.getString(KEY_USERPHONE, "");
    }
    public static void setUseremail(String useremail)
    {
        editor.putString(KEY_USEREMAIL, useremail);
    }
    public static String getUseremail()
    {
        return preference.getString(KEY_USEREMAIL, "");
    }
    public static void setUserpass(String userpass)
    {
        editor.putString(KEY_USERPASS,userpass );
    }
    public static String getUserpass()
    {
        return preference.getString(KEY_USERPASS, "");
    }
    public static String getUserLogin() {
        return preference.getString(KEY_USERLOGIN, "");
    }
    public static void setUserLogin(String userLogin) {
        editor.putString(KEY_USERLOGIN, userLogin);
    }
    public static String getOldudid() {
        return preference.getString(KEY_OLDUDID, "");
    }
    public static void setOldudid(String oldudid) {
        editor.putString(KEY_OLDUDID, oldudid);
    }
    public static DeviceInfo getDeviceInfo()
    {
        getScreenResolution((Activity) mContext);
        DeviceInfo deviceInfo = new DeviceInfo();
        TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        deviceInfo.setDeviceId(Util.getDeviceID(mContext));
        if(tMgr!=null)
        {
            String simSerialNumber=tMgr.getSimSerialNumber();
        if(simSerialNumber!=null) {
            deviceInfo.setSimOperator(tMgr.getSimOperator());
            deviceInfo.setSimCountryIso(tMgr.getSimCountryIso());

            deviceInfo.setSimSerialNumber(simSerialNumber);
            deviceInfo.setSimOperatorName(tMgr.getSimOperatorName());
            deviceInfo.setSimNetworkOperator(tMgr.getNetworkOperator());
        }
        }
        deviceInfo.setDeviceMacAddress(Util.getDeviceMacAddres(mContext));
        deviceInfo.setEmeiMeidEsn(tMgr.getDeviceId());
        deviceInfo.setDeviceModel(android.os.Build.MODEL);
        deviceInfo.setOsBuildNumber(Build.FINGERPRINT);
        deviceInfo.setOsVersion(System.getProperty("os.version"));
        deviceInfo.setOsType(System.getProperty("os.name"));
        deviceInfo.setAppToken(APP_TOKEN);
        deviceInfo.setDeviceDensity(getDensity(mContext));
        deviceInfo.setAppVersion(APP_VERSION);
        deviceInfo.setCurrAppClientVersion(Util.getAppVersion(mContext));
        deviceInfo.setDeviceResolution(width + "x" + height);
        //deviceInfo.setCloudKeyToNotify(getGcmRegisteredID());
        return deviceInfo;
    }
    public static boolean isLargeScreen() {
        return screenType.equals("largescreen");
    }

    public static boolean isNormalScreen() {

        return screenType.equals("normalscreen");
    }

    public static boolean isSmallScreen() {

        return screenType.equals("smallscreen");
    }

    public static String getScreenSize(Activity activity) {
        if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return "largescreen";

        } else if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            return "normalscreen";

        } else if ((activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            return "smallscreen";

        } else {
            return "none";

        }
    }

    public static void getScreenResolution(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    public static boolean isHDPIDevice() {

        return deviceType.equals("hdpi");
    }

    public static boolean isMDPIDevice() {

        return deviceType.equals("mdpi");
    }

    public static boolean isLDPIDevice() {

        return deviceType.equals("ldpi");
    }

    // Method to get the Density of the device.

    private static String getDensity(Context context) {
        String r;
        DisplayMetrics metrics = new DisplayMetrics();

        if (!(context instanceof Activity)) {
            r = "hdpi";
        } else {
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi <= DisplayMetrics.DENSITY_LOW) {
                r = "ldpi";
            } else if (metrics.densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
                r = "mdpi";
            } else {
                r = "hdpi";
            }
        }

        return r;
    }

    public static void saveUserDetail(JSONObject dataObj,String authCode) {
        try {
            UserProfileData userProfileData=new UserProfileData();

            if (dataObj != null)
            {
                ArrayList<UserProfileData> userProfileDataList=null;
                userProfileDataList=new ArrayList<UserProfileData>();
                Config.setUserId(dataObj.getLong(SignupData.KEY_USER_ID));
                userProfileData.user_id= Integer.parseInt(dataObj.getString(SignupData.KEY_USER_ID));
                if(authCode!=null&&authCode.trim().length()!=0)
                {
                    Config.setAUTHORIZED(authCode);
                }
                Config.setUserLogin(Constants.LOGIN);

                if(dataObj.has(SignupData.KEY_FIRST_NAME)) {
                    Config.setUserfname(dataObj.getString(SignupData.KEY_FIRST_NAME));
                    userProfileData.first_name=dataObj.getString(SignupData.KEY_FIRST_NAME);
                }
                if(dataObj.has(SignupData.KEY_LAST_NAME)) {
                    Config.setUserlname(dataObj.getString(SignupData.KEY_LAST_NAME));
                    userProfileData.last_name=dataObj.getString(SignupData.KEY_LAST_NAME);
                }
                if(dataObj.has(SignupData.KEY_DISPLAY_NAME)) {
                    Config.setUserDispalyName(dataObj.getString(SignupData.KEY_DISPLAY_NAME));
                    userProfileData.display_name=dataObj.getString(SignupData.KEY_DISPLAY_NAME);
                }

                if(dataObj.has(SignupData.KEY_EMAIL)) {
                    Config.setUseremail(dataObj.getString(SignupData.KEY_EMAIL));
                    userProfileData.email=dataObj.getString(SignupData.KEY_EMAIL);
                }
                if(dataObj.has(SignupData.KEY_PHONE)) {
                    Config.setUserphone(dataObj.getString(SignupData.KEY_PHONE));
                    userProfileData.phone_number=dataObj.getString(SignupData.KEY_PHONE);
                }

                if(dataObj.has(SignupData.KEY_ABOUT_ME)) {
                    Config.setUserAboutMe(dataObj.optString(SignupData.KEY_ABOUT_ME));
                    userProfileData.about_me=dataObj.optString(SignupData.KEY_ABOUT_ME);
                }
                if(dataObj.has(Constants.FLD_PIC_URL)) {
                    Config.setUserImageUrl(dataObj.optString(Constants.FLD_PIC_URL));
                    userProfileData.profile_picture_url=dataObj.optString(Constants.FLD_PIC_URL);
                }
                if(dataObj.has(Constants.FLD_NOTIFY_PREF)) {

                    userProfileData.notify_pref=dataObj.optInt(Constants.FLD_NOTIFY_PREF);
                }
                if(dataObj.has(Constants.FLD_USER_CATEGORY)) {
                    //  Config.setUserCategoryArray(dataObj.getJSONArray(Constants.FLD_USER_CATEGORY).toString());
                    userProfileData.selected_category=dataObj.getJSONArray(Constants.FLD_USER_CATEGORY).toString();
                }
                userProfileDataList.add(userProfileData);
                DatabaseMgrSpliro.insertDataToUserProfile(userProfileDataList);
                Config.savePreferencese();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void setCurrntLocation(String address)
    {
        editor.putString(KEY_CURRNENT_LOCATIN, address);
    }
    public static  String getCurrentLocation()
    {
        return preference.getString(KEY_CURRNENT_LOCATIN, "");
    }
    public static void setCurrentLocationZipCode(String zipCode)
    {
        editor.putString(KEY_CURRNENT_LOCATION_ZIP, zipCode);
    }
    public static String getCurrentLocationZipCode()
    {
        return preference.getString(KEY_CURRNENT_LOCATION_ZIP, "");
    }
    public static String getUserDispalyName() {
        return preference.getString(KEY_DISPLAYNAME,"");
    }

    public static void setUserDispalyName(String dispalyName) {
        editor.putString(KEY_DISPLAYNAME, dispalyName);

    }
    public static String getUserAboutMe() {
        return preference.getString(KEY_ABOUTME,"");
    }

    public static void setUserAboutMe(String aboutMe)
    {
        editor.putString(KEY_ABOUTME, aboutMe);

    }
    public static String getUserImageUrl()
    {
        return preference.getString(KEY_PROFILE_IMAGE, "");
    }

    public static void setUserImageUrl(String imageurl)
    {
        editor.putString(KEY_PROFILE_IMAGE,imageurl);
    }
    public static void setUserCategoryArray(String categoryArray)
    {
        editor.putString(KEY_CATEGORY_ARRAY,categoryArray);
    }
    public static String getUserCategoryArray()
    {
        return preference.getString(KEY_CATEGORY_ARRAY, "");
    }
}

