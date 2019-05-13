package com.smarthome.app.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.smarthome.app.R;
import com.smarthome.app.api.ApiMethod;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.ui.BaseActivity;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.DialogUtils;

/**
 * @author smmh
 *
 */
public class StatusSensorActivity extends BaseActivity {
	
	private Equipment equipment;
	private JSONObject jsonObject;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_sensor);
		
		initData();
		initTitle();
		initLayout();
	}
	
	/**
	 *  初始化标题栏
	 */
	private void initTitle() {
		tv_title = (TextView) findViewById(R.id.layout_st_ti_title);
		tv_title.setText(equipment.getName());
		
		bt_back = (ImageButton) findViewById(R.id.layout_st_ti_back);
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				StatusSensorActivity.this.finish();
			}
		});
		
		bt_more = (ImageButton) findViewById(R.id.layout_st_ti_more);
		bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				Intent intent = new Intent(StatusSensorActivity.this, LogActivity.class);
				intent.putExtra("equipmentId", "weather");
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取设备信息
	 */
	private void initData() {
		Intent intent = getIntent();
		if (null != intent && null != intent.getStringExtra("equipmentId")) {
			equipment = MainApplication.equipmentsMap.get(intent.getStringExtra("equipmentId"));
		}
	}
	
	
	/**
	 * 初始化布局
	 */
	private void initLayout() {
		final TextView tv_sensor0 = (TextView) findViewById(R.id.activity_st_se_tv0);
		final TextView tv_sensor1 = (TextView) findViewById(R.id.activity_st_se_tv1);
		final TextView tv_sensor2 = (TextView) findViewById(R.id.activity_st_se_tv2);
		final TextView tv_sensor3 = (TextView) findViewById(R.id.activity_st_se_tv3);
		final TextView tv_sensor4 = (TextView) findViewById(R.id.activity_st_se_tv4);
		layout = (RelativeLayout) findViewById(R.id.activity_st_se);
		//默认为灰色
		layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
		
		ApiMethod.queryCurrentSensorData(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					layout.setBackgroundColor(getResources().getColor(R.color.green));
					String value = new String(responseBody,"utf-8");
					jsonObject = new JSONObject(value);
					tv_sensor0.setText(jsonObject.getString("numericalSensor1") + " ℃");
					tv_sensor1.setText(jsonObject.getString("numericalSensor2") + " %");
					tv_sensor2.setText(jsonObject.getString("numericalSensor3") + " lux");
					tv_sensor3.setText(jsonObject.getString("numericalSensor4") + " lux");
					tv_sensor4.setText(jsonObject.getString("numericalSensor5") + " lux");
					DBbean.getInstance().insertWeather(jsonObject.getString("numericalSensor1") + " ℃", jsonObject.getString("numericalSensor2") + " %");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showToastShort(StatusSensorActivity.this, "获取失败, 请重试!");
			}
		});
	}
}
