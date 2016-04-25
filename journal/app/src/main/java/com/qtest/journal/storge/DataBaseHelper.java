package com.qtest.journal.storge;

import com.qtest.journal.model.MessageConstantField;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 1;
    private static final String DB_NAME = "share.db";
    private static DataBaseHelper instance = null;
    public static final String MESSAGE_TABLE_NAME = "message_list";
	public static final String CREATE_TABLE = "create table " + MESSAGE_TABLE_NAME + 
			"( " + MessageConstantField.ID + " TEXT primary key, "
			+ MessageConstantField.NAME + " TEXT, "
			+ MessageConstantField.READ + " TEXT "
			+ MessageConstantField.TITLE + " TEXT "

	
			+ ");";
    public synchronized static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context);
        }
        return instance;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("drop table if exists " + MESSAGE_TABLE_NAME);
		db.execSQL(CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

}
