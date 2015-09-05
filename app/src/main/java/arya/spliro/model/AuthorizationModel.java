package arya.spliro.model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;

import com.arya.lib.cmds.Cmd;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONException;
import org.json.JSONObject;

import arya.spliro.R;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.config.Config;
import arya.spliro.dao.SignupData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/6/2015.
 */
public class AuthorizationModel extends BasicModel {
    public Task task;
    private String authCode;
    private String regId;
    private boolean resendPinFlag;
    private String phone;
    private String emailId;

    public void doVarification(String regId,String authCode,boolean resendPinFlag)
   {
       this.resendPinFlag=resendPinFlag;
       this.authCode=authCode;
       this.regId=regId;
           if (task == null) {
               task = new Task();
               task.execute();
           }
   }
    public void doResendPinCode(String regId,String phone,String emailId,boolean resendPinFlag)
    {
        this.resendPinFlag=resendPinFlag;
        this.phone=phone;
        this.regId=regId;
        this.emailId=emailId;
        if (task == null) {
            task = new Task();
            task.execute();
        }
    }
    public class Task extends AsyncTask<String, String, NetworkResponse> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected NetworkResponse doInBackground(String... params) {
            Cmd cmd;
            if(resendPinFlag) {
                cmd=CmdFactory.createResendPinCodeCmd(regId,phone,emailId);
            }
            else
            {
                cmd = CmdFactory.createAuthCodeCmd(regId,authCode);
            }
            NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL,Constants.FLD_KEY_DATA, cmd.toJSONString());
            if (response != null) {
                if (response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                    JSONObject dataObj=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
                    if(dataObj!=null&&dataObj.has(SignupData.KEY_USER_ID))
                    {
                       Config. saveUserDetail(dataObj,authCode);
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse networkResponse) {
            Util.dimissProDialog();
            AuthorizationModel.this.notifyObservers(networkResponse);
            task=null;
        }
    }
}
