package arya.spliro.dao;

import java.io.Serializable;

/**
 * Created by Admin on 8/19/2015.
 */
public class InviteeData implements Serializable{
    public static  String FLD_ID="_id";

    public static final String TABLE_NAME="sp_dta_post_user";
    public static final String FLD_POST_ID="post_id";
    public static final String FLD_POST_GUID="post_guid";
    public static final String FLD_POST_CONTACT_TYPE="post_contact_type";
    public static final String FLD_POST_CONTACT_ID="post_contact_id";
    public static final String FLD_CREATED_AT="created_at";
    public static final String FLD_IS_INVITED="is_invited";
    public static final String FLD_IS_FAVORITE="is_favorite";
    public static final String FLD_STATUS="status";
    public static final String FLD_OFFER_SHARES_REQUESTED="offer_shares_requested";
    public static final String FLD_OFFER_SHARES_APPROVED="offer_shares_approved";
    public static final String FLD_OFFER_USER_LATITUDE="offer_user_latitude";
    public static final String FLD_OFFER_USER_LONGITUD="offer_user_longitude";
    public static final String FLD_OFFER_REMARK="offer_remark";
    public static final String FLD_OFFER_PRICE="offer_price";
    public static final String FLD_UPDATED_AT="updated_at";
    public static final String FLD_TRNO="trno";
    public static final String FLD_DISPLAY_NAME="display_name";
    public String first_name;
    public String last_name;
    public Float rate;
    public String post_contact_id;
    public String display_name;
    public  long trno;
    public String post_contact_type;
    public int _id;
    public int post_id;

}
