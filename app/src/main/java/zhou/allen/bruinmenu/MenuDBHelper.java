package zhou.allen.bruinmenu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 10/20/2015.
 */
public class MenuDBHelper extends SQLiteOpenHelper {

    private static final String LOG = MenuDBHelper.class.getName();

    public MenuDBHelper(Context context) {
        super(context, MenuDBContract.DATABASE_NAME, null, MenuDBContract.DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MenuDBContract.MenuEntry.CREATE_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MenuDBContract.MenuEntry.DELETE_TABLE);
        onCreate(db);
    }

    /* Access database */

    public List<String> getEntryByLocAndMealTime(String loc,String mealTime) {
        List<String> returnList = new ArrayList<String>();

        String selectQuery = "SELECT " + MenuDBContract.MenuEntry.COLUMN_NAME_ITEM + " FROM " + MenuDBContract.MenuEntry.TABLE_NAME +
                " WHERE " + MenuDBContract.MenuEntry.COLUMN_NAME_LOC + "='" + loc +
                "' AND " + MenuDBContract.MenuEntry.COLUMN_NAME_MEALTIME + "='" + mealTime + "'";
        /*
        SELECT menuitem
        FROM menu
        WHERE loc='String loc' AND mealtime='String mealTime'
         */

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM));

                // adding to list
                returnList.add(s);
            } while (c.moveToNext());
        }
        c.close();
        return returnList;
    }
}