package arya.spliro.dao;

import java.io.Serializable;

/**
 * Created by Admin on 9/3/2015.
 */
public class UserProfileData implements Serializable
{
    public static final String FLD_ID="id";
    public static final String TABLE_NAME = "sp_dta_user";
    public static String FLD_USER_ID="user_id" ;
    public static String FLD_FIRST_NAME="first_name" ;
    public static String FLD_LAST_NAME = "last_name" ;
    public static String FLD_DISPLAY_NAME="display_name" ;
    public static String FLD_PHONE ="phone" ;
    public static String FLD_EMAIL ="email" ;
    public static String FLD_ABOUT_ME ="about_me" ;
    public static String FLD_PROFILE_PICTURE_NAME ="profile_picture_name" ;
    public static String FLD_PROFILE_PICTURE_URL ="profile_picture_url" ;
    public static String FLD_ADDRESS="address";
    public static String FLD_LOCATION_COUNTRY_CODE="location_country_code";
    public static String FLD_LOCATION_LATITUDE="location_latitude";
    public static String FLD_LOCATION_LONGITUDE="location_longitude";
    public static String FLD_TIMEZONE ="timezone" ;
    public static String FLD_RATE ="rate" ;
    public static String FLD_STATUS="status";
    public static String FLD_NOTIFY_PREF="notify_pref";
    public static String FLD_CREATED_AT="created_at";
    public static String FLD_SELECTED_CATEGORY="selected_category";
    public static String FLD_TRNO="trno";


    public int user_id;
    public String first_name;
    public String last_name;
    public String display_name;
    public String phone_number;
    public String email;
    public String about_me;
    public String profile_picture_name;
    public String profile_picture_url;
    public String address;
    public String location_country_code;
    public double location_latitude;
    public double location_longitude;
    public String timezone;
    public int notify_pref;
    public int rate;
    public String status;
    public String created_at;
    public String selected_category;
    public int trno;



}
