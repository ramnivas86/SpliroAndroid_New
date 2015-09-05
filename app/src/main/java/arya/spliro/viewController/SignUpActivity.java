package arya.spliro.viewController;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;
import com.facebook.LoginActivity;
import com.facebook.android.DialogError;
import com.facebook.android.FBUtil;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.dao.SignupData;
import arya.spliro.model.SignUpModel;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

public class SignUpActivity extends AbstractFragmentActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private AppHeaderView appHeaderView;
    private EditText eTxtFirstNameSU;
    private EditText eTextLastNameSU;
    private EditText eTxtEmailOrPhoneSU;
    private EditText eTxtPassSU;
    private EditText eTxtReTypePassSU;
    private TextView txtOrSU;
    private TextView txtSignUpWithFbSU;
    private TextView txtPolicySU;
    private Button btnJoin;
    private Facebook facebook=new Facebook(Constants.facebook_app_id);
    private LinearLayout linearSingUpWithFb;
    private CheckBox cBPolicySU;
    private boolean flag=true;
    boolean checkValueIsNumeric=false;
    public String fName="";
    public String lName="";
    public String phone="";
    public String countryCode="";
    public String eMailId="";
    public String pass="";
    public String reg_typ="";
    public String fb_access_token;
    public String fbaccess_token_expires;
    public String last_updated_time;
    public String fb_post_enabled;
    public String fb_user_id;
    public String gender;
private SignUpModel signupModelObj=new SignUpModel();
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.act_sign_up);
        init();
    }

    @Override
    protected BasicModel getModel() {
        return signupModelObj;
    }

    private void init()
    {
        appHeaderView=(AppHeaderView)findViewById(R.id.appHeaderView);
        appHeaderView.setHeaderContent(getResources().getDrawable(R.drawable.spl_ic_back_w_l), getResources().getDrawable(R.drawable.logo),null,  getResources().getColor(android.R.color.transparent));
         eTxtFirstNameSU=(EditText)findViewById(R.id.eTxtFirstNameSU);
          eTextLastNameSU=(EditText)findViewById(R.id.eTextLastNameSU);
          eTxtEmailOrPhoneSU=(EditText)findViewById(R.id.eTxtEmailOrPhoneSU);
          eTxtPassSU=(EditText)findViewById(R.id.eTxtPassSU);
          eTxtReTypePassSU=(EditText)findViewById(R.id.eTxtReTypePassSU);
          txtOrSU=(TextView)findViewById(R.id.txtOrSU);
        txtSignUpWithFbSU=(TextView)findViewById(R.id.txtSignUpWithFbSU);
          txtPolicySU=(TextView)findViewById(R.id.txtPolicySU);
          linearSingUpWithFb=(LinearLayout)findViewById(R.id.linearSingUpWithFb);
          cBPolicySU=(CheckBox)findViewById(R.id.cBPolicySU);
        btnJoin=(Button)findViewById(R.id.btnJoin);
        txtSignUpWithFbSU.setOnClickListener(this);
        txtPolicySU.setOnClickListener(this);
        linearSingUpWithFb.setOnClickListener(this);
        cBPolicySU.setOnCheckedChangeListener(this);
        btnJoin.setOnClickListener(this);
//        txtPolicySU .setMovementMethod(LinkMovementMethod.getInstance());
        setTypeFaceToAllFields();
//
        if(Util.isSamsung()) {
            eTxtEmailOrPhoneSU.addTextChangedListener(new TextWatcher() {
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
                        eTxtEmailOrPhoneSU.setFilters(fArray);
                    }
                    if (flag && checkValueIsNumeric && s.length() <= 14) {
                        int maxLength = 14;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        eTxtEmailOrPhoneSU.setFilters(fArray);
                        ss = Util.setPhoneNumberFormat(s.toString().replaceAll("\\D", ""), before, start);
                    }
                    if (ss != null) {
                        flag = false;
                        eTxtEmailOrPhoneSU.setText(ss);
                    } else {
                        flag = true;
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    eTxtEmailOrPhoneSU.setSelection(eTxtEmailOrPhoneSU.getText().length());
                }
            });

        }
        else
        {
            eTxtEmailOrPhoneSU.setFilters(new InputFilter[] {Util.filter });
        }
    }
    private void setTypeFaceToAllFields() {
        eTxtFirstNameSU.setTypeface(SpliroApp.getFontLight());
        eTextLastNameSU.setTypeface(SpliroApp.getFontLight());
        eTxtEmailOrPhoneSU.setTypeface(SpliroApp.getFontLight());
        eTxtPassSU.setTypeface(SpliroApp.getFontLight());
        eTxtReTypePassSU.setTypeface(SpliroApp.getFontLight());
        txtOrSU.setTypeface(SpliroApp.getFontSemiBold());
        btnJoin  .setTypeface(SpliroApp.getFontSemiBold());
        txtSignUpWithFbSU.setTypeface(SpliroApp.getFontLight());
        txtPolicySU.setTypeface(SpliroApp.getFontLight());
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId== R.id.btnJoin)
        {
            reg_typ= Constants.REG_TYP_WITHOUT_FACEBOOK;
           if( onValidation(eTxtFirstNameSU,eTextLastNameSU,eTxtEmailOrPhoneSU,eTxtPassSU,eTxtReTypePassSU,btnJoin,cBPolicySU,reg_typ))
           {
               Util.showProDialog(Env.currentActivity.getString(R.string.wait));
               if(eMailId.trim().length()!=0)
               {
                   phone="";
               }

               signupModelObj.doSignUp(fName,lName,eMailId,phone,countryCode,pass,reg_typ,fb_user_id,fb_access_token,fbaccess_token_expires,last_updated_time,fb_post_enabled,gender);
           }
        }
        else if(vId==R.id.imgBackAppH)
        {
            onBackPressed();
        }
        else if(vId==R.id.linearSingUpWithFb||vId==R.id.txtSignUpWithFbSU)
        {
            registerWithFacebook();
        }
        else if(vId==R.id.txtPolicySU)
        {
            Util.showOkDialog(null,getResources().getString(R.string.terms_and_conditions_message));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void update(Observable observable, Object o) {
        btnJoin.setEnabled(true);
        if(o instanceof NetworkResponse) {
            NetworkResponse response=(NetworkResponse) o;
            if(response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                JSONObject jObject=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);

                if(jObject.has(SignupData.KEY_REG_ID)) {
                    Intent  intent = new Intent(SignUpActivity.this, AuthorizationActivity.class);
                    try {
                        intent.putExtra(SignupData.KEY_REG_ID, jObject.getString(SignupData.KEY_REG_ID));
                        intent.putExtra(SignupData.KEY_VERIFICATION_CODE, jObject.getString(SignupData.KEY_VERIFICATION_CODE));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Util.showCenteredToast(response.getMessageFromServer());
                    startActivity(intent);
                    Util.startActAnimation(SignUpActivity.this);
                    this.finish();
                }
                else
                {
                    Util.showOkDialog(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            Intent  intent = new Intent(SignUpActivity.this, SignInActivity.class);
                            startActivity(intent);
                            Util.startActAnimation(SignUpActivity.this);
                            SignUpActivity.this.finish();
                        }
                    },getResources().getString(R.string.your_login_id_pass_send));
                }
            }
            else
            {
                Util.showOkDialog(null, response.getMessageFromServer());
            }
        }
        else if(o instanceof JSONObject)
        {
            JSONObject fbjson=(JSONObject)o;
            try {
                if(fbjson.has("email"))
                {
                    eMailId=fbjson.getString("email");
                    fb_user_id= fbjson.getString("id");
                    fName=fbjson.getString("first_name");
                    lName=fbjson.getString("last_name");
                    gender=fbjson.getString("gender");
                    last_updated_time=fbjson.getString("updated_time");
                    fb_post_enabled="1";
                    reg_typ=Constants.REG_TYP_WITH_FACEBOOK;
                    Util.showProDialog(Env.currentActivity.getString(R.string.wait));
                    signupModelObj.doSignUp(fName,lName,eMailId,phone,countryCode,pass,reg_typ,fb_user_id,fb_access_token,fbaccess_token_expires,last_updated_time,fb_post_enabled,gender);
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


    public boolean onValidation(EditText editFirstName,EditText editLastName,EditText editPhoneNo,EditText editPassword , EditText editRePassword,Button singUpBtn,CheckBox cbPolicy,String reg_typ) {
        boolean checkValidationFlag = false;
        if(Util.isDeviceOnline(true))
        {
            singUpBtn.setEnabled(false);
            fName = editFirstName.getText().toString().trim();
            lName = editLastName.getText().toString().trim();
             phone = editPhoneNo.getText().toString().trim();
            pass = editPassword.getText().toString().trim();
            String rePass = editRePassword.getText().toString().trim();

            this.reg_typ=reg_typ;
            String phoneNumber=phone;
            if(phone.trim().length()>=4)
            {
                phoneNumber = phone.substring(1, 4);
            }
            if(Util.isNumeric(phoneNumber))
            {
                eMailId="";
                phone=phone.replaceAll("\\D", "");
                countryCode= Constants.COUNTRYCODE;
            }
            else
            {
                eMailId=phone;
            }


            if (fName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
                Util. showOkErrorDialog(getString(R.string.first_name_error_msg),editFirstName,singUpBtn);
            }
            else if (Util.checkStringsContainsOnlySpecialChar(fName)) {
                Util.showOkErrorDialog(getString(R.string.first_name_only_special_msg),editFirstName,singUpBtn);
            }
            else if (Util.isNumeric(fName)) {
                Util. showOkErrorDialog(getString(R.string.first_name_only_numeric_msg),editFirstName,singUpBtn);
            }
            else if (fName.trim().length() > Constants.MAXLENGTHOFFIRSTNAME) {
                Util. showOkErrorDialog(getString(R.string.first_name_error_msg_max),editFirstName,singUpBtn);
            } else if (lName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
                Util.  showOkErrorDialog(getString(R.string.last_name_error_msg),editLastName,singUpBtn);
            }
            else if (Util.checkStringsContainsOnlySpecialChar(lName)) {
                Util.showOkErrorDialog(getString(R.string.last_name_only_special_msg),editLastName,singUpBtn);
            }
            else if (Util.isNumeric(lName)) {
                Util. showOkErrorDialog(getString(R.string.last_name_only_numeric_msg),editLastName,singUpBtn);
            }
            else if (lName.trim().length() > Constants.MAXLENGTHOFLASTNAME) {
                Util.   showOkErrorDialog(getString(R.string.last_name_error_msg_max),editLastName,singUpBtn);

            }

            else if (phone.trim().length() == 0) {
                Util. showOkErrorDialog(getString(R.string.enter_valid_email_phone),editPhoneNo,singUpBtn);

            }
            else if (phone.trim().length() != 0) {

                if(Util.isNumeric(phone)&&phone.trim().length() != Constants.MAXLENGTHOFPHONENUMBER)
                {
                    Util.  showOkErrorDialog(getString(R.string.phone_num_error_msg),editPhoneNo,singUpBtn);

                }else if (eMailId.trim().length()!=0 && (!Util.isEmailValid(phone)))
                {
                    Util. showOkErrorDialog(getString(R.string.email_error_msg),editPhoneNo,singUpBtn);
                }
                else {
                    if ( pass.trim().length() < Constants.MINIMUMLENGTHOFPASS) {
                        Util.showOkErrorDialog(getString(R.string.password_must_error_msg),editPassword,singUpBtn);

                    }
                    else if (!Util.passwordPatternValidation(pass))
                    {
                        Util.showOkErrorDialog(getString(R.string.msg_password_must_contain_numeric_special_character),editPassword,singUpBtn);
                    }
                    else if (!pass.equals(rePass)) {
                        Util.showOkErrorDialog(getString(R.string.pass_do_not_match_error_msg),editRePassword,singUpBtn);

                    }
                    else if(!cbPolicy.isChecked())
                    {
                        singUpBtn.setEnabled(true);
                        Util.showOkDialog(null, getString(R.string.check_policy_signup));

                    }
                    else {
                        if ( pass.trim().length() < Constants.MINIMUMLENGTHOFPASS) {
                            Util.showOkErrorDialog(getString(R.string.password_must_error_msg),editPassword,singUpBtn);

                        } else if (!pass.equals(rePass)) {
                            Util.showOkErrorDialog(getString(R.string.pass_do_not_match_error_msg),editRePassword,singUpBtn);
                        }
                        else if(!cbPolicy.isChecked())
                        {
                            singUpBtn.setEnabled(true);
                            Util.showOkDialog(null, getString(R.string.check_policy_signup));
                        }
                        else {
                            checkValidationFlag = true;
                        }
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
    private void registerWithFacebook()
    {
        try {
            if(Util.isDeviceOnline(true))
            {
                if (android.os.Build.VERSION.SDK_INT > 9)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                facebook.logout(SignUpActivity.this);
                loginWithFacebook(facebook);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void loginWithFacebook(final Facebook fb) {
        fb.authorize(SignUpActivity.this, new String[]{"email", "public_profile","publish_stream"}, facebook.FORCE_DIALOG_AUTH, new Facebook.DialogListener() {
            @Override
            public void onComplete(Bundle values) {
                try {
                    fb_access_token=fb.getAccessToken();
                    fbaccess_token_expires=""+fb.getAccessExpires();
                    signupModelObj.getFacebookInfo(fb);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFacebookError(FacebookError e) {
                System.out.print(e + "");
            }

            @Override
            public void onError(DialogError e) {

            }

            @Override
            public void onCancel() {
                System.out.print("cancelled");
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
