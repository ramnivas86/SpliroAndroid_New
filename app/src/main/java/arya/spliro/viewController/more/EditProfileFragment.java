package arya.spliro.viewController.more;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.custom.CategoryListDialog;
import arya.spliro.custom.OpenCameraDialog;
import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.UserProfileData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.model.EditProfileModel;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;
import arya.spliro.viewController.HomeActivity;
import arya.spliro.viewController.SpliroApp;

public class EditProfileFragment extends AbstractFragment implements View.OnClickListener{
    private AppHeaderView appHeaderView;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editDisplayName;
    private EditText editEmailPhn;

    private EditText editAbout;
    private EditText editPassword;
    private TextView txtUserName;
    private CheckBox checkNotification;
    TextView txtLogout;
    TextView txtEdit;
    ImageView imgBackAppH;
    ImageView imgInterestCat;
    ImageView profile_img;
    boolean categoryclickable;
    private String imagepath;
    private String filePath;
    private String fileName;
    boolean passwordFlag=false;
    boolean rePasswordFlag=false;
   // RequestCreator rc;
    private  OpenCameraDialog pickImageDialog;
    private LinearLayout linearRetypePassword;
    private EditText editRetypePassword;

    View view;
    EditProfileModel mydata=new EditProfileModel();
    ArrayList<Categories> categoryList;
    UserProfileData userProfileData;


    @Override
    protected View onCreateViewPost(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.frg_edit_profile,container,false);
        init();
        return view;
    }

    @Override
    protected BasicModel getModel() {
        return mydata;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }

    private void init()
    {
//        appHeaderView=(AppHeaderView)view.findViewById(R.id.appHeaderView);
//        appHeaderView.setHeaderContent(getResources().getDrawable(R.drawable.spl_ic_back_w_l), getResources().getString(R.string.my_profile_cap),getResources().getString(R.string.edit),  getResources().getColor(android.R.color.transparent));
        editFirstName =(EditText)view.findViewById(R.id.editFirstName);
        editLastName=(EditText)view.findViewById(R.id.editLastName);
        editDisplayName=(EditText)view.findViewById(R.id.editDisplayName);
        editEmailPhn=(EditText)view.findViewById(R.id.editEmailPhn);
        editAbout=(EditText)view.findViewById(R.id.editAbout);
        editPassword=(EditText)view.findViewById(R.id.editPassword);
        checkNotification=(CheckBox)view.findViewById(R.id.checkNotification);
        txtLogout=(TextView)view.findViewById(R.id.txtLogout);
        txtUserName=(TextView)view.findViewById(R.id.txtUserName);
        txtEdit=(TextView)view.findViewById(R.id.txtEdit);
        imgBackAppH=(ImageView)view.findViewById(R.id.imgBackAppH);
        imgInterestCat=(ImageView)view.findViewById(R.id.imgInterestCat);
        profile_img=(ImageView)view.findViewById(R.id.profile_img);
        editRetypePassword=(EditText)view.findViewById(R.id.editRetypePassword);
        linearRetypePassword=(LinearLayout)view.findViewById(R.id.linearRetypePassword);
        txtLogout.setOnClickListener(this);
        txtEdit.setOnClickListener(this);
        imgBackAppH.setOnClickListener(this);
        imgInterestCat.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        editPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!passwordFlag)
                {
                    editPassword.setText("");
                    passwordFlag=true;
                    linearRetypePassword.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        editRetypePassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!rePasswordFlag)
                {
                    editRetypePassword.setText("");
                    rePasswordFlag=true;

                }
                return false;
            }
        });

        mydata.getProfileData();

        setTypeFaceToAllFields();
        setEditable(false);
       // setProfileData();

    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.txtLogout)
        {
            Util.logOutMethod(getActivity(),true);


        }
        else if(view.getId()==R.id.txtEdit)
        {

            if(txtEdit.getText().toString().equals("edit"))
            {
                setEditable(true);
                txtEdit.setText("save");
            }
            else if(txtEdit.getText().toString().equals("save"))
            {
                if(onValidation())
                {
                    String first_name=editFirstName.getText().toString();
                    String last_name=editLastName.getText().toString();
                    String about_me=editAbout.getText().toString();
                    String email="";
                    String password="";
                    String phone=editEmailPhn.getText().toString();
                    String display_name=editDisplayName.getText().toString();
                    if(!editPassword.getText().toString().equals("12345678"))
                    password=editPassword.getText().toString();
                    int noti_prif=0;
                    if(checkNotification.isChecked())
                    {
                        noti_prif=1;
                    }
                    else
                    {
                        noti_prif=0;
                    }

                    if (Util.isNumeric(phone))
                    {
                        email = "";
                        phone = phone.replaceAll("\\D", "");
                    }
                    else
                    {
                        email = phone;
                        phone="";
                    }
                    //String first_name,String last_name,String phone,String email,String about_me
                    mydata.myProfileData(first_name,last_name,display_name,phone,email,about_me,categoryList,imagepath,fileName,password,noti_prif);
                }
            }
            /*else if(view.getId()==R.id.editPassword)
            {
                editPassword.setText("");
            }*/


        }
        else if(view.getId()==R.id.imgBackAppH)
        {
            getActivity().onBackPressed();
            Util.hideKeyBoardMethod(getActivity(),imgBackAppH);
//            getFragmentManager().beginTransaction().remove(this).commit();
        }
        else if(view.getId()==R.id.imgInterestCat)
        {
            CategoryListDialog dialog_cat=null;
            if(dialog_cat==null)
            {
                dialog_cat = CategoryListDialog.getInstance(categoryList,categoryclickable);
                dialog_cat.show(getFragmentManager(), "");
            }
        }
        else if(view.getId()==R.id.profile_img)
        {
            if(pickImageDialog==null) {
                pickImageDialog = new OpenCameraDialog();
            }
            if(!pickImageDialog.isVisible())
                pickImageDialog.show(getActivity().getSupportFragmentManager(),  "missiles");
        }

    }
    public static void logOutMethod(Context ctx,boolean setAnimation) {

        Config.removeOrClearPerferance(null);
//        dbhelHelper.clearDataBase();
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        Intent intent = new Intent(ctx.getApplicationContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();

        if(setAnimation)
        {
//            endActAnimation(((Activity) ctx));
        }

    }
    private void setTypeFaceToAllFields()
    {
        editFirstName.setTypeface(SpliroApp.getFontLight());
        editLastName.setTypeface(SpliroApp.getFontLight());
        editDisplayName.setTypeface(SpliroApp.getFontLight());
        editEmailPhn.setTypeface(SpliroApp.getFontLight());
        editAbout.setTypeface(SpliroApp.getFontLight());

    }
    private boolean onValidation()
    {
        boolean flag=false;
        if(Util.validationFirstName(getActivity(),editFirstName,txtLogout))
        {
            if(Util.validationLastName(getActivity(), editLastName, txtLogout))
            {
                if(Util.validationDisplayName(getActivity(), editDisplayName, txtLogout))
                {
                    if(Util.validationPhoneEmail(getActivity(), editEmailPhn, txtLogout))
                    {
                        if(Util.validationAboutYourSelf(getActivity(), editAbout, txtLogout))
                        {
                            if(!passwordFlag)
                            {
                                flag=true;
                            }else
                            {
                                if(ValidationPassword(getActivity(), editPassword, editRetypePassword.getText().toString(),txtLogout))
                                {
                                    flag=true;
                                }
                            }

                        }

                    }
                }
            }
        }

        return flag;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if(data instanceof NetworkResponse)
        {
            setEditable(false);
            txtEdit.setText("edit");
            mydata.getProfileData();

        }
        else if(data instanceof ArrayList)
        {
            ArrayList<Object> arrayList=(ArrayList<Object>)data;
            if(!arrayList.isEmpty() && arrayList.get(0) instanceof Categories)
            {
                categoryList = (ArrayList<Categories>) data;
                setProfileData();
                setEditable(false);
                txtEdit.setText("edit");
            }
            else if(!arrayList.isEmpty() && arrayList.get(0) instanceof UserProfileData)
            {
                userProfileData=(UserProfileData) ((ArrayList<Object>) data).get(0);
                //mydata.getProfileData();
                mydata.getCategoryData(userProfileData.selected_category);
            }

        }

    }

    private void setEditable(boolean flag)
    {

        editFirstName.setEnabled(flag);
        editLastName.setEnabled(flag);
        editDisplayName.setEnabled(flag);
        editEmailPhn.setEnabled(flag);
        editAbout.setEnabled(flag);
        editPassword.setEnabled(flag);
        profile_img.setEnabled(flag);
        checkNotification.setClickable(flag);
        categoryclickable=flag;

        // focus on first name and show keybord
        if(flag)
        {
            editFirstName.requestFocus();
            editFirstName.setSelection(editFirstName.getText().toString().trim().length());
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editFirstName, InputMethodManager.SHOW_IMPLICIT);
        }
       /* editFirstName.setFocusable(flag);
        editLastName.setFocusable(flag);
        editDisplayName.setFocusable(flag);
        editEmailPhn.setFocusable(flag);
        editAbout.setFocusable(flag);
        editPassword.setFocusable(flag);*/



    }
    private void setProfileData()
    {

        if(userProfileData.first_name!=null)
            editFirstName.setText(userProfileData.first_name);
        if(userProfileData.last_name!=null)
            editLastName.setText(userProfileData.last_name);
        if(userProfileData.display_name!=null)
            editDisplayName.setText(userProfileData.display_name);
        if(userProfileData.email!=null)
            editEmailPhn.setText(userProfileData.email);
        else if(userProfileData.phone_number!=null)
            editEmailPhn.setText(userProfileData.phone_number);

        if(userProfileData.about_me!=null)
            editAbout.setText(Config.getUserAboutMe());

        if(userProfileData.display_name!=null)
            txtUserName.setText(userProfileData.display_name);

        if(userProfileData.notify_pref!=0)
            checkNotification.setChecked(true);
        else
            checkNotification.setChecked(false);
       if(userProfileData.profile_picture_url!=null&& userProfileData.profile_picture_url.length()!=0)
          Util.loadImage(getActivity(),profile_img,userProfileData.profile_picture_url,R.drawable.user);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.PICK_IMAGE_FROM_GALLERY){
            Constants.imageUri=data.getData();
        }
        if(requestCode == Constants.PICK_IMAGE_FROM_CAMERA||requestCode == Constants.PICK_IMAGE_FROM_GALLERY) {

            goToImageCropView(Constants.imageUri);
        }
        else if (requestCode == Constants.PICK_CROP_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap image = extras.getParcelable("data");

                imagepath = getPath(Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), image, "profile_image" + ".jpg", "Temp image for profile")),getActivity());
                fileName=imagepath.substring(imagepath.lastIndexOf("/")+1,imagepath.length());

                if (!(image == null)) {
                   // callToAsynTask = true;
                    profile_img.setImageBitmap(image);
                }
            }
        }
    }
    public String getPath(Uri uri,Context context) {
        if(uri != null){
            String[] projection = { MediaStore.Images.Media.DATA };
            @SuppressWarnings("deprecation")
            Cursor cursor = ((Activity)context).managedQuery(uri, projection, null, null, null);
            // IN CASE OF SELECTING IMAGE FROM FILE MANAGER CUSSER IS NULL
            if(cursor != null)
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                return cursor.getString(column_index);
            }
            else{
                return null;
            }
        }else{
            return null;
        }
    }
        private void goToImageCropView(Uri fileUri)
        {
            try {
                Intent intent = new Intent("com.android.camera.action.CROP");
                // ******** code for crop image
                intent.setDataAndType(fileUri, "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, Constants.PICK_CROP_IMAGE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    boolean ValidationPassword(Context context,EditText editText,String repassword,View singUpBtn)
    {
        String pass=editText.getText().toString();
        int startIndex=-1;
        int endIndex=-1;
        if(pass.length()!=0) {
            startIndex = pass.charAt(0);
            endIndex = pass.charAt(pass.length() - 1);
        }
        boolean flag=false;
        if (pass.length() == 0 || pass.length() < Constants.MINIMUMLENGTHOFPASS)
        {
            Util.showOkErrorDialog(context.getString(R.string.password_must_error_msg),editText,singUpBtn);
        }
        else if(startIndex!=-1 && endIndex!=-1 &&startIndex==endIndex)
        {
            Util.showOkErrorDialog(context.getString(R.string.space_validation),editText,singUpBtn);
        }
        else if (!Util.passwordPatternValidation(pass))
        {
            Util.showOkErrorDialog(context.getString(R.string.msg_password_must_contain_numeric_special_character),editText,singUpBtn);
        }
        else if(!pass.equals(repassword))
        {
            Util.showOkErrorDialog(context.getString(R.string.msg_pass_and_repass_msg),editText,singUpBtn);
        }
        else
        {
            flag=true;
        }
        return  flag;
    }

}


