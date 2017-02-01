package com.rhombus.bruinmenu;

import android.provider.BaseColumns;

/**
 * Created by Owner on 10/20/2015.
 */
public final class MenuDBContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "menudb.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT";
    private static final String COMMA_SEP = ",";

    public MenuDBContract() {
    }

    public static abstract class MenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "menu";
        public static final String COLUMN_NAME_ITEM = "menuitem";
        public static final String COLUMN_NAME_KITCHEN = "kitchen_id";
        public static final String COLUMN_NAME_NUTRIURL = "nutriurl";
        public static final String COLUMN_NAME_VEG = "vegetarian";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_KITCHEN + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_NUTRIURL + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_VEG + INT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class HallEntry implements BaseColumns {
        public static final String TABLE_NAME = "halls";
        public static final String COLUMN_NAME_ITEM = "hall";
        public static final String COLUMN_NAME_MEALTIME = "mealtime";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_MEALTIME + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class KitchenEntry implements BaseColumns {
        public static final String TABLE_NAME = "kitchens";
        public static final String COLUMN_NAME_ITEM = "kitchen";
        public static final String COLUMN_NAME_HALL = "hall_id";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_HALL + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Favorites implements BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_NAME_ITEM = "itemname";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ITEM + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}