package com.smarthome.app.utils;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.smarthome.app.model.Scenes;
import com.smarthome.app.receiver.ScenesAlamrReceiver;

/**
 * @author smmh
 *
 */
public class AlarmUtils {
	
	/** 
     * 设定提醒 
     * @param context 
     * @param aManager 
     * @param scenes 
     */  
    public static void setAlarm(Context context, AlarmManager aManager, Scenes scenes)  
    {  
    	String hour = scenes.getTime().split(":")[0];
    	String minute = scenes.getTime().split(":")[1];
    	
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour));  
        c.set(Calendar.MINUTE, Integer.valueOf(minute));
        c.set(Calendar.SECOND, 0);
        
        // 注册AlarmManager的定时服务  
        Intent intent = new Intent(context, ScenesAlamrReceiver.class);// Constants.ACITON_REMIND是自定义的一个action  
        intent.putExtra("id", scenes.getId());
        
        // 使用scenes实体的ID作为PendingIntent的requestCode可以保证PendingIntent的唯一性  
        PendingIntent pi = PendingIntent.getBroadcast(context, scenes.getId(), intent,  
                PendingIntent.FLAG_UPDATE_CURRENT);
		
        // 设定的时间是scenes实体中封装的时间  
        //立即执行
        if (scenes.getType() == 0)
        	aManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        //执行一次
        else if(scenes.getType() == 1) {
        	aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        //每天执行一次
        } else if (scenes.getType() == 2) {
        	aManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24*60*60*1000, pi);
        	//每隔三分钟执行一次，查看门窗磁是否异常
        } else if (scenes.getType() == 3) {
        	aManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5*60*1000, pi);
        }
        	
    }  
    
    
    /** 
     * 取消提醒 
     * @param context 
     * @param aManager 
     * @param scenes 
     */  
    public static void cancelAlarm(Context context,AlarmManager aManager,Scenes scenes)  
    {  
        // 取消AlarmManager的定时服务  
        Intent intent=new Intent(context, ScenesAlamrReceiver.class);// 和设定闹钟时的action要一样  
  
        // 这里PendingIntent的requestCode、intent和flag要和设定闹钟时一样  
        PendingIntent pi=PendingIntent.getBroadcast(context, scenes.getId(), intent,  
                PendingIntent.FLAG_UPDATE_CURRENT);  
        aManager.cancel(pi);  
    }  
}
