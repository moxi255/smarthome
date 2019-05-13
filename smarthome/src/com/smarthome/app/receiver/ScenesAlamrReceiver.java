package com.smarthome.app.receiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.smarthome.app.api.ApiHttpClient;
import com.smarthome.app.api.ApiMethod;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.fragment.ScenesFragment;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.ValidateUtils;

/**
 * @author smmh
 *
 */
public class ScenesAlamrReceiver extends BroadcastReceiver {  
  
    @Override  
    public void onReceive(Context context, Intent intent) {  
    	
    	ScenesThread thread = new ScenesThread(intent, context);
    	new Thread(thread).start();
    	
    }
}


class ScenesThread implements Runnable {
	
	private String[] EQUIPMENTID = {
			"light1","light2","light3","light4","light5","light6",
			"curtain1","curtain2","air","tv"
	};
	
	private String[] ORDER = { "open", "close", "power" };
	
	private String[] URL = { "light/lightControl", "curtain/curtainControl", "air/airControl", "av/avControl" };
	
	private List<String> list = new ArrayList<>();
	private boolean unusualFlag= false;
	private String text = "";
	
	private Scenes scenes;
	
	private Intent intent;
	private Context context;
	
	private SyncHttpClient client;
	
	public ScenesThread(Intent intent, Context context) {
		this.intent = intent;
		this.context = context;
		this.client  = new SyncHttpClient();
	}
	
	@Override
	public void run() {
		int id = intent.getIntExtra("id", -1);
    	if (id > 0) {
    		scenes = DBbean.getInstance().queryScenesById(id);
    		String[] events = scenes.getEvent().split("\\|");
    		startScenes(events);
    		//任务执行完关闭闹钟
    		scenes.setState(0);
    		DBbean.getInstance().updateScenes(scenes);
    		if (DBbean.getInstance().querySettingById("scenes") == 1)
    			notification(context, 0);
    		DBbean.getInstance().insertMessage("场景提醒", scenes.getName() + "已完成");
    	} else if (id == -1) {
    		unusualFlag = true;
    		ApiMethod.queryProtectSensor(responseHandler);
    	}
	}
	
	/**
	 * 通知
	 */
	private void notification(Context context, int type) {
		String title = null;
		if (type == 0) {
			title = "场景执行";
			text = scenes.getName() + "已完成";
		} else if (type == 1)
		{
			title = "异常提醒";
		}
			
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  
                new Intent(context, ScenesFragment.class), 0);  
        // 通过Notification.Builder来创建通知，注意API Level  
        // API16之后才支持  
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.alert_dark_frame)  
                .setTicker("您有新通知，请注意查收！") 
                .setContentTitle(title) 
                .setContentText(text)  
                .setContentIntent(pendingIntent).setNumber(type).build();  
                                                                        
        notify.flags |= Notification.FLAG_AUTO_CANCEL;   
        manager.notify(0, notify); 
	}
	
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			if (unusualFlag) {
				try {
					String value = new String(responseBody,"utf-8");
					JSONArray jsonArray = new JSONArray(value);
					for (int i = 0; i < 8; i++) {
						if ("异常".equals(jsonArray.getJSONObject(i).getString("state"))) {
							list.add(jsonArray.getJSONObject(i).getString("name"));
						}
					}
					if ((DBbean.getInstance().querySettingById("unusual") == 1) && (list.size() != 0)) {
		    			for (int i = 0; i< list.size(); i++)
		    				text += list.get(i) + " ";
		    			text += "异常";
		    			DBbean.getInstance().insertMessage("异常提醒", text);
		    			notification(context, 1);
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
	};
    
	
	private void changeEquipmentState(String id, int state) {
			Equipment equipment = DBbean.getInstance().queryEquipmentFromId(id);
			equipment.setState(state == 0 ? 1:0);
			DBbean.getInstance().updateEquipment(equipment);
			MainApplication.equipmentsMap.put(equipment.getId(), equipment);
	}
	
	private void startScenes(String[] events)   {
		int[] eventsId = new int[events.length];
		for (int i = 0; i < events.length; i++) {
			eventsId[i] = Integer.valueOf(events[i]);
		}
		Arrays.sort(eventsId);
		
    	for (int i = 0; i < events.length; i++) {
    		RequestParams params = new RequestParams();
    		changeEquipmentState(EQUIPMENTID[eventsId[i]/2], eventsId[i]%2);
    		if (eventsId[i] < 12) {
    			params.put("key", EQUIPMENTID[eventsId[i]/2]);
    			params.put("order", ORDER[eventsId[i]%2]);
    			client.post("http://192.168.27.100:8080/js/html/" + URL[0], params, responseHandler1);
//    			ApiHttpClient.post(URL[0], params, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] responseBody) {
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//					}
//    				
//    			});
    		} else if (eventsId[i] < 16) {
    			params.put("key", EQUIPMENTID[eventsId[i]/2]);
    			params.put("order", ORDER[eventsId[i]%2]);
    			client.post("http://192.168.27.100:8080/js/html/" + URL[1], params, responseHandler);
    		} else if (eventsId[i] < 18) {
    			params.put("type", EQUIPMENTID[eventsId[i]/2]);
    			params.put("order", ORDER[eventsId[i]%2]);
    			client.post("http://192.168.27.100:8080/js/html/" + URL[2], params, responseHandler1);
//    			ApiHttpClient.post(URL[2], params, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] responseBody) {
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//					}
//    				
//    			});
    		} else if (eventsId[i] < 20) {
    			params.put("equ", EQUIPMENTID[eventsId[i]/2]);
    			params.put("order", "power");
    			client.post("http://192.168.27.100:8080/js/html/" + URL[3], params, responseHandler1);
//    			ApiHttpClient.post(URL[3], params, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] responseBody) {
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//					}
//    				
//    			});
    		}
    		try {
        		Thread.sleep(2000);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
    	}
    }
	
	AsyncHttpResponseHandler responseHandler1 = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
		}
		
	};
	
	
}

