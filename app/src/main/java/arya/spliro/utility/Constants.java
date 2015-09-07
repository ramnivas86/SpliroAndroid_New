package arya.spliro.utility;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;

/**
 * Created by Admin on 7/31/2015.
 */
public class Constants {
    public static final String POSTING_STATUS="o";
    public static final String SAVING_STATUS="s";
    public static final String DELETE_STATUS="d";
    public static final String CLOSED_STATUS="c";
    public static final String BY_POSTER="p";
    public static final String BY_DOER="d";
    public static final String YOU_TEXT="You";

    public static String UTC_TIMEZONE="UTC";
    public static final String DISTANCE_TEXT="mi\naway";
    public static final String PEOPLE_JOINED="\r\npeople\njoined";
    public static final String TIME_LEFT_TEXT ="m\nleft";
    public static final String KEY_FOR_PREVIEW_DATA ="createDataKey";

    public static final String POST_CONTACT_TYPE_PHONE="p";
    public static final String POST_CONTACT_TYPE_EMAIL="e";
    public static final String POST_CONTACT_TYPE_USER="u";
    public static final String CURRENCY="$";//22/8/2015 phoosaram
    public static final String APP_FOLDER_NAME = "spliro";
    public static final String SAMSUNGDEVICENAME ="samsung" ;
    public static final java.lang.String ShareEndDateFormat_create_show ="dd MMM, h:mm a" ;
    public static final java.lang.String ShareEndDateFormat_create_send ="yyyy-MM-dd HH:mm:ss" ;
    public static final String INVITEELIST ="inviteelist" ;
    public static final String FLD_FILE_NAME ="file_name" ;
    public static String BASE_URL ="http://182.237.17.106:8080/spliro/sitedev/api/web/v2/index/index";
    public static String UPLOAD_URL="http://182.237.17.106:8080/spliro/sitedev/api/web/v2/index/upload";
    /**
     *   common Constans
     */
    public static final String SOURCE_APP = "android";
    public static final String INVALID_ID_STRING = "-1";
    public static final long INVALID_ID = -1;
    public static final String FLD_KEY_DATA = "data";
    public static String ApiFOrCheckNumberIsExist="https://data.searchbug.com/api/search.aspx?CO_CODE=85845&PASS=onlyone1&TYPE=api_loc&F=";
    public static String Const_PhoneNumber="PhoneNumber";
    public static String Const_CELLULAR="CELLULAR";
    public static String COUNTRYCODE ="91";
    public static final int MINIMUMLENGTHOFNAME = 1;
    public static final int MAXLENGTHOFLASTNAME =12 ;
    public static final int MAXLENGTHOFPHONENUMBER = 10;
    public static final int MAX_LENGTH_LOCATION_LIST=10;
    public static final int MINIMUMLENGTHOFPASS = 8;
    public static String googleMapKey="AIzaSyDOm6g5fMoukRBjNEyeSzQ6U_ulE-D0aPU";
    public static int MAXLENGTHOFFIRSTNAME=14;
    public static String LOGIN="login";
    public static String REG_TYP_WITH_FACEBOOK="f";
    public static String REG_TYP_WITHOUT_FACEBOOK="s";
    public static final String facebook_app_id="1460703184232728";//app id for loginwith facebook and registration with facebook
    public static int PICK_IMAGE_FROM_CAMERA=1;
    public static int PICK_IMAGE_FROM_GALLERY=2;
    public static final int PICK_CROP_IMAGE = 3;
    public static final int PICK_CONTACTS = 5;
    public static int REQ_SELECT_LOCATION= 101;
    public static int REQ_FOR_SHARE_PREVIEW= 102;
    public static final int RESULT_CODE_REFRESHMYSHARES = 103;
    public static Uri imageUri;
    /**
     *   common fields for API Request
     */
    public static final String FLD_APP_TOKEN = "app_token";
    public static final String FLD_USER_TOKEN = "user_token";
    public static final String FLD_EMAIL_ID = "email";// Email ID
    public static final String FLD_USER_ID = "user_id";
    public static final String FLD_LANG_CODE = "language_code";
    public static final String FLD_SOURCE_APP = "source_app";
    public static final String FLD_USER_DEVICE_ID = "user_device_id";
    public static final String FLD_REMOTE_DEVICE_ID="remote_device_id";
    public static String AppFolderName="spliro_app";
    /**
     *   common fields for update MyProfile API Request
     */
    public static final String FLD_FIRST_NAME="first_name";
    public static final String FLD_LAST_NAME="last_name";
    public static final String FLD_PHONE="phone";
    public static final String FLD_EMAIL="email";
    public static final String FLD_ABOUT_ME="about_me";
    public static final String FLD_USER_CATEGORY="user_category";
    public static final String FLD_FILENAME="file_name";
    public static final String FLD_PROFILE_PIC_NAME="profile_picture_name";
    public static final String FLD_DISPLAY_NAME="display_name";
    public static final String FLD_PIC_NAME="profile_picture_name";
    public static final String FLD_PIC_URL="profile_pic_url";
    public static final String FLD_PWD="pwd";
    public static final String FLD_NOTIFY_PREF="notify_pref";

    /**
     *   common fields for update MyProfile API Request
     */

    public static File captured_image_path;
    public static String imageFormat=".png";
    public static String profileImge="profile_image";
    public static String tempFolderName="temp";
    public static String createFolder="create";
    public static String SELECTED_LOCATION="selected_location";
    public static String REPOST_SHARE="repost_share";
    public static String EDIT_SHARE="edit_share";


    public static String PRICE_PER_SHARE="";
    public static String TOTAL_PRICE_FOR_SHARE="";
    public static double MAX_PRICE_VALUE=99999;
    public static String PER_SHARE=" each";//22/8/2015 phoosaram
    public static final int MIN_LINES_TO_SHOW=4;//22/8/2015 phoosaram
    public static final int NINE_LINES_TO_SHOW=9;//
    public static final int MAX_LINES_TO_SHOW=1000;//22/8/2015 phoosaram
    public static String OTHER="other";
    public static final String DB_NAME="spliro";
    /**
     *   common fields for CATEGORY_LIST API Request
     */



}
