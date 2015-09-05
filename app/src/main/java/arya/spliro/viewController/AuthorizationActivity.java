package arya.spliro.viewController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.dao.SignupData;
import arya.spliro.model.AuthorizationModel;
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

public class AuthorizationActivity extends AbstractFragmentActivity implements View.OnClickListener {
    private EditText eTxtAuthCode;
    private  Button btnActAccount  ;
    private Button btResendPin;
    private  String authCode="";
    private String regId="";
    private AuthorizationModel authorizationModel=new AuthorizationModel();
    private boolean resendPinCodeFlag=false;
    private ImageView imgBackAuth;

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.act_authorization);
        init();
    }

    private void init() {
        eTxtAuthCode=(EditText)findViewById(R.id.eTxtAuthCode);
        btnActAccount=(Button)findViewById(R.id.btnActAccount);
        btResendPin=(Button)findViewById(R.id.btResendPin);
        imgBackAuth=(ImageView)findViewById(R.id.imgBackAuth);
        imgBackAuth.setOnClickListener(this);
        btnActAccount  .setOnClickListener(this);
        btResendPin.setOnClickListener(this);
        eTxtAuthCode.setTypeface(SpliroApp.getFontRegular());
        btnActAccount .setTypeface(SpliroApp.getFontSemiBold());
        btResendPin.setTypeface(SpliroApp.getFontSemiBold());
        Bundle b=getIntent().getExtras();
        if(b!=null&&b.containsKey(SignupData.KEY_REG_ID))
        {
            authCode=getIntent().getExtras().getString(SignupData.KEY_VERIFICATION_CODE);
            regId=getIntent().getExtras().getString(SignupData.KEY_REG_ID);
            eTxtAuthCode.setText(authCode);
        }
    }
    @Override
    protected BasicModel getModel() {
        return authorizationModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        btResendPin.setEnabled(true);
        btnActAccount.setEnabled(true);
    if(o instanceof NetworkResponse)
    {
        NetworkResponse response= (NetworkResponse) o;
        if(response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA))
        {
            JSONObject dataObj=response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
            if(dataObj.has(SignupData.KEY_USER_ID))
            {
                Intent intent=new Intent(AuthorizationActivity.this, TabActivity.class);
                startActivity(intent);
                Util.startActAnimation(AuthorizationActivity.this);
                finish();
            }
           else  if(dataObj.has(SignupData.KEY_VERIFICATION_CODE)&&resendPinCodeFlag)
            {
                try {
                    authCode=dataObj.getString(SignupData.KEY_VERIFICATION_CODE);
                    eTxtAuthCode.setText(authCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String toString = Config.getUserphone();
                if(toString.trim().length()==0) {
                    toString = Config.getUseremail();
                }
                Util.showOkDialog(null,getString(R.string.varification_code_has)+" "+toString+".");
            }
        }
        else
        {
//            if (resendPinCodeFlag)
//            {
//                Util.showOkDialog(null,getString(R.string.authorization_error));
//            }
//            else
//            {
                Util.showOkDialog(null,response.getMessageFromServer());
//            }
        }
    }
    }



    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.btnActAccount)
        {
            resendPinCodeFlag=false;
       onAuthVerification();
        }
        else if(vId==R.id.btResendPin)
        {
            resendPinCodeFlag=true;
            onResendVerificationCode();
        }
        else if(vId==R.id.imgBackAuth)
        {
            onBackPressed();
        }
    }

    public void onResendVerificationCode() {
        if (Util.isDeviceOnline(true)) {
            btResendPin.setEnabled(false);
                Util.showProDialog( getString(R.string.wait));
                authorizationModel.doResendPinCode(regId, Config.getUserphone(),Config.getUseremail(), resendPinCodeFlag);
        }
    }

    public void onAuthVerification() {
        if (Util.isDeviceOnline(true)) {
            btnActAccount.setEnabled(false);
            authCode = eTxtAuthCode.getText().toString().trim();
            if (authCode.length() == 0) {
                Util.showOkDialog(null, getString(R.string.please_enter_correct_varification_code));
                btnActAccount.setEnabled(true);
            } else {
                Util.showProDialog( getString(R.string.wait));
                authorizationModel.doVarification(regId,authCode,resendPinCodeFlag);
            }

        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.endActAnimation(AuthorizationActivity.this);
    }
}
