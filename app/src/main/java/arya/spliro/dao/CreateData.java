package arya.spliro.dao;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Admin on 8/17/2015.
 */
public class CreateData implements Serializable {
    public static final String TABLE_NAME="sp_dta_post";

    public static String FLD_ID="_id";
    public static String FLD_POST_ID="post_id";
    public static String FLD_INVITED_USER="invited_user";
    public static String FLD_POST_GUID="post_guid";
    public static String FLD_UESR_ID="user_id";
    public static String KEY_SHARE_STATUS="status";
    public static String KEY_ACTION_FROM="action_from";
    public static String KEY_SHARE_TITLE="title" ;
    public static String KEY_SHARE_DESCRIPTION="description" ;
    public static String KEY_SHARE_CATEGORY_ID="category_id";
    public static String KEY_SHARE_CATEGORY_NAME="category_name" ;
    public static String KEY_SHARE_POST_HASHTAG="post_hashtag";
    public static String KEY_SHARE_POST_KEYWORDS="keywords";
    public static String KEY_SHARE_POST_LATITUDE="post_latitude";
    public static String KEY_SHARE_POST_LONGITUDE="post_longitude";
    public static String KEY_SHARE_ZIPCODE="zipcode";
    public static String KEY_SHARE_ADDRESS="address";
    public static String KEY_SHARE_PIC_NAME="pic_name";
    public static String KEY_SHARE_PIC_NAME_URL="pic_name_url";
    public static String KEY_SHARE_PIC_THUMB="pic_thumb";
    public static String KEY_SHARE_PIC_THUMB_URL="pic_thumb_url";
    public static String KEY_SHARE_TOTAL_SHARE="total_share";
    public static String KEY_SHARE_TOTAL_SHARES_LEFT="total_shares_left";
    public static String KEY_SHARE_POST_EXPIRE_DATE="post_expire_date";
    public static String KEY_SHARE_INVOICE_PRICE="invoice_price";
    public static String KEY_SHARE_PER_SHARE_PRICE="per_share_price";
    public static String KEY_SHARE_INVOICE_IMAGE_NAME="invoice_image_name";
    public static String FLD_CREATED_AT="created_at";
    public static String FLD_UPDATED_AT="updated_at";
    public static String FLD_CATEGORY_IMG_PATH="category_img_path";
    public static String FLD_RECEIPT_IMG_PATH="receipt_img_path";
    public static String FLD_TRNO="trno";
    public static String FLD_CREATOR_NAME="display_name";
    public static String FLD_CREATOR_RATE="rate";
    public static String FLD_PROFILE_PIC_URL="profile_pic_url";

    public double invoice_price;
    public String no_of_shares="1";
    public LocationData location_data;
    public ArrayList<InviteeData> invitee_data =new ArrayList<InviteeData>();
    public Categories categories_data=new Categories();
    public String status;
    public double per_share_price;
    public String invoice_image_name="";
    public String invoice_image_url="";
    public String totalSharesLeft="100";
    public String post_guid;
    public String title;
    public  long  post_id;
    public String created_at="";
    public String updated_at="";
    public String receipt_image_path;
    public int trno;
    public String display_name;
    public float rate;
    public String profile_pic_url;
    public int _id;
    public long user_id;
    public String errorMsg="";
    public String post_expire_date;
    public boolean isSuccess;

    public int no_people_joined;
    public Context ctx;
}
