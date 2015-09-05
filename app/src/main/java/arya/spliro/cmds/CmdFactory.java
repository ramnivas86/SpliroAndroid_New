package arya.spliro.cmds;

import com.arya.lib.cmds.BaseCmd;
import com.arya.lib.cmds.Cmd;
import com.arya.lib.logger.Logger;

import org.json.JSONArray;

import arya.spliro.config.Config;
import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.InviteeData;
import arya.spliro.dao.SignupData;
import arya.spliro.utility.CategoriesMgr;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by ARCHANA on 19-07-2015.
 */
public class CmdFactory {

    public static Cmd createSignInCmd(String signinId,String countryCode, String pwd,String login_type){
        CmdPreSignIn cmd = new CmdPreSignIn(CmdConstants.SIGNIN_USER);
        if(countryCode.trim().length()!=0)
        {
            cmd.addData(SignupData.KEY_PHONE, signinId);
        }
        else
        {
            cmd.addData(SignupData.KEY_EMAIL, signinId);
        }
        cmd.addData(SignupData.KEY_LOGIN_TYPE,login_type);
        cmd.addData(SignupData.KEY_PASSWORD, pwd);
        return cmd;
    }

    public static Cmd createSignUpCmd(String fName, String lName,String Phone,String countryCode,String Email,String pass,String reg_typ,String fb_user_id, String fb_access_token,String fb_expires,String fb_last_updated_on,String fb_post_enabled){
        CmdPreSignIn cmd = new CmdPreSignIn(CmdConstants.SIGNUP_USER);
        cmd.addData(SignupData.KEY_FIRST_NAME, fName);
        if(!lName.isEmpty())
        {
            cmd.addData(SignupData.KEY_LAST_NAME, lName);
        }
        if(!Phone.isEmpty())
        {
            cmd.addData(SignupData.KEY_PHONE, Phone);
            cmd.addData(SignupData.FLD_LOCATION_COUNTRY_CODE,Constants.COUNTRYCODE);
        }
        if(!Email.isEmpty())
        {
            cmd.addData(SignupData.KEY_EMAIL, Email);
        }
        if(!pass.isEmpty())
        {
            cmd.addData(SignupData.KEY_PASSWORD, pass);
        }
        cmd.addData(SignupData.KEY_REG_TYP, reg_typ);

        if(reg_typ.equals(Constants.REG_TYP_WITH_FACEBOOK))
        {
            cmd=setDataForFacebook(cmd,fb_user_id,fb_access_token,fb_expires,fb_last_updated_on,fb_post_enabled);
        }
        return cmd;
    }

    private static CmdPreSignIn setDataForFacebook(CmdPreSignIn cmd,String fb_user_id, String fb_access_token,String fb_expires,String fb_last_updated_on,String fb_post_enabled) {
        cmd.addData(SignupData.KEY_FACEBOOK_ID, fb_user_id);
        cmd.addData(SignupData.KEY_FACEBOOK_ACCESS_TOKEN, fb_access_token);
        cmd.addData(SignupData.KEY_FACEBOOK_EXPIRES, fb_expires);
        cmd.addData(SignupData.KEY_FB_LAST_UPDATED_ON, fb_last_updated_on);
        cmd.addData(SignupData.KEY_FB_POST_ENABLED, fb_post_enabled);
        return cmd;

    }

    public static Cmd createAuthCodeCmd(String regId,String authCode) {
        CmdPreSignIn cmd = new CmdPreSignIn(CmdConstants.VERIFY_USER);
        cmd.addData(SignupData.KEY_REG_ID, regId);
        cmd.addData(SignupData.KEY_VERIFICATION_CODE, authCode);
        return cmd;
    }

    public static Cmd createResendPinCodeCmd(String regId,String phone,String eMail) {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.RESEND_VERIFICATION_CODE);
        cmd.addData(SignupData.KEY_REG_ID, regId);
        cmd.addData(SignupData.KEY_PHONE, phone);
        cmd.addData(SignupData.KEY_EMAIL, eMail);
        return cmd;
    }

    public static Cmd createForgotPassCmd(String phoneOrEmail) {
        CmdPreSignIn cmd = new CmdPreSignIn(CmdConstants.FORGOT_PASSWORD_SEND_OTP_USER);
        String key=SignupData.KEY_EMAIL;
        if(Util.isNumeric(phoneOrEmail))
        {
            key=SignupData.KEY_PHONE;
        }
        cmd.addData(key, phoneOrEmail);
        return cmd;
    }

    public static Cmd createLocationListingCmd()
    {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.LOCATION_LIST);
        cmd.addData(Constants.FLD_USER_ID, Config.getUserId());
        return cmd;
    }
    public static Cmd createAddLocationCmd()
    {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.LOCATION_CREATE);
        cmd.addData(Constants.FLD_USER_ID, Config.getUserId());
        return cmd;
    }
    public static Cmd createDeleteLocationCmd()
    {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.LOCATION_DELETE);
        cmd.addData(Constants.FLD_USER_ID, Config.getUserId());
        return cmd;
    }
    public static Cmd createCategoryListCmd()
    {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.CATEGORY_LIST);
        cmd.addData(Categories.KEY_TRNO, CategoriesMgr.getLastTrno());
        cmd.addData("hash_tag","");
        return cmd;
    }
    public static Cmd createCheckPendingEvents()
    {
        CmdPostSignIn cmd = new CmdPostSignIn("fetch_pending_events");

        return cmd;
    }
    public static Cmd updateMyProfileData(String first_name,String last_name,String display_name,String phone,String email,String about_me,JSONArray catIdJsonArray,String file_name,String password,int noti_prif)
    {
        CmdPostSignIn cmd = new CmdPostSignIn(CmdConstants.UPDATE_MYPROFILE);
        cmd.addData(Constants.FLD_USER_ID, Config.getUserId());
        cmd.addData(Constants.FLD_FIRST_NAME,first_name);
        cmd.addData(Constants.FLD_LAST_NAME,last_name);
        cmd.addData(Constants.FLD_PHONE,phone);
        cmd.addData(Constants.FLD_EMAIL,email);
        cmd.addData(Constants.FLD_ABOUT_ME,about_me);
        cmd.addData(Constants.FLD_USER_CATEGORY,catIdJsonArray);
        cmd.addData(Constants.FLD_PROFILE_PIC_NAME,file_name);
        cmd.addData(Constants.FLD_PWD,password);
        cmd.addData(Constants.FLD_NOTIFY_PREF,noti_prif);
        cmd.addData(Constants.FLD_DISPLAY_NAME,display_name);
        return cmd;
    }



    public static Cmd createCreatePostCmd(CreateData obj,JSONArray inviteeArray)
    {
        CmdPostSignIn cmd=null;
        if(obj.post_id>0)
        {
            cmd=new CmdPostSignIn(CmdConstants.EDIT_POST);
            cmd.addData(CreateData.FLD_POST_ID,obj.post_id);
        }
        else
        {
            cmd=new CmdPostSignIn(CmdConstants.CREATE_POST);
        }
        try
        {
            cmd.addData(CreateData.KEY_SHARE_TITLE,obj.title);
            cmd.addData(CreateData.KEY_SHARE_DESCRIPTION,obj.categories_data.description);
            cmd.addData(CreateData.KEY_SHARE_STATUS,obj.status);
            cmd.addData(CreateData.KEY_SHARE_CATEGORY_ID,obj.categories_data.catgory_id);
            cmd.addData(CreateData.KEY_SHARE_CATEGORY_NAME,obj.categories_data.name);
            cmd.addData(CreateData.KEY_SHARE_POST_HASHTAG,obj.categories_data.hashtag);
            cmd.addData(CreateData.KEY_SHARE_POST_LATITUDE,obj.location_data.location_latitude);
            cmd.addData(CreateData.KEY_SHARE_POST_LONGITUDE,obj.location_data.location_longitude);
            cmd.addData(CreateData.KEY_SHARE_ZIPCODE,obj.location_data.zipcode);
            cmd.addData(CreateData.KEY_SHARE_ADDRESS,obj.location_data.address);
            cmd.addData(CreateData.KEY_SHARE_TOTAL_SHARE,obj.no_of_shares);
            cmd.addData(CreateData.KEY_SHARE_POST_EXPIRE_DATE,obj.post_expire_date);
            cmd.addData(CreateData.KEY_SHARE_INVOICE_PRICE,obj.invoice_price);
            cmd.addData(CreateData.KEY_SHARE_PIC_NAME,obj.categories_data.pic_name);

            cmd.addData(CreateData.KEY_SHARE_PER_SHARE_PRICE,obj.per_share_price);
            cmd.addData(CreateData.KEY_SHARE_INVOICE_IMAGE_NAME,obj.invoice_image_name);
            cmd.addData(CreateData.KEY_SHARE_TOTAL_SHARES_LEFT,obj.totalSharesLeft);
            cmd.addData(CreateData.FLD_POST_GUID,obj.post_guid);
            String inviteeArrayString =null;
            if(inviteeArray!=null&&inviteeArray.length()>0)
            {
                inviteeArrayString=inviteeArray.toString();
            }
            cmd.addData(CreateData.FLD_INVITED_USER,inviteeArrayString);


        }
        catch (Exception e)
        {
            if(Logger.IS_DEBUG)
            {
                Logger.error(Constants.APP_FOLDER_NAME,"At:PreviewModel, method:[prepareCmdForPostShare(Cmd cmd,CreateData obj)],error["+e+"]");
            }

        }
        return cmd;
    }


    public static Cmd createChangePassCmd(String[] params) {
        CmdPreSignIn cmd=new CmdPreSignIn(CmdConstants.FORGOT_PASSWORD_VERIFY_OTP_USER);
        cmd.addData(SignupData.KEY_VERIFICATION_CODE,params[1]);
        cmd.addData(SignupData.KEY_PASSWORD,params[2]);
        return cmd;
    }

    public static Cmd createChangeShareStatusCmd(CreateData createDataObj) {
        CmdPostSignIn cmd=new CmdPostSignIn(CmdConstants.CHANGE_SHARE_STATUS);
        cmd.addData(CreateData.KEY_SHARE_STATUS,createDataObj.status);
        cmd.addData(CreateData.FLD_POST_ID,createDataObj.post_id);
        return cmd;
    }

    public static Cmd createDeleteShareCmd(CreateData createDataObj) {
        CmdPostSignIn cmd=new CmdPostSignIn(CmdConstants.DELETE_SHARE);
        String actionFrom=Constants.BY_POSTER;
        if(createDataObj.user_id>0&&createDataObj.user_id!=Config.getUserId())
        {
            actionFrom=Constants.BY_DOER;
        }
        cmd.addData(CreateData.KEY_ACTION_FROM,actionFrom);
        cmd.addData(CreateData.FLD_POST_ID,createDataObj.post_id);
        return cmd;
    }
}
