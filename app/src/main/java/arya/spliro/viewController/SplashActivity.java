package arya.spliro.viewController;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;
import com.arya.lib.view.AbstractFragmentActivity;

import java.util.Observable;

import arya.spliro.R;
import arya.spliro.config.Config;
import arya.spliro.model.SplashModel;

public class SplashActivity extends AbstractFragmentActivity {

    SplashModel  splashModelObj = new SplashModel();

    @Override
    protected void onCreatePost(Bundle savedInstanceState) {
        setContentView(R.layout.act_splash);
        splashModelObj.loadScreen();

    }
    @Override
    protected BasicModel getModel() {
        return splashModelObj;
    }
    @Override
    public void update(Observable observable, Object o) {
        startActivity(new Intent(SplashActivity.this,
                HomeActivity.class));
        finish();
    }


}
