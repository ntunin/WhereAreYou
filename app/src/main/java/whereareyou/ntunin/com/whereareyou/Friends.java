package whereareyou.ntunin.com.whereareyou;

import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by nik on 18.02.2018.
 */

public class Friends {

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FriendEntry.TABLE_NAME + " (" +
                    FriendEntry._ID + " INTEGER PRIMARY KEY," +
                    FriendEntry.COLUMN_NAME_DEVICE_ID + TEXT_TYPE + COMMA_SEP +
                    FriendEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    FriendEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP +
                    FriendEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP +
                    FriendEntry.COLUMN_NAME_EMAIL + TEXT_TYPE +
            " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FriendEntry.TABLE_NAME;

    public Friends(){

    }

    public static abstract class FriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "friends";
        public static final String COLUMN_NAME_DEVICE_ID = "device_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_IMAGE = "image";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}
