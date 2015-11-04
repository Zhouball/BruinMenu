package zhou.allen.bruinmenu;

import android.content.ContentValues;
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
        db.execSQL(MenuDBContract.KitchenEntry.CREATE_TABLE);
        db.execSQL(MenuDBContract.HallEntry.CREATE_TABLE);
        db.execSQL(MenuDBContract.MenuEntry.CREATE_TABLE);
        db.execSQL(MenuDBContract.Favorites.CREATE_TABLE);
    }

    // Method is called during an upgrade of the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MenuDBContract.KitchenEntry.DELETE_TABLE);
        db.execSQL(MenuDBContract.HallEntry.DELETE_TABLE);
        db.execSQL(MenuDBContract.MenuEntry.DELETE_TABLE);
        db.execSQL(MenuDBContract.Favorites.DELETE_TABLE);
        onCreate(db);
    }

    /* Access database */
    /*
    public List<String> getEntryByLocAndMealTime(String loc,String mealTime) {
        List<String> returnList = new ArrayList<String>();

        String selectQuery = "SELECT " + MenuDBContract.MenuEntry.COLUMN_NAME_ITEM + " FROM " + MenuDBContract.MenuEntry.TABLE_NAME +
                " WHERE " + MenuDBContract.MenuEntry.COLUMN_NAME_LOC + "='" + loc +
                "' AND " + MenuDBContract.MenuEntry.COLUMN_NAME_MEALTIME + "='" + mealTime + "'";


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
    */

    public List<Hall> getHallsByMealTime(String mealTime) {
        List<Hall> returnList = new ArrayList<Hall>();

        String selectQuery = "SELECT * FROM " + MenuDBContract.HallEntry.TABLE_NAME +
                " WHERE " + MenuDBContract.HallEntry.COLUMN_NAME_MEALTIME + "='" + mealTime + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.HallEntry.COLUMN_NAME_ITEM));
                String m = c.getString(c.getColumnIndex(MenuDBContract.HallEntry.COLUMN_NAME_MEALTIME));
                long id = c.getLong(c.getColumnIndex(MenuDBContract.HallEntry._ID));

                // adding to list
                returnList.add(new Hall(s, m, id));
            } while (c.moveToNext());
        }
        c.close();
        return returnList;
    }

    public List<Kitchen> getKitchensByHall(Hall hall) {
        List<Kitchen> returnList = new ArrayList<Kitchen>();

        String selectQuery = "SELECT * FROM " + MenuDBContract.KitchenEntry.TABLE_NAME +
                " WHERE " + MenuDBContract.KitchenEntry.COLUMN_NAME_HALL + "='" + hall.getId() + "'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.KitchenEntry.COLUMN_NAME_ITEM));
                int id = c.getInt(c.getColumnIndex(MenuDBContract.KitchenEntry._ID));

                // adding to list
                returnList.add(new Kitchen(s, id));
            } while (c.moveToNext());
        }
        c.close();
        return returnList;
    }

    public List<MenuItem> getMenuItemsByKitchen(Kitchen kitchen) {
        List<MenuItem> returnList = new ArrayList<MenuItem>();
        List<String> favorites = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + MenuDBContract.MenuEntry.TABLE_NAME +
                " WHERE " + MenuDBContract.MenuEntry.COLUMN_NAME_KITCHEN + "='" + kitchen.getId() + "'";

        String favoritesQuery = "SELECT " + MenuDBContract.Favorites.COLUMN_NAME_ITEM + " FROM " + MenuDBContract.Favorites.TABLE_NAME;

        Log.e(LOG, favoritesQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor b = db.rawQuery(favoritesQuery, null);
        if (b.moveToFirst()) {
            do {
                String s = b.getString(b.getColumnIndex(MenuDBContract.Favorites.COLUMN_NAME_ITEM));
                favorites.add(s);
            } while (b.moveToNext());
        }
        b.close();

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM));
                String url = c.getString(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_NUTRIURL));
                int v = c.getInt(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_VEG));
                // adding to list
                boolean f = favorites.contains(s);
                int id = c.getInt(c.getColumnIndex(MenuDBContract.MenuEntry._ID));
                returnList.add(new MenuItem(s, url, v, f, id));
            } while (c.moveToNext());
        }
        c.close();
        return returnList;
    }

    public List<String> getFavorites() {
        List<String> returnList = new ArrayList<String>();

        String selectQuery = "SELECT " + MenuDBContract.Favorites.COLUMN_NAME_ITEM + " FROM " + MenuDBContract.Favorites.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.Favorites.COLUMN_NAME_ITEM));

                // adding to list
                returnList.add(s);
            } while (c.moveToNext());
        }
        c.close();
        return returnList;
    }

    public long addFavorite(String favorite) {
        ContentValues values = new ContentValues();
        values.put(MenuDBContract.Favorites.COLUMN_NAME_ITEM, favorite);
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(MenuDBContract.Favorites.TABLE_NAME, null, values);
        return id;
    }
}