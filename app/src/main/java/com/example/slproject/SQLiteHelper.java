package com.example.slproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    // 데이터베이스
    private static final String DATABASE_NAME      = "slp.db";
    private static final int DATABASE_VERSION      = 1;
    // 테이블
    public static final String TABLE_NAME       = "Dictionary";
    public static final String COLUMN_ID        = "id";
    public static final String COLUMN_TITLE      = "title";
    public static final String COLUMN_VIDEO      = "video";
    public static final String COLUMN_ABS        = "pos";
    public static final String COLUMN_EXP        = "exp";

    private static final String DATABASE_CREATE_TEAM = "create table "
            + TABLE_NAME + "(" + COLUMN_ID + " integer primary key,"
            + COLUMN_TITLE + " text, "
            + COLUMN_VIDEO + " text, "
            + COLUMN_ABS + " text, "
            + COLUMN_EXP + " text);";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 앱을 삭제후 앱을 재설치시 DB 삭제 후 재설치
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(DATABASE_CREATE_TEAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
