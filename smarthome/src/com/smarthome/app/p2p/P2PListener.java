package com.smarthome.app.p2p;

import android.content.Intent;
import android.util.Log;

import com.p2p.core.P2PInterface.IP2P;
import com.smarthome.app.activity.StatusDoorbellMonitorActivity;
import com.smarthome.app.ui.MainApplication;

/**
 * Created by wzy on 2016/6/13.
 */
public class P2PListener implements IP2P {
	
	private static String TAG = "P2PListener";
	
    @Override
    public void vCalling(boolean isOutCall, String threeNumber, int type) {
    		Log.d(TAG, "vCalling" + "-type: " + type);
    }

    @Override
    public void vReject(int reason_code) {
    	Log.d(TAG, "vReject");
        Intent intent = new Intent();
        intent.setAction(StatusDoorbellMonitorActivity.P2P_REJECT);
        intent.putExtra("reason_code", reason_code);
        MainApplication.app.sendBroadcast(intent);
    }

    @Override
    public void vAccept(int type, int state) {
    	Log.d(TAG, "vAccept");
        Intent accept = new Intent();
        accept.setAction(StatusDoorbellMonitorActivity.P2P_ACCEPT);
        accept.putExtra("type", new int[]{type, state});
        MainApplication.app.sendBroadcast(accept);
    }

    @Override
    public void vConnectReady() {
    	Log.d(TAG, "vConnectReady");
        Intent intent = new Intent();
        intent.setAction(StatusDoorbellMonitorActivity.P2P_READY);
        MainApplication.app.sendBroadcast(intent);
    }

    @Override
    public void vAllarming(String srcId, int type, boolean isSupportExternAlarm, int iGroup, int iItem, boolean isSurpportDelete) {
    	Log.d(TAG, "vAllarming");
    }

    @Override
    public void vChangeVideoMask(int state) {
    	Log.d(TAG, "vChangeVideoMask");
    }

    @Override
    public void vRetPlayBackPos(int length, int currentPos) {
    	Log.d(TAG, "vRetPlayBackPos");
    }

    @Override
    public void vRetPlayBackStatus(int state) {
    	Log.d(TAG, "vRetPlayBackStatus");
    }

    @Override
    public void vGXNotifyFlag(int flag) {
    	Log.d(TAG, "vGXNotifyFlag");
    }

    @Override
    public void vRetPlaySize(int iWidth, int iHeight) {
    	Log.d(TAG, "vRetPlaySize");
    }

    @Override
    public void vRetPlayNumber(int iNumber) {
    	Log.d(TAG, "vRetPlayNumber");
    }

    @Override
    public void vRecvAudioVideoData(byte[] AudioBuffer, int AudioLen, int AudioFrames, long AudioPTS, byte[] VideoBuffer, int VideoLen, long VideoPTS) {
    	Log.d(TAG, "vRecvAudioVideoData");
    }

    @Override
    public void vAllarmingWitghTime(String srcId, int type, int option, int iGroup, int iItem, int imagecounts, String imagePath, String alarmCapDir, String VideoPath, String sensorName, int deviceType) {
    	Log.d(TAG, "vAllarmingWitghTime");
    }

    @Override
    public void vRetNewSystemMessage(int iSystemMessageType, int iSystemMessageIndex) {
    	Log.d(TAG, "vRetNewSystemMessage");
    }

    @Override
    public void vRetRTSPNotify(int arg2, String msg) {
    	Log.d(TAG, "vRetRTSPNotify");
    }

    @Override
    public void vRetPostFromeNative(int what, int iDesID, int arg1, int arg2, String msgStr) {
    	Log.d(TAG, "vRetPostFromeNative");
    }
}
