package arya.spliro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.dao.LocationData;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 7/30/2015.
 */
public class LocationAdapter extends BaseAdapter{


    private LayoutInflater inflate;
    private Context mcontext;
    private ArrayList<LocationData> locationArrList;
    private View.OnClickListener clickListener;

    public LocationAdapter(Context mcontext, ArrayList<LocationData> locationArrList,View.OnClickListener clickListener) {
        this.mcontext = mcontext;
        inflate = LayoutInflater.from(mcontext);
        this.locationArrList = locationArrList;
        this.clickListener=clickListener;
    }

    @Override
    public int getCount() {
        return locationArrList.size();
    }

    @Override
    public Object getItem(int position) {
        return locationArrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MSPastViewHolder holder;
        if (convertView == null)
        {
            holder = new MSPastViewHolder();
            convertView = inflate.inflate(R.layout.location_row, parent, false);
            holder.img_add_loc=(ImageView)convertView.findViewById(R.id.img_add_loc);
            holder.eTxtLocationR=(EditText)convertView.findViewById(R.id.eTxtLocationR);
            convertView.setTag(holder);

        }
        else
        {
            holder = (MSPastViewHolder) convertView.getTag();
        }
        holder.  img_add_loc.setImageDrawable(mcontext.getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
        holder.img_add_loc.setColorFilter(mcontext.getResources().getColor(R.color.peach_color));
        holder.img_add_loc.setOnClickListener(clickListener);
        LocationData data=locationArrList.get(position);
        holder.eTxtLocationR.setTypeface(SpliroApp.getFontLight());
        holder.eTxtLocationR.setText(data.address);
        holder.eTxtLocationR.setEnabled(false);
        return convertView;
    }



    class MSPastViewHolder
    {
        private ImageView img_add_loc;
        private EditText eTxtLocationR;

    }
}
