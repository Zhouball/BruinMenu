package zhou.allen.bruinmenu;

import android.provider.BaseColumns;

/**
 * Created by Owner on 10/20/2015.
 */
public final class MenuDBContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "menudb.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";


    public MenuDBContract() {
    }

    public static abstract class MenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "menu";
        public static final String COLUMN_NAME_ITEM = "menuitem";
        public static final String COLUMN_NAME_LOC = "loc";
        public static final String COLUMN_NAME_MEALTIME = "mealtime";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_LOC + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_MEALTIME + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}