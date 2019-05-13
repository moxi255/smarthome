package com.smarthome.app.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.smarthome.app.model.Equipment;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.model.Weather;
import com.smarthome.app.model.mLog;

/**
 * @author smmh
 *	数据库操作类
 */
public class DBbean {
	
	private static DBbean sInstance;
	private static DatabaseHelper dbHelper;
	private static SQLiteDatabase db;
	private static ContentValues values;
	
	/**
	 * 初始化bean
	 */
	public static void initDBbean(DatabaseHelper helper) {
		dbHelper = helper;
		sInstance = new DBbean();
		db = dbHelper.getWritableDatabase();
		values = new ContentValues();
	}
	
	/**
	 *  获得DBbean实例
	 */
	public static DBbean getInstance() {
		return sInstance;
	}
	
	/**
	 *  插入所有设备
	 *  
	 *  @param List
	 */
	public void insertAllEquipments(List<Equipment> list) {
		for (int i = 0; i < list.size(); i++) {
			values.clear();
			values.put("id", list.get(i).getId());
			values.put("name", list.get(i).getName());
			values.put("type", list.get(i).getType());
			values.put("imageId", list.get(i).getImageId());
			values.put("state", list.get(i).getState());
			db.insert("Equipment", null, values);
		}
	}
	
	
	/**
	 *  查询所有设备
	 *  
	 *  @return List
	 */
	public List<Equipment> queryAllEquipmentsToList() {
		List<Equipment> list = new ArrayList<Equipment>();
		Cursor cursor = db.query("Equipment", null, null, null, 
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Equipment equipment = new Equipment();
				equipment.setId(cursor.getString(cursor.getColumnIndex("id")));
				equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
				equipment.setType(cursor.getInt(cursor.getColumnIndex("type")));
				equipment.setImageId(cursor.getInt(cursor.getColumnIndex("imageId")));
				equipment.setState(cursor.getInt(cursor.getColumnIndex("state")));
				list.add(equipment);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	
	/**
	 *  查询所有设备
	 *  
	 * @return Map
	 */
	public Map<String, Equipment> queryAllEquipmentsToMap() {
		Map<String, Equipment> map = new HashMap<String, Equipment>();
		Cursor cursor = db.query("Equipment", null, null, null, 
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Equipment equipment = new Equipment();
				equipment.setId(cursor.getString(cursor.getColumnIndex("id")));
				equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
				equipment.setType(cursor.getInt(cursor.getColumnIndex("type")));
				equipment.setImageId(cursor.getInt(cursor.getColumnIndex("imageId")));
				equipment.setState(cursor.getInt(cursor.getColumnIndex("state")));
				map.put(equipment.getId(), equipment);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return map;
	}
	
	
	/**
	 *  查询指定 id 设备
	 */
	public Equipment queryEquipmentFromId(String id) {
		Equipment equipment = new Equipment();
		Cursor cursor = db.query("Equipment", null, "id=?", new String[]{ id }, 
				null, null, null);
		if (cursor.moveToFirst()) {
			equipment.setId(cursor.getString(cursor.getColumnIndex("id")));
			equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
			equipment.setType(cursor.getInt(cursor.getColumnIndex("type")));
			equipment.setImageId(cursor.getInt(cursor.getColumnIndex("imageId")));
			equipment.setState(cursor.getInt(cursor.getColumnIndex("state")));
		}
		cursor.close();
		return equipment;
	}
	
	/**
	 *  查询指定 type 设备
	 */
	public List<Equipment> queryEquipmentsFormType(int type) {
		Equipment equipment = new Equipment();
		List<Equipment> list = new ArrayList<Equipment>();
		Cursor cursor = db.query("Equipment", null, "type=?", new String[]{ String.valueOf(type) }, 
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				equipment.setId(cursor.getString(cursor.getColumnIndex("id")));
				equipment.setName(cursor.getString(cursor.getColumnIndex("name")));
				equipment.setType(cursor.getInt(cursor.getColumnIndex("type")));
				equipment.setImageId(cursor.getInt(cursor.getColumnIndex("imageId")));
				equipment.setState(cursor.getInt(cursor.getColumnIndex("state")));
				list.add(equipment);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 更新指定设备状态
	 */
	public void updateEquipment(Equipment equipment) {
		values.clear();
		values.put("state", equipment.getState());
		db.update("Equipment", values, "id = ?", new String[] { equipment.getId() });
	}
	
	/**
	 * 更新所有设备状态
	 * 
	 * @param List
	 */
	public void updateAllEquipmentsByList(List<Equipment> list) {
		for (int i = 0; i < list.size(); i++) {
			updateEquipment(list.get(i));
		}
	}
	
	/**
	 * 更新所有设备状态
	 * 
	 * @param Map
	 */
	public void updateAllEquipmentsByMap(Map<String, Equipment> map) {
		for (Map.Entry<String, Equipment> entry : map.entrySet()) {
			updateEquipment(entry.getValue());
		}
	}
	
	/**
	 * 重置所有设备状态为 关闭
	 */
	public void reasetEquipmentsState() {
		values.clear();
		values.put("state", 0);
		db.update("Equipment", values, null, null);
	}
	
	/**
	 * 插入日志
	 */
	public void insertEquipmentLog(Equipment equipment, String order) {
		values.clear();
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("id", equipment.getId());
		values.put("context", equipment.getName() + "被触发一次操作: " + order);
		values.put("datetime", sdf.format(datetime)+"");
		db.insert("mLog", null, values);
	}
	
	/**
	 * 取出日志
	 */
	public List<mLog> queryEquipmentLog(String id) {
		List<mLog> list = new ArrayList<mLog>();
		Cursor cursor = null;
		if ("all".equals(id)) {
			cursor = db.query("mLog", null, null, null, 
					null, null, null);
		} else {
			cursor = db.query("mLog", null, "id=?", new String[]{ id }, 
					null, null, null);
		}
		if (cursor.moveToLast()) {
			do {
				mLog log = new mLog();
				log.setId(cursor.getString(cursor.getColumnIndex("id")));
				log.setContext(cursor.getString(cursor.getColumnIndex("context")));
				log.setDatetime(cursor.getString(cursor.getColumnIndex("datetime")));
				list.add(log);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 删除日志
	 */
	public void deleteEquipmentLog(String id) {
		if ("all".equals(id)) {
			db.delete("mLog", null, null);
		} else {
			db.delete("mLog", "id=?", new String[]{ id });
		}
	}
	
	/**
	 * 存储场景
	 */
	public void insertScenes(Scenes scenes) {
		values.clear();
		values.put("name", scenes.getName());
		values.put("type", scenes.getType());
		values.put("event", scenes.getEvent());
		values.put("time", scenes.getTime());
		values.put("state", scenes.getState());
		db.insert("Scenes", null, values);
	}
	
	/**
	 *  查询指定 id 场景
	 */
	public Scenes queryScenesById(int id) {
		Scenes scenes = new Scenes();
		Cursor cursor = db.query("Scenes", null, "id=?", new String[]{ id+"" }, 
				null, null, null);
		if (cursor.moveToFirst()) {
			scenes.setId(cursor.getInt(cursor.getColumnIndex("id")));
			scenes.setName(cursor.getString(cursor.getColumnIndex("name")));
			scenes.setType(cursor.getInt(cursor.getColumnIndex("type")));
			scenes.setEvent(cursor.getString(cursor.getColumnIndex("event")));
			scenes.setTime(cursor.getString(cursor.getColumnIndex("time")));
			scenes.setState(cursor.getInt(cursor.getColumnIndex("state")));
		}
		cursor.close();
		return scenes;
	}
	
	/**
	 *  查询所有场景
	 */
	public List<Scenes> queryAllScenes() {
		List<Scenes> list = new ArrayList<Scenes>();
		Cursor cursor = db.query("Scenes", null, null, null, 
				null, null, null);
		if (cursor.moveToLast()) {
			do {
				Scenes scenes = new Scenes();
				scenes.setId(cursor.getInt(cursor.getColumnIndex("id")));
				scenes.setName(cursor.getString(cursor.getColumnIndex("name")));
				scenes.setType(cursor.getInt(cursor.getColumnIndex("type")));
				scenes.setEvent(cursor.getString(cursor.getColumnIndex("event")));
				scenes.setTime(cursor.getString(cursor.getColumnIndex("time")));
				scenes.setState(cursor.getInt(cursor.getColumnIndex("state")));
				list.add(scenes);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}
	
	/**
	 * 删除场景
	 */
	public void deleteScenesById(String id) {
			db.delete("Scenes", "id=?", new String[]{ id });
	}
	
	/**
	 * 更新场景
	 */
	public void updateScenes(Scenes scenes) {
		values.clear();
		values.put("event", scenes.getEvent());
		values.put("name", scenes.getName());
		values.put("time", scenes.getTime());
		values.put("state", scenes.getState());
		values.put("type", scenes.getType());
		db.update("Scenes", values, "id = ?", new String[] { scenes.getId()+"" });
	}
	
	/**
	 * 存储设置
	 */
	public void insertSetting(String id, int state) {
		values.clear();
		values.put("id", id);
		values.put("state", state);
		db.insert("Setting", null, values);
	}
	
	/**
	 * 修改设置
	 */
	public void updateSetting(String id, int state) {
		values.clear();
		values.put("state", state);
		db.update("Setting", values, "id = ?", new String[] { id });
	}
	
	/**
	 * 查询设置
	 */
	public int querySettingById(String id) {
		values.clear();
		Cursor cursor = db.query("Setting", null, "id=?", new String[]{ id }, 
				null, null, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(cursor.getColumnIndex("state"));
		}
		return 1;
	}
	
	/**
	 * 插入消息
	 */
	public void insertMessage(String type, String context) {
		values.clear();
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("type", type);
		values.put("context", context);
		values.put("datetime", sdf.format(datetime)+"");
		db.insert("Message", null, values);
	}
	
	/**
	 * 取出消息
	 */
	public List<mLog> queryMessage() {
		List<mLog> list = new ArrayList<mLog>();
		Cursor cursor = null;
		cursor = db.query("Message", null, null, null, 
				null, null, null);
		if (cursor.moveToLast()) {
			do {
				mLog log = new mLog();
				log.setId(cursor.getString(cursor.getColumnIndex("type")));
				log.setContext(cursor.getString(cursor.getColumnIndex("context")));
				log.setDatetime(cursor.getString(cursor.getColumnIndex("datetime")));
				list.add(log);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 删除消息
	 */
	public void deleteMessage() {
		db.delete("Message", null, null);
	}
	
	/**
	 * 插入天气
	 */
	public void insertWeather(String temp, String hum) {
		values.clear();
		Date datetime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		values.put("temp", temp);
		values.put("humidity", hum);
		values.put("datetime", sdf.format(datetime)+"");
		db.insert("Weather", null, values);
	}
	
	/**
	 * 取出天气
	 */
	public List<Weather> queryWeather() {
		List<Weather> list = new ArrayList<Weather>();
		Cursor cursor = null;
		cursor = db.query("Weather", null, null, null, 
				null, null, null);
		if (cursor.moveToLast()) {
			do {
				Weather weather = new Weather();
				weather.setTemp(cursor.getString(cursor.getColumnIndex("temp")));
				weather.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
				weather.setDatetime(cursor.getString(cursor.getColumnIndex("datetime")));
				list.add(weather);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
		return list;
	}
	
	
	/**
	 * 删除天气
	 */
	public void deleteWeather() {
		db.delete("Weather", null, null);
	}
	
}
