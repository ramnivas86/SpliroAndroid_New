package arya.spliro.viewController.more;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.util.ArrayList;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.adapters.MoreCellAdapter;

import arya.spliro.dao.LocationData;
import arya.spliro.dao.MoreData;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SelectLocationActivity;
import arya.spliro.viewController.SpliroApp;

public class MoreFragment extends AbstractFragment implements View.OnClickListener{

    private String[] imgArray;
    private ArrayList<MoreData> moreItemList;
    private GridView gvMore;
    private MoreCellAdapter moreCellAdapter;
    private  String tag="spliro";
    View v;
    FragmentManager fm;
    FragmentTransaction ft;
    ImageView imgFacebook;
    ImageView imgTwitter;
    ImageView imgGoogle;
    ImageView imgEmail;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.frg_more,container,false);
        init();
        return v;
    }
    private void init()
    {
        gvMore = (GridView) v.findViewById(R.id.gvMore);
        gvMore.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                clickOnItem((MoreData) adapterView.getItemAtPosition(position));
            }
        });
        moreItemList = new ArrayList<MoreData>();
        imgArray = getResources().getStringArray(R.array.more_images_arr);
        for (int i = 0; i < imgArray.length; i++) {
            MoreData moreModelObj = new MoreData();
            String imageName = imgArray[i];
            moreModelObj.setImgName(imageName);
            moreModelObj.locationData= SpliroApp.defaultLocation;//by aditi 28 Aug 2015
            moreModelObj.setImgId(("ic_" + imageName.toLowerCase()).trim().replace(" ", "_"));
            moreItemList.add(moreModelObj);
        }
        moreCellAdapter = new MoreCellAdapter(getActivity(), moreItemList);
        gvMore.setAdapter(moreCellAdapter);
        fm=getActivity().getSupportFragmentManager();
        ft=fm.beginTransaction();
    }
    private void clickOnItem(MoreData moreModelObj)
    {
        String clickedItemName=moreModelObj.getImgName();
        Intent intent=null;
        if(clickedItemName.equals(getResources().getString(R.string.my_profile)))
        {
            ft=Util.addFragments(fm,new EditProfileFragment());
        }
        //{by aditi 28 Aug 2015
        else if(clickedItemName.equals("Location"))
        {
            intent=new Intent(getActivity(), SelectLocationActivity.class);
            startActivityForResult(intent, Constants.REQ_SELECT_LOCATION);
            Util.startActAnimation(getActivity());
        }
        else if(clickedItemName.equals(getResources().getString(R.string.feedback)))
        {
            feedbackOnEmail("rajeshs@aryavratinfotech.com","feedback");
        }
        else if(clickedItemName.equals(getResources().getString(R.string.idea)))
        {
            intentHtmlFile("file:///android_asset/Our_Story.html");
        }
        else if(clickedItemName.equals(getResources().getString(R.string.support)))
        {
            feedbackOnEmail("rajeshs@aryavratinfotech.com", "help");
        }
        else if(clickedItemName.equals(getResources().getString(R.string.share)))
        {

            shareDialog();
        }
        //}ends here
       /* if(intent!=null)
        {
            startActivity(intent);
            Util.startActAnimation(getActivity());
        }*/
    }
    //{by aditi 28 Aug 2015
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == getActivity().RESULT_OK)
        {
            if(requestCode==Constants.REQ_SELECT_LOCATION&&data!=null)
            {

                moreItemList.get(1).locationData=(LocationData)data.getSerializableExtra(Constants.SELECTED_LOCATION);
                if(moreItemList.get(1).locationData!=null)
                {
                    moreCellAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    //}ends here
    @Override
    protected BasicModel getModel() {
        return null;
    }
    @Override
    public void update(Observable observable, Object o) {

    }
    private void shareDialog()
    {
        Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sp_share_dialog);
        dialog.show();

        imgFacebook=(ImageView)dialog.findViewById(R.id.img_facebook);
        imgTwitter=(ImageView)dialog.findViewById(R.id.img_twitter);
        imgGoogle=(ImageView)dialog.findViewById(R.id.img_google);
        imgEmail=(ImageView)dialog.findViewById(R.id.img_email);
        imgFacebook.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgGoogle.setOnClickListener(this);
        imgEmail.setOnClickListener(this);

    }

    public void feedbackOnEmail(String mailTo,String subject) {
        if (Util.isDeviceOnline(true))
        {
            Intent reportProblem = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mailTo, null));
            reportProblem.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(Intent.createChooser(reportProblem, "Send email..."));
        }
        else
        {
            Toast.makeText(getActivity(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
        }
    }

    private void intentHtmlFile(String url) {
        DisplayFileFragment deisplayFileFrag =new DisplayFileFragment(url);
        ft=Util.addFragments(fm,deisplayFileFrag);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.img_facebook)
        {
            Util.shareFacebookLink(getActivity(),"https://www.google.co.in/?gfe_rd=cr&ei=NMnnVf7tIaPG8AebhLCICg&gws_rd=ssl");
        }
        else if(v.getId()==R.id.img_twitter)
        {
            Util.shareTwitterLink(getActivity(),"title","https://www.google.co.in/?gfe_rd=cr&ei=NMnnVf7tIaPG8AebhLCICg&gws_rd=ssl");
        }
        else if(v.getId()==R.id.img_google)
        {
            Util.shareGooglePlusLink(getActivity(),"https://www.google.co.in/?gfe_rd=cr&ei=NMnnVf7tIaPG8AebhLCICg&gws_rd=ssl");
        }
        else if(v.getId()==R.id.img_email)
        {
            Util.shareMailLink(getActivity(),"title","https://www.google.co.in/?gfe_rd=cr&ei=NMnnVf7tIaPG8AebhLCICg&gws_rd=ssl");
        }
    }

}
