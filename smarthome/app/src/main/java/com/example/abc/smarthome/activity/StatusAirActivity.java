package com.example.abc.smarthome.activity;

import java.util.Arrays;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abc.smarthome.api.ApiMethod;
import com.example.abc.smarthome.model.Equipment;
import com.example.abc.smarthome.ui.BaseActivity;
import com.example.abc.smarthome.http.AsyncHttpResponseHandler;

import com.example.abc.smarthome.ui.MainApplication;
import com.example.abc.smarthome.utils.DialogUtils;
import com.example.abc.smarthome.utils.ValidateUtils;
import com.example.abc.smarthome.wheelview.WheelView;



public class StatusAirActivity extends BaseActivity {
	
	private Equipment equipment;
	private JSONObject jsonObject;
	
	private RelativeLayout layout;
	private TextView tv_title;
	private ImageButton bt_back, bt_more;
	
	private String temp = "23";
	private TextView[] air_tvs;
	private Button[] air_bts;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status_air);
		
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
				StatusAirActivity.this.finish();
			}
		});
		
		bt_more = (ImageButton) findViewById(R.id.layout_st_ti_more);
		bt_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//注销页面时把状态写出
				MainApplication.equipmentsMap.put(equipment.getId(), equipment);
				Intent intent = new Intent(StatusAirActivity.this, LogActivity.class);
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
		layout = (RelativeLayout) findViewById(R.id.activity_st_ai);
		
		final TextView air_text0 = (TextView) findViewById(R.id.activity_st_ai_text0);
		final TextView air_text1 = (TextView) findViewById(R.id.activity_st_ai_text1);
		final TextView air_text2 = (TextView) findViewById(R.id.activity_st_ai_text2);
		final TextView air_text3 = (TextView) findViewById(R.id.activity_st_ai_text3);
		final TextView air_text4 = (TextView) findViewById(R.id.activity_st_ai_text4);
		final TextView air_text5 = (TextView) findViewById(R.id.activity_st_ai_text5);
		final TextView air_text6 = (TextView) findViewById(R.id.activity_st_ai_text6);
		final Button air_bt0 = (Button) findViewById(R.id.activity_st_ai_tempbt);
		final Button air_bt1 = (Button) findViewById(R.id.activity_st_ai_tempadd);
		final Button air_bt2 = (Button) findViewById(R.id.activity_st_ai_tempsub);
		final Button air_bt3 = (Button) findViewById(R.id.activity_st_ai_windspeed);
		final Button air_bt4 = (Button) findViewById(R.id.activity_st_ai_windire);
		final Button air_bt5 = (Button) findViewById(R.id.activity_st_ai_bt_mod);
		final Button air_bt6 = (Button) findViewById(R.id.activity_st_ai_bt_dehu);
		final Button air_bt7 = (Button) findViewById(R.id.activity_st_ai_bt_onff);
		//初始化控件数组
		air_bts = new Button[]{
				air_bt0, air_bt1, air_bt2, air_bt3, air_bt4, air_bt5, air_bt6, air_bt7
		};
		air_tvs = new TextView[]{
				air_text0, air_text1, air_text2, air_text3, air_text4, air_text5, air_text6
		};
		
		//初始化所有textview
		layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
		ApiGetAirState();
		
		for (int i = 0; i < 8; i++) {
			air_bts[i].setOnClickListener(airListener);
		}
	}
	
	/**
	 * 空调控制按钮监听
	 */
	OnClickListener airListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() != R.id.activity_st_ai_bt_onff && equipment.getState() == 0) {
				DialogUtils.showToastShort(StatusAirActivity.this, "请先打开电源!");
			} else {
				switch(v.getId()) {
				case R.id.activity_st_ai_tempbt:
					ControlAirTemp();
					break;
				case R.id.activity_st_ai_tempadd:
					ApiControlAir("air", "add");
					break;
				case R.id.activity_st_ai_tempsub:
					ApiControlAir("air", "reduce");
					break;
				case R.id.activity_st_ai_windspeed:
					ApiControlAir("air", "speed");
					break;
				case R.id.activity_st_ai_windire:
					ApiControlAir("air", "hand");
					break;	
				case R.id.activity_st_ai_bt_mod:
					ApiControlAir("air", "model");
					break;
				case R.id.activity_st_ai_bt_dehu:
					ApiControlAir("air", "auto");
					break;
				case R.id.activity_st_ai_bt_onff:
					if (equipment.getState() == 1) {
						ApiControlAir("air", "close");
					}
					else if (equipment.getState() == 0) {
						ApiControlAir("air", "open");
					}
					break;
				default:
					break;
				}
			}
		}
	};
	
	/**
	 * 控制空调温度
	 */
	@SuppressLint("InflateParams")
	private void ControlAirTemp() {
		final String[] temps = new String[16];
		for (int i = 16; i < 32; i++)
			temps[i-16] = i+"";
		
		View outerView = LayoutInflater.from(this).inflate(R.layout.layout_status_air_dialog, null);
		WheelView wv = (WheelView) outerView.findViewById(R.id.activity_st_ai_di_view);
		wv.setOffset(1);
		//放入数据
        wv.setItems(Arrays.asList(temps));
        //选中的是第几个
        wv.setSeletion(6);
        //选中项监听
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            	setTempValue(item);
            }
        });
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设定温度");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ApiControlAir("temp", getTempValue());
				dialog.dismiss();
			}
		});
        builder.setView(outerView);
        builder.show();
	}
	
	/**
	 * 取得设置的温度值
	 */
	private String getTempValue() {
		return temp;
	}
	/**
	 * 设置温度值
	 */
	private void setTempValue(String value) {
		temp = value;
	}
	
	/**
	 * 设置空调显示当前温度
	 */
	private void AirInitTempTv(TextView[] tvs, String string) {
		tvs[0].setText(string);
	}
	
	/**
	 * 设置空调控制页面显示字体和图标
	 */
	private void AirInitTextView(TextView[] tvs, Button[] bts, String[] strings) {
		
		//只是更改当前温度
		if (null != strings[2] && null != strings[5]) {
			if ("----".equals(strings[0]) && "----".equals(strings[4])) {
				equipment.setState(0);
				layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
				strings[5] = "关闭";
				for (int i = 0; i < 6; i++)
					tvs[i+1].setText(strings[i]);
			} else if ("打开".equals(strings[5])){
				equipment.setState(1);
				layout.setBackgroundColor(this.getResources().getColor(R.color.green));
				for (int i = 0; i < 6; i++) {
					if (i == 1)
						tvs[i+1].setText("风速—" + strings[i]);
					if (i == 2)
						tvs[i+1].setText("风向—" + strings[i]);
					if (i == 3) {
						tvs[i+1].setText(strings[i] + "模式");
						if ("制冷".equals(strings[i]))
							bts[5].setBackgroundResource(R.drawable.status_air_coolmod);
						else if ("制热".equals(strings[i]))
							bts[5].setBackgroundResource(R.drawable.status_air_hotmod);
						else if ("除湿".equals(strings[i]))
							bts[5].setBackgroundResource(R.drawable.status_air_dehumod);
						else if ("送风".equals(strings[i]))
							bts[5].setBackgroundResource(R.drawable.status_air_windmod);
						else if ("自动".equals(strings[i]))
							bts[5].setBackgroundResource(R.drawable.status_air_automod);
					}
					if (i == 4) {
						tvs[i+1].setText("自动风向-" + strings[i]);
						if ("关闭".equals(strings[i]))
							bts[6].setBackgroundResource(R.drawable.status_air_manualoff);
						else if ("打开".equals(strings[i])) 
							bts[6].setBackgroundResource(R.drawable.status_air_manualon);
					}
					if (i == 0 || i == 5)
						tvs[i+1].setText(strings[i]);
				}
			}
		}
		
	}
	
	/**
	 * 控制空调
	 */
	private void ApiControlAir(String type, String order) {
		DialogUtils.showProgressDialog(StatusAirActivity.this, true);
		ApiMethod.controlAir(type, order, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (ValidateUtils.validationOrder(responseBody)) {
					ApiGetAirState();
					DialogUtils.showProgressDialog(StatusAirActivity.this, false);
				} else {
					DialogUtils.showProgressDialog(StatusAirActivity.this, false);
					DialogUtils.showToast(StatusAirActivity.this, "控制失败，请重试！");
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showProgressDialog(StatusAirActivity.this, false);
				DialogUtils.showToast(StatusAirActivity.this, "控制失败，请重试！");
			}
		});
	}
	
	/**
	 * 获取空调当前状态
	 */
	private void ApiGetAirState() {
		final String[] strings = new String[6];
		//获取当前室内温度
		ApiMethod.queryCurrentSensorData(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					jsonObject = new JSONObject(new String(responseBody,"utf-8"));
					if (null != jsonObject) {
						String string = jsonObject.getString("numericalSensor1").split("\\.")[0] + "℃";
						AirInitTempTv(air_tvs, string);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showToastShort(StatusAirActivity.this, "获取失败, 请重试!");
			}
		});
		
		//获取空调当前状态
		ApiMethod.queryAirState(new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					System.out.println(new String(responseBody, "utf-8"));
					jsonObject = new JSONObject(new String(responseBody, "utf-8"));
					if (null != jsonObject) {
						strings[0] = jsonObject.getString("airTemp");
						strings[1] = jsonObject.getString("airSpeed");
						strings[2] = jsonObject.getString("airDir");
						strings[3] = jsonObject.getString("airModel");
						strings[4] = jsonObject.getString("airAuto");
						strings[5] = jsonObject.getString("airPower");
						AirInitTextView(air_tvs, air_bts, strings);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				DialogUtils.showToastShort(StatusAirActivity.this, "获取失败, 请重试!");
			}
		});
		
	}
	
	
}
