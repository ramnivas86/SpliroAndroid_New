package arya.spliro.cmds;



import com.arya.lib.cmds.BaseCmd;
import com.arya.lib.cmds.Cmd;

import org.json.JSONException;

import arya.spliro.config.Config;
import arya.spliro.config.DeviceInfo;
import arya.spliro.utility.Constants;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Ramnivas Singh on 8/6/2015.
 */
public class CmdPreSignIn extends Cmd {
    public CmdPreSignIn(String cmd) {
        super(cmd);
        try {
            if(!Config.getUserToken().isEmpty())
            {
                this.data.put(Constants.FLD_USER_TOKEN, Config.getUserToken());
                this.data.put(Constants.FLD_USER_DEVICE_ID,  Config.getUserDeviceId());
            }
            this.data.put(Constants.FLD_LANG_CODE,  Config.getLangCode());
            this.data.put(Constants.FLD_SOURCE_APP, Constants.SOURCE_APP);
            this.data.put(DeviceInfo.FLD_SIM_SERIAL_NUMBER, Config.deviceInfo.getSimSerialNumber());
            this.data.put(Constants.FLD_REMOTE_DEVICE_ID,Config.deviceInfo.getDeviceMacAddress());
            this.data.put(DeviceInfo.FLD_DEVICE_MAC_ADDRESS, Config.deviceInfo.getDeviceMacAddress());
            this.data.put(DeviceInfo.FLD_EMEI_MEID_ESN, Config.deviceInfo.getEmeiMeidEsn());
            this.data.put(DeviceInfo.FLD_APP_TOKEN, Config.deviceInfo.getAppToken());
            this.data.put(DeviceInfo.FLD_DEVICE_ID, Config.deviceInfo.getDeviceId());
            this.data.put(DeviceInfo.FLD_APP_VERSION, Config.deviceInfo.getAppVersion());
            this.data.put(DeviceInfo.FLD_DEVICE_MODEL, Config.deviceInfo.getDeviceModel());
            this.data.put(DeviceInfo.FLD_DEVICE_DENSITY, Config.deviceInfo.getDeviceDensity());
            this.data.put(DeviceInfo.FLD_DEVICE_RESOLUTION, Config.deviceInfo.getDeviceResolution());
            this.data.put(DeviceInfo.FLD_OS_TYPE, Config.deviceInfo.getOsType());
            this.data.put(DeviceInfo.FLD_OS_VERSION, Config.deviceInfo.getOsVersion());
            this.data.put(DeviceInfo.FLD_OS_BUILD_NUMBER, Config.deviceInfo.getOsBuildNumber());
            this.data.put(DeviceInfo.FLD_SIM_OPERATOR_NAME, Config.deviceInfo.getSimOperatorName());
            this.data.put(DeviceInfo.FLD_SIM_OPERATOR, Config.deviceInfo.getSimOperator());
            this.data.put(DeviceInfo.FLD_SIM_NETWORK_OPERATOR, Config.deviceInfo.getSimNetworkOperator());
            this.data.put(DeviceInfo.FLD_SIM_COUNTRY_ISO, Config.deviceInfo.getSimCountryIso());
            this.data.put(DeviceInfo.FLD_CURRENT_APP_CLIENT_VERSION, Config.deviceInfo.getCurrAppClientVersion());
            this.data.put(DeviceInfo.FLD_CLOUD_KEY_TO_NOTIFY, Config.deviceInfo.getCloudKeyToNotify());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
