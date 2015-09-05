package arya.spliro.adapters;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import java.util.ArrayList;

        import arya.spliro.R;
        import arya.spliro.dao.InviteeData;
        import arya.spliro.dao.MoreData;
        import arya.spliro.utility.Constants;
        import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 7/24/2015.
 */
public class InviteAllAdapter extends BaseAdapter {

    private LayoutInflater inflate;
    private Context mcontext;
private View.OnClickListener onRemoveInviteeClick;
    public InviteAllAdapter() {
        super();
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    private ArrayList<InviteeData> inviteeList;

    public InviteAllAdapter(Context mcontext, ArrayList<InviteeData> inviteeList,View.OnClickListener onRemoveInviteeClick) {
        this.mcontext = mcontext;
        inflate = LayoutInflater.from(mcontext);
        this.inviteeList = inviteeList;
        this.onRemoveInviteeClick=onRemoveInviteeClick;
    }

    @Override
    public int getCount() {
        return inviteeList.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InviteeViewHolder holder;
        if (convertView == null) {
            holder = new InviteeViewHolder();
            convertView = inflate.inflate(R.layout.invite_list_row, parent, false);
            holder.cImgInA=(ImageView)convertView.findViewById(R.id.cImgInA);
            holder.txtDisplayNameInA=(TextView)convertView.findViewById(R.id.txtDisplayNameInA);
            holder.txtPhoneInA=(TextView)convertView.findViewById(R.id.txtPhoneInA);
            holder.rtInA=(RatingBar)convertView.findViewById(R.id.rtInA);
            holder.relCancelInA=(RelativeLayout)convertView.findViewById(R.id.relCancelInA);
            convertView.setTag(holder);
        } else {
            holder = (InviteeViewHolder) convertView.getTag();
        }
        holder.relCancelInA.setOnClickListener(onRemoveInviteeClick);
        holder.txtDisplayNameInA.setTypeface(SpliroApp.getFontLight());
        holder. txtPhoneInA.setTypeface(SpliroApp.getFontLight());
        InviteeData inviteeData =inviteeList.get(position);
        holder.relCancelInA.setTag(inviteeData);
        if(inviteeData.post_contact_id!=null&&inviteeData.post_contact_type.equals(Constants.POST_CONTACT_TYPE_PHONE))
        {
            holder.rtInA.setVisibility(View.GONE);
            holder.txtPhoneInA.setVisibility(View.VISIBLE);
            holder.txtDisplayNameInA.setText(inviteeData.display_name);
            holder.txtPhoneInA.setText(inviteeData.post_contact_id);
        }
        else {
            holder.rtInA.setVisibility(View.VISIBLE);
            holder.txtPhoneInA.setVisibility(View.GONE);
            holder.txtDisplayNameInA.setText(inviteeData.display_name);

//            if(inviteeData.rate!=null&&(!inviteeData.rate.isEmpty())) {
//                rating=Float.valueOf(inviteeData.rate);
//            }
            holder.rtInA.setRating(inviteeData.rate);
        }
        return convertView;
    }



    class InviteeViewHolder {
        private ImageView cImgInA;
        private TextView txtDisplayNameInA;
        private TextView txtPhoneInA;
        private RatingBar rtInA;
        private RelativeLayout relCancelInA;

    }
}
