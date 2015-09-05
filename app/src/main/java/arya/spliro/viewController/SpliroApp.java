package arya.spliro.viewController;

import android.app.Application;
import android.graphics.Typeface;
import android.provider.ContactsContract;

import com.arya.lib.database.DatabaseMgr;
import com.arya.lib.init.Env;

import java.util.ArrayList;
import java.util.EventListener;

import arya.spliro.config.Config;
import arya.spliro.dao.LocationData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.database.SplrioDBHelper;
import arya.spliro.model.DataSyncModel;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/3/2015.
 */


public class SpliroApp extends Application {
    private static Typeface fontLight;
    private static Typeface fontSemiBold;
    private static Typeface fontRegular;
    public DatabaseMgr databaseMgr;
    public static ArrayList<LocationData> default_locations = new ArrayList<LocationData>(0);
    public static LocationData defaultLocation=new LocationData();
    @Override
    public void onCreate() {
        super.onCreate();
        fontSemiBold = Typeface.createFromAsset(getAssets(), "aileron_semibold.ttf");
        fontLight = Typeface.createFromAsset(getAssets(), "aileron_light.ttf");
        fontRegular = Typeface.createFromAsset(getAssets(), "aileron_regular.ttf");
        Env.init(this,new SplrioDBHelper(),null,true);

        try {

            databaseMgr= DatabaseMgr.getInstance();
            DatabaseMgrSpliro.init();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException (thread, e);
            }
        });
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        e.printStackTrace();
        System.exit(1);
    }


    public static Typeface getFontSemiBold() {
        return fontSemiBold;
    }
    public static Typeface getFontLight() {
        return fontLight;
    }
    public static Typeface getFontRegular() {
        return fontRegular;
    }
    public static Typeface getAppHeaderTypeface(){return fontLight ;}

}
