package arya.spliro.viewController.createPost;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.TimeZone;

import arya.spliro.R;
import arya.spliro.adapters.AutoCompleteAdapter;
import arya.spliro.config.Config;
import arya.spliro.custom.CustomEditText;
import arya.spliro.custom.DatePickerDailog;
import arya.spliro.custom.OpenCameraDialog;
import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.InviteeData;
import arya.spliro.dao.LocationData;
import arya.spliro.interfaces.ClickOnPreImeInterFace;
import arya.spliro.model.CreatePostModel;
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.CategoriesMgr;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.SelectLocationActivity;
import arya.spliro.viewController.SpliroApp;


public class CreatePostFragment extends AbstractFragment implements View.OnClickListener,ClickOnPreImeInterFace {
    private View rootView;
    private TextView txtTitleCP;
    private TextView txtPreviewCP;
    private ImageView imgTitleInfo;
    private EditText autoCompleteTxt;
    private ImageView imgLocationInfo;
    private TextView txtLocationCP;
    private RelativeLayout relChangeLocation;
    private  ImageView imgUploadInfo;
    private ImageView imgUploadCP;
    private ImageView imgHashTagInfo;
    private  EditText eTxtHashTag;
    private  ImageView imgDescriptionInfo;
    private  TextView txtDescrTitle;
    private ImageView imgShareCounterInfo;
    private ImageView imgPlusCP;
    private  ImageView imgMinus;
    private TextView txtUploadCp;
    private  CustomEditText eTxtSharesCounter;
    private TextView txtNoOfShares;
    private  ImageView imgShareEndsInfo;
    private  TextView txtShareEndDate;
    private  TextView txtShareEnds;
    private ImageView imgInviteAllInfo;
    private TextView txtInviteAll;
    private ImageView imgPriceInfo;
    private RelativeLayout relPrice;
    private RelativeLayout relChangeLMain;
    private RelativeLayout relUploadImg;
    private RelativeLayout relDescrptionMain;
    private LinearLayout linearNoOfSharesMain;
    private RelativeLayout relShareEndMain;
    private RelativeLayout relIviteAllMain;
    private RelativeLayout relPriceMain;
    private RelativeLayout relTiteleMain;
    private TextView txtDescription;
    private ImageView imgDescription;
    private ScrollView scrollViewCP;
    private TextView txtEndDateMarging;
    private  OpenCameraDialog pickImageDialog;
   public CreatePostModel createPostModel =new CreatePostModel();
    private DatePickerDailog dpDailog;
    public CreateData createDataObj;
    private int numberofShares=1;
    private int maxNumberofShares=1000;
    private LocationData current_location;
    private int onBackClick;
    private boolean  enableScroll=false;
    private CustomEditText eTxtDummy;
    private ImageView imgHideKeyBoard;
    private RelativeLayout relCpRootContainer;
    public boolean keyBoardVisibility=false;
    FragmentManager fm;
    FragmentTransaction ft;
    private TextView txtPricePerShare;
    private TextView  txttotal_priceCreate;
    private ImageView img_price_create;
    private TextView txtInviteeNumber;
    private ImageView imgInvitee;
    private double total_price=0;
    private double perSharePrice=0;
    private  ArrayList<Categories> catArrayList;
    private AutoCompleteAdapter autoCompleteAdapter;
    public  String  savedimage_path;
    private boolean isFirstTime;
    private String Tag="spliro";
    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_create_post,container,false);
        init();
        return rootView;
    }
    private void init() {
        isFirstTime=true;
        keyBoardVisibility=false;
        txtTitleCP =(TextView)rootView.findViewById(R.id.txtTitleCP);
        txtPreviewCP =(TextView)rootView.findViewById(R.id.txtPreviewCP);
        imgTitleInfo = (ImageView)rootView.findViewById(R.id.imgTitleInfo);
        autoCompleteTxt = (EditText)rootView.findViewById(R.id.autoCompleteTxt);
         imgLocationInfo = (ImageView)rootView.findViewById(R.id.imgLocationInfo);
        txtLocationCP =(TextView)rootView.findViewById(R.id.txtLocationCP);
         relChangeLocation = (RelativeLayout)rootView.findViewById(R.id.relChangeLocation);
        imgUploadInfo = (ImageView)rootView.findViewById(R.id.imgUploadInfo);
        imgUploadCP = (ImageView)rootView.findViewById(R.id.imgUploadCP);
         imgHashTagInfo = (ImageView)rootView.findViewById(R.id.imgHashTagInfo);
        eTxtHashTag = (EditText)rootView.findViewById(R.id.eTxtHashTag);
        imgDescriptionInfo = (ImageView)rootView.findViewById(R.id.imgDescriptionInfo);
         txtDescrTitle =(TextView)rootView.findViewById(R.id.txtDescrTitle);
         imgShareCounterInfo = (ImageView)rootView.findViewById(R.id.imgShareCounterInfo);
        imgPlusCP = (ImageView)rootView.findViewById(R.id.imgPlusCP);
        imgMinus = (ImageView)rootView.findViewById(R.id.imgMinus);
        eTxtSharesCounter =(CustomEditText)rootView.findViewById(R.id.eTxtSharesCounter);
        txtNoOfShares =(TextView)rootView.findViewById(R.id.txtNoOfShares);
        imgShareEndsInfo = (ImageView)rootView.findViewById(R.id.imgShareEndsInfo);
         txtShareEndDate =(TextView)rootView.findViewById(R.id.txtShareEndDate);
         txtShareEnds =(TextView)rootView.findViewById(R.id.txtShareEnds);
         imgInviteAllInfo = (ImageView)rootView.findViewById(R.id.imgInviteAllInfo);
         txtInviteAll =(TextView)rootView.findViewById(R.id.txtInviteAll);
         imgPriceInfo  = (ImageView)rootView.findViewById(R.id.imgPriceInfo);
         relPrice = (RelativeLayout)rootView.findViewById(R.id.relPrice);
         relChangeLMain = (RelativeLayout)rootView.findViewById(R.id.relChangeLMain);
         relUploadImg = (RelativeLayout)rootView.findViewById(R.id.relUploadImg);
        relDescrptionMain = (RelativeLayout)rootView.findViewById(R.id.relDescrptionMain);
         linearNoOfSharesMain = (LinearLayout)rootView.findViewById(R.id.linearNoOfSharesMain);
         relShareEndMain = (RelativeLayout)rootView.findViewById(R.id.relShareEndMain);
         relIviteAllMain = (RelativeLayout)rootView.findViewById(R.id.relIviteAllMain);
         relPriceMain = (RelativeLayout)rootView.findViewById(R.id.relPriceMain);
        relTiteleMain=(RelativeLayout)rootView.findViewById(R.id.relTiteleMain);
        txtDescription=(TextView)rootView.findViewById(R.id.txtDescription);
        imgDescription=(ImageView)rootView.findViewById(R.id.imgDescription);
        scrollViewCP=(ScrollView)rootView.findViewById(R.id.scrollViewCP);
        txtEndDateMarging=(TextView)rootView.findViewById(R.id.txtEndDateMarging);
        imgHideKeyBoard=(ImageView)rootView.findViewById(R.id.imgHideKeyBoard);
        eTxtDummy=(CustomEditText)rootView.findViewById(R.id.eTxtDummy);
        relCpRootContainer=(RelativeLayout)rootView.findViewById(R.id.relCpRootContainer);
        txtPricePerShare=(TextView)rootView.findViewById(R.id.txtPricePerShare);
        txttotal_priceCreate=(TextView)rootView.findViewById(R.id.txtTotalPriceCreate);
        img_price_create=(ImageView)rootView.findViewById(R.id.img_price_create);
        txtInviteeNumber=(TextView)rootView.findViewById(R.id.txtInviteeNumber);
        imgInvitee=(ImageView)rootView.findViewById(R.id.imgInvitee);
        txtUploadCp=(TextView)rootView.findViewById(R.id.txtUploadCp);
        imgHideKeyBoard.setOnClickListener(this);
        imgHideKeyBoard.setOnTouchListener(touchListner);
        eTxtDummy.setOnPreImeListener(this);
       eTxtSharesCounter .setOnPreImeListener(this);
        setTypeFaceToAllFields();
        relPriceMain.setOnClickListener(this);
        img_price_create.setOnClickListener(this);
        txtPreviewCP.setOnClickListener(this);
        imgTitleInfo.setOnClickListener(this);
        imgLocationInfo.setOnClickListener(this);
        relChangeLocation.setOnClickListener(this);
        txtLocationCP.setOnClickListener(this);
        imgUploadInfo.setOnClickListener(this);
        imgHashTagInfo.setOnClickListener(this);
        imgDescriptionInfo.setOnClickListener(this);
        txtDescrTitle.setOnClickListener(this);
        imgShareCounterInfo.setOnClickListener(this);
        imgPlusCP.setOnClickListener(this);
        imgMinus .setOnClickListener(this);
        eTxtSharesCounter.setOnClickListener(this);
        txtNoOfShares.setOnClickListener(this);
        imgShareEndsInfo.setOnClickListener(this);
        txtShareEnds.setOnClickListener(this);
        imgInviteAllInfo.setOnClickListener(this);
        relIviteAllMain.setOnClickListener(this);
        imgPriceInfo.setOnClickListener(this);
//        relPrice.setOnClickListener(this);
        relTiteleMain.setOnClickListener(this);
        txtDescription.setOnClickListener(this);
        relUploadImg.setOnClickListener(this);
        txtUploadCp.setOnClickListener(this);
        imgUploadCP.setOnClickListener(this);
        relDescrptionMain.setOnClickListener(this);
        eTxtDummy.setMovementMethod(LinkMovementMethod.getInstance());
       txtDescription. setMovementMethod(LinkMovementMethod.getInstance());
        eTxtDummy.setOnTouchListener(touchListner);
        txtPreviewCP.setOnTouchListener(touchListner);

        eTxtSharesCounter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!textView.getText().toString().isEmpty())
                {
                    int num=Integer.valueOf(""+textView.getText().toString());
                    if(num>0)
                        numberofShares=Integer.valueOf(textView.getText().toString());
                }
                return false;
            }
        });
        eTxtSharesCounter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(!charSequence.toString().isEmpty())
                {
                    int num=Integer.valueOf(""+charSequence);
                    if(num>0)
                    numberofShares=Integer.valueOf(charSequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fm=getActivity().getSupportFragmentManager();
        ft=fm.beginTransaction();
            Util.showProDialog(null);
            createPostModel.getCatgoriesDataFromLocalDB(null);
    }
    View.OnTouchListener touchListner= new View.OnTouchListener()
    {
        public boolean onTouch(View view, MotionEvent event)
        {
            if (view.getId() == R.id.eTxtDummy||view.getId() == R.id.txtPreviewCP||view.getId() == R.id.imgHideKeyBoard ){
                view.getParent().requestDisallowInterceptTouchEvent(enableScroll);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
            }
            return false;
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK)
        {
            if(requestCode == Constants.PICK_IMAGE_FROM_GALLERY)
            {
             Constants.imageUri=data.getData();
            }
            if(requestCode == Constants.PICK_IMAGE_FROM_CAMERA||requestCode == Constants.PICK_IMAGE_FROM_GALLERY)
            {
                LoadImagesFromSDCard loadImagesFromSDCard=new LoadImagesFromSDCard();
                loadImagesFromSDCard.execute();
            }
        else if(requestCode==Constants.REQ_SELECT_LOCATION&&data!=null)
        {
            current_location=(LocationData)data.getSerializableExtra(Constants.SELECTED_LOCATION);
            if(current_location!=null)
            {
                createDataObj.location_data=current_location;
                txtLocationCP.setText(current_location.address);
            }
        }
        else if(requestCode==Constants.REQ_FOR_SHARE_PREVIEW&&data!=null)
            {
                if(data.hasExtra(Constants.REPOST_SHARE))
                {
                    ArrayList<CreateData> createDataArrayList=new ArrayList<CreateData>();
                    createDataArrayList.add((CreateData)data.getSerializableExtra(Constants.REPOST_SHARE));
                    setDataToCreatePostCard(createDataArrayList);
                }
                else if(data.hasExtra(Constants.EDIT_SHARE))// 3/9/2015 phoosaram
                {
                    ArrayList<CreateData> createDataArrayList=new ArrayList<CreateData>();
                    createDataArrayList.add((CreateData)data.getSerializableExtra(Constants.EDIT_SHARE));
                    setDataToCreatePostCard(createDataArrayList);
                }

            }
        }
    }

    private void setTypeFaceToAllFields() {
        Typeface lite= SpliroApp.getFontLight();
        Typeface semibold= SpliroApp.getFontSemiBold();
        txtTitleCP.setTypeface(SpliroApp.getAppHeaderTypeface());
        txtPreviewCP.setTypeface(semibold);
        autoCompleteTxt.setTypeface(lite);
        txtLocationCP.setTypeface(lite);
        eTxtHashTag.setTypeface(lite);
        txtDescrTitle.setTypeface(lite);
        eTxtSharesCounter.setTypeface(lite);
        txtNoOfShares.setTypeface(lite);
        txtShareEnds.setTypeface(lite);
        txtInviteAll.setTypeface(lite);
        txtDescription.setTypeface(lite);
        txtPricePerShare.setTypeface(lite);
        txttotal_priceCreate.setTypeface(lite);
        txtInviteeNumber.setTypeface(lite);
        txtPricePerShare.setTypeface(lite);
        txttotal_priceCreate.setTypeface(lite);
        txtUploadCp.setTypeface(lite);
    }
    @Override
    protected BasicModel getModel() {
        return createPostModel;
    }
    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.txtPreviewCP)
        {
            Util.hideKeyBoardMethod(view.getContext(),view);
            saveCreatedCardToSavePref(true);
        }
        else if(vId==R.id.imgTitleInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.title_info_msg));
        }
        else if(vId==R.id.imgLocationInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.change_location_info_msg));
        }
        else if(vId==R.id.imgUploadInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.upload_photo_info_msg));
        }
        else if(vId==R.id.imgHashTagInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.hash_tag_info_msg));
        }
        else if(vId==R.id.imgDescriptionInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.description_info_msg));
        }
        else if(vId==R.id.imgShareCounterInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.no_of_shares_info_msg));
        }
        else if(vId==R.id.imgShareEndsInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.share_end_info_msg));
        }
        else if(vId==R.id.imgInviteAllInfo)
        {
            Util.showOkDialog(null,getResources().getString(R.string.invite_all_info_msg));
        }
        else if(vId==R.id.imgPriceInfo)
        {
            Util.showOkDialog(null, getResources().getString(R.string.price_info_msg));
        }
        else if(vId==R.id.imgHideKeyBoard)
        {
            clickOnBackPressed(true);
            Util.hideKeyBoardMethod(view.getContext(),view);
        }
        else if(vId==R.id.txtDescrTitle||vId==R.id.relDescrptionMain||vId==R.id.txtDescription)
        {
            enableScroll=true;
            TabActivity.disabledScrolling(enableScroll);
            eTxtDummy.setVisibility(View.VISIBLE);
            scrollViewCP.setVisibility(View.GONE);
            eTxtDummy.requestFocus();
            Util.openKeyBoard(eTxtDummy);
            if(!txtDescription.getText().toString().isEmpty()) {
                eTxtDummy.setText(txtDescription.getText().toString().trim());
                eTxtDummy.setSelection(txtDescription.getText().toString().trim().length());
            }
            eTxtDummy.setGravity(Gravity.LEFT | Gravity.TOP);
        }
        else if(vId==R.id.eTxtSharesCounter)
        {
            keyBoardVisibility=true;
            eTxtSharesCounter.requestFocus();
            eTxtSharesCounter.setCursorVisible(true);
            eTxtSharesCounter.setSelection(eTxtSharesCounter.getText().toString().trim().length());
        }
        else if(vId==R.id.txtShareEnds)
        {

            showDatePicker();
        }
        else if(vId==R.id.relIviteAllMain)
        {
            InviteAllFragment inviteAllFragment=new InviteAllFragment();
            Bundle b=new Bundle();
            b.putSerializable(Constants.INVITEELIST,createDataObj.invitee_data);
            inviteAllFragment.setArguments(b);
          ft= Util.addFragments(fm,inviteAllFragment);
        }
        else if(vId==R.id.relPriceMain||vId==R.id.img_price_create)
        {
            double no_of_shares=Util.toDouble(eTxtSharesCounter.getText().toString());
            PriceCalculateFragment fragment=PriceCalculateFragment.getInstance(total_price,no_of_shares);
            ft=Util.addFragments(fm,fragment);
        }
        else if(vId==R.id.relTiteleMain)
        {
            autoCompleteTxt.requestFocus();
            Util.openKeyBoard(autoCompleteTxt);
        }
        else if(vId==R.id.relUploadImg||vId==R.id.imgUploadCP)
        {
            String imagePath= (String) view.getTag();
            if(imagePath!=null)
                Util.showImageInDefaultImageViewer(getActivity(),imagePath);
        }

        else if(vId==R.id.txtUploadCp)
        {
            if(pickImageDialog==null) {
                pickImageDialog = new OpenCameraDialog();
            }
            if(!pickImageDialog.isVisible())
                pickImageDialog.show(getActivity().getSupportFragmentManager(),  "missiles");
        }
        else if (vId==R.id.imgPlusCP)
        {
            if(numberofShares<maxNumberofShares)
            numberofShares++;
            setNumbersOfShares();
        }
        else if (vId==R.id.imgMinus)
        {
            if(numberofShares>1)
            numberofShares--;
         setNumbersOfShares();
        }
        else if((vId==R.id.txtLocationCP)||(vId==R.id.relChangeLocation))
        {
            Intent i=new Intent(getActivity(), SelectLocationActivity.class);
            startActivityForResult(i,Constants.REQ_SELECT_LOCATION);
            Util.startActAnimation(getActivity());
        }
    }

    private void setNumbersOfShares()
    {
        createDataObj.no_of_shares=""+numberofShares;
        eTxtSharesCounter.setText(""+numberofShares);
        txtNoOfShares.setText(getActivity().getResources().getString(R.string.no_of_shares)+" (+"+numberofShares+")");
        eTxtSharesCounter.setSelection(eTxtSharesCounter.getText().toString().trim().length());
        if(keyBoardVisibility)
        {
            eTxtSharesCounter.setSelection(eTxtSharesCounter.getText().toString().trim().length());
        }
        if(total_price!=0)
        {
            setDataToPrice(total_price,null);
        }
        else
        {
            this.total_price=0;
            img_price_create.setVisibility(View.VISIBLE);
            txttotal_priceCreate.setVisibility(View.GONE);
            txtPricePerShare.setVisibility(View.GONE);
        }
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if(o instanceof ArrayList)
            {
                ArrayList<Categories> categoriesListTemp = (ArrayList<Categories>) o;
                if(!categoriesListTemp.isEmpty()&&categoriesListTemp.get(0).searchKeyWord)
                {
                    if (categoriesListTemp.get(0)!=null&&categoriesListTemp.get(0).catgory_id!=0) {
                        setDataToTitleAndHashTagAndImag(categoriesListTemp.get(0), true);
                    } else {
                        setDataToTitleAndHashTagAndImagIfEmpty();
                    }
                }else {
                    catArrayList = (ArrayList<Categories>) o;
                    setDataToAutoCOmpleteAdapter();
                    setDataToCreatePostCard(Config.getCreateCardArray());
                    isFirstTime = false;
                }
            }
        else if(o instanceof Boolean)
        {
         setDataToCreatePostCard(Config.getCreateCardArray());
        }
    }

    @Override
    public void clickOnBackPressed(boolean onOnBack)
    {
        if (onOnBack) {
            if(TabActivity.disablePagerScroll&&scrollViewCP.getVisibility()==View.GONE)
            {
                setDescriptionData();
            }
            else
            {
                setDataToCounter();
            }
        }
    }

    private void setDataToCounter() {
        keyBoardVisibility=false;
        String cuounter=eTxtSharesCounter.getText().toString();
        if(!cuounter.isEmpty())
        {
            int num=Integer.valueOf(cuounter);
            if(num<1)
            {
                numberofShares=1;
            }
        }
        else {
            numberofShares=1;
        }
        eTxtSharesCounter.setText(""+numberofShares);
        txtNoOfShares.setText(getActivity().getResources().getString(R.string.no_of_shares)+" (+"+numberofShares+")");
        eTxtSharesCounter.setCursorVisible(false);
        if(total_price>0)
        {
            setDataToPrice(total_price,null);
        }
    }

    public void setDescriptionData() {
            enableScroll = false;
            TabActivity.disabledScrolling(enableScroll);
            scrollViewCP.setVisibility(View.VISIBLE);
            if (eTxtDummy.getText().toString().trim().isEmpty())
            {
                txtDescription.setText(eTxtDummy.getText().toString());
                txtDescription.setVisibility(View.GONE);
                imgDescription.setVisibility(View.VISIBLE);
                if(createDataObj!=null&&createDataObj.categories_data!=null)
                {
                    createDataObj.categories_data.description = "";
                }
            }
            else
            {
                txtDescription.setVisibility(View.VISIBLE);
                imgDescription.setVisibility(View.GONE);
                txtDescription.setText(eTxtDummy.getText().toString().trim());
                txtDescription.setGravity(Gravity.CENTER);
                if( createDataObj.categories_data!=null)
                {
                    createDataObj.categories_data.description = txtDescription.getText().toString().trim();
                }
            }
            eTxtDummy.setVisibility(View.GONE);

    }

    public class LoadImagesFromSDCard extends AsyncTask<Void, Bitmap, Bitmap>
    {
        Bitmap mBitmap;
        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/
           Util.showProDialog(getActivity().getResources().getString(R.string.photo_uploading_from_sdcard));
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            if (Constants.imageUri != null) {
                savedimage_path =Util.compressImage(getActivity(),Constants.imageUri.toString());
                mBitmap = BitmapFactory.decodeFile(savedimage_path);
                SystemClock.sleep(2000);
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap bm) {
            Util.dimissProDialog();
            if (bm != null) {
                LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                imgUploadCP.setLayoutParams(layoutParams);
                imgUploadCP.setScaleType(ImageView.ScaleType.FIT_XY);
                Util.loadImageFromSDcard(getActivity(),imgUploadCP,savedimage_path,R.drawable.ic_uploadimg);
                imgUploadCP.invalidate();
                imgUploadCP.setTag(savedimage_path);
            }
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }
    }
    /** -------------DATE PICKER DAILOG FOR VIRTUAL AND IN-PERSON----------- */
    private void showDatePicker()
    {
        Date d = null;
        SimpleDateFormat sdf=new SimpleDateFormat(Constants.ShareEndDateFormat_create_show);
        try {
             d =sdf.parse(txtShareEndDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        dpDailog = new DatePickerDailog(false, getActivity(),cal , new DatePickerDailog.DatePickerListner() {
        @Override
        public void OnDoneButton(Dialog datedialog, Calendar c, String str) {
//            txtShareEndDate.setText(   Util. setShareEndDate(new SimpleDateFormat(Constants.ShareEndDateFormat_create_show).format(c.getTime())));
            SimpleDateFormat utc_format=new SimpleDateFormat(Constants.ShareEndDateFormat_create_send);
            utc_format.setTimeZone(TimeZone.getTimeZone(Constants.UTC_TIMEZONE));
            createDataObj.post_expire_date = utc_format.format(c.getTime());
            txtShareEndDate.setText(Util.setShareEndDate(utc_format.format(c.getTime())));
            dpDailog.dismiss();
        }

        @Override
        public void OnCancelButton(Dialog datedialog) {
            dpDailog.dismiss();
        }
    });
    dpDailog.show();
}
//    private void setShareEndDate(String shareEndDate) {
//        SpannableStringBuilder s = new SpannableStringBuilder(shareEndDate);
//        String[] shareEndDateArray=shareEndDate.split(",");
//        String first=shareEndDateArray[0];
//        s.setSpan (new CustomTypefaceSpan("", SpliroApp.getFontSemiBold()), 0, first.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        s.setSpan (new CustomTypefaceSpan("", SpliroApp.getFontLight()), first.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        txtShareEndDate.setText(s);
//    }


    private final void focusOnView(final ScrollView scrollView, final View eTxt){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, eTxt.getBottom());
            }
        });
    }
    public void setDataToPrice(Double invoice_price,String receiptImgPath)
    {
        if(receiptImgPath!=null&&!receiptImgPath.isEmpty())
        {
            createDataObj.receipt_image_path=receiptImgPath;
        }
        if(invoice_price!=0.0)
        {
            this.total_price=invoice_price;
            if(this.total_price!=0)
            {
                img_price_create.setVisibility(View.GONE);
                txttotal_priceCreate.setVisibility(View.VISIBLE);
                txtPricePerShare.setVisibility(View.VISIBLE);
                createDataObj.invoice_price=total_price;
                txttotal_priceCreate.setText("$"+total_price);
                this.perSharePrice=Util.toDouble(Util.calculatePerSharePrice(this.total_price,Double.parseDouble(eTxtSharesCounter.getText().toString())));
                txtPricePerShare.setText(Constants.CURRENCY+this.perSharePrice+Constants.PER_SHARE);
            }
        }
        else
        {
            this.total_price=0;
            img_price_create.setVisibility(View.VISIBLE);
            txttotal_priceCreate.setVisibility(View.GONE);
            txtPricePerShare.setVisibility(View.GONE);
        }


    }

    public void setDataToInvitees(ArrayList<InviteeData> invitee_data)
    {
        if(invitee_data!=null&&invitee_data.size()>0)
        {
            createDataObj.invitee_data = invitee_data;
            imgInvitee.setVisibility(View.GONE);
            txtInviteeNumber.setVisibility(View.VISIBLE);
        }
        else
        {
            imgInvitee.setVisibility(View.VISIBLE);
            txtInviteeNumber.setVisibility(View.GONE);
        }
        txtInviteeNumber.setText(""+invitee_data.size());
    }


    public void setDataToAutoCOmpleteAdapter()
    {
//    autoCompleteAdapter = new AutoCompleteAdapter(getActivity(), catArrayList);
//    autoCompleteTxt.setAdapter(autoCompleteAdapter);
        eTxtDummy.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    setDescriptionData();
                Util.hideKeyBoardMethod(getActivity(),eTxtDummy);
                return false;
            }
        });
        autoCompleteTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (!isFirstTime) {
                        String[] keywordsArray=textView.getText().toString().trim().toLowerCase().split(" ");
                        String searchKeyWord=keywordsArray[keywordsArray.length-1];
                        HashMap<String, Categories> catHashMap = createPostModel.categoryListHashMap;
                        if(searchKeyWord.trim().length()>1) {
                            createPostModel.getCatgoriesDataFromLocalDB(searchKeyWord);
                        }
                        if(textView.getText().toString().trim().toLowerCase().length()==0)
                        {
                            setDataToTitleAndHashTagAndImagIfEmpty();
                        }
//                }
                }
                return false;
            }
        });
    autoCompleteTxt.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if(!isFirstTime) {
                String[] keywordsArray=charSequence.toString().trim().toLowerCase().split(" ");
                String searchKeyWord=keywordsArray[keywordsArray.length-1];
                HashMap<String, Categories> catHashMap = createPostModel.categoryListHashMap;
                if(searchKeyWord.trim().length()>1) {
                  createPostModel.getCatgoriesDataFromLocalDB(searchKeyWord);
                }
                if(charSequence.toString().trim().toLowerCase().length()==0)
                {
                    setDataToTitleAndHashTagAndImagIfEmpty();
                }
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
            autoCompleteTxt.setSelection(autoCompleteTxt.getText().toString().length());
        }
    });

//        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int posi, long l) {
//                setDataToTitleAndHashTagAndImag((Categories) autoCompleteAdapter.getItem(posi),false);
//            }
//        });
    }
    private void setDataToTitleAndHashTagAndImag(Categories categories_data,boolean setOnHashTag) {
        if(categories_data!=null) {
            if (setOnHashTag) {
                eTxtHashTag.setText(categories_data.hashtag.trim());

            } else {
                createDataObj.title=categories_data.name;
//                autoCompleteTxt.setText(createDataObj.title);

                eTxtHashTag.setText(categories_data.hashtag.trim());
//                Util.hideKeyBoardMethod(getActivity(), autoCompleteTxt);
            }
            if(categories_data.image_path!=null&&(!categories_data.image_path.isEmpty())) {
                File f=new File(categories_data.image_path);
                if(f.exists())
                {
                    LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    imgUploadCP.setLayoutParams(layoutParams);
                    imgUploadCP.setScaleType(ImageView.ScaleType.FIT_XY);
                    imgUploadCP.setTag(categories_data.image_path);
                    Util.loadImageFromSDcard(getActivity(),imgUploadCP,categories_data.image_path,R.drawable.ic_uploadimg);
//                   imgUploadCP.setImageBitmap(BitmapFactory.decodeFile(categories_data.image_path));
                }

            }
            eTxtDummy.setText(categories_data.description);
            setDescriptionData();
            createDataObj.categories_data = categories_data;
        }
    }
    private void setDataToTitleAndHashTagAndImagIfEmpty()
    {
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        imgUploadCP.setLayoutParams(layoutParams);
        imgUploadCP.setImageDrawable(getResources().getDrawable(R.drawable.ic_uploadimg));
        imgUploadCP.setScaleType(ImageView.ScaleType.CENTER_CROP);
        eTxtDummy.setText("");
        eTxtHashTag.setText("");
        setDescriptionData();
        if(createDataObj!=null&&createDataObj.categories_data!=null) {
            createDataObj.categories_data = null;
        }
    }

    public void saveCreatedCardToSavePref(boolean checkValidation) {
        String titleName=autoCompleteTxt.getText().toString().trim();
        String imagPath= ((String) imgUploadCP.getTag());
        String hashTag=eTxtHashTag.getText().toString().trim();
        String description=txtDescription.getText().toString().trim();
        String numberOfShares=eTxtSharesCounter.getText().toString().trim();
        String shareEndDate=txtShareEndDate.getText().toString().trim();
//        String inviteeCount=txtInviteeNumber.getText().toString().trim();
        String changeLocation=txtLocationCP.getText().toString().trim();
        if(createDataObj.invitee_data==null)
        {
            createDataObj.invitee_data  =new ArrayList<InviteeData>();
        }
        int inviteeSize= createDataObj.invitee_data.size();

       
//if(inviteeCount.isEmpty())
//{
//    inviteeCount="0";
//}
        if(checkValidation)
        {
            if(titleName.isEmpty())
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.title_error_msg));
            }
            else if(changeLocation.isEmpty())
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.change_location_error_msg));
            }
            else if(hashTag.isEmpty())
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.hash_tag_error_msg));
            }
            else if(hashTag.isEmpty())
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.hash_tag_error_msg));
            }
            else if(description.isEmpty())
            {
                Util.showOkDialog(null,getActivity().getResources().getString(R.string.description_error_msg));
            }
//            else if(total_price.isEmpty()||total_price==0)
//            {
//                Util.showOkDialog(null, getActivity().getResources().getString(R.string.total_price_error_msg));
//            }
            else
            {
                PreviewFragment fragment = PreviewFragment.getInstance(setDataToCreateDataObject(titleName, imagPath, hashTag, description, numberOfShares, shareEndDate, inviteeSize, total_price));
                ft = Util.addFragments(fm, fragment);
            }
        }
        else
        {
            setDataToCreateDataObject(titleName, imagPath, hashTag, description, numberOfShares, shareEndDate,inviteeSize, total_price);
        }
    }

    private CreateData setDataToCreateDataObject( String titleName, String imagePath,String hashTag,String description, String numberOfShares,String shareEndDate,int inviteeCount,Double total_price) {

        if(createDataObj.categories_data==null)
        {
            Categories catListData=new Categories();
            catListData.name=Constants.OTHER;
            catListData.hashtag=hashTag;
            catListData.custom_image_path=null;
            if(imagePath!=null&&imagePath.contains(Constants.profileImge))
            {
                catListData.custom_image_path=imagePath;
            }
            else {
                catListData.image_path=imagePath;
            }



            createDataObj.categories_data=catListData;
        }
        else
        {
            createDataObj.categories_data.custom_image_path=null;
            if(imagePath!=null&&imagePath.contains(Constants.profileImge))
            {
                createDataObj.categories_data.custom_image_path=imagePath;
            }
            else {
                createDataObj.categories_data.image_path=imagePath;
            }


        }

        createDataObj.title=titleName;
        createDataObj.categories_data.description=description;
        createDataObj.no_of_shares=numberOfShares;
        createDataObj.invoice_price=total_price;
        createDataObj.post_guid=Util.getGUID(createDataObj.post_guid);
        createDataObj.per_share_price=this.perSharePrice;
        createDataObj.display_name=Config.getUserDispalyName();
        createDataObj.user_id=Config.getUserId();

        ArrayList<CreateData> createDataArrayList=new ArrayList<CreateData>();
        createDataArrayList.add(createDataObj);
//        Config.setCreateCardArray(createDataArrayList);
//        Config.savePreferencese();
        return createDataObj;
    }
  private void  setDataToCreatePostCard(ArrayList<CreateData> createDataArrayList)
  {
      try {
          createDataObj=null;
          String address = "";
          if (createDataArrayList != null && createDataArrayList.size() > 0)
          {
              createDataObj = createDataArrayList.get(0);
              if (createDataObj.categories_data != null)
              {
                  if (createDataObj.categories_data.name != null && (!createDataObj.categories_data.name.isEmpty()))
                      isFirstTime=true;
                      autoCompleteTxt.setText(createDataObj.title.trim());
                  if (createDataObj.categories_data.hashtag != null && (!createDataObj.categories_data.hashtag.isEmpty())) {
                      eTxtHashTag.setText(createDataObj.categories_data.hashtag);
                  }
                  Log.d(Tag,"createDataObj.categories_data. value is : "+createDataObj.categories_data.image_path);
                  if (createDataObj.categories_data.image_path != null && (!createDataObj.categories_data.image_path.isEmpty())) {
                      File f=new File(createDataObj.categories_data.image_path);
                      if(f.exists()) {
                          imgUploadCP.setTag(createDataObj.categories_data.image_path);
                          Util.loadImageFromSDcard(getActivity(),imgUploadCP,createDataObj.categories_data.image_path,R.drawable.ic_uploadimg);
//                          imgUploadCP.setImageBitmap(BitmapFactory.decodeFile(createDataObj.categories_data.image_path));
                      }
                  }
              }
              eTxtDummy.setText(createDataObj.categories_data.description);
              numberofShares = Integer.valueOf(createDataObj.no_of_shares);
              if(createDataObj.invitee_data==null)
              {

                  createDataObj.invitee_data=new ArrayList<InviteeData>();
              }
              setDataToInvitees(createDataObj.invitee_data);
              if (createDataObj.location_data != null && !createDataObj.location_data.address.isEmpty())
              {
                  address = createDataObj.location_data.address;
              }
              else
              {
                  address = SpliroApp.defaultLocation.address;
                  createDataObj.location_data=SpliroApp.defaultLocation;
              }
              txtLocationCP.setText(address);
              setDataToPrice(createDataObj.invoice_price,createDataObj.receipt_image_path);
              txtShareEndDate.setText(Util.setShareEndDate(createDataObj.post_expire_date));
              setDescriptionData();
              if(createDataObj.status.equals(Constants.CLOSED_STATUS))
              {
                  createDataObj.post_guid=null;
                  createDataObj.post_id=0;
              }
              setDataToAutoCOmpleteAdapter();
              isFirstTime=false;
          }
          else
          {
              total_price=0;
              perSharePrice=0;
              numberofShares=1;
              eTxtDummy.setText("");
              txttotal_priceCreate.setText("");
              createDataObj = new CreateData();
              txtShareEndDate.setText(Util.setShareEndDate(Util.getCurrentTimeInUTC()));
              createDataObj.post_expire_date=Util.getCurrentTimeInUTC();
              createDataObj.location_data=SpliroApp.defaultLocation;
              address = createDataObj.location_data.address;
              autoCompleteTxt.setText("");
              txtLocationCP.setText("");
              createDataObj.post_guid="";
              eTxtHashTag.setText("");
              LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
              imgUploadCP.setLayoutParams(layoutParams);
              imgUploadCP.setImageDrawable(getResources().getDrawable(R.drawable.ic_uploadimg));
              imgUploadCP.setScaleType(ImageView.ScaleType.CENTER_CROP);
              setDataToInvitees(new ArrayList<InviteeData>());
          }
          if (createDataObj.post_expire_date==null)
          {
              createDataObj.post_expire_date="";
          }
          txtLocationCP.setText(address);
          setNumbersOfShares();
          setDescriptionData();
          setDataToPrice(total_price,null);

      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
  }
    @Override
    public void onStop() {
        super.onStop();
        saveCreatedCardToSavePref(false);

    }
}