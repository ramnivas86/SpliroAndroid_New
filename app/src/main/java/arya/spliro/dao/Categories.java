package arya.spliro.dao;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Admin on 8/21/2015.
 */
public class Categories implements Serializable{
    public static final String FLD_ID="id";

    public static final String TABLE_NAME = "sp_mst_category";
    public static String KEY_CATEGORY_ID="category_id";
    public static String KEY_NAME="name";
    public static String KEY_PARENT_CATEGORY_NAME="parent_category_name";
    public static String KEY_PARENT_CATEGORY_ID="parent_category_id";
    public static String KEY_HASHTAG="hashtag";
    public static String KEY_KEYWORDS="keywords";
    public static String KEY_PIC_NAME="pic_name";
    public static String KEY_PIC_URL="pic_url";
    public static String KEY_PIC_THUMB_NAME="pic_thumb_name";
    public static String KEY_PIC_THUMB_URL="pic_thumb_url";
    public static String KEY_IMAGEPATH="image_path";
    public static String KEY_STATUS="status";
    public static String KEY_CREATED_AT="created_at";
    public static String KEY_TRNO="trno";
    public static String KEY_UPDATED_AT="updated_at";
    public static String KEY_DESCRIPTION="description";
    public int catgory_id;
    public  String name="";
    public int parent_category_id ;
    public String parent_category_name="";
    public String hashtag="";
    public String pic_name="";
    public String pic_url="";
    public  String pic_thumb_name="";
    public  String pic_thumb_url="";
    public String status="";
    public long trno;
    public String created_at;
    public String updated_at;
    public String description="";
    public String image_path="";
    public String keywords="";
   public String custom_image_path;
    public boolean searchKeyWord;
    public boolean is_selected;
}
