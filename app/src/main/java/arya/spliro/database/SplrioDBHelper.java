package arya.spliro.database;

import android.database.sqlite.SQLiteDatabase;

import com.arya.lib.database.DBHelper;

import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.InviteeData;
import arya.spliro.dao.UserProfileData;
import arya.spliro.utility.Constants;

/**
 * Created by phoosaram on 8/25/2015.
 */
public class SplrioDBHelper implements DBHelper{
    @Override
    public int getDBVersion() {
        return 1;
    }

    @Override
    public String getDBName() {
        return Constants.DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCategoryTable(db);
        createPostDataTable(db);
        createPostUserDataTable(db);
        createUserProfileTable(db);


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void createCategoryTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableType.CategoryTable.getTableName() + "("
                + Categories.FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Categories.KEY_CATEGORY_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE,"
                + Categories.KEY_NAME + " TEXT,"
                + Categories.KEY_PARENT_CATEGORY_ID + " INTEGER,"
                + Categories.KEY_PARENT_CATEGORY_NAME + " TEXT,"
                + Categories.KEY_TRNO + " LONG,"
                + Categories.KEY_HASHTAG + " TEXT,"
                + Categories.KEY_KEYWORDS + " TEXT,"
                + Categories.KEY_PIC_NAME + " TEXT,"
                + Categories.KEY_PIC_URL + " TEXT,"
                + Categories.KEY_STATUS + " TEXT,"
                + Categories.KEY_PIC_THUMB_NAME + " TEXT,"
                + Categories.KEY_PIC_THUMB_URL + " TEXT,"
                + Categories.KEY_IMAGEPATH + " VARCHAR,"
                + Categories.KEY_DESCRIPTION + " TEXT,"
                + Categories.KEY_CREATED_AT + " DATETIME,"
                + Categories.KEY_UPDATED_AT + " DATETIME);");
        db.execSQL("CREATE INDEX category_trno_index on "+TableType.CategoryTable.getTableName()+"(trno);");
        db.execSQL("CREATE INDEX category_keyowrds_index on "+TableType.CategoryTable.getTableName()+"("+Categories.KEY_KEYWORDS+","+Categories.KEY_HASHTAG+");");
    }
    private void createPostDataTable(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableType.CreatePostTable.getTableName() + "("
                + CreateData.FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + CreateData.FLD_POST_ID + " LONG,"
                + CreateData.FLD_POST_GUID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,"
                + CreateData.FLD_UESR_ID + " LONG,"
                + CreateData.KEY_SHARE_STATUS + " TEXT,"
                + CreateData.KEY_SHARE_TITLE + " TEXT,"
                + CreateData.KEY_SHARE_DESCRIPTION + " TEXT,"
                + CreateData.KEY_SHARE_CATEGORY_ID + " INTEGER,"
                + CreateData.KEY_SHARE_CATEGORY_NAME + " TEXT,"
                + CreateData.KEY_SHARE_POST_HASHTAG + " TEXT,"
                + CreateData.KEY_SHARE_POST_KEYWORDS + " TEXT,"
                + CreateData.KEY_SHARE_POST_LATITUDE + " NUMERIC(10,6),"
                + CreateData.KEY_SHARE_POST_LONGITUDE  + " NUMERIC(10,6),"
                + CreateData.KEY_SHARE_ADDRESS  + " TEXT,"
                + CreateData.KEY_SHARE_ZIPCODE  + " TEXT,"
                + CreateData.KEY_SHARE_PIC_NAME  + " TEXT,"
                + CreateData.KEY_SHARE_PIC_NAME_URL  + " TEXT,"
                + CreateData.KEY_SHARE_PIC_THUMB  + " TEXT,"
                + CreateData.KEY_SHARE_PIC_THUMB_URL + " TEXT,"
                + CreateData.KEY_SHARE_TOTAL_SHARE + " INTEGER,"
                + CreateData.KEY_SHARE_TOTAL_SHARES_LEFT + " INTEGER,"
                + CreateData.KEY_SHARE_POST_EXPIRE_DATE + " DATETIME,"
                + CreateData.KEY_SHARE_POST_CLOSE_DATE + " DATETIME,"
                + CreateData.KEY_SHARE_INVOICE_PRICE + " NUMERIC(11,2),"
                + CreateData.KEY_SHARE_PER_SHARE_PRICE + " NUMERIC(11,2),"
                + CreateData.KEY_SHARE_INVOICE_IMAGE_NAME + " TEXT,"
                + CreateData.FLD_CREATED_AT + " DATETIME,"
                + CreateData.FLD_UPDATED_AT + " DATETIME,"
                + CreateData.FLD_CATEGORY_IMG_PATH + " TEXT,"
                + CreateData.FLD_RECEIPT_IMG_PATH + " TEXT,"
                + CreateData.FLD_CREATOR_NAME + " TEXT,"
                + CreateData.FLD_PROFILE_PIC_URL + " TEXT,"
                + CreateData.FLD_CREATOR_RATE + " TEXT,"
                + CreateData.FLD_TRNO+ " LONG);");
        db.execSQL("CREATE INDEX post_trno_index on "+TableType.CreatePostTable.getTableName()+"(trno);");
    }
    private void createPostUserDataTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TableType.CreatePostDataUserTable.getTableName() + "("
                + InviteeData.FLD_ID + " INTEGER PRIMARY KEY  NOT NULL ON CONFLICT REPLACE,"
                + InviteeData.FLD_POST_ID + " INTEGER NOT NULL,"
                + InviteeData.FLD_POST_GUID + " TEXT,"
                + InviteeData.FLD_POST_CONTACT_TYPE + " TEXT,"
                + InviteeData.FLD_POST_CONTACT_ID + " TEXT,"
                + InviteeData.FLD_MIGRATED_USER_ID + " INTEGER,"
                + InviteeData.FLD_CREATED_AT + " DATETIME,"
                + InviteeData.FLD_DISPLAY_NAME + " TEXT,"
                + InviteeData.FLD_IS_INVITED + " INTEGER,"
                + InviteeData.FLD_IS_FAVORITE + " INTEGER,"
                + InviteeData.FLD_STATUS + " TEXT,"
                + InviteeData.FLD_POSTER_SHARE_STATUS + " TEXT,"
                + InviteeData.FLD_OFFER_SHARES_REQUESTED + " INTEGER,"
                + InviteeData.FLD_OFFER_SHARES_APPROVED + " INTEGER,"
                + InviteeData.FLD_OFFER_USER_LATITUDE + " NUMERIC(10,6),"
                + InviteeData.FLD_OFFER_USER_LONGITUD + " NUMERIC(10,6),"
                + InviteeData.FLD_OFFER_REMARK + " TEXT,"
                + InviteeData.FLD_OFFER_PRICE + " NUMERIC(11,2),"
                + InviteeData.FLD_UPDATED_AT + " DATETIME,"
                + InviteeData.FLD_PROFILE_PIC_URL + " TEXT,"
                + InviteeData.FLD_TRNO + " LONG);");
        db.execSQL("CREATE INDEX post_data_trno_idx1 on "+TableType.CreatePostDataUserTable.getTableName()+"(trno);");
        db.execSQL("CREATE INDEX post_data_post_idx2 on "+TableType.CreatePostDataUserTable.getTableName()+"("+InviteeData.FLD_POST_ID+","+InviteeData.FLD_POST_CONTACT_TYPE+","+InviteeData.FLD_POST_CONTACT_ID+");");
    }
    private void createUserProfileTable(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TableType.CreateUserProfileTable.getTableName() + "("
                + UserProfileData.FLD_ID + " INTEGER PRIMARY KEY  NOT NULL,"
                + UserProfileData.FLD_USER_ID + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE,"
                + UserProfileData.FLD_FIRST_NAME + " TEXT,"
                + UserProfileData.FLD_LAST_NAME + " TEXT,"
                + UserProfileData.FLD_DISPLAY_NAME + " TEXT,"
                + UserProfileData.FLD_PHONE + " TEXT,"
                + UserProfileData.FLD_EMAIL + " TEXT,"
                + UserProfileData.FLD_ABOUT_ME + " TEXT,"
                + UserProfileData.FLD_PROFILE_PICTURE_NAME + " TEXT,"
                + UserProfileData.FLD_PROFILE_PICTURE_URL + " TEXT,"
                + UserProfileData.FLD_ADDRESS + " TEXT,"
                + UserProfileData.FLD_ZIPCODE + " TEXT,"
                + UserProfileData.FLD_LOCATION_COUNTRY_CODE + " TEXT,"
                + UserProfileData.FLD_LOCATION_LATITUDE + " NUMERIC(10,6),"
                + UserProfileData.FLD_LOCATION_LONGITUDE + " NUMERIC(10,6),"
                + UserProfileData.FLD_TIMEZONE + " TEXT,"
                + UserProfileData.FLD_RATE + " NUMERIC,"
                + UserProfileData.FLD_STATUS + " TEXT,"
                + UserProfileData.FLD_CREATED_AT + " DATETIME,"
                + UserProfileData.FLD_SELECTED_CATEGORY + " TEXT,"
                + UserProfileData.FLD_NOTIFY_PREF + " INTEGER,"
                + UserProfileData.FLD_TRNO + " LONG);");
        db.execSQL("CREATE INDEX profile_trno_index on " + TableType.CreateUserProfileTable.getTableName() + "(trno);");
    }
}
