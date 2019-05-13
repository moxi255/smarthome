package com.smarthome.app.activity;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.smarthome.app.utils.ValidateUtils;

/**
 * @author smmh
 *
 */
public class StatusCurtainActivity extends BaseActivity {
	
	private Equipment equipment;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_curtain);
		
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
				StatusCurtainActivity.this.finish();
			}
		});
		
		bt_more = (ImageButton) findViewById(R.id.layout_st_ti_more);
		bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				Intent intent = new Intent(StatusCurtainActivity.this, LogActivity.class);
				intent.putExtra("equipmentId", equipment.getId());
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
		Button bt_open = (Button) findViewById(R.id.activity_st_cu_bt_open);
		Button bt_stop = (Button) findViewById(R.id.activity_st_cu_bt_stop);
		Button bt_close = (Button) findViewById(R.id.activity_st_cu_bt_close);
		layout = (RelativeLayout) findViewById(R.id.activity_st_cu);
		if (equipment.getState() == 0)
			layout.setBackgroundColor(getResources().getColor(R.color.gray));
		else if (equipment.getState() == 1)
			layout.setBackgroundColor(getResources().getColor(R.color.green));
		else if (equipment.getState() == 2)
			layout.setBackgroundColor(getResources().getColor(R.color.blue));
		
		//设置监听器
		bt_open.setOnClickListener(curtainListener);
		bt_stop.setOnClickListener(curtainListener);
		bt_close.setOnClickListener(curtainListener);
	}
	
	/**
	 * 监听窗帘开关命令
	 */
	OnClickListener curtainListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.activity_st_cu_bt_open:
				ApiControlCurtain(0, "open");
				break;
			case R.id.activity_st_cu_bt_stop:
				ApiControlCurtain(1, "stop");
				break;
			case R.id.activity_st_cu_bt_close:
				ApiControlCurtain(2, "close");
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 调用控制窗户的API
	 */
	private void ApiControlCurtain(final int i, String order) {
		ApiMethod.controlCurtain(equipment.getId(), order, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (ValidateUtils.validationOrder(responseBody)) {
					if (i == 0) {
						equipment.setState(1);
						DBbean.getInstance().insertEquipmentLog(equipment, "开启");
						layout.setBackgroundColor(getResources().getColor(R.color.green));
					} else if (i == 1) {
						//暂停
						equipment.setState(2);
						DBbean.getInstance().insertEquipmentLog(equipment, "暂停");
						layout.setBackgroundColor(getResources().getColor(R.color.blue));
					} else if (i == 2) {
						equipment.setState(0);
						DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
						layout.setBackgroundColor(getResources().getColor(R.color.gray));
					}
				} else {
					DialogUtils.showToastShort(StatusCurtainActivity.this, "控制失败，请重试！");
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showToastShort(StatusCurtainActivity.this, "控制失败，请重试！");
			}
		});
	}
	
	
}
