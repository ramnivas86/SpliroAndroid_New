package arya.spliro.viewController;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.slidingTabBar.TabActivity;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

public class HomeActivity extends BaseFragActivity implements View.OnClickListener {
    private Button btnNewUser;
    private Button btnExistUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Config.getUserId()!=Constants.INVALID_ID)
        {
            startActivity(new Intent(this,TabActivity.class));
            Util.startActAnimation(HomeActivity.this);
        }
        else {
            setContentView(R.layout.act_home);
            init();
        }
    }

private void init()
{
    btnNewUser=(Button) findViewById(R.id.btnNewUser);
    btnExistUser=(Button)findViewById(R.id.btnExistUser);
    btnNewUser.setOnClickListener(this);
    btnExistUser.setOnClickListener(this);
    setTypeFaceToAllFields();
}
    @Override
    public void onClick(View view) {
int vId=view.getId();
        if(vId== R.id.btnNewUser)
        {
            startActivity(new Intent(view.getContext(),SignUpActivity.class));
            Util.startActAnimation(HomeActivity.this);
        }
        else if(vId== R.id.btnExistUser)
        {
            startActivity(new Intent(view.getContext(),SignInActivity.class));
            Util.startActAnimation(HomeActivity.this);
        }
    }
public void setTypeFaceToAllFields()
{
    btnNewUser.setTypeface(SpliroApp.getFontSemiBold());
    btnExistUser.setTypeface(SpliroApp.getFontSemiBold());
}
}
