package com.smarthome.app.activity;

import java.util.Map;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.smarthome.app.R;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.ui.TitleActivity;
import com.smarthome.app.widget.AlertDialog;

/**
 * @author smmh
 *
 */
public class GeneralActivity extends TitleActivity {

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }
	
	private void initUI() {

        setContentView(R.layout.activity_general);
        setTitle(R.string.activity_general_title);
        showBackwardView(R.string.button_back, true);
        
        Button bt_reset = (Button) findViewById(R.id.activity_ge_resetdata);
        bt_reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GeneralActivity.this);
	            builder.setTitle("提示");
	            builder.setMessage("该操作会重置设备状态数据, 是否重置？");
	            builder.setNegativeButton("取消", null);
	            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				DBbean.getInstance().reasetEquipmentsState();
	    				for (Map.Entry<String, Equipment> entry : MainApplication.equipmentsMap.entrySet()) {
	    					entry.getValue().setState(0);
	    				}
	    				dialog.dismiss();
	    			}
	    		});
	            builder.show();
			}
		});
        
        final Button bt_unusual = (Button) findViewById(R.id.activity_ge_tixing);
        if (DBbean.getInstance().querySettingById("unusual") == 0)
        	bt_unusual.setSelected(false);
        else
        	bt_unusual.setSelected(true);
        bt_unusual.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DBbean.getInstance().querySettingById("unusual") == 0) {
					bt_unusual.setSelected(true);
					DBbean.getInstance().updateSetting("unusual", 1);
				} else {
					bt_unusual.setSelected(false);
					DBbean.getInstance().updateSetting("unusual", 0);
				}
			}
        });
        
        final Button bt_scenes = (Button) findViewById(R.id.activity_ge_tongzhi);
        if (DBbean.getInstance().querySettingById("scenes") == 0)
        	bt_scenes.setSelected(false);
        else
        	bt_scenes.setSelected(true);
        bt_scenes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (DBbean.getInstance().querySettingById("scenes") == 0) {
					bt_scenes.setSelected(true);
					DBbean.getInstance().updateSetting("scenes", 1);
				} else {
					bt_scenes.setSelected(false);
					DBbean.getInstance().updateSetting("scenes", 0);
				}
			}
        });
        
    }
	
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        
        default:
            break;
        }
    }
	
}
