package arya.spliro.viewController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;
import com.facebook.android.DialogError;
import com.facebook.android.FBUtil;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.dao.SignupData;
import arya.spliro.model.ForgotPasswordModel;
import arya.spliro.model.SignInModel;
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

public class SignInActivity extends AbstractFragmentActivity implements View.OnClickListener{
    private LinearLayout linearLoginWF;
    private TextView txtSignInWithFb;
    private EditText eTextMail;
    private EditText eTextLPassword;
    private TextView txtForgotPass;
    private TextView txtOr;
    private  Button btnSignIn;

    SignInModel model = new SignInModel();
    private String phone="";
    private String countrCode="";
    private String pass="";
    private String eMailId="";
    private String login_type="";
    private String fb_user_id="";
    private String fb_access_token;
    private String fbaccess_token_expires;
    private String last_updated_time;
    private String fb_post_enabled;


    private String dummyEmail="ram1@gmail.com";
    private String dummyPass="qwertyui";
    private ImageView imgBackS;
    private boolean flag=true;
    boolean checkValueIsNumeric=false;
    private Facebook facebook=new Facebook(Constants.facebook_app_id);

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.sign_in);
        init();
        Button button = (Button)findViewById(R.id.btnSignIn);
        button.setOnClickListener(this);
    }

    private void init() {
        imgBackS=(ImageView)findViewById(R.id.imgBackS);
        linearLoginWF=(LinearLayout)findViewById(R.id.linearLoginWF);
       txtSignInWithFb=(TextView)findViewById(R.id.txtSignInWithFb);
       eTextMail=(EditText)findViewById(R.id.eTextMail);
         eTextLPassword=(EditText)findViewById(R.id.eTextLPassword);
      txtForgotPass=(TextView)findViewById(R.id.txtForgotPass);
       txtOr=(TextView)findViewById(R.id.txtOr);
      btnSignIn=(Button)findViewById(R.id.btnSignIn);
        linearLoginWF.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
        btnSignIn .setOnClickListener(this);
        imgBackS.setOnClickListener(this);

        if(Util.isSamsung()) {
            eTextMail.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String ss = null;
                    if (s.toString().trim().length() <= 3 && Util.isNumeric(s.toString())) {
                        checkValueIsNumeric = true;
                    } else if (s.toString().trim().length() == 0) {
                        checkValueIsNumeric = false;
                        int maxLength = 100;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        eTextMail.setFilters(fArray);
                    }
                    if (flag && checkValueIsNumeric && s.length() <= 14) {
                        ss = Util.setPhoneNumberFormat(s.toString().replaceAll("\\D", ""), before, start);
                        int maxLength = 14;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        eTextMail.setFilters(fArray);
                    }
                    if (ss != null) {
                        flag = false;
                        eTextMail.setText(ss);
                    } else {
                        flag = true;
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    eTextMail.setSelection(eTextMail.getText().length());
                }
            });
        }
        else
        {
            eTextMail.setFilters(new InputFilter[] { Util.filter });
        }

    setTypeFaceToallFields();
//        eTextMail.setText(dummyEmail);
//        eTextLPassword.setText(dummyPass);
    }
    private void setTypeFaceToallFields() {
        txtSignInWithFb.setTypeface(SpliroApp.getFontLight());
        eTextMail.setTypeface(SpliroApp.getFontLight());
        eTextLPassword.setTypeface(SpliroApp.getFontLight());
        txtForgotPass.setTypeface(SpliroApp.getFontLight());
        txtOr.setTypeface(SpliroApp.getFontSemiBold());
        btnSignIn.setTypeface(SpliroApp.getFontSemiBold());
    }




    @Override
    protected BasicModel getModel() {
        return model;
    }
    @Override
    public void update(Observable observable, Object data) {
        if(data instanceof NetworkResponse) {
            btnSignIn.setEnabled(true);
            NetworkResponse response=(NetworkResponse) data;
            if(response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                JSONObject jObject=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
                Intent intent=new Intent(SignInActivity.this, TabActivity.class);
                startActivity(intent);
                Util.startActAnimation(SignInActivity.this);
                this.finish();
            }
            else
            {
                Util.showOkDialog(null, response.getMessageFromServer());
            }
        }
        else if(data instanceof JSONObject)
        {
            JSONObject fbjson=(JSONObject)data;
            try {
                if(fbjson.has("email"))
                {
                    eMailId=fbjson.getString("email");
                    login_type=Constants.REG_TYP_WITH_FACEBOOK;
                    countrCode="";
                    Util.showProDialog(Env.currentActivity.getString(R.string.wait));
                    model.doLogin(eMailId, countrCode, pass,login_type);
                }
                else
                {
                    Util.showOkDialog(null,getResources().getString(R.string.email_not_retreived_message));
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        int vId=v.getId();
        if(vId==R.id.btnSignIn) {
            onValidation();
        }
        else  if(vId==R.id.linearLoginWF)
        {
            loginWithFacebook(facebook);
        }
        else  if(vId==R.id.txtForgotPass)
        {
            Intent intent=new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            Util.startActAnimation(SignInActivity.this);
        } else if(vId==R.id.imgBackS)
        {
           onBackPressed();
        }
    }

    public boolean onValidation() {
        boolean checkValidationFlag = false;
        if(Util.isDeviceOnline(true)) {
            btnSignIn.setEnabled(false);
            phone = eTextMail.getText().toString().trim();
            pass = eTextLPassword.getText().toString().trim();
            String phoneNumber=phone;
            if(phone.trim().length()>=4)
            {
                phoneNumber = phone.substring(1, 4);
            }
            if(Util.isNumeric(phoneNumber))
            {
                eMailId="";
                phone=phone.replaceAll("\\D", "");
                countrCode= Constants.COUNTRYCODE;
            }
            else
            {
                countrCode="";
                eMailId=phone;
            }
             if (phone.trim().length() == 0) {

                 Util.showOkErrorDialog(getString(R.string.enter_valid_email_phone),eTextMail,btnSignIn);

            }
            else if (phone.trim().length() != 0) {
                if(Util.isNumeric(phone)&&phone.trim().length() != Constants.MAXLENGTHOFPHONENUMBER)
                {
                    Util.showOkErrorDialog(getString(R.string.phone_num_error_msg),eTextMail,btnSignIn);
                }else if (eMailId.trim().length()!=0 && (!Util.isEmailValid(phone)))
                {
                    Util.showOkErrorDialog(getString(R.string.email_error_msg),eTextMail,btnSignIn);
                }
                else {
                    if (pass.trim().length() == 0 || pass.trim().length() < Constants.MINIMUMLENGTHOFPASS) {
                        Util.showOkErrorDialog(getString(R.string.password_must_error_msg),eTextLPassword,btnSignIn);

                    }
                    else if (!Util.passwordPatternValidation(pass))
                    {
                        Util.showOkErrorDialog(getString(R.string.msg_password_must_contain_numeric_special_character),eTextLPassword,btnSignIn);
                    }
                    else {
                        checkValidationFlag = true;
                        if(countrCode.trim().length()==0)
                        {
                            phone=eMailId;
                        }
                        Util.showProDialog(Env.currentActivity.getString(R.string.wait));
                        login_type=Constants.REG_TYP_WITHOUT_FACEBOOK;
                        model.doLogin(phone, countrCode, pass,login_type);
                    }
                }
            }
        }
        return checkValidationFlag;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Util.endActAnimation(this);
    }
    private void loginWithFacebook(Facebook fb)
    {
        try
        {
            if (android.os.Build.VERSION.SDK_INT > 9)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            fb.logout(SignInActivity.this);
            getFacebookUser(fb);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getFacebookUser(final Facebook fb)
    {
        fb.authorize(SignInActivity.this, new String[]{"email", "public_profile","publish_stream"}, fb.FORCE_DIALOG_AUTH, new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                try {
                    fb_access_token=fb.getAccessToken();
                    fbaccess_token_expires=""+fb.getAccessExpires();
                    model.getFacebookInfo(fb);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFacebookError(FacebookError e) {

            }

            @Override
            public void onError(DialogError e) {

            }

            @Override
            public void onCancel() {

            }
        });

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
                    .getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
        return ret;
    }

}
