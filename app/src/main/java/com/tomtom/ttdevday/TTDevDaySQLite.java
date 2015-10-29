package com.tomtom.ttdevday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by marekdef on 28.10.15.
 */
public class TTDevDaySQLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "presentations";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_PRESENTATIONS = "PRESENTATIONS";
    public static final String ID = "id";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String NO_VOTES = "noVotes";

    public TTDevDaySQLite(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PRESENTATIONS + " ( " + ID + " TEXT PRIMARY KEY, " + AUTHOR + " TEXT, " + TITLE + " TEXT, " + DESCRIPTION + " TEXT, " + NO_VOTES + " INTEGER ) ");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        if(oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENTATIONS);
            onCreate(sqLiteDatabase);
        }
    }
}
