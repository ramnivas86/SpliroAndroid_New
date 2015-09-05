package arya.spliro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arya.lib.logger.Logger;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.dao.LocationData;
import arya.spliro.viewController.SelectLocationActivity;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by phoosaram on 8/17/2015.
 */
public class SelectLocationListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<LocationData> location_list;
    private boolean flag;//by aditi 31 Aug 2015
    public SelectLocationListAdapter(Context context,ArrayList<LocationData> location_list)
    {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.location_list=location_list;
    }
    @Override
    public int getCount()
    {
        return location_list.size();
    }

    @Override
    public Object getItem(int position) {
        return location_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowViewHolder holder;
        try
        {
            if(convertView==null)
            {
                holder=new RowViewHolder();
                convertView = inflater.inflate(R.layout.brows_screen_location_row, parent, false);
                holder.txt_loc_address=(TextView)convertView.findViewById(R.id.txt_loc_address);
                convertView.setTag(holder);
            }
            else
            {
                holder = (RowViewHolder) convertView.getTag();
            }
            holder.txt_loc_address.setTypeface(SpliroApp.getFontLight());
            LocationData data=location_list.get(position);
            //{by aditi 31 Aug 2015
           /* if(data.is_default.equals("1"))
            {
                holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
            }
            else
            {
                holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.white));
            }*/

            for(LocationData loc:location_list){
                if(loc.is_default.equals("1")){
                    flag=true;
                }
            }
            if(flag) {
                if (data.is_default.equals("1") ) {
                    holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
                } else {
                    holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }else{
                if(data.address.equals(SpliroApp.defaultLocation.address)){
                    holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.sky_blue));
                }else{
                    holder.txt_loc_address.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }
            //}ends here
            holder.txt_loc_address.setText(data.address);
        }catch(Exception e)
        {
            e.printStackTrace();
            if(Logger.IS_DEBUG)
            {

            }
        }


        return convertView;
    }
    class RowViewHolder
    {
        public TextView txt_loc_address;
    }

}
