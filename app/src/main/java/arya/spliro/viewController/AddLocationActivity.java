package arya.spliro.viewController;

import android.app.Dialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arya.lib.logger.Logger;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.JSONParser;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.adapters.LocationAdapter;
import arya.spliro.config.Config;
import arya.spliro.dao.LocationData;
import arya.spliro.model.AddLocationModel;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

public class AddLocationActivity extends AbstractFragmentActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{
    private static final LatLng latLng = new LatLng(7.0722, 125.6131);
    private AppHeaderView appHeaderView;
    private GoogleMap map;
    private EditText eTxtLocationR;
    private ListView lvPast;
    private LocationAdapter adapter;
    private ImageView img_add_loc;
    private ArrayList<LocationData> loaction_list=new ArrayList<LocationData>();
    float zoomLevel=19.0f;
    private AddLocationModel addLocationModel=new AddLocationModel();
    private Dialog dialog;
    private HashMap<Marker, LocationData> markerHasMap;

    @Override
    protected void onCreatePost(Bundle savedInstanceState)
    {
        setContentView(R.layout.act_location);
        init();
    }

    @Override
    protected BasicModel getModel() {
        return addLocationModel;
    }

    private void init()
    {
        appHeaderView=(AppHeaderView)findViewById(R.id.appHeaderView);
        eTxtLocationR=(EditText)findViewById(R.id.eTxtLocationR);
        img_add_loc=(ImageView)findViewById(R.id.img_add_loc);
        img_add_loc.setImageDrawable(getResources().getDrawable(R.drawable.spl_ic_circle_p));
        img_add_loc.setOnClickListener(this);
        img_add_loc.setColorFilter(getResources().getColor(R.color.green));
        lvPast=(ListView)findViewById(R.id.lvPast);
        appHeaderView.setHeaderContent(getResources().getDrawable(R.drawable.spl_ic_back_w_l), getResources().getString(R.string.location),null,  getResources().getColor(android.R.color.transparent));
        setLocationList();
      if(Util.isGoogleMapsInstalled(this)) {
          setUpMapIfNeeded();
          map.setOnMyLocationChangeListener(myLocationChangeListener);
          drawMarkers(SpliroApp.default_locations);

      }
        else
      {
         this.findViewById(R.id.frameMapContainer).setVisibility(View.GONE);
      }
        eTxtLocationR.setTypeface(SpliroApp.getFontLight());
    }
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null)
        {
            // Try to obtain the map from the SupportMapFragment.
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null)
            {
                setUpMap();
            }
        }
    }

    private void setUpMap()
    {

        map.setMyLocationEnabled(true);
      Location currentLoc=  map.getMyLocation();
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()), zoomLevel));
//        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }
    @Override
    public void onClick(View view)
    {
        int vId=view.getId();
        if(vId==R.id.imgBackAppH)
        {
            onBackPressed();
        }
        else if(vId==R.id.img_add_loc)
        {
            String str=eTxtLocationR.getText().toString().trim();
            if(!str.isEmpty())
            {
                if(Util.isDeviceOnline(true))
                {
                    if(SpliroApp.default_locations.size()>= Constants.MAX_LENGTH_LOCATION_LIST)
                    {
                        Util.showOkDialog(null,getResources().getString(R.string.more_than_list_location));
                    }
                    else
                    {
                        addLocationModel.getLatLongFromAddress(str);
                    }
                }

            }

        }

    }

    private String tag="spliro";
    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location)
        {

        }
    };

    private void setAnimationOnMarker(double latitude, double longitude) {
        LatLng latLong = new LatLng(latitude, longitude);

            if(map != null)
            {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoomLevel));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,zoomLevel ));
            }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.endActAnimation(AddLocationActivity.this);
    }
    private void goToSearchLocations()
    {
        try
        {
            String str  = eTxtLocationR.getText().toString().trim().replaceAll(" ", ",");
            if(!str.isEmpty())
            {
                String[] searchArr =str.split(",");
                String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ searchArr[0] + "&sensor=true&key=" + Config.GPLACE_API_KEY;
                if(!searchArr[0].isEmpty())
                {
                    loadLocationData(url);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            if(Logger.IS_DEBUG)
            {

            }
        }

    }
    public void loadLocationData(String url)
    {
        new AsyncTask<String,Void,Void>()
        {
            @Override
            protected void onPreExecute() {
                Util.showProDialog("Wait..");
            }
            @Override
            protected Void doInBackground(String[] strings) {
                try {
                    JSONParser parser=new JSONParser();
                    JSONObject jobj=parser.getJSONFromUrl(strings[0]);
                    if(jobj!=null)
                    {
                        JSONArray address = jobj.getJSONArray("predictions");
                            for (int i = 0; i < address.length(); i++)
                            {
                                JSONObject c = address.getJSONObject(i);
                                LocationData location = getLatLngFromAddress(c.getString("description"));
                                SpliroApp.default_locations.add(location);
                            }
                    }
                }

                catch (Exception e)
                {
                  e.printStackTrace();
                    if(Logger.IS_DEBUG)
                    {

                    }

                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                setLocationList();
                Util.dimissProDialog();
            }
        }.execute(url);
    }

    private LocationData getLatLngFromAddress(String address)
    {
        LocationData location = new LocationData();
        location.address = address;
        location.is_default = "0";
        JSONParser jParser = new JSONParser();
            try {
                JSONArray jsonObject = jParser.getLatLongFromGivenAddress(address).getJSONArray("results");
                if(jsonObject.length()>0)
                {
                    location.location_longitude = jsonObject.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    location.location_latitude = jsonObject.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                }
        }
        catch (JSONException e) {
            e.printStackTrace();
            if(Logger.IS_DEBUG)
            {

            }

        }

        return location;
    }
    private void setLocationList()
    {
        adapter = new LocationAdapter(AddLocationActivity.this, SpliroApp.default_locations, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = lvPast.getPositionForView((View) view.getParent());
                dialog=   Util.showYesNoMessageDialog(AddLocationActivity.this,getString(R.string.do_you_want_to_delete_location),getString(R.string.app_name), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        LocationData loc=SpliroApp.default_locations.get(position);
                        addLocationModel.deleteLocation(loc);

                    }
                },null,null,null);


            }
        });
        lvPast.setAdapter(adapter);
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if(o instanceof LocationData)
        {
            LocationData loc=(LocationData)o;
            if(loc.location_latitude==0||loc.zipcode==null)
            {
               Util.showOkDialog(null,getResources().getString(R.string.msg_valid_location));
            }
            else
            {
                SpliroApp.default_locations.add(0,loc);
                drawMarkers(SpliroApp.default_locations);
                eTxtLocationR.setText("");
            }
            adapter.notifyDataSetChanged();
        }
        else if(o instanceof NetworkResponse)
        {
            drawMarkers(SpliroApp.default_locations);
            adapter.notifyDataSetChanged();
        }
    }
    // ----------Method for add Markers on route point-------
    public void drawMarkers(ArrayList<LocationData> userLocationList) {
        map.clear();
        if(!userLocationList.isEmpty()) {
            Marker marker;
            markerHasMap = new HashMap<Marker, LocationData>();
            for (int i = 0; i < 1; i++) {
                if (userLocationList.get(i).location_latitude != 0) {
                    double lati = userLocationList.get(i)
                            .location_latitude;
                    double longi = userLocationList.get(i)
                            .location_longitude;
                    LatLng src = new LatLng(lati, longi);
                    if (i == (userLocationList.size() - 1)) {
                        marker = map
                                .addMarker(new MarkerOptions()
                                        .position(src)
                                        .icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    } else {
                        marker = map
                                .addMarker(new MarkerOptions().position(src).icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    markerHasMap.put(marker, userLocationList.get(i));
                }
            }
            setAnimationOnMarker(userLocationList.get(0).location_latitude, userLocationList.get(0).location_longitude);
            setMarkerInfo();
        }
        else
        {
            if(SpliroApp.defaultLocation!=null) {
                setAnimationOnMarker(SpliroApp.defaultLocation.location_latitude, SpliroApp.defaultLocation.location_longitude);
            }
        }
    }

    private  void setMarkerInfo()
    {
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LocationData locationData = markerHasMap.get(marker);
                String address = locationData.address;
                View v = AddLocationActivity.this.getLayoutInflater().inflate( R.layout.marker_info_window, null, false);
                TextView txtTitleMI = (TextView) v.findViewById(R.id.txtTitleMI);
                TextView txtAddressMI = (TextView) v.findViewById(R.id.txtAddressMI);
                txtTitleMI.setTypeface(SpliroApp.getFontSemiBold());
                txtAddressMI.setTypeface(SpliroApp.getFontLight());
                if (address != null &&! address.isEmpty()) {
                    txtAddressMI.setText(address);
                }
                return v;
            }
            @Override
            public View getInfoContents(Marker marker) {
                // TODO Auto-generated method stub

                return null;
            }
        });
    }
}
