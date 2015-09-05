package arya.spliro.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.arya.lib.database.DatabaseMgr;
import com.arya.lib.init.Env;
import com.arya.lib.logger.Logger;

import java.util.ArrayList;

import arya.spliro.config.Config;
import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.InviteeData;
import arya.spliro.dao.LocationData;
import arya.spliro.dao.UserProfileData;
import arya.spliro.utility.Util;

/**
 * Created by Admin on 8/25/2015.
 */
public class DatabaseMgrSpliro {
    public static DatabaseMgr dbMgr;
    public static void init()
    {
        try {
            dbMgr=DatabaseMgr.getInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void insertDataToCategoryTable(ArrayList<Categories> categoriesList)
    {

        if(categoriesList!=null&&(!categoriesList.isEmpty())) {
            try {
                ContentValues[] contentValues = new ContentValues[categoriesList.size()];
                int index = 0;
                for (Categories categories_dataObj : categoriesList) {
                    ContentValues values = new ContentValues();
                    values.put(Categories.KEY_CATEGORY_ID, categories_dataObj.catgory_id);
                    values.put(Categories.KEY_NAME, categories_dataObj.name.toLowerCase());
                    values.put(Categories.KEY_PARENT_CATEGORY_NAME, categories_dataObj.parent_category_name);
                    values.put(Categories.KEY_HASHTAG, categories_dataObj.hashtag);
                    values.put(Categories.KEY_PIC_NAME, categories_dataObj.pic_name);
                    values.put(Categories.KEY_PIC_URL, categories_dataObj.pic_url);
                    values.put(Categories.KEY_PIC_THUMB_NAME, categories_dataObj.pic_thumb_name);
                    values.put(Categories.KEY_PIC_THUMB_URL, categories_dataObj.pic_thumb_url);
                    values.put(Categories.KEY_IMAGEPATH, categories_dataObj.image_path);
                    values.put(Categories.KEY_STATUS, categories_dataObj.status);
                    values.put(Categories.KEY_CREATED_AT, categories_dataObj.created_at);
                    values.put(Categories.KEY_TRNO, categories_dataObj.trno);
                    values.put(Categories.KEY_UPDATED_AT, categories_dataObj.updated_at);
                    values.put(Categories.KEY_DESCRIPTION, categories_dataObj.description);

                    contentValues[index] = values;
                    index++;
                }
                DatabaseMgr.getInstance().insertRows(Categories.TABLE_NAME, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static ArrayList<Categories> getCategoriesList(String keyword) {
        Cursor cursor = null;
        ArrayList<Categories> categoriesesList=new ArrayList<Categories>();
        try {
            if(keyword!=null)
            {
                cursor=dbMgr.sqLiteDb.rawQuery("Select * from "+TableType.CategoryTable.getTableName()+" where "+Categories.KEY_HASHTAG+" like '%"+keyword+"%' or "+Categories.KEY_KEYWORDS+" like '%"+keyword+"%' limit 1",null);
//                cursor=queryTable(TableType.CategoryTable, null,Categories.KEY_NAME + "=?",new String[] {keyword },null, null,null,null);
            }
            else
            {
                cursor = queryTable(TableType.CategoryTable, null, null, null, null, null, null, null);
            }

            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                Categories categoriesObj=new Categories();
                categoriesObj. catgory_id= cursor.getInt(cursor.getColumnIndex(Categories.KEY_CATEGORY_ID));
                categoriesObj.  name= cursor.getString(cursor.getColumnIndex(Categories.KEY_NAME));
                categoriesObj. parent_category_name= cursor.getString(cursor.getColumnIndex(Categories.KEY_PARENT_CATEGORY_NAME));
                categoriesObj.  parent_category_id= cursor.getInt(cursor.getColumnIndex(Categories.KEY_PARENT_CATEGORY_ID));
                categoriesObj.  hashtag= cursor.getString(cursor.getColumnIndex(Categories.KEY_HASHTAG));
                categoriesObj.  pic_name= cursor.getString(cursor.getColumnIndex(Categories.KEY_PIC_NAME));
                categoriesObj.  pic_url= cursor.getString(cursor.getColumnIndex(Categories.KEY_PIC_URL));
                categoriesObj.  pic_thumb_name= cursor.getString(cursor.getColumnIndex(Categories.KEY_PIC_THUMB_NAME));
                categoriesObj.  pic_thumb_url= cursor.getString(cursor.getColumnIndex(Categories.KEY_PIC_THUMB_URL));
                categoriesObj.  status= cursor.getString(cursor.getColumnIndex(Categories.KEY_STATUS));
                categoriesObj.  created_at= cursor.getString(cursor.getColumnIndex(Categories.KEY_CREATED_AT));
                categoriesObj.  trno= cursor.getLong(cursor.getColumnIndex(Categories.KEY_TRNO));
                categoriesObj.  updated_at= cursor.getString(cursor.getColumnIndex(Categories.KEY_UPDATED_AT));
                categoriesObj.  description= cursor.getString(cursor.getColumnIndex(Categories.KEY_DESCRIPTION));
                categoriesObj.  image_path= cursor.getString(cursor.getColumnIndex(Categories.KEY_IMAGEPATH));
                categoriesesList.add(categoriesObj);
                } while (cursor.moveToNext());
                if(keyword!=null)
                {
                    categoriesesList.get(0).searchKeyWord=true;
                }
            }

            else
            {
                if(keyword!=null)
                {
                        categoriesesList.add(new Categories());
                        categoriesesList.get(0).searchKeyWord=true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return categoriesesList;
    }
    public static Cursor queryTable(TableType tableType, String[] columns, String selection, String[] selectionArgs, String groupBy,String having, String orderBy,String limit) {
        Cursor cur = null;
        try {
            if(limit!=null)
            cur = dbMgr.sqLiteDb.query(tableType.getTableName(), columns, selection,selectionArgs, groupBy, having, orderBy,limit);
            else
            cur = dbMgr.sqLiteDb.query(tableType.getTableName(), columns, selection,selectionArgs, groupBy, having, orderBy);
        } catch (Exception e) {

            e.printStackTrace();

            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            cur = null;
        }
        return cur;
    }
    public static void insertDataToPostTable(ArrayList<CreateData> postList)
    {
        if (postList != null && (!postList.isEmpty())) {
            try {
                ContentValues[] contentValues = new ContentValues[postList.size()];
                int index = 0;
                for (CreateData postDataObj : postList) {
                    ContentValues values = new ContentValues();
                    values.put(CreateData.FLD_POST_ID, postDataObj.post_id);
                    values.put(CreateData.FLD_POST_GUID, postDataObj.post_guid);
                    values.put(CreateData.FLD_UESR_ID, postDataObj.user_id);
                    values.put(CreateData.KEY_SHARE_STATUS, postDataObj.status);
                    values.put(CreateData.KEY_SHARE_TITLE, postDataObj.title);
                    values.put(CreateData.KEY_SHARE_DESCRIPTION, postDataObj.categories_data.description);
                    values.put(CreateData.KEY_SHARE_CATEGORY_ID, postDataObj.categories_data.catgory_id);
                    values.put(CreateData.KEY_SHARE_CATEGORY_NAME, postDataObj.categories_data.name);
                    values.put(CreateData.KEY_SHARE_POST_HASHTAG, postDataObj.categories_data.hashtag);
                    values.put(CreateData.KEY_SHARE_POST_KEYWORDS, postDataObj.categories_data.keywords);
                    values.put(CreateData.KEY_SHARE_POST_LATITUDE, postDataObj.location_data.location_latitude);
                    values.put(CreateData.KEY_SHARE_POST_LONGITUDE, postDataObj.location_data.location_longitude);
                    values.put(CreateData.KEY_SHARE_PIC_NAME, postDataObj.categories_data.pic_name);
                    values.put(CreateData.KEY_SHARE_PIC_NAME_URL,postDataObj.categories_data.pic_url);
                    values.put(CreateData.KEY_SHARE_PIC_THUMB, postDataObj.categories_data.pic_thumb_name);
                    values.put(CreateData.KEY_SHARE_PIC_THUMB_URL, postDataObj.categories_data.pic_thumb_url);
                    values.put(CreateData.KEY_SHARE_TOTAL_SHARE, postDataObj.no_of_shares);
                    values.put(CreateData.KEY_SHARE_TOTAL_SHARES_LEFT, postDataObj.totalSharesLeft);
                    values.put(CreateData.KEY_SHARE_POST_EXPIRE_DATE, postDataObj.post_expire_date);
                    values.put(CreateData.KEY_SHARE_INVOICE_PRICE, postDataObj.invoice_price);
                    values.put(CreateData.KEY_SHARE_PER_SHARE_PRICE, postDataObj.per_share_price);
                    values.put(CreateData.KEY_SHARE_INVOICE_IMAGE_NAME, postDataObj.invoice_image_name);

                    values.put(CreateData.FLD_CREATED_AT, postDataObj.created_at);
                    values.put(CreateData.FLD_UPDATED_AT, postDataObj.updated_at);
                    if(postDataObj.categories_data.custom_image_path!=null)
                    {
                        values.put(CreateData.FLD_CATEGORY_IMG_PATH, postDataObj.categories_data.custom_image_path);
                    }
                    else
                    {
                        values.put(CreateData.FLD_CATEGORY_IMG_PATH, postDataObj.categories_data.image_path);
                    }
                    values.put(CreateData.FLD_RECEIPT_IMG_PATH, postDataObj.receipt_image_path);
                    values.put(CreateData.FLD_CREATOR_NAME, postDataObj.display_name);
                    values.put(CreateData.FLD_CREATOR_RATE, postDataObj.rate);
                    values.put(CreateData.FLD_PROFILE_PIC_URL, postDataObj.profile_pic_url);
                    values.put(CreateData.FLD_TRNO, postDataObj.trno);
                    contentValues[index] = values;
                    index++;
                }
                DatabaseMgr.getInstance().insertRows(CreateData.TABLE_NAME, contentValues);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }



    public static void insertDataToPostUserTable( CreateData createData)
    {
        if (createData != null &&createData.invitee_data!=null&& (!createData.invitee_data.isEmpty())) {
            try {
                ContentValues[] contentValues = new ContentValues[createData.invitee_data.size()];
                int index = 0;
                for (InviteeData inviteeData : createData.invitee_data) {
                    ContentValues values = new ContentValues();
                    values.put(InviteeData.FLD_POST_ID, createData.post_id);
                    values.put(InviteeData.FLD_POST_GUID, createData.post_guid);
                    values.put(InviteeData.FLD_DISPLAY_NAME,inviteeData.display_name );
                    values.put(InviteeData.FLD_POST_CONTACT_TYPE,inviteeData.post_contact_type );
                    values.put(InviteeData.FLD_POST_CONTACT_ID,inviteeData.post_contact_id );
                    values.put(CreateData.FLD_TRNO, inviteeData.trno);
                    contentValues[index] = values;
                    index++;
                }
                DatabaseMgr.getInstance().insertRows(InviteeData.TABLE_NAME, contentValues);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    /**
     * This method is used to delete rows from the table.
     *
     * @param tableType
     * @param whereClause
     * @param whereArgs
     * @return
     */
    public static int deleteTableRow(TableType tableType, String whereClause,
                              String[] whereArgs) {
        int retCode = -1;
        try {
            retCode = dbMgr.sqLiteDb.delete(tableType.getTableName(), whereClause,
                    whereArgs);
        } catch (Exception e) {
            if (Logger.IS_DEBUG)// This log is not print now.
                Logger.error("spliro","deleteTableRow() : Exception is : " + e);
        }
        return retCode;
    }
    /*
*@param status of the shares may be c for closed ,s for saved ,o for current shares
 */
    public ArrayList<CreateData> getSharesList(String status)
    {
        ArrayList<CreateData> list=new ArrayList<CreateData>();
        Cursor cursor=null;
        try
        {
            cursor = queryTable(TableType.CreatePostTable, null, CreateData.KEY_SHARE_STATUS + "=?", new String[]{status}, null, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0)
            {
                while (!cursor.isAfterLast())
                {
                    list.add(getShareData(cursor));
                    cursor.moveToNext();
                }
            }

        }catch(Exception e)
        {
            e.printStackTrace();
            if(Logger.IS_DEBUG)
            {
                Logger.error(DatabaseMgr.TAG, "getSharesList(): Exception [" + e + "] tableName [" +TableType.CreatePostTable+ "]");
            }
        }
        finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;

    }
    private CreateData getShareData(Cursor cursor)
    {       CreateData createData=null;
        try {
            createData = new CreateData();
            createData._id=cursor.getInt(cursor.getColumnIndex(CreateData.FLD_ID));
            createData.post_id=cursor.getLong(cursor.getColumnIndex(CreateData.FLD_POST_ID));
            createData.post_guid=cursor.getString(cursor.getColumnIndex(CreateData.FLD_POST_GUID));
            createData.user_id=cursor.getLong(cursor.getColumnIndex(CreateData.FLD_UESR_ID));
            createData.status=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_STATUS));
            createData.title=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_TITLE));
            createData.display_name=cursor.getString(cursor.getColumnIndex(CreateData.FLD_CREATOR_NAME));
            createData.rate=cursor.getFloat(cursor.getColumnIndex(CreateData.FLD_CREATOR_RATE));
            createData.profile_pic_url=cursor.getString(cursor.getColumnIndex(CreateData.FLD_PROFILE_PIC_URL));
            createData.categories_data=new Categories();
            createData.categories_data.description=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_DESCRIPTION));
            createData.categories_data.catgory_id=cursor.getInt(cursor.getColumnIndex(CreateData.KEY_SHARE_CATEGORY_ID));
            createData.categories_data.name=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_CATEGORY_NAME));
            createData.categories_data.hashtag=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_POST_HASHTAG));
            createData.categories_data.keywords=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_POST_KEYWORDS));
            createData.location_data=new LocationData();
            createData.location_data.location_latitude=cursor.getDouble(cursor.getColumnIndex(CreateData.KEY_SHARE_POST_LATITUDE));
            createData.location_data.location_longitude=cursor.getDouble(cursor.getColumnIndex(CreateData.KEY_SHARE_POST_LONGITUDE));
            createData.categories_data.pic_name=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_PIC_NAME));
            createData.categories_data.pic_url=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_PIC_NAME_URL));
            createData.categories_data.pic_thumb_name=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_PIC_THUMB));
            createData.categories_data.pic_thumb_url=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_PIC_THUMB_URL));
            createData.no_of_shares=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_TOTAL_SHARE));
            createData.totalSharesLeft=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_TOTAL_SHARES_LEFT));
            createData.post_expire_date=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_POST_EXPIRE_DATE));
            createData.invoice_price=cursor.getDouble(cursor.getColumnIndex(CreateData.KEY_SHARE_INVOICE_PRICE));
            createData.per_share_price=cursor.getDouble(cursor.getColumnIndex(CreateData.KEY_SHARE_PER_SHARE_PRICE));
            createData.invoice_image_name=cursor.getString(cursor.getColumnIndex(CreateData.KEY_SHARE_INVOICE_IMAGE_NAME));

            createData.created_at=cursor.getString(cursor.getColumnIndex(CreateData.FLD_CREATED_AT));
            createData.updated_at=cursor.getString(cursor.getColumnIndex(CreateData.FLD_UPDATED_AT));
            createData.categories_data.image_path=cursor.getString(cursor.getColumnIndex(CreateData.FLD_CATEGORY_IMG_PATH));
            createData.receipt_image_path=cursor.getString(cursor.getColumnIndex(CreateData.FLD_RECEIPT_IMG_PATH));
            createData.trno=cursor.getInt(cursor.getColumnIndex(CreateData.FLD_TRNO));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Logger.error(DatabaseMgr.TAG, "getSharesList(): Exception [" + e + "] tableName [" +TableType.CreatePostTable+ "]");
        }

        return createData;
    }

    private ArrayList<InviteeData> getInvteeList(String guid)
    {
        ArrayList<InviteeData> list=new ArrayList<InviteeData>();
        Cursor cursor=null;
        try
        {
            cursor=queryTable(TableType.CreatePostDataUserTable, null, CreateData.FLD_POST_GUID + "=?", new String[]{guid}, null, null, null, null);
            if (cursor.getCount() > 0)
            {
                while (!cursor.isAfterLast())
                {
                    list.add(getInviteeData(cursor));
                    cursor.moveToNext();
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            if(Logger.IS_DEBUG)
            {
                Logger.error(DatabaseMgr.TAG, "getInvteeList(): Exception [" + e + "] tableName [" +TableType.CreatePostDataUserTable+ "]");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;
    }
    private InviteeData getInviteeData(Cursor cursor)
    {
        InviteeData inviteeData=null;
        try
        {
            inviteeData=new InviteeData();
            inviteeData._id=cursor.getInt(cursor.getColumnIndex(InviteeData.FLD_ID));
            inviteeData.post_id=cursor.getInt(cursor.getColumnIndex(InviteeData.FLD_POST_ID));


        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return inviteeData;
    }

    public static void insertDataToUserProfile(ArrayList<UserProfileData> userProfileDataList)
    {
        try {
            ContentValues[] contentValues = new ContentValues[userProfileDataList.size()];
            int index = 0;
            for (UserProfileData userProfileData : userProfileDataList) {
                ContentValues values = new ContentValues();
                values.put(UserProfileData.FLD_USER_ID, userProfileData.user_id);
                values.put(UserProfileData.FLD_FIRST_NAME, userProfileData.first_name);
                values.put(UserProfileData.FLD_LAST_NAME, userProfileData.last_name);
                values.put(UserProfileData.FLD_DISPLAY_NAME, userProfileData.display_name);
                values.put(UserProfileData.FLD_PHONE, userProfileData.phone_number);
                values.put(UserProfileData.FLD_EMAIL, userProfileData.email);
                values.put(UserProfileData.FLD_ABOUT_ME, userProfileData.about_me);
                values.put(UserProfileData.FLD_PROFILE_PICTURE_NAME, userProfileData.profile_picture_name);
                values.put(UserProfileData.FLD_PROFILE_PICTURE_URL, userProfileData.profile_picture_url);
                values.put(UserProfileData.FLD_ADDRESS, userProfileData.address);
                values.put(UserProfileData.FLD_LOCATION_COUNTRY_CODE, userProfileData.location_country_code);
                values.put(UserProfileData.FLD_LOCATION_LATITUDE, userProfileData.location_latitude);
                values.put(UserProfileData.FLD_LOCATION_LONGITUDE, userProfileData.location_longitude);
                values.put(UserProfileData.FLD_TIMEZONE, userProfileData.timezone);
                values.put(UserProfileData.FLD_RATE, userProfileData.rate);
                values.put(UserProfileData.FLD_STATUS, userProfileData.status);
                values.put(UserProfileData.FLD_CREATED_AT, userProfileData.created_at);
                values.put(UserProfileData.FLD_NOTIFY_PREF, userProfileData.notify_pref);
                values.put(UserProfileData.FLD_SELECTED_CATEGORY, userProfileData.selected_category);
                values.put(UserProfileData.FLD_TRNO, userProfileData.trno);
                contentValues[index] = values;
                index++;
            }
            DatabaseMgr.getInstance().insertRow(UserProfileData.TABLE_NAME,contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<UserProfileData> getUserProfileData()
    {
        Cursor cursor = null;
        ArrayList<UserProfileData> userProfileDataList=new ArrayList<UserProfileData>();
        try {
            String user_id= String.valueOf(Config.getUserId());
            cursor = queryTable(TableType.CreateUserProfileTable,null, UserProfileData.FLD_USER_ID + "=?", new String[] {user_id}, null, null, null,null);
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                do {
                    UserProfileData userProfileData=new UserProfileData();
                    userProfileData.user_id= cursor.getInt(cursor.getColumnIndex(UserProfileData.FLD_USER_ID));
                    userProfileData.first_name= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_FIRST_NAME));
                    userProfileData.last_name= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_LAST_NAME));
                    userProfileData.display_name= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_DISPLAY_NAME));
                    userProfileData.phone_number= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_PHONE));
                    userProfileData.email= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_EMAIL));
                    userProfileData.about_me= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_ABOUT_ME));
                    userProfileData.profile_picture_name= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_PROFILE_PICTURE_NAME));
                    userProfileData.profile_picture_url= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_PROFILE_PICTURE_URL));
                    userProfileData.address= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_ADDRESS));
                    userProfileData.location_country_code= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_LOCATION_COUNTRY_CODE));
                    userProfileData.location_latitude= cursor.getDouble(cursor.getColumnIndex(UserProfileData.FLD_LOCATION_LATITUDE));
                    userProfileData.location_longitude= cursor.getDouble(cursor.getColumnIndex(UserProfileData.FLD_LOCATION_LONGITUDE));
                    userProfileData.timezone= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_TIMEZONE));
                    userProfileData.rate= cursor.getInt(cursor.getColumnIndex(UserProfileData.FLD_RATE));
                    userProfileData.status= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_STATUS));
                    userProfileData.created_at= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_CREATED_AT));
                    userProfileData.selected_category= cursor.getString(cursor.getColumnIndex(UserProfileData.FLD_SELECTED_CATEGORY));
                    userProfileData.notify_pref= cursor.getInt(cursor.getColumnIndex(UserProfileData.FLD_NOTIFY_PREF));
                    userProfileData.trno= cursor.getInt(cursor.getColumnIndex(UserProfileData.FLD_TRNO));
                    userProfileDataList.add(userProfileData);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return userProfileDataList;
    }

}
