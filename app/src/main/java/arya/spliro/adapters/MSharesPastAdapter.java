package arya.spliro.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.dao.CreateData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by phoosaram on 8/27/2015
 */
public class MSharesPastAdapter extends BaseAdapter{


    private LayoutInflater inflater;
    private Context mcontext;
    private ArrayList<CreateData> shares_List;
    private String current_Time;

    public MSharesPastAdapter(Context mcontext, ArrayList<CreateData> shares_List,String current_Time) {
        this.mcontext = mcontext;
        inflater = LayoutInflater.from(mcontext);
        this.shares_List = shares_List;
        this.current_Time=current_Time;
    }

    @Override
    public int getCount() {
        return shares_List.size();
    }

    @Override
    public Object getItem(int position) {
        return shares_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MSPastViewHolder holder;
        if (convertView == null)
        {
            holder = new MSPastViewHolder();
            convertView = inflater.inflate(R.layout.ms_past_row, parent, false);
            holder.cImgSharesContainer=(RelativeLayout)convertView.findViewById(R.id.cImgSharesContainer);
            holder.relTxtSPTimeContainer=(RelativeLayout)convertView.findViewById(R.id.relTxtSPTimeContainer);
            holder.txtTitleMyshares=(TextView)convertView.findViewById(R.id.txtTitleMyshares);
            holder.cImgItemShare=(ImageView)convertView.findViewById(R.id.cImgItemShare);
            holder.rtShareOwner=(RatingBar)convertView.findViewById(R.id.rtShareOwner);
            holder.imgCreatorMyShares=(ImageView)convertView.findViewById(R.id.imgCreatorMyShares);
            holder.imgFavMShare=(ImageView)convertView.findViewById(R.id.imgFavMShare);
            holder.txtSharesName=(TextView)convertView.findViewById(R.id.txtSharesName);
            holder.txtAddFavMyShare=(TextView)convertView.findViewById(R.id.txtAddFavMyShare);
            holder.txtTimeLeftMShare=(TextView)convertView.findViewById(R.id.txtTimeLeftMShare);
            holder.txtDistanceLeft=(TextView)convertView.findViewById(R.id.txtDistanceLeft);
            holder.txtNoPplJoinedMShare=(TextView)convertView.findViewById(R.id.txtNoPplJoinedMShare);
            holder.txtRequestType=(TextView)convertView.findViewById(R.id.txtRequestType);
            convertView.setTag(holder);
        }
        else
        {
            holder = (MSPastViewHolder) convertView.getTag();

        }
        setTypeFaceToAllFields(holder);
        CreateData data=shares_List.get(position);
        Util.loadImage(mcontext,holder.imgCreatorMyShares,data.profile_pic_url,R.drawable.user);
        holder.txtTitleMyshares.setText(data.title);
        holder.txtSharesName.setText(data.display_name);
        holder.rtShareOwner.setRating(data.rate);
        holder.txtNoPplJoinedMShare.setText(Util.getSpannedString(""+data.no_people_joined,(int)mcontext.getResources().getDimension(R.dimen.joined_people_txt_size)).append(Constants.PEOPLE_JOINED));
        if(data.status.equals(Constants.SAVING_STATUS))
        {
            setShapeAndTextColor(holder);
            holder.txtTimeLeftMShare.setText(Util.getTimeLeft(data.post_expire_date,current_Time));
            holder.cImgSharesContainer.setVisibility(View.INVISIBLE);
            holder.relTxtSPTimeContainer.setVisibility(View.INVISIBLE);
            holder.txtNoPplJoinedMShare.setText(mcontext.getResources().getString(R.string.not_posted_yet_msg));
            holder.txtRequestType.setText(data.created_at);
        }
        else if(data.status.equals(Constants.CLOSED_STATUS))
        {
            Util.setColorToShape(holder.txtTimeLeftMShare,mcontext.getResources().getColor(R.color.peach_color));
            Util.setColorToShape(holder.txtDistanceLeft,mcontext.getResources().getColor(R.color.gray_light));
            Util.setColorToShape(holder.txtNoPplJoinedMShare,mcontext.getResources().getColor(R.color.gray_light));
            holder.txtTimeLeftMShare.setTextColor(mcontext.getResources().getColor(R.color.white));
            holder.txtDistanceLeft.setTextColor(mcontext.getResources().getColor(R.color.gray_dark));
            holder.txtNoPplJoinedMShare.setTextColor(mcontext.getResources().getColor(R.color.gray_dark));
            holder.txtTimeLeftMShare.setText(mcontext.getString(R.string.closed));

        }
        else
        {
            setShapeAndTextColor(holder);
            holder.txtTimeLeftMShare.setText(Util.getTimeLeft(data.post_expire_date,current_Time));
            holder.cImgSharesContainer.setVisibility(View.VISIBLE);
            holder.relTxtSPTimeContainer.setVisibility(View.VISIBLE);


        }
        if(data.user_id==(Config.getUserId()))
        {
            holder.txtDistanceLeft.setText(Util.getSpannedString("0",(int)mcontext.getResources().getDimension(R.dimen.spannable_txt_size)).append(Constants.DISTANCE_TEXT));
            holder.txtRequestType.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.txtDistanceLeft.setText(Util.getSpannedString(Util.getLocationDistance(data.location_data.location_latitude,data.location_data.location_longitude,SpliroApp.defaultLocation.location_latitude,SpliroApp.defaultLocation.location_longitude),(int)mcontext.getResources().getDimension(R.dimen.spannable_txt_size)).append(Constants.DISTANCE_TEXT));

            holder.txtRequestType.setVisibility(View.VISIBLE);
        }
        if(new File(data.categories_data.image_path).exists())
        {
//            holder.cImgItemShare.setImageBitmap(BitmapFactory.decodeFile(data.categories_data.image_path));
            Util.loadImageFromSDcard(mcontext,holder.cImgItemShare,data.categories_data.image_path,R.drawable.ic_launcher);
        }
        else
        {
            Util.loadImage(mcontext,holder.cImgItemShare,data.categories_data.pic_thumb_url,R.drawable.ic_launcher);
        }
        return convertView;
    }

    private void setShapeAndTextColor(MSPastViewHolder holder) {
        Util.setColorToShape(holder.txtTimeLeftMShare,mcontext.getResources().getColor(R.color.gray_light));
        Util.setColorToShape(holder.txtDistanceLeft,mcontext.getResources().getColor(R.color.gray_light));
        Util.setColorToShape(holder.txtNoPplJoinedMShare,mcontext.getResources().getColor(R.color.peach_color));
        holder.txtTimeLeftMShare.setTextColor(mcontext.getResources().getColor(R.color.black));
        holder.txtDistanceLeft.setTextColor(mcontext.getResources().getColor(R.color.black));
        holder.txtNoPplJoinedMShare.setTextColor(mcontext.getResources().getColor(R.color.white));

    }

    private void setTypeFaceToAllFields(MSPastViewHolder holder) {
        Typeface lightTTF= SpliroApp.getFontLight();
        Typeface semiboldTTF=SpliroApp.getFontSemiBold();
        holder.txtTitleMyshares.setTypeface(semiboldTTF);
        holder.txtSharesName.setTypeface(lightTTF);
        holder.txtAddFavMyShare.setTypeface(lightTTF);
        holder.txtDistanceLeft.setTypeface(semiboldTTF);
        holder.txtNoPplJoinedMShare.setTypeface(semiboldTTF);
        holder.txtRequestType.setTypeface(lightTTF);
        holder.txtTimeLeftMShare.setTypeface(semiboldTTF);
    }


    class MSPastViewHolder {
        RelativeLayout cImgSharesContainer;
        RelativeLayout relTxtSPTimeContainer;
        TextView txtTitleMyshares;
        TextView txtSharesName;
        TextView txtAddFavMyShare;
        TextView txtDistanceLeft;
        TextView txtNoPplJoinedMShare;
        TextView txtRequestType;
        TextView txtTimeLeftMShare;
        RatingBar rtShareOwner;
        ImageView cImgItemShare;
        ImageView imgCreatorMyShares;
        ImageView imgFavMShare;
    }
}
