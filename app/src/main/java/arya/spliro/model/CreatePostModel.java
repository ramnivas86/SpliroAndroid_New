package arya.spliro.model;

import android.content.Context;
import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.database.DatabaseMgr;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.dao.Categories;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/12/2015.
 */
public class CreatePostModel extends BasicModel {
  private  ArrayList<Categories> catArrayList;
    public HashMap<String,Categories> categoryListHashMap;
    private Task task;
    public void getCatgoriesDataFromLocalDB(String keyword)
    {
        if(task==null) {
            task = new Task();
            task.execute(keyword);
        }
    }
    public class Task extends AsyncTask<String, String, ArrayList<Categories>> {
        @Override
        protected ArrayList<Categories> doInBackground(String... params) {
            catArrayList= DatabaseMgrSpliro.getCategoriesList(params[0]);
            categoryListHashMap=new HashMap<String,Categories>();
            for(Categories categoriesObj:catArrayList)
            {
                categoryListHashMap.put(categoriesObj.name.toLowerCase(),categoriesObj);
            }
            return catArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Categories> arrayList) {
            Util.dimissProDialog();
           CreatePostModel. this.notifyObservers(arrayList);
            task=null;
        }
    }
}
