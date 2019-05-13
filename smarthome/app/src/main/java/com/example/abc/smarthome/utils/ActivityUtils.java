package com.example.abc.smarthome.utils;

import android.content.Context;
import android.content.Intent;

import com.example.abc.smarthome.model.Equipment;
import com.smarthome.app.activity.StatusAirActivity;
import com.smarthome.app.activity.StatusAvActivity;
import com.smarthome.app.activity.StatusCurtainActivity;
import com.smarthome.app.activity.StatusDoorbellMonitorActivity;
import com.smarthome.app.activity.StatusLightActivity;
import com.smarthome.app.activity.StatusNetCameraActivity;
import com.smarthome.app.activity.StatusProtectCameraActivity;
import com.smarthome.app.activity.StatusProtectSensorActivity;
import com.smarthome.app.activity.StatusSensorActivity;
import com.smarthome.app.model.Equipment;

/**
 * @author smmh
 *
 */
public class ActivityUtils {
	

	/**
	 * 页面跳转
	 */
	public static void startOtherActivity(Equipment equipment, Context mContext) {
		Class<?> cls = null;
		switch(equipment.getType()) {
		case 0:
			cls = StatusLightActivity.class;
			break;
		case 1:
			cls = StatusCurtainActivity.class;
			break;
		case 2:
			cls = StatusAvActivity.class;
			break;
		case 3:
			cls = StatusAirActivity.class;
			break;
		case 4:
			cls = StatusSensorActivity.class;
			break;
		case 5:
			cls = StatusProtectSensorActivity.class;
			break;
		case 6:
			cls = StatusProtectCameraActivity.class;
			break;
		case 7:
			cls = StatusNetCameraActivity.class;
			break;
		case 8:
			cls = StatusDoorbellMonitorActivity.class;
			break;
		default:
			break;
		}
		Intent intent = new Intent(mContext, cls);
		intent.putExtra("equipmentId", equipment.getId());
		mContext.startActivity(intent);
	}
	
}
