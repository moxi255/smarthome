package com.smarthome.app.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PView;
import com.smarthome.app.R;
import com.smarthome.app.utils.DialogUtils;
import com.smarthome.app.utils.UDPUtils;

/**
 * 门铃监控
 * @author smmh
 *
 */
public class StatusDoorbellMonitorActivity extends BaseMonitorActivity implements View.OnClickListener {

	private static String TAG = "StatusDoorbellMonitorActivity";
	
	public static String P2P_ACCEPT = "com.yoosee.P2P_ACCEPT";
    public static String P2P_READY = "com.yoosee.P2P_READY";
    public static String P2P_REJECT = "com.yoosee.P2P_REJECT";
    
    private String pwd,callId,callPwd;
    private Button btn_screenshot, btn_opendoor, btn_talk, bt_back;
    private TextView title;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_doorbeelmonitor);
        
        initUI();
        regFilter();
        initPlay();
        
    }
    

	private void initUI() {
		title = (TextView) findViewById(R.id.layout_ti_text_title);
		title.setText("可视门铃");
    	btn_screenshot = (Button) findViewById(R.id.activity_st_do_screenshot);
    	btn_opendoor = (Button) findViewById(R.id.activity_st_do_opendoor);
    	btn_talk = (Button) findViewById(R.id.activity_st_do_talk);
    	bt_back = (Button) findViewById(R.id.layout_ti_button_backward);
    	bt_back.setText(R.string.button_back);
    	bt_back.setVisibility(View.VISIBLE);
    	btn_screenshot.setOnClickListener(this);
    	btn_opendoor.setOnClickListener(this);
    	btn_talk.setOnClickListener(this);
    	bt_back.setOnClickListener(this);
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        pView = (P2PView) findViewById(R.id.activity_st_do_p2pview);
        //7 视频
        initP2PView(7);
        //setMute(true);  //设置手机静音
        P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
    }
	
	/**
	 * 发送广播
	 */
	private void regFilter() {
		IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onP2PViewSingleTap() {
		
	}

	/**
	 * 截图反馈
	 */
	@Override
	protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
		if(isSuccess){
            DialogUtils.showToastShort(StatusDoorbellMonitorActivity.this, "截图成功");
        }else{
        	DialogUtils.showToastShort(StatusDoorbellMonitorActivity.this, "截图失败");
        }
		
	}

	@Override
	public int getActivityInfo() {
		return 0;
	}

	@Override
	protected void onGoBack() {
	}

	@Override
	protected void onGoFront() {
		
	}

	@Override
	protected void onExit() {
		
	}
	
	@Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_st_do_screenshot:
			captureScreen(-1);
			break;
		case R.id.layout_ti_button_backward:
			finish();
			break;
		case R.id.activity_st_do_opendoor:
			new Thread(new UDPUtils()).start();
			DialogUtils.showToastShort(StatusDoorbellMonitorActivity.this, "开门成功");
			break;
		default:
			break;
		}
	}
	
	

	/**
	 * 开始播放监控
	 */
	private void initPlay() {
		callId = "3540042";
        callPwd = "ourselec123";
        pwd = P2PHandler.getInstance().EntryPassword(callPwd);//经过转换后的设备密码
        String contactId = "0810090";//登陆的用户账号
        P2PHandler.getInstance().call(contactId, pwd, true, 1,callId, "", "", 2,callId);
	}
	
	
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(P2P_ACCEPT)){
                Log.d(TAG, "监控应答");
            }else if(intent.getAction().equals(P2P_READY)){
            	Log.d(TAG, "监控开始");
            }else if(intent.getAction().equals(P2P_REJECT)){
            	Log.d(TAG, "监控挂断");
            }
        }
    };
	
}
