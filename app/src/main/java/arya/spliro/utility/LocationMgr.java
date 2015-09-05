package arya.spliro.utility;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import arya.spliro.config.Config;

/**
 * Created by phoosaram on 8/13/2015.
 */
public class LocationMgr implements LocationListener{
    private static Context context;
    private LocationManager locationManager;

    private static LocationMgr instance;
    public String currentAddress="";
    public String zip_code="";
    public LocationMgr()
    {

    }

    public static LocationMgr getInstance(Context ctx)
    {
        context = ctx;
        if(instance==null)
        {
            instance=new LocationMgr();
        }
        return instance;
    }

    public Location getCurrentLocation()
    {
        Location currentLocation=null;
        try
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnable)
            {
                Util.showOkDialog(null,"Please Enable Location service");
            }
            else
            {
                if (isGPSEnabled ||isNetworkEnable)
                {
                    if (currentLocation == null)
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
                        if (locationManager != null)
                        {
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (currentLocation == null && isNetworkEnable)
                            {
                                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }

                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return currentLocation;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        getAddressFromLocation(context,location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public String getAddressFromLatLong(final Location loc)
    {
        String address="";
        try
        {
            JSONObject jsonObj=Util.getJsonFromUrl("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + loc.getLatitude()+ ","+ loc.getLongitude()+ "&sensor=true");
            if(jsonObj!=null)
            {
                String status=jsonObj.getString("status");
                if(jsonObj.getString("status").equalsIgnoreCase("OK"))
                {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    address=currentAddress=zero.getString("formatted_address");
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return address;
    }

    public String getAddressFromLocation(Context ctx,Location loc)
    {
        StringBuffer  address=new StringBuffer();
        String postel_code="";
        try
        {
            Geocoder gc = new Geocoder(ctx, Locale.getDefault());
            List<Address> lstAdd = gc.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
            Address ad = lstAdd.get(0);
            for(int i=0;i<=ad.getMaxAddressLineIndex();i++)
            {
                String s=ad.getAddressLine(i);
                address.append(s);
                if(s.contains(ad.getCountryName()))
                {
                    postel_code=zip_code=ad.getAddressLine(i-1).replaceAll("\\D","");
                }
            }
            Config.setCurrentLocationZipCode(postel_code);
            Config.setCurrntLocation(address.toString());
            Config.savePreferencese();


        }
        catch(Exception e)
        {
            e.printStackTrace();

        }
        return address.toString();
    }
}
