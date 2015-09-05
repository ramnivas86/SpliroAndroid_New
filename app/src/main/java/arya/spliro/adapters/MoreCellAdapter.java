package arya.spliro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import arya.spliro.R;
import arya.spliro.dao.MoreData;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 7/24/2015.
 */
public class MoreCellAdapter extends BaseAdapter  {

    private LayoutInflater inflate;
    private Context mcontext;

    public MoreCellAdapter() {
        super();
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    private ArrayList<MoreData
            > moreImageList;

    public MoreCellAdapter(Context mcontext, ArrayList<MoreData> moreImageList) {
        this.mcontext = mcontext;
        inflate = LayoutInflater.from(mcontext);
        this.moreImageList = moreImageList;
    }

    @Override
    public int getCount() {
        return moreImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return moreImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoreViewHolder holder;
        if (convertView == null) {
            holder = new MoreViewHolder();
            convertView = inflate.inflate(R.layout.frg_more_cell, parent, false);
            holder.imgCommon=(ImageView)convertView.findViewById(R.id.imgCommon);
            holder.btnGoToNext=(Button)convertView.findViewById(R.id.btnGoToNext);
            holder.txtCommon = (TextView) convertView.findViewById(R.id.txtCommon);//by aditi 28 Aug 2015
            convertView.setTag(holder);
        } else {
            holder = (MoreViewHolder) convertView.getTag();
        }
        holder.txtCommon.setTypeface(SpliroApp.getFontLight());//by aditi 28 Aug 2015
        holder.btnGoToNext.setText(moreImageList.get(position).getImgName());
        int imageId=mcontext.getResources().getIdentifier(moreImageList.get(position).getImgId(),"drawable",mcontext.getPackageName());
        holder.imgCommon.setImageResource(imageId);
        //{by aditi 28 Aug 2015
        if (moreImageList.get(position).getImgName().equals("Location") && moreImageList.get(position).locationData != null) {
            holder.txtCommon.setText(moreImageList.get(position).locationData.address);
            holder.txtCommon.setVisibility(View.VISIBLE);
            holder.imgCommon.setVisibility(View.GONE);
        } else {
            holder.txtCommon.setVisibility(View.GONE);
            holder.imgCommon.setVisibility(View.VISIBLE);
        }
        //}ends here
        return convertView;
    }



    class MoreViewHolder {
        private ImageView imgCommon;
        private Button btnGoToNext;
        private TextView txtCommon;//by aditi 28 Aug 2015
    }
}