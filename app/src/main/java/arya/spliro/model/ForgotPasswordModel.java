package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONObject;

import arya.spliro.cmds.CmdConstants;
import arya.spliro.cmds.CmdFactory;
import arya.spliro.config.Config;
import arya.spliro.dao.SignupData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/11/2015.
 */
public class ForgotPasswordModel  extends BasicModel
{
    private ForgotTask task;

    public void doForgotPass( String[] requestParams) {
        if (task == null) {
            task = new ForgotTask();
            task.execute(requestParams);
        }
    }
        public class ForgotTask extends AsyncTask<String, String, NetworkResponse> {


            @Override
            protected NetworkResponse doInBackground(String... params) {
                Cmd cmd=null;
                String callApi = (String) params[0];
                if(callApi.equals(CmdConstants.FORGOT_PASSWORD_SEND_OTP_USER)) {
                    String phoneOrEmail = (String) params[1];
                     cmd = CmdFactory.createForgotPassCmd(phoneOrEmail);
                }
             else if(callApi.equals(CmdConstants.FORGOT_PASSWORD_VERIFY_OTP_USER)) {
                    cmd = CmdFactory.createChangePassCmd(params);
                }
                NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, "data", cmd.toJSONString());
                return response;
            }

            @Override
            protected void onPostExecute(NetworkResponse networkResponse) {
                Util.dimissProDialog();
                task=null;
                ForgotPasswordModel.this.notifyObservers(networkResponse);
            }
        }

}
