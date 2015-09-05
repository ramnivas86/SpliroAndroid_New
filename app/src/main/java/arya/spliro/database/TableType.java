package arya.spliro.database;

import arya.spliro.dao.Categories;
import arya.spliro.dao.CreateData;
import arya.spliro.dao.InviteeData;
import arya.spliro.dao.UserProfileData;

public enum TableType
{
    CategoryTable(1, Categories.TABLE_NAME),
    CreatePostTable(2,CreateData.TABLE_NAME),
    CreatePostDataUserTable(3, InviteeData.TABLE_NAME),CreateUserProfileTable(4, UserProfileData.TABLE_NAME);;
    private int index;
    private String tableName;

    TableType(int index , String tableName)
    {
        this.index = index;
        this.tableName = tableName;
    }

    public int getIndex()
    {
        return index;
    }

    public String getTableName()
    {
        return tableName;
    }
}