package com.smarthome.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author smmh
 *	数据库帮助类
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	//创建 equipment表 语句
	private static final String CREATE_EQUIPMENT = "create table Equipment (" 
			+ "id text primary key, "
			+ "name text, "
			+ "type integer, "
			+ "imageId integer, "
			+ "state integer) ";
	
	//创建 log表 语句
	private static final String CREATE_LOG = "create table mLog ("
			+ "number integer primary key autoincrement, "
			+ "id text, "
			+ "context text, "
			+ "datetime text)";
	
	//创建 message表 语句
	private static final String CREATE_MESSAGE = "create table Message ("
			+ "number integer primary key autoincrement, "
			+ "type text, "
			+ "context text, "
			+ "datetime text)";
	
	//创建 Scenes表 语句
	private static final String CREATE_SCENES = "create table Scenes ("
			+ "id integer primary key autoincrement, "
			+ "name text, "
			+ "type integer, "
			+ "event text, "
			+ "time text, "
			+ "state integer)";
	
	//创建 setting表 语句
	private static final String CREATE_SETTING = "create table Setting ("
			+ "id text primary key, "
			+ "state integer)";
		
	//创建 setting表 语句
	private static final String CREATE_WEATHER = "create table Weather ("
			+ "id integer primary key autoincrement, "
			+ "temp text, "
			+ "humidity text, "
			+ "datetime text) ";
	
	@SuppressWarnings("unused")
	private Context mContext;
	
	public DatabaseHelper(Context context, String name, 
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}


	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_EQUIPMENT);
		db.execSQL(CREATE_LOG);
		db.execSQL(CREATE_SCENES);
		db.execSQL(CREATE_SETTING);
		db.execSQL(CREATE_MESSAGE);
		db.execSQL(CREATE_WEATHER);
	}

	/**
	 * 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists Equipment");
		db.execSQL("drop table if exists mLog");
		db.execSQL("drop table if exists Scenes");
		db.execSQL("drop table if exists Setting");
		db.execSQL("drop table if exists Message");
		db.execSQL("drop table if exists Weather");
		onCreate(db);
	}
	
}
