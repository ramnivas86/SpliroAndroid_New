package arya.spliro.utility;

import com.arya.lib.cmds.Cmd;
import com.arya.lib.database.DatabaseMgr;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.dao.Categories;
import arya.spliro.database.DatabaseMgrSpliro;

/**
 * Created by Admin on 8/25/2015.
 */
public class CategoriesMgr {
    public static ArrayList<Categories> toCategoriesList(JSONObject mainObj) {
        ArrayList<Categories> catArrayList=new ArrayList<Categories>();
        try {
            JSONArray jarray = mainObj.getJSONArray(Constants.FLD_KEY_DATA);
            if (jarray != null) {
                for (int i = 0; i < jarray.length(); i++) {
                    Categories categoryList = new Categories();
                    JSONObject dataObj = jarray.getJSONObject(i);
                    if (dataObj.has(Categories.KEY_CATEGORY_ID)) {
                        categoryList.catgory_id = dataObj.getInt(Categories.KEY_CATEGORY_ID);
                    }
                    if (dataObj.has(Categories.KEY_DESCRIPTION)) {
                        categoryList.description = dataObj.getString(Categories.KEY_DESCRIPTION);
                    }
                    if (dataObj.has(Categories.KEY_NAME)) {
                        categoryList.name = dataObj.getString(Categories.KEY_NAME);
                    }
                    if (dataObj.has(Categories.KEY_PARENT_CATEGORY_NAME)) {
                        categoryList.parent_category_name = dataObj.getString(Categories.KEY_PARENT_CATEGORY_NAME);
                    }
                    if (dataObj.has(Categories.KEY_HASHTAG)) {
                        categoryList.hashtag = dataObj.getString(Categories.KEY_HASHTAG);
                    }
                    if (dataObj.has(Categories.KEY_PIC_NAME)) {
                        categoryList.pic_name = dataObj.getString(Categories.KEY_PIC_NAME);
                    }
                    if (dataObj.has(Categories.KEY_PIC_URL)) {
                        categoryList.pic_url = dataObj.getString(Categories.KEY_PIC_URL);
                        categoryList.image_path=  Util.downloadFileUsingUrl(categoryList.pic_url);
                    }
                    if (dataObj.has(Categories.KEY_PIC_THUMB_NAME)) {
                        categoryList.pic_thumb_name = dataObj.getString(Categories.KEY_PIC_THUMB_NAME);
                    }
                    if (dataObj.has(Categories.KEY_PIC_THUMB_URL)) {
                        categoryList.pic_thumb_url = dataObj.getString(Categories.KEY_PIC_THUMB_URL);
                    }
                    if (dataObj.has(Categories.KEY_STATUS)) {
                        categoryList.status = dataObj.getString(Categories.KEY_STATUS);
                    }
                    if (dataObj.has(Categories.KEY_CREATED_AT)) {
                        categoryList.created_at = dataObj.getString(Categories.KEY_CREATED_AT);
                    }
                    if (dataObj.has(Categories.KEY_TRNO)) {
                        categoryList.trno = dataObj.getLong(Categories.KEY_TRNO);
                    }
                    if (dataObj.has(Categories.KEY_UPDATED_AT)) {
                        categoryList.updated_at = dataObj.getString(Categories.KEY_UPDATED_AT);
                    }
                    catArrayList.add(categoryList);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return catArrayList;
    }

    public static int getLastTrno() {

//    Select Max (trno) from ms_category table

        return 0;
    }

    public static void syncCategoryData() {
            Cmd cmd= CmdFactory.createCategoryListCmd();
           NetworkResponse response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
            if (response != null&&response.isSuccess()&&response.getJsonObject().has(Constants.FLD_KEY_DATA)) {
                DatabaseMgrSpliro.insertDataToCategoryTable(CategoriesMgr.toCategoriesList(response.getJsonObject()));
            }
    }


}


