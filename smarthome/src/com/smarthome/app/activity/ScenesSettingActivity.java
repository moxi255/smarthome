package com.smarthome.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.ui.TitleActivity;
import com.smarthome.app.widget.AlertDialog;
import com.smarthome.app.widget.CycleWheelView;
import com.smarthome.app.widget.CycleWheelView.CycleWheelViewException;
import com.smarthome.app.widget.CycleWheelView.WheelItemSelectedListener;
import com.wangjie.wheelview.WheelView;

/**
 * @author smmh
 *
 */
public class ScenesSettingActivity extends TitleActivity {

	private TextView tv_repeat, tv_label, tv_event;
	private CycleWheelView wheelView_hour, wheelView_minute;
	private LinearLayout layout_repeat, layout_label, layout_event, layout_delete;
	
	private Scenes scenes;
	private String MODE = null;
	private String time_hour = null, time_minute = null;
	private int scenes_type;
	
	private String[] types = new String[]{"立即执行","只执行一次","每天重复"};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenes_setting);
        
        initData();
        initUI();
        
    }
	
	private void initData() {
		Intent intent = getIntent();
		if (null != intent) {
			MODE = intent.getStringExtra("mode");
			if ("new".equals(MODE)) {
				//隐藏删除按钮
				layout_delete = (LinearLayout) findViewById(R.id.activity_sc_se_delete);
				layout_delete.setVisibility(View.GONE);
				scenes = new Scenes();
				scenes.setType(0);
				scenes.setName("场景");
				scenes.setEvent("");
			} else {
				layout_delete = (LinearLayout) findViewById(R.id.activity_sc_se_delete);
				scenes = DBbean.getInstance().queryScenesById(Integer.valueOf(intent.getStringExtra("mode")));
			}
		}
	}
	
	
	private void initUI() {
        
        setTitle(R.string.activity_scensesetting_title);
        showBackwardView(R.string.button_back, true);
        showForwardView(R.string.button_save, true);
        
        tv_repeat = (TextView) findViewById(R.id.activity_sc_se_tv_repeat);
        tv_label = (TextView) findViewById(R.id.activity_sc_se_tv_label);
        tv_event = (TextView) findViewById(R.id.activity_sc_se_tv_event);
        
        
        wheelView_hour = (CycleWheelView) findViewById(R.id.activity_sc_se_hour);
        wheelView_minute = (CycleWheelView) findViewById(R.id.activity_sc_se_minute);
        layout_repeat = (LinearLayout) findViewById(R.id.activity_sc_se_repeat);
        layout_label = (LinearLayout) findViewById(R.id.activity_sc_se_label);
        layout_event = (LinearLayout) findViewById(R.id.activity_sc_se_event);
        
        initWheelView();
        initLayoutButton();
        
    }
	
	/**
	 * 初始化滑动控件事件
	 */
	private void initWheelView() {
		List<CycleWheelView> cycleWheelViews = new ArrayList<>();
		cycleWheelViews.add(wheelView_hour);
        cycleWheelViews.add(wheelView_minute);
        
        List<String> labels1 = new ArrayList<>();
        List<String> labels2 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
        	if (i < 24)
        		labels1.add("" + i);
        	labels2.add("" + i);
        }
        wheelView_hour.setLabels(labels1);
        wheelView_minute.setLabels(labels2);
        initWheelViewStyle(cycleWheelViews);
        
        if ("new".equals(MODE)) {
	        Calendar c = Calendar.getInstance();
	        int c_hour = c.get(Calendar.HOUR_OF_DAY);
	        int c_minute = c.get(Calendar.MINUTE);
	        wheelView_hour.setSelection(c_hour);
	        wheelView_minute.setSelection(c_minute);
	        tv_repeat.setText("立即执行");
	        tv_label.setText("场景");
	        tv_event.setText("0");
        } else {
        	String hour = scenes.getTime().split(":")[0];
        	String minute = scenes.getTime().split(":")[1];
        	wheelView_hour.setSelection(Integer.valueOf(hour));
        	wheelView_minute.setSelection(Integer.valueOf(minute));
	        tv_repeat.setText(types[scenes.getType()]);
	        tv_label.setText(scenes.getName());
	        String[] num = scenes.getEvent().split("\\|");
	        tv_event.setText(""+num.length);
        }
        
        wheelView_minute.setOnWheelItemSelectedListener(new WheelItemSelectedListener() {
        	@Override
        	public void onItemSelected(int position, String label) {
        		time_minute = label;
        	}
        });
        
        wheelView_hour.setOnWheelItemSelectedListener(new WheelItemSelectedListener() {
        	@Override
        	public void onItemSelected(int position, String label) {
        		time_hour = label;
        	}
        });
	}

	/**
	 * 初始化按钮事件
	 */
	private void initLayoutButton() {
		
		
		
		final View outerView = LayoutInflater.from(this).inflate(R.layout.layout_status_air_dialog, null);
		WheelView wv = (WheelView) outerView.findViewById(R.id.activity_st_ai_di_view);
		wv.setOffset(1);
		//放入数据
        wv.setItems(Arrays.asList(types));
        //选中的是第几个
        wv.setSeletion(1);
        //选中项监听
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
            	scenes_type = selectedIndex-1;
            }
        });
		
        final AlertDialog.Builder builder_repeat = new AlertDialog.Builder(this);
        layout_repeat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder_repeat.setTitle("重复次数");
				builder_repeat.setNegativeButton("取消", null);
				builder_repeat.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						scenes.setType(scenes_type);
						tv_repeat.setText(types[scenes_type]);
						dialog.dismiss();
					}
				});
				builder_repeat.setView(outerView);
				builder_repeat.show();
			}
		});
        
        final EditText et_name = new EditText(this);
        final AlertDialog.Builder builder_name = new AlertDialog.Builder(this);
        layout_label.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder_name.setTitle("标签");
				builder_name.setNegativeButton("取消", null);
				builder_name.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String label = et_name.getText().toString();
						if ("".equals(label)) {
							tv_label.setText("场景");
							scenes.setName("场景");
						}
						else {
							tv_label.setText(label);
							scenes.setName(label);
						}
						dialog.dismiss();
					}
				});
				builder_name.setView(et_name);
				builder_name.show();
			}
        });
        
        layout_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ScenesSettingActivity.this, ScenesEventActivity.class);
				intent.putExtra("event", scenes.getEvent());
				startActivityForResult(intent, 1);
			}
        });
        
       final  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        layout_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.setTitle("提示");
                builder.setMessage("删除后将无法恢复，确认删除?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBbean.getInstance().deleteScenesById(scenes.getId()+"");
						dialog.dismiss();
						finish();
					}
				});
                builder.show();
				
			}
		});
        
	}
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.layout_ti_button_forward:
        	if ("".equals(scenes.getEvent())) {
        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("请添加事件后再进行操作");
                builder.setNegativeButton("确定", null);
                builder.show();
        	} else {
        		if ("new".equals(MODE)) {
        			scenes.setTime(time_hour + ":" + time_minute);
        			scenes.setState(0);
        			DBbean.getInstance().insertScenes(scenes);
        			this.finish();
        		} else {
        			scenes.setTime(time_hour + ":" + time_minute);
        			scenes.setState(0);
        			DBbean.getInstance().updateScenes(scenes);
        			this.finish();
        		}
        	}
        default:
            break;
        }
    }
	
	private void initWheelViewStyle(List<CycleWheelView> views) {
		for (int i = 0; i < views.size(); i++) {
			try {
				views.get(i).setWheelSize(3);
		    } catch (CycleWheelViewException e) {
		         e.printStackTrace();
		    }
			views.get(i).setCycleEnable(true);
			views.get(i).setAlphaGradual(0.6f);
			views.get(i).setDivider(Color.parseColor("#d8d8d8"), 2);
			views.get(i).setSolid(Color.WHITE,Color.WHITE);
			views.get(i).setLabelColor(Color.BLACK);
			views.get(i).setLabelSelectColor(Color.parseColor("#00C957"));
		}
	}
	
	
	 protected  void onActivityResult(int requestCode, int resultCode, Intent data)  {
		 super.onActivityResult(requestCode, resultCode,  data);
//        //resultCode就是在B页面中返回时传的parama，可以根据需求做相应的处理
         switch (requestCode) {
         case 1:
        	 if (resultCode == 5)
			 {
	        	 String event = data.getStringExtra("event");
	        	 scenes.setEvent(event);
	        	 String[] events = event.split("\\|");
	        	 tv_event.setText(""+events.length);
			 }
        	 break;
         default:
        	break;
         }
	 }
	
}

