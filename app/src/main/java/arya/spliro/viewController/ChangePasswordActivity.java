package arya.spliro.viewController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkResponse;
import com.arya.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.cmds.CmdConstants;
import arya.spliro.model.ForgotPasswordModel;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/11/2015.
 */
public class ChangePasswordActivity extends AbstractFragmentActivity implements View.OnClickListener{
    private ImageView imgBackCPwd;
    private EditText eTxtOtp;
    private EditText eTxtChangeP;
    private EditText eTxtReTypeChangeP;
    private Button btnSubmitChangeP;
    private boolean flag=true;
    private ForgotPasswordModel forgotPasswordModel=new ForgotPasswordModel();
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.act_change_pwd);
        init();
    }

    private void init() {
        eTxtOtp=(EditText)findViewById(R.id.eTxtOtp);
        eTxtChangeP=(EditText)findViewById(R.id.eTxtChangeP);
        eTxtReTypeChangeP=(EditText)findViewById(R.id.eTxtReTypeChangeP);
        btnSubmitChangeP=(Button)findViewById(R.id.btnSubmitChangeP);
        eTxtOtp.setTypeface(SpliroApp.getFontLight());
        btnSubmitChangeP.setTypeface(SpliroApp.getFontSemiBold());
        eTxtChangeP.setTypeface(SpliroApp.getFontLight());
        eTxtReTypeChangeP.setTypeface(SpliroApp.getFontLight());
        btnSubmitChangeP.setOnClickListener(this);
        imgBackCPwd=(ImageView)findViewById(R.id.imgBackCPwd);
        imgBackCPwd.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return forgotPasswordModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        btnSubmitChangeP.setEnabled(true);
        if(o instanceof NetworkResponse)
        {
            NetworkResponse response= (NetworkResponse) o;
            if (response != null) {
                if(response.isSuccess())
                {
                    Util.showCenteredToast(response.getMessageFromServer());
                    onBackPressed();
                }
                else {
                    Util.showOkDialog(null, response.getMessageFromServer());
                }
            }
        }
    }

    public boolean onValidation() {
        boolean checkValidationFlag = false;
        if(Util.isDeviceOnline(true)) {
            String otp = eTxtOtp.getText().toString().trim();
            String pass = eTxtChangeP.getText().toString().trim();
            String rePass = eTxtReTypeChangeP.getText().toString().trim();
            if (otp.isEmpty()) {
                Util.showOkDialog(null, getResources().getString(R.string.otp_error_msg));
            } else if (Util.validationPassword(this, eTxtChangeP, btnSubmitChangeP)) {
                if (!pass.equals(rePass)) {
                    Util.showOkErrorDialog(getString(R.string.pass_do_not_match_error_msg), eTxtReTypeChangeP, btnSubmitChangeP);
                } else {
                    Util.showProDialog(Env.currentActivity.getString(R.string.wait));
                    forgotPasswordModel.doForgotPass(new String[]{CmdConstants.FORGOT_PASSWORD_VERIFY_OTP_USER, otp, pass});
                }
            }
        }
        return checkValidationFlag;
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.btnSubmitChangeP)
        {
            onValidation();
        }
        else if(vId==R.id.imgBackCPwd)
        {
            onBackPressed();
        }
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Util.endActAnimation(this);
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
