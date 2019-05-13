package com.example.abc.smarthome;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends Activity {

//	private BrdcstReceiver receiver;


	private TextView text_name, text_condition;
	private String name;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();

	}

	protected void init() {
		Intent intent = getIntent();
		name = intent.getStringExtra("Username");
		text_name = (TextView) findViewById(R.id.text_name);
		text_name.setText(name);
	}


	public void onIVDoor(View v) {
		// 通过指定类名的显式意图
		Intent i = new Intent(MainPage.this, StatusAvActivity.class);

		// 启动目标活动
		startActivity(i);
	}

	public void onIVAir(View v) {
		// 通过指定类名的显式意图
		Intent i = new Intent(MainPage.this, StatusAirActivity.class);

		// 启动目标活动
		startActivity(i);
	}

	//窗帘控制的响应
	public void onIVCurtain(View v) {
		Intent i = new Intent(MainPage.this, StatusCurtainActivity.class);
		// 启动目标活动
		startActivity(i);
	}

	public void onIVLamp(View v) {
		Intent i = new Intent(MainPage.this, StatusLightActivity.class);
		// 启动目标活动
		startActivity(i);
	}

	public void Temp(View v) {
		Intent i = new Intent(MainPage.this,StatusSensorActivity.class);
		// 启动目标活动
		startActivity(i);

	}

	public void onIVExit(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
		builder.setMessage("确认退出?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog

					}
				}).show();        //finish();
	}


	@Override
	protected void onStop() {
		Log.e("mainactivity ", "start onStop~~~");
		super.onStop();
	}
}

/*
	@Override protected void onDestroy() {
		unregisterReceiver(receiver); //  注销
		this.stopService(new Intent(this, ServiceSocket.class));// 停止service
		Log.v("mainactivity ", "mainactivity onDestroy" );
		super.onDestroy();
	}
	*/


	/**************广播:接收后台的service发送的广播******************/
//	private class BrdcstReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
////            Bundle bundle = intent.getExtras();
//			String stringValue=intent.getStringExtra("strRecvMsg");
//
//		}
//	}
//}