package arya.spliro.model;

import android.os.AsyncTask;

import com.arya.lib.cmds.Cmd;

import com.arya.lib.database.DatabaseMgr;
import com.arya.lib.model.BasicModel;
import com.arya.lib.network.NetworkMgr;
import com.arya.lib.network.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import arya.spliro.cmds.CmdFactory;
import arya.spliro.config.Config;
import arya.spliro.dao.Categories;
import arya.spliro.dao.UserProfileData;
import arya.spliro.database.DatabaseMgrSpliro;
import arya.spliro.utility.Constants;
import arya.spliro.utility.Util;

/**
 * Created by hitesh on 8/21/2015.
 */
public class EditProfileModel extends BasicModel
{

    MyProfileDataTask myDataTask=null;
    GetCategoryData myCatDataTask=null;
    GetProfileData getProfileDataTask=null;
    String first_name;
    String last_name;
    String email;
    String about_me;
    String phone;
    String display_name;
    ArrayList<Categories> categoryList;
    ArrayList<Integer> selectedCategoryList;
    String filePath;
    String file_name;
    String password;int noti_prif;
    String categoryJsonString;

        public void myProfileData(String first_name,String last_name, String display_name,String phone,String email,String about_me,ArrayList<Categories> categoryList,String filePath,String file_name,String password,int noti_prif)
        {
            this.first_name=first_name;
            this.last_name=last_name;
            this.email=email;
            this.about_me=about_me;
            this.phone=phone;
            this.display_name=display_name;
            this.categoryList=categoryList;
            this.filePath=filePath;
            this.file_name=file_name;
            this.password=password;
            this.noti_prif=noti_prif;
           if(myDataTask==null)
           {
               myDataTask=new MyProfileDataTask();
               myDataTask.execute();
           }
        }
    public void getCategoryData(String categoryJsonString)
    {
        this.categoryJsonString=categoryJsonString;
        if(myCatDataTask==null)
        {
            myCatDataTask=new GetCategoryData();
            myCatDataTask.execute();
        }
    }
    public void getProfileData()
    {
        if(getProfileDataTask==null)
        {
            getProfileDataTask=new GetProfileData();
            getProfileDataTask.execute();
        }
    }

    public class MyProfileDataTask extends AsyncTask<String, String, NetworkResponse>
    {

        @Override
        protected void onPreExecute()
        {

            Util.showProDialog("");
        }
        @Override
        protected NetworkResponse doInBackground(String... strings)
        {
            NetworkResponse response=null;
            try
            {
            if(file_name!=null)
            {
                response = NetworkMgr.uploadImageToServer(Constants.UPLOAD_URL, filePath, file_name);
                if (response.isSuccess() && response.getJsonObject().has(Constants.FLD_KEY_DATA))
                {

                    JSONObject object = response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA);
                    if (object.has(Constants.FLD_FILENAME))
                    {
                        file_name = object.getString(Constants.FLD_FILENAME);
                    }

                }
                else
                {
                    System.out.println("doInBackground(): Error");
                }
            }
                Cmd cmd = CmdFactory.updateMyProfileData(first_name, last_name, display_name, phone, email, about_me, getCategoryJsonArray(),file_name,password,noti_prif);
                response = NetworkMgr.httpPost(Constants.BASE_URL, Constants.FLD_KEY_DATA, cmd.toJSONString());
                if (response.isSuccess() && response.getJsonObject().has(Constants.FLD_KEY_DATA))
                {
                    //saveDataToSharedPrefs(response);
                    Config.saveUserDetail( response.getJSonObjectUsingKey(Constants.FLD_KEY_DATA),null);
                }
                else
                {
                    System.out.println("doInBackground(): Error");
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(NetworkResponse networkResponse)
        {
            Util.dimissProDialog();

          //  getCategoryData(categoryJsonString);
            EditProfileModel.this.notifyObservers(networkResponse);
        }
    }


    public class GetCategoryData extends AsyncTask<String, String, ArrayList<Categories>>
    {
        @Override
        protected void onPreExecute()
        {
          //  Util.showProDialog("");
        }
        @Override
        protected ArrayList<Categories> doInBackground(String... strings)
        {
            ArrayList<Categories> cat_list= DatabaseMgrSpliro.getCategoriesList(null);

            try {
                selectedCategoryList=null;
                JSONArray newJArray =null;
                selectedCategoryList=new ArrayList<Integer>();

                String selectedList=categoryJsonString;
                if(!selectedList.isEmpty()) {
                    newJArray = new JSONArray(selectedList);
                    for (int i = 0; i < newJArray.length(); i++) {
                        selectedCategoryList.add(newJArray.getJSONObject(i).getInt("category_id"));
                    }

                    for (Categories obj:cat_list)
                    {
                        if (selectedCategoryList.contains(obj.catgory_id)) {
                            obj.is_selected = true;
                        } else {
                            obj.is_selected = false;
                        }
                    }

                }
                return cat_list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Categories> cat_list)
        {
            Util.dimissProDialog();
            myCatDataTask=null;
            EditProfileModel.this.notifyObservers(cat_list);
        }
    }

    public class GetProfileData extends AsyncTask<String, String, ArrayList<UserProfileData>>
    {
        @Override
        protected void onPreExecute()
        {
            if(myDataTask==null)
            Util.showProDialog("");
        }
        @Override
        protected ArrayList<UserProfileData> doInBackground(String... strings)
        {
            ArrayList<UserProfileData> userProfileDataList= DatabaseMgrSpliro.getUserProfileData();
            return userProfileDataList;
        }

        @Override
        protected void onPostExecute(ArrayList<UserProfileData> userProfileDataList)
        {
            Util.dimissProDialog();
            getProfileDataTask=null;
            myDataTask=null;
            EditProfileModel.this.notifyObservers(userProfileDataList);
        }
    }

    private void saveDataToSharedPrefss(NetworkResponse response)
    {
        try {
            JSONObject data = response.getJSonObjectUsingKey( Constants.FLD_KEY_DATA);
            Config.setUserfname(data.getString(Constants.FLD_FIRST_NAME));
            Config.setUserlname(data.getString(Constants.FLD_LAST_NAME));
            Config.setUseremail(data.getString(Constants.FLD_EMAIL));
            Config.setUserphone(data.getString(Constants.FLD_PHONE));
            Config.setUserDispalyName(data.getString(Constants.FLD_DISPLAY_NAME));
            Config.setUserAboutMe(data.getString(Constants.FLD_ABOUT_ME));
            Config.setUserImageUrl(data.getString(Constants.FLD_PIC_URL));
            Config.setUserCategoryArray(data.getJSONArray(Constants.FLD_USER_CATEGORY).toString());
            Config.savePreferencese();
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
    }
     private JSONArray getCategoryJsonArray()
     {
         JSONArray catIdJsonArray = new JSONArray();
        for(int i=0;i<categoryList.size();i++)
         {
             if(categoryList.get(i).is_selected)
             catIdJsonArray.put(categoryList.get(i).catgory_id);
         }
         return catIdJsonArray;
     }
}
