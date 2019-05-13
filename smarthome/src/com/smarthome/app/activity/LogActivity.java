package com.smarthome.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.smarthome.app.R;
import com.smarthome.app.adapter.LogListViewAdapter;
import com.smarthome.app.adapter.MessageListViewAdapter;
import com.smarthome.app.adapter.WeatherListViewAdapter;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Weather;
import com.smarthome.app.model.mLog;
import com.smarthome.app.ui.TitleActivity;
import com.smarthome.app.widget.AlertDialog;

/**
 *  设置页面中的 消息页面和日志页面
 *	用来显示所有设备的被操作情况
 * @author smmh
 */
public class LogActivity extends TitleActivity {

	List<mLog> adapterData;
	List<Weather> weatherData;
	LogListViewAdapter adapter_Log;
	MessageListViewAdapter adapter_Mess;
	WeatherListViewAdapter adapter_Weather;
	ListView listView;
	String id;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }
	
	private void initUI() {

        setContentView(R.layout.activity_log);
        setTitle(R.string.activity_log_title);
        showBackwardView(R.string.button_back, true);
        showForwardView(R.string.button_delete, true);
        listView = (ListView) findViewById(R.id.activity_lo_lv);
        
    }
	
	
	private void initData() {
		Intent intent = getIntent();
		adapterData = new ArrayList<mLog>();
		if ( null != intent ) {
			id = intent.getStringExtra("equipmentId");
			if ("message".equals(id)) {
				adapterData = DBbean.getInstance().queryMessage();
				adapter_Mess = new MessageListViewAdapter(this, R.layout.item_log, adapterData);
				listView.setAdapter(adapter_Mess);
			} else if ("weather".equals(id)) {
				weatherData = DBbean.getInstance().queryWeather();
				adapter_Weather = new WeatherListViewAdapter(this, R.layout.item_log, weatherData);
				listView.setAdapter(adapter_Weather);
			} else {
				adapterData = DBbean.getInstance().queryEquipmentLog(id);
				adapter_Log = new LogListViewAdapter(this, R.layout.item_log, adapterData);
				listView.setAdapter(adapter_Log);
			}
		}
        
	}
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.layout_ti_button_forward:
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("该操作会永久删除数据, 是否删除？");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				//相当于把当前的数据源全清空
    				adapterData.clear();
    				if ("message".equals(id)) {
    					DBbean.getInstance().deleteMessage();
    					adapter_Mess.notifyDataSetChanged();
    				} else if ("weather".equals(id)) {
    					DBbean.getInstance().deleteWeather();
    					weatherData.clear();
    					adapter_Weather.notifyDataSetChanged();
    				}
    				else {
    					DBbean.getInstance().deleteEquipmentLog(id);
    					adapter_Log.notifyDataSetChanged();
    				}
    				dialog.dismiss();
    			}
    		});
            builder.show();
        	break;
        default:
            break;
        }
    }
	
}
