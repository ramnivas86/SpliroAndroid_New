package arya.spliro.model;

import com.arya.lib.init.Env;
import com.arya.lib.model.BasicModel;

import java.util.Timer;
import java.util.TimerTask;

import arya.spliro.config.Config;

/**
 * Created by Admin on 8/3/2015.
 */
public class SplashModel extends BasicModel {
    public boolean processIsgoingOn=false;
    final int welcomeScreenDisplay = 4000;
    Timer timer;
    public void loadScreen() {
if(timer==null) {
    timer=new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            Config.init(Env.currentActivity);
            SplashModel.this.notifyObservers(null);
            timer=null;
        }
    }, welcomeScreenDisplay);
}
    }
}
