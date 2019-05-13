package com.smarthome.app.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.smarthome.app.widget.SuccinctProgress;

/**
 * @author smmh
 *
 */
public class StatusProtectSensorActivity extends BaseActivity {
	
	private Equipment equipment;
	private JSONArray jsonArray;
	
	private  List<TextView> tvlist;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;
	
	private Context mContext;
	private boolean flag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_protectsensor);
		mContext = this;
		
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
				StatusProtectSensorActivity.this.finish();
			}
		});
		
		bt_more = (ImageButton) findViewById(R.id.layout_st_ti_more);
		bt_more.setBackground(getResources().getDrawable(R.drawable.fresh));
		bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;
//				DialogUtils.showProgressDialog(mContext, true);
				SuccinctProgress.showSuccinctProgress(mContext, "请稍等...",
						SuccinctProgress.THEME_ULTIMATE, true, true);
				//注销页面时把状态写出
				Log.d("status", System.currentTimeMillis()+"");
				initUI();
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
		final TextView tv_prosensor0 = (TextView) findViewById(R.id.activity_st_prose_tv0);
		final TextView tv_prosensor1 = (TextView) findViewById(R.id.activity_st_prose_tv1);
		final TextView tv_prosensor2 = (TextView) findViewById(R.id.activity_st_prose_tv2);
		final TextView tv_prosensor3 = (TextView) findViewById(R.id.activity_st_prose_tv3);
		final TextView tv_prosensor4 = (TextView) findViewById(R.id.activity_st_prose_tv4);
		final TextView tv_prosensor5 = (TextView) findViewById(R.id.activity_st_prose_tv5);
		final TextView tv_prosensor6 = (TextView) findViewById(R.id.activity_st_prose_tv6);
		final TextView tv_prosensor7 = (TextView) findViewById(R.id.activity_st_prose_tv7);
		tvlist = new ArrayList<TextView>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
			add(tv_prosensor0); add(tv_prosensor1);
			add(tv_prosensor2); add(tv_prosensor3);
			add(tv_prosensor4); add(tv_prosensor5);
			add(tv_prosensor6); add(tv_prosensor7);
			}};
			
		layout = (RelativeLayout) findViewById(R.id.activity_st_protectsensor);
		//默认为灰色
		layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
		
		initUI();
		
		
	}

	private void initUI() {
		ApiMethod.queryProtectSensor(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					layout.setBackgroundColor(getResources().getColor(R.color.green));
					String value = new String(responseBody,"utf-8");
					jsonArray = new JSONArray(value);
					for (int i = 0; i < 8; i++) {
						tvlist.get(i).setText(jsonArray.getJSONObject(i).getString("state"));
						if ("异常".equals(jsonArray.getJSONObject(i).getString("state"))) {
							DBbean.getInstance().insertEquipmentLog(equipment, "状态为异常");
							tvlist.get(i).setTextColor(getResources().getColor(R.color.red));
						} else {
							tvlist.get(i).setTextColor(getResources().getColor(R.color.white));
						}
					}
					
					try {
						Thread.sleep(1000);
					} catch (Exception e) { e.printStackTrace(); }
					Log.d("status", System.currentTimeMillis()+"");
					if (flag)
						DialogUtils.showProgressDialog(mContext, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showToastShort(StatusProtectSensorActivity.this, "获取失败, 请重试!");
			}
		});
		
	}
}
