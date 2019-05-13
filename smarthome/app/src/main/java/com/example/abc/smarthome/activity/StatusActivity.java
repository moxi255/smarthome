package com.example.abc.smarthome.activity;//package com.smarthome.app.activity;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.http.Header;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.animation.AnimatorSet.Builder;
//import android.annotation.SuppressLint;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Build.VERSION_CODES;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.LayoutAlgorithm;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.smarthome.app.R;
//import com.smarthome.app.api.ApiMethod;
//import com.smarthome.app.model.Equipment;
//import com.smarthome.app.ui.BaseActivity;
//import com.smarthome.app.utils.DialogUtils;
//import com.smarthome.app.widget.AlertDialog;
//import com.wangjie.wheelview.WheelView;
//
///**
// * 弃用
// * @author smmh
// *
// */
//public class StatusActivity extends BaseActivity
//{
//	private Equipment equipment;
//	private JSONObject jsonObject;
//	private JSONArray jsonArray;
//	private RelativeLayout layout;
//	
//	private TextView tv_title;
//	private ImageButton bt_back, bt_more;
//	
//	private String temp = "23";
//	private TextView[] air_tvs;
//	private Button[] air_bts;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		initLayout();
//		
//	}
//	
//
//	/**
//	 * 根据不同的按钮来进行界面选择
//	 */
//	private void initLayout() {
//		Intent intent = getIntent();
//		if (null != intent) {
//			equipment = (Equipment) intent.getSerializableExtra("Equipment");
//			switch(equipment.getType()) {
//			case 0:
//				initLightUI();
//				break;
//			case 1:
//				initCurtainUI();
//				break;
//			case 2:
//				initAvUI();
//				break;
//			case 3:
//				initAirUI();
//				break;
//			case 4:
//				initSensorUI();
//				break;
//			case 5:
//				initProtectSensorUI();
//				break;
//			case 6:
//				initProtectCameraUI();
//			default:
//				break;
//			}
//		}
//	}
//	
//	
//	/**
//	 * 安防摄像头显示界面
//	 */
//	@SuppressWarnings("deprecation")
//	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
//	private void initProtectCameraUI() {
//		setContentView(R.layout.activity_status_protectcamera);
//		initTitle();
//		WebView webView = (WebView) findViewById(R.id.activity_st_proca_webview);
//		//设置标题栏背景色
//		layout = (RelativeLayout) findViewById(R.id.layout_st_titlebar);
//		layout.setBackgroundColor(this.getResources().getColor(R.color.green));
//		//允许js执行
//        webView.getSettings().setJavaScriptEnabled(true); 
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        //设置WebView适应屏幕大小
//        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON); //设置webview支持插件
//        webView.getSettings().setDomStorageEnabled(true);
//        //js跨域支持
//        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) 
//        	webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//		webView.setWebViewClient(new WebViewClient());
//		if ("protectcamera1".equals(equipment.getId())) {
//			System.out.println(111);
//			webView.loadUrl("file:///android_asset/protectcamera1.html");
//		}
//		else if ("protectcamera2".equals(equipment.getId()))
//			webView.loadUrl("file:///android_asset/protectcamera2.html");
////		String url = "http://192.168.27.160/web/admin.html";
////		Intent intent = new Intent();           
////	    intent.setAction("android.intent.action.VIEW");  
////	    intent.setData(Uri.parse(url));              
////	    intent.setClassName("com.UCMobile","com.UCMobile.main.UCMobile");     
////	    startActivity(intent);
//	}
//
//
//	/**
//	 *  初始化标题栏
//	 */
//	private void initTitle() {
//		tv_title = (TextView) findViewById(R.id.layout_st_ti_title);
//		tv_title.setText(equipment.getName());
//		
//		bt_back = (ImageButton) findViewById(R.id.layout_st_ti_back);
//		bt_back.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				StatusActivity.this.finish();
//			}
//		});
//	}
//	
//	/**
//	 * 安防传感器显示界面
//	 */
//	private void initProtectSensorUI() {
//		setContentView(R.layout.activity_status_protectsensor);
//		initTitle();
//		final TextView tv_prosensor0 = (TextView) findViewById(R.id.activity_st_prose_tv0);
//		final TextView tv_prosensor1 = (TextView) findViewById(R.id.activity_st_prose_tv1);
//		final TextView tv_prosensor2 = (TextView) findViewById(R.id.activity_st_prose_tv2);
//		final TextView tv_prosensor3 = (TextView) findViewById(R.id.activity_st_prose_tv3);
//		final TextView tv_prosensor4 = (TextView) findViewById(R.id.activity_st_prose_tv4);
//		final TextView tv_prosensor5 = (TextView) findViewById(R.id.activity_st_prose_tv5);
//		final TextView tv_prosensor6 = (TextView) findViewById(R.id.activity_st_prose_tv6);
//		final TextView tv_prosensor7 = (TextView) findViewById(R.id.activity_st_prose_tv7);
//		final List<TextView> tvlist = new ArrayList<TextView>(){/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//		{
//			add(tv_prosensor0); add(tv_prosensor1);
//			add(tv_prosensor2); add(tv_prosensor3);
//			add(tv_prosensor4); add(tv_prosensor5);
//			add(tv_prosensor6); add(tv_prosensor7);
//			}};
//			
//		layout = (RelativeLayout) findViewById(R.id.activity_st_protectsensor);
//		//默认为灰色
//		layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
//		
//		ApiMethod.queryProtectSensor(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				try {
//					layout.setBackgroundColor(getResources().getColor(R.color.green));
//					String value = new String(responseBody,"utf-8");
//					jsonArray = new JSONArray(value);
//					for (int i = 0; i < 8; i++) {
//						tvlist.get(i).setText(jsonArray.getJSONObject(i).getString("state"));
//						if ("异常".equals(jsonArray.getJSONObject(i).getString("state")))
//							tvlist.get(i).setTextColor(getResources().getColor(R.color.red));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "获取失败, 请重试!");
//			}
//		});
//	}
//
//	/**
//	 * 空调控制界面
//	 */
//	private void initAirUI() {
//		setContentView(R.layout.activity_status_air);
//		initTitle();
//		
//		layout = (RelativeLayout) findViewById(R.id.activity_st_ai);
//		
//		final TextView air_text0 = (TextView) findViewById(R.id.activity_st_ai_text0);
//		final TextView air_text1 = (TextView) findViewById(R.id.activity_st_ai_text1);
//		final TextView air_text2 = (TextView) findViewById(R.id.activity_st_ai_text2);
//		final TextView air_text3 = (TextView) findViewById(R.id.activity_st_ai_text3);
//		final TextView air_text4 = (TextView) findViewById(R.id.activity_st_ai_text4);
//		final TextView air_text5 = (TextView) findViewById(R.id.activity_st_ai_text5);
//		final TextView air_text6 = (TextView) findViewById(R.id.activity_st_ai_text6);
//		final Button air_bt0 = (Button) findViewById(R.id.activity_st_ai_tempbt);
//		final Button air_bt1 = (Button) findViewById(R.id.activity_st_ai_tempadd);
//		final Button air_bt2 = (Button) findViewById(R.id.activity_st_ai_tempsub);
//		final Button air_bt3 = (Button) findViewById(R.id.activity_st_ai_windspeed);
//		final Button air_bt4 = (Button) findViewById(R.id.activity_st_ai_windire);
//		final Button air_bt5 = (Button) findViewById(R.id.activity_st_ai_bt_mod);
//		final Button air_bt6 = (Button) findViewById(R.id.activity_st_ai_bt_dehu);
//		final Button air_bt7 = (Button) findViewById(R.id.activity_st_ai_bt_onff);
//		//初始化控件数组
//		air_bts = new Button[]{
//				air_bt0, air_bt1, air_bt2, air_bt3, air_bt4, air_bt5, air_bt6, air_bt7
//		};
//		air_tvs = new TextView[]{
//				air_text0, air_text1, air_text2, air_text3, air_text4, air_text5, air_text6
//		};
//		
//		//初始化所有textview
//		ApiGetAirState();
//		
//		for (int i = 0; i < 8; i++) {
//			air_bts[i].setOnClickListener(airListener);
//		}
//        
//	}
//	
//	/**
//	 * 空调控制按钮监听
//	 */
//	OnClickListener airListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch(v.getId()) {
//			case R.id.activity_st_ai_tempbt:
//				ControlAirTemp();
//				break;
//			case R.id.activity_st_ai_tempadd:
//				ApiControlAir("air", "add");
//				break;
//			case R.id.activity_st_ai_tempsub:
//				ApiControlAir("air", "reduce");
//				break;
//			case R.id.activity_st_ai_windspeed:
//				ApiControlAir("air", "speed");
//				break;
//			case R.id.activity_st_ai_windire:
//				ApiControlAir("air", "hand");
//				break;	
//			case R.id.activity_st_ai_bt_mod:
//				ApiControlAir("air", "model");
//				break;
//			case R.id.activity_st_ai_bt_dehu:
//				ApiControlAir("air", "auto");
//				break;
//			case R.id.activity_st_ai_bt_onff:
//				if (equipment.getState() == 1) {
//					ApiControlAir("air", "close");
//				}
//				else if (equipment.getState() == 0) {
//					ApiControlAir("air", "open");
//				}
//				break;
//			default:
//				break;
//			}
//		}
//	};
//	
//	/**
//	 * 控制空调温度
//	 */
//	private void ControlAirTemp() {
//		final String[] temps = new String[16];
//		for (int i = 16; i < 32; i++)
//			temps[i-16] = i+"";
//		
//		View outerView = LayoutInflater.from(this).inflate(R.layout.layout_status_air_dialog, null);
//		WheelView wv = (WheelView) outerView.findViewById(R.id.activity_st_ai_di_view);
//		wv.setOffset(1);
//		//放入数据
//        wv.setItems(Arrays.asList(temps));
//        //选中的是第几个
//        wv.setSeletion(6);
//        //选中项监听
//        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//            	setTempValue(item);
//            }
//        });
//        
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("设定温度");
//        builder.setNegativeButton("取消", null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				ApiControlAir("temp", getTempValue());
//				dialog.dismiss();
//			}
//		});
//        builder.setView(outerView);
//        builder.show();
//	}
//	
//	/**
//	 * 取得设置的温度值
//	 */
//	private String getTempValue() {
//		return temp;
//	}
//	/**
//	 * 设置温度值
//	 */
//	private void setTempValue(String value) {
//		temp = value;
//	}
//	
//	/**
//	 * 设置空调显示当前温度
//	 */
//	private void AirInitTempTv(TextView[] tvs, String string) {
//		tvs[0].setText(string);
//	}
//	
//	/**
//	 * 设置空调控制页面显示字体和图标
//	 */
//	private void AirInitTextView(TextView[] tvs, Button[] bts, String[] strings) {
//		
//		//只是更改当前温度
//		if (null != strings[2] && null != strings[5]) {
//			if ("----".equals(strings[0]) && "----".equals(strings[4])) {
//				equipment.setState(0);
//				layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
//				strings[5] = "关闭";
//				for (int i = 0; i < 6; i++)
//					tvs[i+1].setText(strings[i]);
//			} else if ("打开".equals(strings[5])){
//				equipment.setState(1);
//				layout.setBackgroundColor(this.getResources().getColor(R.color.green));
//				for (int i = 0; i < 6; i++) {
//					if (i == 1)
//						tvs[i+1].setText("风速—" + strings[i]);
//					if (i == 2)
//						tvs[i+1].setText("风向—" + strings[i]);
//					if (i == 3) {
//						tvs[i+1].setText(strings[i] + "模式");
//						if ("制冷".equals(strings[i]))
//							bts[5].setBackgroundResource(R.drawable.status_air_coolmod);
//						else if ("制热".equals(strings[i]))
//							bts[5].setBackgroundResource(R.drawable.status_air_hotmod);
//						else if ("除湿".equals(strings[i]))
//							bts[5].setBackgroundResource(R.drawable.status_air_dehumod);
//						else if ("送风".equals(strings[i]))
//							bts[5].setBackgroundResource(R.drawable.status_air_windmod);
//						else if ("自动".equals(strings[i]))
//							bts[5].setBackgroundResource(R.drawable.status_air_automod);
//					}
//					if (i == 4) {
//						tvs[i+1].setText("自动风向-" + strings[i]);
//						if ("关闭".equals(strings[i]))
//							bts[6].setBackgroundResource(R.drawable.status_air_manualoff);
//						else if ("打开".equals(strings[i])) 
//							bts[6].setBackgroundResource(R.drawable.status_air_manualon);
//					}
//					if (i == 0 || i == 5)
//						tvs[i+1].setText(strings[i]);
//				}
//			}
//		}
//		
//	}
//	
//	/**
//	 * 控制空调
//	 */
//	private void ApiControlAir(String type, String order) {
//		ApiMethod.controlAir(type, order, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				if (ValidationOrder(responseBody)) {
//					ApiGetAirState();
//				} else 
//					DialogUtils.showToast(StatusActivity.this, "控制失败，请重试！");
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "控制失败，请重试！");
//			}
//		});
//	}
//	
//	/**
//	 * 获取空调当前状态
//	 */
//	private void ApiGetAirState() {
//		final String[] strings = new String[6];
//		//获取当前室内温度
//		ApiMethod.queryCurrentSensorData(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				try {
//					jsonObject = new JSONObject(new String(responseBody,"utf-8"));
//					if (null != jsonObject) {
//						String string = jsonObject.getString("numericalSensor1").split("\\.")[0] + "℃";
//						AirInitTempTv(air_tvs, string);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "获取失败, 请重试!");
//			}
//		});
//		
//		//获取空调当前状态
//		ApiMethod.queryAirState(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				try {
//					System.out.println(new String(responseBody, "utf-8"));
//					jsonObject = new JSONObject(new String(responseBody, "utf-8"));
//					if (null != jsonObject) {
//						strings[0] = jsonObject.getString("airTemp");
//						strings[1] = jsonObject.getString("airSpeed");
//						strings[2] = jsonObject.getString("airDir");
//						strings[3] = jsonObject.getString("airModel");
//						strings[4] = jsonObject.getString("airAuto");
//						strings[5] = jsonObject.getString("airPower");
//						AirInitTextView(air_tvs, air_bts, strings);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} 
//			}
//			
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "获取失败, 请重试!");
//			}
//		});
//		
//	}
//	
//	
//	
//	/**
//	 * 电视机控制界面
//	 */
//	private void initAvUI() {
//		setContentView(R.layout.activity_status_av);
//		initTitle();
//		layout = (RelativeLayout) findViewById(R.id.activity_st_av);
//		layout.setBackgroundColor(getResources().getColor(R.color.green));
//		//绑定按钮
//		Button bt0 = (Button) findViewById(R.id.activity_st_av_bt0);
//		Button bt1 = (Button) findViewById(R.id.activity_st_av_bt1);
//		Button bt2 = (Button) findViewById(R.id.activity_st_av_bt2);
//		Button bt3 = (Button) findViewById(R.id.activity_st_av_bt3);
//		Button bt4 = (Button) findViewById(R.id.activity_st_av_bt4);
//		Button bt5 = (Button) findViewById(R.id.activity_st_av_bt5);
//		Button bt6 = (Button) findViewById(R.id.activity_st_av_bt6);
//		Button bt7 = (Button) findViewById(R.id.activity_st_av_bt7);
//		Button bt8 = (Button) findViewById(R.id.activity_st_av_bt8);
//		Button bt9 = (Button) findViewById(R.id.activity_st_av_bt9);
//		Button bt10 = (Button) findViewById(R.id.activity_st_av_bt10);
//		Button bt11 = (Button) findViewById(R.id.activity_st_av_bt11);
//		Button bt12 = (Button) findViewById(R.id.activity_st_av_bt12);
//		Button bt13 = (Button) findViewById(R.id.activity_st_av_bt13);
//		Button bt14 = (Button) findViewById(R.id.activity_st_av_bt14);
//		Button bt15 = (Button) findViewById(R.id.activity_st_av_bt15);
//		Button bt16 = (Button) findViewById(R.id.activity_st_av_bt16);
//		Button bt17 = (Button) findViewById(R.id.activity_st_av_bt17);
//		Button bt18 = (Button) findViewById(R.id.activity_st_av_bt18);
//		Button bt19 = (Button) findViewById(R.id.activity_st_av_bt19);
//		Button bt20 = (Button) findViewById(R.id.activity_st_av_bt20);
//		Button bt21 = (Button) findViewById(R.id.activity_st_av_bt21);
//		Button bt22 = (Button) findViewById(R.id.activity_st_av_bt22);
//		Button bt23 = (Button) findViewById(R.id.activity_st_av_bt23);
//		Button av_bts[] = new Button[]{bt0, bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt10,
//				bt11, bt12, bt13, bt14, bt15, bt16, bt17, bt18, bt19, bt20, bt21, bt22, bt23};
//		//按钮order
//		String av_orders[] = new String[]{
//				"power", "setting", "show", "view", "game", "app", "sourceLeft", "source",
//				"sourceRight", "index", "up", "info", "left", "ok", "right", "back", "down",
//				"menu", "voiceReduce", "voiceAdd", "none1", "none2", "none3", "none4"
//		};
//		for (int i = 0; i < 24; i++) {
//			av_bts[i].setTag(av_orders[i]);
//			av_bts[i].setOnClickListener(avListener);
//		}
//	}
//
//	/**
//	 * 监听电视机控制命令
//	 */
//	OnClickListener avListener = new OnClickListener() {
//		@Override
//		public void onClick(final View v) {
//			//电视机的控制设备类型 tv
//			ApiMethod.controlAv("tv", v.getTag().toString(), new AsyncHttpResponseHandler() {
//				@Override
//				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//					if (ValidationOrder(responseBody)) {
//						if("power".equals(v.getTag().toString()))
//							layout.setBackgroundColor(getResources().getColor(R.color.gray));
//					}
//				}
//				@Override
//				public void onFailure(int statusCode, Header[] headers,
//						byte[] responseBody, Throwable error) {
//					DialogUtils.showToast(StatusActivity.this, "控制失败，请重试！");
//				}
//			});
//		}
//	};
//	
//	
//	
//	
//	/**
//	 * 窗帘控制界面
//	 */
//	private void initCurtainUI() {
//		setContentView(R.layout.activity_status_curtain);
//		initTitle();
//		Button bt_open = (Button) findViewById(R.id.activity_st_cu_bt_open);
//		Button bt_stop = (Button) findViewById(R.id.activity_st_cu_bt_stop);
//		Button bt_close = (Button) findViewById(R.id.activity_st_cu_bt_close);
//		layout = (RelativeLayout) findViewById(R.id.activity_st_cu);
//		if (equipment.getState() == 0)
//			layout.setBackgroundColor(getResources().getColor(R.color.gray));
//		else if (equipment.getState() == 1)
//			layout.setBackgroundColor(getResources().getColor(R.color.green));
//		else if (equipment.getState() == 2)
//			layout.setBackgroundColor(getResources().getColor(R.color.blue));
//		
//		//设置监听器
//		bt_open.setOnClickListener(curtainListener);
//		bt_stop.setOnClickListener(curtainListener);
//		bt_close.setOnClickListener(curtainListener);
//		
//	}
//	
//	/**
//	 * 监听窗帘开关命令
//	 */
//	OnClickListener curtainListener = new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//			switch(v.getId()) {
//			case R.id.activity_st_cu_bt_open:
//				ApiControlCurtain(0, "open");
//				break;
//			case R.id.activity_st_cu_bt_stop:
//				ApiControlCurtain(1, "stop");
//				break;
//			case R.id.activity_st_cu_bt_close:
//				ApiControlCurtain(2, "close");
//				break;
//			default:
//				break;
//			}
//		}
//	};
//	
//	/**
//	 * 调用控制窗户的API
//	 */
//	private void ApiControlCurtain(final int i, String order) {
//		ApiMethod.controlCurtain(equipment.getId(), order, new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				if (ValidationOrder(responseBody)) {
//					if (i == 0) {
//						equipment.setState(1);
//						layout.setBackgroundColor(getResources().getColor(R.color.green));
//					} else if (i == 1) {
//						//暂停
//						equipment.setState(2);
//						layout.setBackgroundColor(getResources().getColor(R.color.blue));
//					} else if (i == 2) {
//						equipment.setState(0);
//						layout.setBackgroundColor(getResources().getColor(R.color.gray));
//					}
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "失败，请重试！");
//			}
//		});
//	}
//	
//	
//	
//	/**
//	 * 环境传感器显示界面
//	 */
//	private void initSensorUI() {
//		setContentView(R.layout.activity_status_sensor);
//		initTitle();
//		final TextView tv_sensor0 = (TextView) findViewById(R.id.activity_st_se_tv0);
//		final TextView tv_sensor1 = (TextView) findViewById(R.id.activity_st_se_tv1);
//		final TextView tv_sensor2 = (TextView) findViewById(R.id.activity_st_se_tv2);
//		final TextView tv_sensor3 = (TextView) findViewById(R.id.activity_st_se_tv3);
//		final TextView tv_sensor4 = (TextView) findViewById(R.id.activity_st_se_tv4);
//		layout = (RelativeLayout) findViewById(R.id.activity_st_se);
//		//默认为灰色
//		layout.setBackgroundColor(this.getResources().getColor(R.color.gray));
//		
//		ApiMethod.queryCurrentSensorData(new AsyncHttpResponseHandler() {
//			@Override
//			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//				try {
//					layout.setBackgroundColor(getResources().getColor(R.color.green));
//					String value = new String(responseBody,"utf-8");
//					jsonObject = new JSONObject(value);
//					tv_sensor0.setText(jsonObject.getString("numericalSensor1") + " ℃");
//					tv_sensor1.setText(jsonObject.getString("numericalSensor2") + " %");
//					tv_sensor2.setText(jsonObject.getString("numericalSensor3") + " lux");
//					tv_sensor3.setText(jsonObject.getString("numericalSensor4") + " lux");
//					tv_sensor4.setText(jsonObject.getString("numericalSensor5") + " lux");
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			@Override
//			public void onFailure(int statusCode, Header[] headers,
//					byte[] responseBody, Throwable error) {
//				DialogUtils.showToast(StatusActivity.this, "获取失败, 请重试!");
//			}
//		});
//	}
//
//	/**
//	 * 灯光控制界面
//	 */
//	private void initLightUI() {
//		setContentView(R.layout.activity_status_light);
//		initTitle();
//		final Button bt_onoff = (Button) findViewById(R.id.activity_st_li_bt_onoff);
//		layout = (RelativeLayout) findViewById(R.id.activity_st_li);
//		layout.setBackgroundColor(getResources().getColor(R.color.gray));
//		
//		bt_onoff.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (equipment.getState() == 0) {
//					ApiMethod.controlLight(equipment.getId(), "open", new AsyncHttpResponseHandler() {
//						@Override
//						public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//							if (ValidationOrder(responseBody)) {
//								//按钮设为 选中，设备实体的状态 属性设为 开，换页面背景颜色
//								bt_onoff.setSelected(true);
//								equipment.setState(1);
//								layout.setBackgroundColor(getResources().getColor(R.color.green));
//							}
//						}
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								byte[] responseBody, Throwable error) {
//							bt_onoff.setSelected(false);
//							DialogUtils.showToast(StatusActivity.this, "打开失败, 请重试!");
//						}
//					});
//				}
//				else {
//					ApiMethod.controlLight(equipment.getId(), "close", new AsyncHttpResponseHandler() {
//						@Override
//						public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//							if (ValidationOrder(responseBody)) {
//								bt_onoff.setSelected(false);
//								equipment.setState(0);
//								layout.setBackgroundColor(getResources().getColor(R.color.gray));
//							}
//						}
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								byte[] responseBody, Throwable error) {
//							bt_onoff.setSelected(false);
//							DialogUtils.showToast(StatusActivity.this, "关闭失败, 请重试!");
//						}
//					});
//				}
//			}
//		});
//		
//	}
//	
//		
//	/**
//	 * 验证命令是否执行成功
//	 */
//	private boolean ValidationOrder(byte[] responseBody) {
//		try {
//			JSONObject object = new JSONObject(new String(responseBody, "utf-8"));
//			if (null != object && "true".equals(object.getString("success"))) {
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//	
//	
//}
