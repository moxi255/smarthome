package com.smarthome.app.utils;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 验证工具
 * @author smmh
 * 
 */
public class ValidateUtils {
	
	 /**
     * 检查当前网络是否可用
     *
     * @param context
     */
    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null){
            return false;
        }else{
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){

                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
	 * 验证命令是否执行成功
	 * 
	 * @param byte[]
	 */
	public static boolean validationOrder(byte[] responseBody) {
		try {
			JSONObject object = new JSONObject(new String(responseBody, "utf-8"));
			if (null != object && "true".equals(object.getString("success"))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

}
