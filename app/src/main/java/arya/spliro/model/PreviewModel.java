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
import org.json.JSONException;
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


public class PreviewModel extends BasicModel {
    private Context mcontext;
    private SaveCreatedShareTask saveCreatedShareTask;

    public void createPostToServer(Context mcontext,CreateData createData) {

        if (saveCreatedShareTask == null) {
            this.mcontext=mcontext;
            saveCreatedShareTask = new SaveCreatedShareTask();
            saveCreatedShareTask.execute(createData);
        }
    }

    class SaveCreatedShareTask extends AsyncTask<CreateData, Void, CreateData> {
        @Override
        protected CreateData doInBackground(CreateData... createDatas) {
            CreateData createDataObj = createDatas[0];
            try {

                if (createDataObj.status.equals(Constants.POSTING_STATUS)) {
                    NetworkResponse response;
                    createDataObj = uploadImageToServer(createDataObj);
                    Cmd cmd = CmdFactory.createCreatePostCmd(createDataObj, createInviteeArray(createDataObj));
                    response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
                    if (response != null && response.isSuccess() && response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                        createDataObj = toCreatPostObject(createDataObj, response.getJsonObject());
                        saveDataToLocalDB(createDataObj);
                    } else {
                        if (response != null)
                        {
                            createDataObj.errorMsg = response.getMessageFromServer();
                        }
                    }
                } else if (createDataObj.status.equals(Constants.SAVING_STATUS)) {
                    saveDataToLocalDB(createDataObj);

                } else if (createDataObj.status.equals(Constants.DELETE_STATUS)) {
                    Util.deleteCreatePostRow(TableType.CreatePostTable, createDataObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
                createDataObj.errorMsg = mcontext.getString(R.string.post_deleted_error);


            }


            return createDataObj;
        }

        @Override
        protected void onPostExecute(CreateData createData) {
            Util.dimissProDialog();

            if (createData != null) {
                PreviewModel.this.notifyObservers(createData);
            }
            saveCreatedShareTask = null;
        }
    }

    private CreateData uploadImageToServer(CreateData createDataObj) throws JSONException {

        if (createDataObj.categories_data.custom_image_path != null) {
            String[] pic_image_array = createDataObj.categories_data.custom_image_path.split("/");
            String pic_old_name = pic_image_array[pic_image_array.length - 1];
            createDataObj.categories_data.pic_name = Util.uploadImageToServer(createDataObj.categories_data.custom_image_path, pic_old_name);

            if (!pic_old_name.equals(createDataObj.categories_data.pic_name)) {
                Util.renameFile(pic_old_name, createDataObj.categories_data.custom_image_path, createDataObj.categories_data.pic_name);
                createDataObj.categories_data.custom_image_path = createDataObj.categories_data.custom_image_path.replace(pic_old_name, createDataObj.categories_data.pic_name);
            }
        } else {
            createDataObj.categories_data.pic_name = "";
        }
        if (createDataObj.receipt_image_path != null && createDataObj.receipt_image_path.contains(Constants.profileImge)) {
            String[] pic_image_array = createDataObj.receipt_image_path.split("/");
            String pic_old_name = pic_image_array[pic_image_array.length - 1];
            createDataObj.invoice_image_name = Util.uploadImageToServer(createDataObj.receipt_image_path, pic_old_name);
            if (!pic_old_name.equals(createDataObj.invoice_image_name)) {
                Util.renameFile(pic_old_name, createDataObj.receipt_image_path, createDataObj.invoice_image_name);
                createDataObj.receipt_image_path = createDataObj.receipt_image_path.replace(pic_old_name, createDataObj.invoice_image_name);
            }
        } else {
            createDataObj.invoice_image_name = "";
        }
        return createDataObj;
    }

    private JSONArray createInviteeArray(CreateData createDataObj) {
        JSONArray jsonArray = new JSONArray();
        try {
            if (createDataObj.invitee_data != null && (!createDataObj.invitee_data.isEmpty())) {
                for (InviteeData inviteeObj : createDataObj.invitee_data) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(InviteeData.FLD_DISPLAY_NAME, inviteeObj.display_name);
                    jsonObject.put(InviteeData.FLD_POST_CONTACT_ID, Constants.COUNTRYCODE + inviteeObj.post_contact_id);
                    jsonObject.put(InviteeData.FLD_POST_CONTACT_TYPE, inviteeObj.post_contact_type);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    private void saveDataToLocalDB(CreateData createDataObj) {
        ArrayList<CreateData> createList = new ArrayList<CreateData>();
        createList.add(createDataObj);
        DatabaseMgrSpliro.insertDataToPostTable(createList);
        Util.deleteCreatePostRow(TableType.CreatePostDataUserTable, createDataObj);
        DatabaseMgrSpliro.insertDataToPostUserTable(createDataObj);
    }

    public static CreateData toCreatPostObject(CreateData createData, JSONObject mainObj) {
        try {
            JSONObject jsonObject = mainObj.getJSONObject(Constants.FLD_KEY_DATA);
            JSONObject dataObj = jsonObject.getJSONObject(Constants.FLD_KEY_DATA);
            if (dataObj.has(CreateData.FLD_CREATED_AT))
                createData.created_at = dataObj.getString(CreateData.FLD_CREATED_AT);
            if (dataObj.has(CreateData.FLD_UPDATED_AT))
                createData.updated_at = dataObj.getString(CreateData.FLD_UPDATED_AT);
            if (dataObj.has(CreateData.FLD_POST_ID))
                createData.post_id = dataObj.getLong(CreateData.FLD_POST_ID);
            if (dataObj.has(CreateData.KEY_SHARE_STATUS))
                createData.status = dataObj.getString(CreateData.KEY_SHARE_STATUS);
            if (dataObj.has(CreateData.FLD_TRNO))
                createData.trno = dataObj.getInt(CreateData.FLD_TRNO);
            if (dataObj.has(Categories.KEY_PIC_NAME)) {
                createData.categories_data.pic_name = dataObj.getString(Categories.KEY_PIC_NAME);
            }
            if (dataObj.has(Categories.KEY_PIC_URL)) {
                createData.categories_data.pic_url = dataObj.getString(Categories.KEY_PIC_URL);
            }
            if (dataObj.has(Categories.KEY_PIC_THUMB_NAME)) {
                createData.categories_data.pic_thumb_name = dataObj.getString(Categories.KEY_PIC_THUMB_NAME);
            }
            if (dataObj.has(Categories.KEY_PIC_THUMB_URL)) {
                createData.categories_data.pic_thumb_url = dataObj.getString(Categories.KEY_PIC_THUMB_URL);
            }
            if (dataObj.has(CreateData.FLD_CREATOR_NAME)) {
                createData.display_name = dataObj.getString(CreateData.FLD_CREATOR_NAME);
            }
            if (dataObj.has(CreateData.FLD_CREATOR_RATE)) {
                createData.rate = Float.parseFloat(dataObj.getString(CreateData.FLD_CREATOR_RATE));
            }
            if (dataObj.has(CreateData.FLD_PROFILE_PIC_URL)) {
                createData.profile_pic_url = dataObj.getString(CreateData.FLD_PROFILE_PIC_URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return createData;
    }
}
