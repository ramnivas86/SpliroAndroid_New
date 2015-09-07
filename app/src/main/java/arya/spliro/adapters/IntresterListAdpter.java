package arya.spliro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.dao.InviteeData;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by phoosaram on 9/7/2015.
 */
public class IntresterListAdpter extends BaseAdapter {
    private LayoutInflater inflater;
    public Context mContext;
    private ArrayList<InviteeData> intrestersList;


    public IntresterListAdpter(Context context,ArrayList<InviteeData> intrestersList)
    {
       this.mContext=context;
       this.intrestersList=intrestersList;
       inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return intrestersList.size();
    }

    @Override
    public Object getItem(int position) {
        return intrestersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        InviteListHolder holder;
        if(convertView==null)
        {
            holder=new InviteListHolder();
            convertView = inflater.inflate(R.layout.my_share_current_list_row, parent, false);
            holder.imgInviteeImgSharePreview=(ImageView)convertView.findViewById(R.id.imgInviteeImgSharePreview);
            holder.txtInviteeNameSharePreview=(TextView)convertView.findViewById(R.id.txtInviteeNameSharePreview);
            holder.ratingInviteeSPList=(RatingBar)convertView.findViewById(R.id.ratingInviteeSPList);
            convertView.setTag(holder);
        }
        else
        {
            holder=(InviteListHolder)convertView.getTag();
        }
        setTypeFaceToAllFields(holder);
        InviteeData data=intrestersList.get(position);
        holder.txtInviteeNameSharePreview.setText(data.display_name);
        if(data.profile_pic_url!=null&&!data.profile_pic_url.isEmpty())
        {
            Util.loadImage(mContext,holder.imgInviteeImgSharePreview,data.profile_pic_url,R.drawable.user);
        }
        if(data.rate!=null)
        {
            holder.ratingInviteeSPList.setRating(data.rate);
        }
        return convertView;
    }

    private void setTypeFaceToAllFields(InviteListHolder mholder)
    {
        mholder.txtInviteeNameSharePreview.setTypeface(SpliroApp.getFontLight());

    }


    class InviteListHolder
    {
        ImageView imgInviteeImgSharePreview;
        TextView txtInviteeNameSharePreview;
        RatingBar ratingInviteeSPList;

    }
}
