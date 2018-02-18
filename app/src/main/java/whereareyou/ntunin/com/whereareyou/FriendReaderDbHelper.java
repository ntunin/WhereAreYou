package whereareyou.ntunin.com.whereareyou;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static whereareyou.ntunin.com.whereareyou.Friends.SQL_CREATE_ENTRIES;
import static whereareyou.ntunin.com.whereareyou.Friends.SQL_DELETE_ENTRIES;

/**
 * Created by nik on 18.02.2018.
 */

public class FriendReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Friends.db";

    public FriendReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(Friend friend) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Friends.FriendEntry.COLUMN_NAME_DEVICE_ID, friend.getId());
        values.put(Friends.FriendEntry.COLUMN_NAME_NAME, friend.getName());
        values.put(Friends.FriendEntry.COLUMN_NAME_EMAIL, friend.getEmail());
        values.put(Friends.FriendEntry.COLUMN_NAME_PHONE, friend.getPhone());
        values.put(Friends.FriendEntry.COLUMN_NAME_IMAGE, friend.getImage());
        long newRowId = db.insert(
                Friends.FriendEntry.TABLE_NAME,
                "null",
                values);
    }

    public List<Friend> select() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                Friends.FriendEntry.COLUMN_NAME_DEVICE_ID,
                Friends.FriendEntry.COLUMN_NAME_NAME,
                Friends.FriendEntry.COLUMN_NAME_EMAIL,
                Friends.FriendEntry.COLUMN_NAME_PHONE,
                Friends.FriendEntry.COLUMN_NAME_IMAGE
        };
        String[] selection = {
                Friends.FriendEntry.COLUMN_NAME_DEVICE_ID,
                Friends.FriendEntry.COLUMN_NAME_NAME,
                Friends.FriendEntry.COLUMN_NAME_EMAIL,
                Friends.FriendEntry.COLUMN_NAME_PHONE,
                Friends.FriendEntry.COLUMN_NAME_IMAGE
        };
        String sortOrder = Friends.FriendEntry.COLUMN_NAME_NAME + " DESC";
        Cursor c = db.query(
                Friends.FriendEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        c.moveToFirst();
        ArrayList<Friend> friends = new ArrayList<>();
        while (c != null && !c.isAfterLast()) {
            String id = c.getString(c.getColumnIndex(Friends.FriendEntry.COLUMN_NAME_DEVICE_ID));
            String name = c.getString(c.getColumnIndex(Friends.FriendEntry.COLUMN_NAME_NAME));
            String image = c.getString(c.getColumnIndex(Friends.FriendEntry.COLUMN_NAME_IMAGE));
            String phone = c.getString(c.getColumnIndex(Friends.FriendEntry.COLUMN_NAME_PHONE));
            String email = c.getString(c.getColumnIndex(Friends.FriendEntry.COLUMN_NAME_EMAIL));

            friends.add(new Friend(id, name, image, phone, email));
            c.moveToNext();
        }
        return  friends;
    }
}
