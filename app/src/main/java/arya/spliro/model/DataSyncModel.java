package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.init.Env;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.dao.Categories;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.utility.CategoriesMgr;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/25/2015.
 */
public class DataSyncModel {

    private static Task task;

    public void syncData() {

        if (task == null&&Util.isDeviceOnline(false)) {
            task = new Task();
            task.execute();
        }
    }

    public class Task extends AsyncTask<String, String, ArrayList<Categories>> {
        @Override
        protected ArrayList<Categories> doInBackground(String... params) {
            try {
                CategoriesMgr.syncCategoryData();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Categories> categorieses) {
            super.onPostExecute(categorieses);
            task = null;
        }
    }


}
