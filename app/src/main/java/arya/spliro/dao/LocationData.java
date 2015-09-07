package arya.spliro.dao;

import java.io.Serializable;

/**
 * Created by Admin on 7/31/2015.
 */
public class LocationData implements Serializable{



    public String FLD_LOCATION_LATITUDE="location_latitude";
    public String FLD_LOCATION_LONGITUDE="location_longitude";
    public String FLD__ADDRESS="address";
    public String FLD__ZIPCODE="zipcode";
    public String FLD_CITY="city";
    public String FLD_STATE="state";
    public String FLD_CREATED_AT="created_at";
    public String FLD_UPDATED_AT="updated_at";
    public String FLD_USER_LOCATION_ID="user_location_id";


    public double location_latitude;
    public double location_longitude;
    public String address="";
    public String zipcode="";
    public String is_deleted;
    public String is_default="0";
    public String city;
    public String state;
    public String country;
    public String updated_at;
    public String created_at;
    public String user_location_id;
}
