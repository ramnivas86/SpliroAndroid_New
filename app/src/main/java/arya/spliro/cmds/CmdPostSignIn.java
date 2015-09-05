package arya.spliro.cmds;

import org.json.JSONException;

import arya.spliro.config.Config;
import arya.spliro.utility.Constants;

/**
 * Created by Admin on 8/6/2015.
 */
public class CmdPostSignIn extends  CmdPreSignIn{
    public CmdPostSignIn(String cmd) {
        super(cmd);
        try {
            this.data.put(Constants.FLD_USER_ID, Config.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
