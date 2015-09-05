package arya.spliro.viewController.createPost;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.custom.CustomTypefaceSpan;
import arya.spliro.dao.CreateData;
import arya.spliro.model.PreviewModel;
import arya.spliro.slidingTabBar.SlidingTabLayout;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SplashActivity;
import arya.spliro.viewController.SpliroApp;

/**
 * Created by Admin on 8/21/2015.
 */
public class PreviewFragment extends AbstractFragment implements View.OnClickListener{
    PreviewModel model=new PreviewModel();
    public  boolean resetCreateScreen=false;
    private int no_of_clicks=0;
    private static CreateData currentCreateData;
    private View rootView;
    private  Context context;
    private static PreviewFragment instance;
    private Button btnBackAPPHPreview;
    private Button btnExpandtxtPreview;
    private TextView txtTitleShare;
    private TextView txtTotalPricePreview;
    private TextView txtPricePerSharePreview;
    private TextView txtSharePosterNamePreview;
    private RatingBar rtShareOwner;
    private LinearLayout llViewReceiptPreview;
    private ImageView cImgItemShareD;
    private TextView txtDistance;
    private TextView txtNoOfSharesPreview;
    private TextView txtLocationPreview;
    private TextView txtlocation;
    private TextView txtSavePreview;
    private TextView txtDeletePreview;
    private TextView txtPostPreview;
    private TextView txtDatePreview;
    private TextView txtNoOfShares;
    private TextView txtShareEnds;
    private TextView txtDescriptionPreview;
    private TextView txtViewReceiptPreview;
    private Dialog dialog;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.act_create_share_preview,container,false);
        init();
        return rootView;
    }
    private void init() {
        resetCreateScreen=false;
        btnBackAPPHPreview=(Button)rootView.findViewById(R.id.btnBackAPPHPreview);
        btnExpandtxtPreview=(Button)rootView.findViewById(R.id.btnExpandtxtPreview);
        txtTitleShare=(TextView)rootView.findViewById(R.id.txtTitleShare);
        txtTotalPricePreview=(TextView)rootView.findViewById(R.id.txtTotalPricePreview);
        txtPricePerSharePreview=(TextView)rootView.findViewById(R.id.txtPricePerSharePreview);
        txtSharePosterNamePreview=(TextView)rootView.findViewById(R.id.txtSharePosterNamePreview);
        txtViewReceiptPreview=(TextView)rootView.findViewById(R.id.txtViewReceiptPreview);
        rtShareOwner=(RatingBar)rootView.findViewById(R.id.rtShareOwner);
        llViewReceiptPreview=(LinearLayout)rootView.findViewById(R.id.llViewReceiptPreview);
        cImgItemShareD=(ImageView)rootView.findViewById(R.id.cImgItemShareD);
        txtDistance=(TextView)rootView.findViewById(R.id.txtDistance);
        txtNoOfSharesPreview=(TextView)rootView.findViewById(R.id.txtNoOfSharesPreview);
        txtLocationPreview=(TextView)rootView.findViewById(R.id.txtLocationPreview);
        txtlocation=(TextView)rootView.findViewById(R.id.txtlocation);
        txtSavePreview=(TextView)rootView.findViewById(R.id.txtSavePreview);
        txtDeletePreview=(TextView)rootView.findViewById(R.id.txtDeletePreview);
        txtPostPreview=(TextView)rootView.findViewById(R.id.txtPostPreview);
        txtDatePreview=(TextView)rootView.findViewById(R.id.txtDatePreview);
        txtNoOfShares=(TextView)rootView.findViewById(R.id.txtNoOfShares);
        txtShareEnds=(TextView)rootView.findViewById(R.id.txtShareEnds);
        txtDescriptionPreview=(TextView)rootView.findViewById(R.id.txtDescriptionPreview);
        setTypeFaceToAllFields();
        txtSavePreview.setOnClickListener(this);
        btnBackAPPHPreview.setOnClickListener(this);
        txtDeletePreview.setOnClickListener(this);
        txtPostPreview.setOnClickListener(this);
        btnExpandtxtPreview.setOnClickListener(this);
        llViewReceiptPreview.setOnClickListener(this);
        cImgItemShareD.setOnClickListener(this);
        setDataToFields(currentCreateData);
        if(currentCreateData.status!=null&& (currentCreateData.status.equals(Constants.CLOSED_STATUS)))
        {

        }
        else
        {
            onClick(txtSavePreview);
        }
    }

    private void setDataToFields(CreateData currentCreateData) {
        try
        {
            String catImagPath=null;
            String titleName=currentCreateData.title;
            catImagPath= currentCreateData.categories_data.image_path;
            if(currentCreateData.categories_data.custom_image_path!=null)
            {
                catImagPath= currentCreateData.categories_data.custom_image_path;
            }

            String hashTag=currentCreateData.categories_data.hashtag;
            String description=currentCreateData.categories_data.description;
            String numberOfShares=currentCreateData.no_of_shares;
            String shareEndDate=currentCreateData.post_expire_date;
            int inviteeCount=currentCreateData.invitee_data.size();
            String changeLocation=currentCreateData.location_data.address;
            double totalPrice=currentCreateData.invoice_price;
            String distance= getString(R.string.zeor_mi_distance);
            txtTitleShare.setText(titleName);
            txtSharePosterNamePreview.setText(currentCreateData.display_name);
            rtShareOwner.setRating(currentCreateData.rate);
            txtTotalPricePreview.setText(Constants.CURRENCY+totalPrice);
            txtDescriptionPreview.setText(description);
            txtDescriptionPreview.setMovementMethod(LinkMovementMethod.getInstance());
            txtDatePreview.setText(Util.setShareEndDate(shareEndDate));
            txtLocationPreview.setText(changeLocation);
            txtPricePerSharePreview.setText(Constants.CURRENCY+currentCreateData.per_share_price+Constants.PER_SHARE);
            if(catImagPath!=null&&!catImagPath.isEmpty()) {
                File catImagePath= new File(catImagPath);
                if(catImagePath.exists()) {
//                    cImgItemShareD.setImageBitmap(BitmapFactory.decodeFile(catImagPath));
                    Util.loadImageFromSDcard(getActivity(),cImgItemShareD,catImagPath,R.drawable.ic_launcher);
                }
            }
            txtNoOfSharesPreview.setText(numberOfShares);
            txtDistance.setText(Util.setDistanseSpan(getActivity(), distance, 0, 0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setTypeFaceToAllFields() {
        Typeface fontLight= SpliroApp.getFontLight();
        Typeface fontSemiBold= SpliroApp.getFontSemiBold();
        txtTitleShare.setTypeface(fontLight);
        txtTotalPricePreview.setTypeface(fontSemiBold);
        txtPricePerSharePreview.setTypeface(fontLight);
        txtSharePosterNamePreview.setTypeface(fontLight);
        txtDistance.setTypeface(fontSemiBold);
        txtNoOfSharesPreview.setTypeface(fontLight);
        txtLocationPreview.setTypeface(fontLight);
        txtlocation.setTypeface(fontLight);
        txtSavePreview.setTypeface(fontLight);
        txtDeletePreview.setTypeface(fontLight);
        txtPostPreview.setTypeface(fontLight);
        txtDatePreview.setTypeface(fontLight);
        txtNoOfShares.setTypeface(fontLight);
        txtShareEnds.setTypeface(fontLight);
        txtViewReceiptPreview.setTypeface(fontLight);
        txtDescriptionPreview.setTypeface(fontLight);
    }

    public static PreviewFragment getInstance(CreateData createData)
    {
        currentCreateData=createData;
        if(instance==null)
        {
            instance=new PreviewFragment();
        }
       return instance;

    }



    @Override
    protected BasicModel getModel() {
        return model;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(o instanceof CreateData)
        {
            CreateData createDataObj=(CreateData)o;
        if(createDataObj.status.equals(Constants.POSTING_STATUS)||createDataObj.status.equals(Constants.DELETE_STATUS)&&createDataObj.errorMsg.isEmpty())
        {
            resetCreateScreen=true;
            Config.removeOrClearPerferance(Config.KEYCREATEMSGARRAY);
             onClick(btnBackAPPHPreview);
          if(  createDataObj.status.equals(Constants.POSTING_STATUS))
          {
             Util.showCenteredToast(getActivity().getString(R.string.post_posted_successfully));
          }
            else if(  createDataObj.status.equals(Constants.DELETE_STATUS))
          {
              Util.showCenteredToast(getActivity().getString(R.string.post_deleted_successfully));
          }
        }
            else
        {
            if(!createDataObj.errorMsg.isEmpty()) {
                Util.showOkDialog(null, createDataObj.errorMsg);
                createDataObj.errorMsg = "";
            }
        }
        }

    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.txtSavePreview)
        {
            Util.showProDialog(getActivity().getResources().getString(R.string.wait));
            currentCreateData.status=Constants.SAVING_STATUS;
            model.createPostToServer(currentCreateData);
        }
        else if(vId==R.id.txtDeletePreview)
        {
            dialog=   Util.showYesNoMessageDialog(getActivity(),getString(R.string.do_you_want_to_delete),getString(R.string.app_name), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Util.showProDialog(null);
                    currentCreateData.status=Constants.DELETE_STATUS;
                    model.createPostToServer(currentCreateData);
                }
            },null,null,null);

        }

        else if(vId==R.id.txtPostPreview)
        {
            Util.showProDialog(null);
            currentCreateData.status=Constants.POSTING_STATUS;
            model.createPostToServer(currentCreateData);
        }

        else if(vId==R.id.llViewReceiptPreview||vId==R.id.cImgItemShareD)
        {

            if(vId==R.id.cImgItemShareD&&currentCreateData.categories_data!=null)
            {
                if(currentCreateData.categories_data.custom_image_path!=null&&!currentCreateData.categories_data.custom_image_path.isEmpty())
                    Util.showImageInDefaultImageViewer(getActivity(),currentCreateData.categories_data.custom_image_path);
                else
                Util.showImageInDefaultImageViewer(getActivity(),currentCreateData.categories_data.image_path);
            }
            else if(vId==R.id.llViewReceiptPreview)
            {
                Util.showImageInDefaultImageViewer(getActivity(),currentCreateData.receipt_image_path);
            }
        }

        else if(vId==R.id.btnBackAPPHPreview)
        {
            currentCreateData=null;
            getActivity().onBackPressed();

        }
        else if(vId==R.id.btnExpandtxtPreview)
        {
            no_of_clicks++;
            if(no_of_clicks%2==0)
            {
                txtDescriptionPreview.setMaxLines(Constants.MIN_LINES_TO_SHOW);
                if(txtDescriptionPreview.getLineCount()>Constants.MIN_LINES_TO_SHOW)
                {
                    btnExpandtxtPreview.setBackgroundResource(R.drawable.spl_ic_more_arrow);
                }

            }
            else
            {
                txtDescriptionPreview.setMaxLines(Constants.MAX_LINES_TO_SHOW);
                if(txtDescriptionPreview.getLineCount()>Constants.MIN_LINES_TO_SHOW)
                {
                    btnExpandtxtPreview.setBackgroundResource(R.drawable.spl_ic_more_arrow_up);
                }

            }
        }


    }
    private String calculatePerSharePrice(double total_price,double no_of_shares)
    {
        String s= Util.formatTwoDecimalPlaces(total_price / no_of_shares);
        return Constants.CURRENCY+ s+" each";
    }



}
