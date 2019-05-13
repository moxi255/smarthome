package com.example.abc.smarthome.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.abc.smarthome.widget.SuccinctProgress;


/**
 * 对话框工具
 * @author smmh
 *
 */
public class DialogUtils {
	
    /**
     * 长时间的Toast提示
     * 
     * @param context
     * @param msg
     */
    public static void showToast(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间的Toast提示
     * 
     * @param context
     * @param msgId
     */
    public static void showToast(Context context,int msgId){
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间的Toast提示
     * 
     * @param context
     * @param msgId
     */
    public static void showToastShort(Context context,int msgId){
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间的Toast提示
     * 
     * @param context
     * @param msg
     */
    public static void showToastShort(Context context,String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 提示居中的Toast
     * 
     * @param mContext
     * @param str
     */
    public static void showToastCenter(Context mContext, String str) {
        Toast toast = new Toast(mContext);
        toast = Toast.makeText(mContext, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);//设置居中
        toast.show();
    }

    /**
     * 进度提示框
     * 
     * @param mContext
     * @param
     */
    public static void showProgressDialog(Context mContext, boolean isOK) {
    	if (isOK) {
    		SuccinctProgress.showSuccinctProgress(mContext, "请稍等...",
					SuccinctProgress.THEME_ULTIMATE, true, true);
    		Log.d("status", "show");
    	}
    	else
    		SuccinctProgress.dismiss();
    }
    
    
    /**
     * 自定义弹出提示框
     * 
     * @param mContext
     * @param mTitle
     * @param mMessage
     * @param mClickListener
     */
    public static void showAlertDialog(Context mContext,String mTitle,String mMessage,DialogInterface.OnClickListener mClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mTitle);
        builder.setMessage(mMessage);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", mClickListener);
        builder.show();
    }

}
