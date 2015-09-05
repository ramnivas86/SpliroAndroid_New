package arya.spliro.viewController.createPost;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.adapters.InviteAllAdapter;
import arya.spliro.dao.InviteeData;
import arya.spliro.model.InviteAllModel;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 8/19/2015.
 */
public class InviteAllFragment extends AbstractFragment implements View.OnClickListener {
   private InviteAllModel inviteAllModel=new InviteAllModel();
    private   View inviteAllView;
    private ImageView imgBackInAll;
    private TextView txtTitleInAll;
    private LinearLayout linSelectContactsNative;
    private TextView txtSelectContactsNative;
    private  LinearLayout linearSelectContactsFav ;
    private  TextView txtSelectContactsFav;
    private  TextView txtInviteeList;
    private ListView lvInvitee;
    private TextView txtApply;
    private TextView txtCancelInAll;
    private Uri contactDataUri;
    private ArrayList<InviteeData> inviteeList;
    private HashMap<String,String> addedContactMap;
    private InviteAllAdapter inviteAllAdapter;
    private Dialog dialog;
    private ArrayList<InviteeData> inviteeListTemp;
   private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inviteAllView = inflater.inflate(R.layout.frg_invite_all,container,false);
        init();
        return inviteAllView;
    }

    private void init() {
         imgBackInAll=(ImageView)inviteAllView.findViewById(R.id.imgBackInAll);
        txtTitleInAll=(TextView)inviteAllView.findViewById(R.id.txtTitleInAll);
        linSelectContactsNative=(LinearLayout)inviteAllView.findViewById(R.id.linSelectContactsNative);
         txtSelectContactsNative=(TextView)inviteAllView.findViewById(R.id.txtSelectContactsNative);
        linearSelectContactsFav =(LinearLayout)inviteAllView.findViewById(R.id.linearSelectContactsFav);
      txtSelectContactsFav=(TextView)inviteAllView.findViewById(R.id.txtSelectContactsFav);
      txtInviteeList=(TextView)inviteAllView.findViewById(R.id.txtInviteeList);
       lvInvitee=(ListView)inviteAllView.findViewById(R.id.lvInvitee);
        txtApply=(TextView)inviteAllView.findViewById(R.id.txtApply);
       txtCancelInAll=(TextView)inviteAllView.findViewById(R.id.txtCancelInAll);
        imgBackInAll.setOnClickListener(this);
        linSelectContactsNative.setOnClickListener(this);
        linearSelectContactsFav .setOnClickListener(this);
        txtSelectContactsNative.setTypeface(SpliroApp.getFontSemiBold());
        txtTitleInAll.setTypeface(SpliroApp.getFontLight());
        txtSelectContactsFav.setTypeface(SpliroApp.getFontSemiBold());
        txtInviteeList.setTypeface(SpliroApp.getFontLight());
        txtApply.setTypeface(SpliroApp.getFontLight());
        txtCancelInAll.setTypeface(SpliroApp.getFontLight());
        inviteeListTemp= (ArrayList<InviteeData>) getArguments().getSerializable(Constants.INVITEELIST);
        if(inviteeListTemp==null)
        {
            inviteeList=new ArrayList<InviteeData>();
        }
        else
        {
            inviteeList= (ArrayList<InviteeData>) inviteeListTemp.clone();
        }
        inviteAllAdapter=new InviteAllAdapter(getActivity(),inviteeList,onRemoveInviteeClick);
        lvInvitee.setAdapter(inviteAllAdapter);
        addedContactMap=new HashMap<String,String>();
        txtApply.setOnClickListener(this);
        txtCancelInAll.setOnClickListener(this);
fm=getActivity().getSupportFragmentManager();
        ft=fm.beginTransaction();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_CONTACTS) {
            if (resultCode == getActivity().RESULT_OK) {
                contactDataUri = data.getData();
                addInviteeFromNative(Util.getSelectedContactFromNative(getActivity(), contactDataUri));
            }
        }
    }


    @Override
    protected BasicModel getModel() {
        return inviteAllModel;
    }
    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.imgBackInAll)
        {
           getActivity().onBackPressed();
        }
        else if(vId==R.id.linSelectContactsNative)
        {
            Util.openContactList(getActivity());
        }
     else   if(vId==R.id.linearSelectContactsFav)
        {
            ft= Util.addFragments(fm,new FavoritesFragment());
        }
     else   if(vId==R.id.txtApply)
        {
            inviteeListTemp=inviteeList;
            getActivity().onBackPressed();
        }
       else if(vId==R.id.txtCancelInAll)
        {
            getActivity().onBackPressed();
        }

    }


    View.OnClickListener onRemoveInviteeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final InviteeData inviteeData= (InviteeData) view.getTag();
          dialog=   Util.showYesNoMessageDialog(getActivity(),getString(R.string.do_you_want_to_delect_selected_invitee),getString(R.string.app_name), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inviteeData.post_contact_id!=null&&addedContactMap.containsKey(inviteeData.post_contact_id))
                    {
                        addedContactMap.remove(inviteeData.post_contact_id);
                    }
                    dialog.dismiss();
                    inviteeList.remove(inviteeData);
                    inviteAllAdapter.notifyDataSetChanged();
                }
            },null,null,null);
        }
    };
    public void addInviteeFromNative(String[] contactInformation) {

        contactInformation= Util.cVForSelectedContactFromNative(contactInformation);
    if(contactInformation!=null)
    {
            InviteeData inviteeData=new InviteeData();
            inviteeData.post_contact_id=	Util.getOnlyPhoneNumber(contactInformation[1]);
            inviteeData.display_name = contactInformation[0];
            if(!addedContactMap.containsKey(inviteeData.post_contact_id)) {
                inviteeData.post_contact_type=Constants.POST_CONTACT_TYPE_PHONE;
                inviteeList.add(inviteeData);
                addedContactMap.put(inviteeData.post_contact_id,inviteeData.post_contact_id);
                inviteAllAdapter.notifyDataSetChanged();
            }
            else
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.phone_num_allready_exist_error_msg));
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public ArrayList<InviteeData> getSelectedInviteeList()
    {
        return inviteeListTemp;
    }
}
