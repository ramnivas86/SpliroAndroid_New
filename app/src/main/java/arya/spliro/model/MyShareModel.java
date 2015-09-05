package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.model.BasicModel;

import java.util.ArrayList;

import arya.spliro.dao.CreateData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.utility.Util;

/**
 * Created by phoosaram on 8/27/2015.
 */
public class MyShareModel extends BasicModel {
    GetSharesTask getSharesTask;


    public void getSharesList(String status)
    {
        if(getSharesTask==null)
        {
            getSharesTask=new GetSharesTask();
            getSharesTask.execute(status);
        }

    }

    class GetSharesTask extends AsyncTask<String,Void,ArrayList<CreateData>>
    {
        @Override
        protected void onPreExecute() {
            Util.showProDialog(null);
        }

        @Override
        protected ArrayList<CreateData> doInBackground(String... strings) {
            DatabaseMgrSpliro databaseMgrSpliro=new DatabaseMgrSpliro();
            ArrayList<CreateData> arrayList=databaseMgrSpliro.getSharesList(strings[0]);
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<CreateData> createDatas) {
            Util.dimissProDialog();
            getSharesTask=null;
            if(createDatas!=null)
            {
                MyShareModel.this.notifyObservers(createDatas);
            }


        }
    }
}
