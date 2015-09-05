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
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.AppHeaderView;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/11/2015.
 */
public class ForgotPasswordActivity extends AbstractFragmentActivity implements View.OnClickListener{
    private ImageView imgBackFp;
    private EditText eTextMailForgot;
    private Button btnSubmit;
    private boolean flag=true;
    boolean checkValueIsNumeric=false;
    private String phone;
    private String eMailId;
    private String countrCode;
    private ForgotPasswordModel forgotPasswordModel=new ForgotPasswordModel();
    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.forgot_password);
        init();
    }

    private void init() {
          eTextMailForgot=(EditText)findViewById(R.id.eTextMailForgot);
         btnSubmit=(Button)findViewById(R.id.btnSubmit);
        eTextMailForgot.setTypeface(SpliroApp.getFontRegular());
        btnSubmit.setTypeface(SpliroApp.getFontRegular());
        btnSubmit.setOnClickListener(this);
        imgBackFp=(ImageView)findViewById(R.id.imgBackFp);
        imgBackFp.setOnClickListener(this);
        if(Util.isSamsung()) {
            eTextMailForgot.addTextChangedListener(new TextWatcher() {
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
                        eTextMailForgot.setFilters(fArray);
                    }
                    if (flag && checkValueIsNumeric && s.length() <= 14) {
                        int maxLength = 14;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        eTextMailForgot.setFilters(fArray);
                        ss = Util.setPhoneNumberFormat(s.toString().replaceAll("\\D", ""), before, start);
                    }
                    if (ss != null) {
                        flag = false;
                        eTextMailForgot.setText(ss);
                    } else {
                        flag = true;
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    eTextMailForgot.setSelection(eTextMailForgot.getText().length());
                }
            });

        }
        else
        {
            eTextMailForgot.setFilters(new InputFilter[] {Util.filter });
        }
    }

    @Override
    protected BasicModel getModel() {
        return forgotPasswordModel;
    }

    @Override
    public void update(Observable observable, Object o) {
        btnSubmit.setEnabled(true);
        if(o instanceof NetworkResponse)
        {
            NetworkResponse response= (NetworkResponse) o;
            if (response != null) {
                if(response.isSuccess())
                {
                    startActivity(new Intent(this,ChangePasswordActivity.class));
                    Util.startActAnimation(ForgotPasswordActivity.this);
                    Util.showCenteredToast(response.getMessageFromServer());
                    this.finish();
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
            btnSubmit.setEnabled(false);
            phone = eTextMailForgot.getText().toString();

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
                btnSubmit.setEnabled(true);
                Util.showOkDialog( null,getString(R.string.enter_valid_email_phone));
            }
            else if (phone.trim().length() != 0) {
                if(Util.isNumeric(phone)&&phone.trim().length() != Constants.MAXLENGTHOFPHONENUMBER)
                {
                    btnSubmit.setEnabled(true);
                    Util.showOkDialog( null,getString(R.string.phone_num_error_msg));
                }else if (eMailId.trim().length()!=0 && !Util.isEmailValid(phone))
                {
                    btnSubmit.setEnabled(true);
                    Util.showOkDialog( null, getString(R.string.email_error_msg));
                }
                else {

            Util.showProDialog(Env.currentActivity.getString(R.string.wait));
              forgotPasswordModel.doForgotPass(new String[]{CmdConstants.FORGOT_PASSWORD_SEND_OTP_USER,phone, countrCode});

                    }

            }
        }
        return checkValidationFlag;
    }

    @Override
    public void onClick(View view) {
int vId=view.getId();
        if(vId==R.id.btnSubmit)
        {
onValidation();
        }
        else if(vId==R.id.imgBackFp)
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
