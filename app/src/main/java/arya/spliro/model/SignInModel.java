package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;
import com.facebook.android.FBUtil;
import com.facebook.android.Facebook;

import org.json.JSONObject;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.config.Config;
import arya.spliro.dao.SignupData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Ramnivas Singh on 27-07-2015.
 */
public class SignInModel extends BasicModel {

    String loginId;
    String countryCode;
    String pwd;
    SignInTask task;
    public String login_type="";
    public GetFacebookLoggedinAsync getFacebookLoggedinAsync;
    public void doLogin(String phone,String countryCode, String pwd,String login_type) {
if(task==null) {
    String[] requestParams={phone, countryCode, pwd,login_type};
    task = new SignInTask();
    task.execute(requestParams);
}
    }
    public class SignInTask extends AsyncTask<String, String, NetworkResponse> {


        @Override
        protected NetworkResponse doInBackground(String... params) {
            String loginId = (String) params[0];
            String countryCode = (String) params[1];
            String pwd=(String) params[2];
            String login_type=(String)params[3];
            Cmd cmd = CmdFactory.createSignInCmd(loginId,countryCode, pwd,login_type);
            NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, "data", cmd.toJSONString());
            if (response != null) {
                if (response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                    JSONObject dataObj=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
                    if(dataObj!=null&&dataObj.has(SignupData.KEY_USER_ID))
                    {
                        Config.saveUserDetail(dataObj,null);
                    }
                }

            }

            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse networkResponse) {
            Util.dimissProDialog();
            task=null;
            SignInModel.this.notifyObservers(networkResponse);
        }
    }
    public void getFacebookInfo(Facebook fbobj)
    {
        if(getFacebookLoggedinAsync==null)
        {
            getFacebookLoggedinAsync=new GetFacebookLoggedinAsync();
        }
        getFacebookLoggedinAsync.execute(fbobj);

    }

    public class GetFacebookLoggedinAsync extends AsyncTask<Facebook,Void,JSONObject>
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
            getFacebookLoggedinAsync=null;
            if(Result!=null)
            {

                SignInModel.this.notifyObservers(Result);
            }
        }
    }

}
