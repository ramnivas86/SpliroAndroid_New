package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.dao.LocationData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 8/18/2015.
 */
public class SelectLocationListModel extends BasicModel
{
private GetLocationListTask locationListTask;
    public void getLocationListFromServer()
    {
    if(locationListTask==null)
    {
        locationListTask=new GetLocationListTask();
        locationListTask.execute();
    }
    }


    class GetLocationListTask extends AsyncTask<Void,Void,NetworkResponse>
    {
        @Override
        protected void onPreExecute()
        {
            Util.showProDialog("Wait..");
        }

        @Override
        protected NetworkResponse doInBackground(Void... voids)
        {
            Cmd cmd= CmdFactory.createLocationListingCmd();
            NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
            if(response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA))
                {
                    parseLocationListResponse(response);
                }
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse response)
        {
            Util.dimissProDialog();
            locationListTask=null;
            if(response!=null)
            {
                notifyObservers(response);
            }

        }
    }

    private void parseLocationListResponse(NetworkResponse response)
    {
      try
    {
        JSONObject object=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
        if(object.has(Constants.FLD_KEY_DATA))
        {
            SpliroApp.default_locations.clear();
            JSONArray jsonArray=object.getJSONArray(Constants.FLD_KEY_DATA);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                LocationData locationData=null;
                locationData=new LocationData();
                locationData.user_location_id=jsonObject.getString(locationData.FLD_USER_LOCATION_ID);
                locationData.address=jsonObject.getString(locationData.FLD__ADDRESS);
                locationData.location_longitude=jsonObject.getDouble(locationData.FLD_LOCATION_LONGITUDE);
                locationData.location_latitude=jsonObject.getDouble(locationData.FLD_LOCATION_LATITUDE);
                locationData.city=jsonObject.getString(locationData.FLD_CITY);
                locationData.zipcode=jsonObject.getString(locationData.FLD__ZIPCODE);
                locationData.state=jsonObject.getString(locationData.FLD_STATE);
                locationData.created_at=jsonObject.getString(locationData.FLD_CREATED_AT);
                locationData.updated_at=jsonObject.getString(locationData.FLD_UPDATED_AT);
                SpliroApp.default_locations.add(locationData);
            }
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }

    }

}
