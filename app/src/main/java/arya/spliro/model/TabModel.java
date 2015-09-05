package arya.spliro.model;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import arya.spliro.utility.LocationMgr;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Phoosaram on 8/13/2015.
 */
public class TabModel extends BasicModel {
    private Location currentLocation;
    private LocationMgr locationMgr;
    private SetLocationTask setLocationTask;



 public void setCurrentLocation(Context ctx)
    {
        locationMgr=LocationMgr.getInstance(ctx);
        currentLocation=locationMgr.getCurrentLocation();
        if(Util.isDeviceOnline(true))
        {
            if(currentLocation!=null)
            {
                if(setLocationTask==null)
                {
                    setLocationTask=new SetLocationTask();
                    setLocationTask.execute(currentLocation);

                }
            }
        }


    }
    class SetLocationTask extends AsyncTask<Location,Void,Boolean>
    {
        @Override
        protected void onPreExecute()
        {
            Util.showProDialog("Wait..");
        }

        @Override
        protected Boolean doInBackground(Location... locations)
        {
            JSONParser parser=new JSONParser();
            JSONObject resultObj=parser.getJSONFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + locations[0].getLatitude()+ ","+ locations[0].getLongitude()+ "&sensor=true");
            SpliroApp.defaultLocation.location_longitude=locations[0].getLongitude();
            SpliroApp.defaultLocation.location_latitude=locations[0].getLatitude();
            boolean responese=parseJsonToLocatinObject(resultObj);
            if(responese==true)
            {

            }
            return responese;
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            Util.dimissProDialog();
            setLocationTask=null;


        }
    }

    private boolean parseJsonToLocatinObject(JSONObject obj)
    {
        boolean is_parse_successfully=false;

            try
            {
               JSONArray array=obj.getJSONArray("results");
                JSONObject object=array.getJSONObject(0);
                SpliroApp.defaultLocation.address=object.getString("formatted_address");
                JSONArray address_information=object.getJSONArray("address_components");
                for(int i=0;i<address_information.length();i++)
                {
                   JSONObject component_address=address_information.getJSONObject(i);
                   if(component_address.has("types"))
                   {
                       JSONArray array_obj=component_address.getJSONArray("types");
                       {
                           if(array_obj.getString(0).equals("administrative_area_level_1"))
                           {
                               SpliroApp.defaultLocation.state=component_address.getString("long_name");
                           }
                           if(array_obj.getString(0).equals("administrative_area_level_2"))
                           {
                               SpliroApp.defaultLocation.city=component_address.getString("long_name");
                           }
                           if(array_obj.getString(0).equals("postal_code"))
                           {
                               SpliroApp.defaultLocation.zipcode=component_address.getString("long_name");
                           }
                       }
                   }
                }
                is_parse_successfully=true;
            }catch(Exception e)
            {
                e.printStackTrace();
            }

    return is_parse_successfully;
    }



}
