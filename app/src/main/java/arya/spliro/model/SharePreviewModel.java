        package arya.spliro.model;
        import android.content.Context;
        import android.os.AsyncTask;
        import com.arya.lib.cmds.Cmd;
        import com.arya.lib.database.DatabaseMgr;
        import com.arya.lib.init.Env;
        import com.arya.lib.logger.Logger;
        import com.arya.lib.model.BasicModel;
        import com.arya.lib.network.NetworkMgr;
        import com.arya.lib.network.NetworkResponse;
        import org.json.JSONArray;
        import org.json.JSONObject;
        import java.util.ArrayList;

        import arya.spliro.R;
        import arya.spliro.cmds.CmdFactory;
        import arya.spliro.dao.Categories;
        import arya.spliro.dao.CreateData;
        import arya.spliro.dao.InviteeData;
        import arya.spliro.database.DatabaseMgrSpliro;
        import arya.spliro.database.TableType;
        import arya.spliro.utility.Constants;
        import arya.spliro.utility.Util;

/**
 * Created by Phoosaram on 8/24/2015.
 */
public class SharePreviewModel extends BasicModel {
    private Task saveCreatedShareTask;
    public void changeShareStatus(CreateData createData )
    {
        if(saveCreatedShareTask==null)
        {
            saveCreatedShareTask=new Task();
            saveCreatedShareTask.execute(createData);
        }
    }

    class Task extends AsyncTask<CreateData,Void,CreateData> {
        @Override
        protected CreateData doInBackground(CreateData... createDatas) {
            CreateData createDataObj=createDatas[0];
            if(createDataObj.status.equals(Constants.CLOSED_STATUS)) {
                NetworkResponse response;
                Cmd cmd = CmdFactory. createChangeShareStatusCmd(createDataObj);
                response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
                if (response != null && response.isSuccess() && response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                    createDataObj= toCreatPostObject(createDataObj, response.getJsonObject());
                    createDataObj.isSuccess=response.isSuccess();
                    saveDataToLocalDB(createDataObj);
                }
                else
                {
                    if (response != null) {
                        createDataObj.errorMsg =response.getMessageFromServer();
                    }
                }
            }
            else if(createDataObj.status.equals(Constants.SAVING_STATUS)) {


            }
            else if(createDataObj.status.equals(Constants.DELETE_STATUS)) {
                if(createDataObj.post_id>0)
                {
                    Cmd cmd = CmdFactory. createDeleteShareCmd(createDataObj);
                    NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
                    if (response != null && response.isSuccess() && response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
//                        createDataObj = toCreatPostObject(createDataObj, response.getJsonObject());
                        createDataObj.errorMsg = response.getMessageFromServer();
                    }
                }
                else
                {
                    createDataObj.errorMsg=createDataObj.ctx.getString(R.string.post_deleted_successfully);
                }
                Util.deleteCreatePostRow(TableType.CreatePostTable,createDataObj);
            }
            return createDataObj;
        }

        @Override
        protected void onPostExecute(CreateData createData) {
            Util.dimissProDialog();
            if (createData != null ) {
                SharePreviewModel.this.notifyObservers(createData);
            }
            saveCreatedShareTask = null;
        }
    }





    private void saveDataToLocalDB(CreateData createDataObj) {
        ArrayList<CreateData> createList=new ArrayList<CreateData>();
        createList .add(createDataObj);
        DatabaseMgrSpliro.insertDataToPostTable(createList);
        DatabaseMgrSpliro. insertDataToPostUserTable(createDataObj);
    }

    public static CreateData toCreatPostObject(CreateData createData,JSONObject mainObj) {
        try {
            JSONObject dataObj=mainObj.getJSONObject(Constants.FLD_KEY_DATA);
            if(dataObj.has(CreateData.FLD_POST_ID))
                createData.post_id=dataObj.getLong(CreateData.FLD_POST_ID);
            if(dataObj.has(CreateData.KEY_SHARE_STATUS))
                createData.status=dataObj.getString(CreateData.KEY_SHARE_STATUS);
            if(dataObj.has(CreateData.FLD_TRNO))
                createData.trno=dataObj.getInt(CreateData.FLD_TRNO);
            if (dataObj.has(Categories.KEY_PIC_NAME)) {
                createData.categories_data.pic_name = dataObj.getString(Categories.KEY_PIC_NAME);
            }
            if (dataObj.has(Categories.KEY_PIC_URL)) {
                createData.categories_data.pic_url = dataObj.getString(Categories.KEY_PIC_URL);
            }
            if (dataObj.has(Categories.KEY_PIC_THUMB_NAME)) {
                createData.categories_data.pic_thumb_name = dataObj.getString(Categories.KEY_PIC_THUMB_NAME);
            }
            if (dataObj.has(Categories.KEY_PIC_THUMB_URL))
            {
                createData.categories_data.pic_thumb_url = dataObj.getString(Categories.KEY_PIC_THUMB_URL);
            }
            if (dataObj.has(CreateData.FLD_CREATOR_NAME))
            {
                createData.display_name = dataObj.getString(CreateData.FLD_CREATOR_NAME);
            }
            if (dataObj.has(CreateData.FLD_CREATOR_RATE))
            {
                createData.rate = Float.parseFloat(dataObj.getString(CreateData.FLD_CREATOR_RATE));
            }
            if (dataObj.has(CreateData.FLD_PROFILE_PIC_URL))
            {
                createData.profile_pic_url=dataObj.getString(CreateData.FLD_PROFILE_PIC_URL);
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return createData;
    }
}

