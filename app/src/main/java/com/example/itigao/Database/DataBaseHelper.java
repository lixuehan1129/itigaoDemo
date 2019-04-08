package com.example.itigao.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 最美人间四月天 on 2018/12/27.
 */

public class DataBaseHelper extends SQLiteOpenHelper {


    //定义数据库文件名
    public static final String TABLE_NAME = "iTiGao.db";

    public static final String CREATE_USER = "create table user ("
            + "id integer primary key autoincrement, "
            + "user_name text, "
            + "user_phone text, "
            + "user_sort integer, "
            + "user_sex integer, "
            + "user_birth text, "
            + "user_picture text)";


    /**
     * @param context
     * @param name    数据库文件的名称
     * @param factory null
     * @param version 数据库文件的版本
     */
    private DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                           int version) {
        super(context, name, factory, version);
    }

    // 对外提供构造函数
    public DataBaseHelper(Context context, int version) {
        //调用该类中的私有构造函数
        this(context, TABLE_NAME, null, version);
    }


    // 当第一次创建数据的时候回调方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

    // 当数据库被打开时回调该方法
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d("DB", "onOpen");
    }
}
