package zhou.allen.bruinmenu;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Owner on 10/20/2015.
 */
public class MenuDBHelper extends SQLiteOpenHelper {

    private static final String LOG = MenuDBHelper.class.getName();
    private Context _context;

    public MenuDBHelper(Context context) {
        super(context, MenuDBContract.DATABASE_NAME, null, MenuDBContract.DATABASE_VERSION);
        _context = context;
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
                long id = c.getLong(c.getColumnIndex(MenuDBContract.KitchenEntry._ID));

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
            ArrayList<String> favoriteFood = new ArrayList<>();
            do {
                String s = c.getString(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_ITEM));
                String url = c.getString(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_NUTRIURL));
                int v = c.getInt(c.getColumnIndex(MenuDBContract.MenuEntry.COLUMN_NAME_VEG));
                // adding to list
                boolean f = favorites.contains(s);
                long id = c.getLong(c.getColumnIndex(MenuDBContract.MenuEntry._ID));
                returnList.add(new MenuItem(s, url, v, f, id));

                // sending notification if favorite
                if(f) {
                    favoriteFood.add(kitchen.getItem() + " - " + s);
                }
            } while (c.moveToNext());
            if(!favoriteFood.isEmpty()) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(_context).
                        setSmallIcon(R.drawable.vegetarian).
                        setContentTitle("Today's Favorites").
                        setContentText(favoriteFood.get(0) + "....");
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.setBigContentTitle("Today's Favorites");
                for(String foods : favoriteFood) {
                    inboxStyle.addLine(foods);
                }
                builder.setStyle(inboxStyle);
                builder.setContentIntent(PendingIntent.getActivity(_context, 0, new Intent(_context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
                builder.setAutoCancel(true);
                NotificationManager notifManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
                notifManager.notify((int) kitchen.getId(), builder.build());
            }
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

    public boolean deleteFavorite(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MenuDBContract.Favorites.TABLE_NAME, MenuDBContract.Favorites.COLUMN_NAME_ITEM + " = ?" , new String[] {name}) > 0;
    }
}