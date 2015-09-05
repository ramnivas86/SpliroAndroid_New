package arya.spliro.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;
import com.facebook.android.FBUtil;
import com.facebook.android.Facebook;

import org.json.JSONObject;

import arya.spliro.R;
import arya.spliro.cmds.CmdFactory;
import arya.spliro.config.Config;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/5/2015.
 */
public class SignUpModel extends BasicModel {
    public String fName="";
    public String lName="";
    String phone="";
    public String eMailId="";
    public String pass="";
    public String countryCode="";
    public String reg_typ="";
    public Task task;
    public GetFacebookInfoAsync getFacebookInfoAsync;
    public String fb_user_id;
    public String fb_access_token;
    public String fbaccess_token_expires;
    public String last_updated_time;
    public String fb_post_enabled;
    public String gender;
    public void doSignUp(String fName,String lName,String eMailId,String phone,String countryCode,String pass ,String reg_typ,String fb_user_id,String fb_access_token,String fbaccess_token_expires,String last_updated_time,String fb_post_enabled,String gender)
    {
       this.fName = fName;
        this.lName =lName ;
        this.eMailId = eMailId;
        this.phone =phone ;
        this.pass =pass ;
        this.reg_typ =reg_typ ;
        this.countryCode =countryCode ;
        this.fb_user_id=fb_user_id;
        this.fb_access_token=fb_access_token;
        this.fbaccess_token_expires=fbaccess_token_expires;
        this.last_updated_time=last_updated_time;
        this.fb_post_enabled=fb_post_enabled;
        this.gender=gender;

        if(task==null)
        {
            task=new Task();
            task.execute();
        }
    }

    public class Task extends AsyncTask<String, String, NetworkResponse> {

        @Override
        protected NetworkResponse doInBackground(String... params) {
            Cmd cmd = CmdFactory. createSignUpCmd(fName,lName,phone,countryCode,eMailId,pass,reg_typ,fb_user_id, fb_access_token,fbaccess_token_expires,last_updated_time,fb_post_enabled);
            NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
            if (response != null) {
                if (response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                    saveDataToSharedPrefs(response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA));
                } else {
                    System.out.println("doInBackground(): Error");
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse networkResponse) {
            Util.dimissProDialog();
            task=null;
            SignUpModel.this.notifyObservers(networkResponse);
        }
    }
    private void saveDataToSharedPrefs(JSONObject jsonObject)
    {
         Config.setUserfname(fName);
        Config. setUserlname(lName);
        Config.setUseremail(eMailId);
        Config.setUserphone(phone);
        Config.setUserpass(pass);
        Config.savePreferencese();
    }
    public void getFacebookInfo(Facebook fbobj)
    {
        if(getFacebookInfoAsync==null)
        {
            getFacebookInfoAsync=new GetFacebookInfoAsync();
        }
        getFacebookInfoAsync.execute(fbobj);

    }

    public class GetFacebookInfoAsync extends AsyncTask<Facebook,Void,JSONObject>
    {
        @Override
        protected void onPreExecute()
        {
            Util.showProDialog("");
        }

        @Override
        protected JSONObject doInBackground(Facebook... facebooks) {
            JSONObject facebook_returned_jsonObj=null;
            try
            {
               String jsonUser = facebooks[0].request("me");
               facebook_returned_jsonObj= FBUtil.parseJson(jsonUser);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return facebook_returned_jsonObj;
        }

        @Override
        protected void onPostExecute(JSONObject Result) {
            Util.dimissProDialog();
            getFacebookInfoAsync=null;
            if(Result!=null)
            {
                SignUpModel.this.notifyObservers(Result);
            }
        }
    }

//    public class CheckEnterNumberIsExistInRealWorldAsync extends AsyncTask<String, String, NetworkResponse> {
//
//
//        public CheckEnterNumberIsExistInRealWorldAsync() {
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Util.showProDialog(mContext, mContext.getString(R.string.wait));
//        }
//
//        @Override
//        protected NetworkResponse doInBackground(String... params) {
//            NetworkResponse response=null;
//if(DisplayPNumber.trim().length()!=0) {
//    response = NetworkMgr.httpPost(Constants.ApiFOrCheckNumberIsExist+Constants.CONTRYCODE+ DisplayPNumber, "", null);
//    if(response!=null&&response.respStr.contains(Constants.Const_PhoneNumber)&&response.respStr.contains(Constants.Const_CELLULAR))
//    {
//
//    }
//
//}
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(NetworkResponse result) {
//            Util.dimissProDialog();
//            if(result!=null&&result.respStr.contains(Constants.Const_PhoneNumber)&&result.respStr.contains(Constants.Const_CELLULAR))
//            {
//                SingupModel.this.notifyObservers(result);
//            }
//            else
//            {
//                Util.showOkDialog(mContext, null, mContext.getResources().getString(R.string.thisNumberDoesNot));
//            }
//
//        }
//    }

}
