package arya.spliro.model;

import android.location.Address;
import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.init.Env;
import com.arya.lib.logger.Logger;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.JSONParser;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.dao.LocationData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.LocationMgr;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 8/18/2015.
 */
public class AddLocationModel extends BasicModel {

    private LocationTask locationTask;
    private LocationData locationData;
    private DeleteLocationTask deleteLocationTask;
    private LocationData deleted_location_object;



    public void getLatLongFromAddress(String address)
    {


        if(locationTask==null)
        {
            locationTask=new LocationTask();
            locationTask.execute(address);
        }


    }

    class LocationTask extends AsyncTask<String,Void,LocationData>
    {
        @Override
        protected void onPreExecute() {
            Util.showProDialog("Wait..");
        }

        @Override
        protected LocationData doInBackground(String... strings)
        {
            locationData=new LocationData();
            locationData.address=strings[0];
            JSONParser jParser = new JSONParser();
            try {
                JSONArray jsonObject = jParser.getLatLongFromGivenAddress(strings[0]).getJSONArray("results");
                if(jsonObject.length()>0)
                {
                    JSONObject object_to_parse=jsonObject.getJSONObject(0);
                    locationData.location_longitude = object_to_parse.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    locationData.location_latitude = object_to_parse.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    JSONArray components_array=object_to_parse.getJSONArray("address_components");
                    for(int i=0;i<components_array.length();i++)
                    {
                        JSONObject obj=components_array.getJSONObject(i);
                        if(obj.has("types"))
                        {
                           JSONArray array=obj.getJSONArray("types");
                            {
                                if(array.getString(0).equals("administrative_area_level_1"))
                                {
                                 locationData.state=obj.getString("long_name");
                                }
                                if(array.getString(0).equals("administrative_area_level_2"))
                                {
                                    locationData.city=obj.getString("long_name");
                                }
                                if(array.getString(0).equals("postal_code"))
                                {
                                    locationData.zipcode=obj.getString("long_name");
                                }
                            }
                        }

                    }

                }
                if(locationData.zipcode!=null&&!locationData.zipcode.isEmpty())
                {
                    Cmd cmd= CmdFactory.createAddLocationCmd();
                    cmd=getCommandToCreateLocation(cmd,locationData);
                    NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
                    locationData= parseLocationListResponse(response,locationData);
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
                if(Logger.IS_DEBUG)
                {

                }

            }
            return locationData;
        }

        @Override
        protected void onPostExecute(LocationData result) {
            Util.dimissProDialog();
            locationTask=null;
            notifyObservers(result);
        }
    }
    private LocationData parseLocationListResponse(NetworkResponse response,LocationData location_data)
    {
        try
        {
            if(response!=null&&response.isSuccess())
            {
                JSONObject object=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
                if(object.has(Constants.FLD_KEY_DATA))
                {
                    JSONArray jsonArray=object.getJSONArray(Constants.FLD_KEY_DATA);
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        location_data.user_location_id=jsonObject.getString(location_data.FLD_USER_LOCATION_ID);
                        location_data.address=jsonObject.getString(location_data.FLD__ADDRESS);
                        location_data.location_longitude=jsonObject.getDouble(location_data.FLD_LOCATION_LONGITUDE);
                        location_data.location_latitude=jsonObject.getDouble(location_data.FLD_LOCATION_LATITUDE);
                        location_data.city=jsonObject.getString(location_data.FLD_CITY);
                        location_data.zipcode=jsonObject.getString(location_data.FLD__ZIPCODE);
//                        location_data.state=jsonObject.getString(location_data.FLD_STATE);
//                        location_data.created_at=jsonObject.getString(location_data.FLD_CREATED_AT);
//                        location_data.updated_at=jsonObject.getString(location_data.FLD_UPDATED_AT);
                    }
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return location_data;
    }

    private Cmd getCommandToCreateLocation(Cmd cmd,LocationData locationData)
    {
        cmd.addData(locationData.FLD__ADDRESS,locationData.address);
        cmd.addData(locationData.FLD__ZIPCODE,locationData.zipcode);
        cmd.addData(locationData.FLD_CITY,locationData.city);
        cmd.addData(locationData.FLD_STATE,locationData.state);
        cmd.addData(locationData.FLD_LOCATION_LATITUDE,locationData.location_latitude);
        cmd.addData(locationData.FLD_LOCATION_LONGITUDE,locationData.location_longitude);
        return cmd;
    }


    public void deleteLocation(LocationData locationData)
    {
        this.deleted_location_object=locationData;
       Cmd cmd=CmdFactory.createDeleteLocationCmd();
       cmd.addData(locationData.FLD_USER_LOCATION_ID,locationData.user_location_id);
        if(Util.isDeviceOnline(true))
        {
            if(deleteLocationTask==null)
            {
                deleteLocationTask=new DeleteLocationTask();
                deleteLocationTask.execute(cmd);

            }
        }


    }
    class DeleteLocationTask extends AsyncTask<Cmd,Void,NetworkResponse>
    {
        @Override
        protected void onPreExecute() {
            Util.showProDialog("Wait..");
        }

        @Override
        protected NetworkResponse doInBackground(Cmd... cmds) {
            NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmds[0].toJSONString());
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse response) {
            Util.dimissProDialog();
            deleteLocationTask=null;
            if(response!=null&&response.isSuccess())
            {
                SpliroApp.default_locations.remove(deleted_location_object);
                notifyObservers(response);
            }
            else
            {
                Util.showOkDialog(null,response.getMessageFromServer());
            }
        }
    }
}
