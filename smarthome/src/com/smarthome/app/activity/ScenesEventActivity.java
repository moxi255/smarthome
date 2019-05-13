package com.smarthome.app.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.smarthome.app.R;
import com.smarthome.app.ui.TitleActivity;
import com.smarthome.app.widget.AlertDialog;
import com.wangjie.wheelview.WheelView;

/**
 * 场景事件编辑页面
 * @author smmh
 *
 */
public class ScenesEventActivity extends TitleActivity {

	private Map<String, String> eventMap = new HashMap<String, String>();
	
	private SwipeMenuListView listView;
	private LinearLayout layout_ok;
	private String event;
	private ArrayAdapter<String> adapter;
	private List<String> adapterData = new ArrayList<String>();
	private String currentEvent = "打开走廊筒灯";
	private int currentEventId = 0;
	
	private String[] eventName = {
			"打开走廊筒灯","关闭走廊筒灯","打开走廊射灯","关闭走廊射灯","打开筒灯","关闭筒灯",
			"打开灯带", "关闭灯带", "打开大灯", "关闭大灯", "打开彩灯", "关闭彩灯", "打开左窗帘",
			"关闭左窗帘", "打开右窗帘","关闭右窗帘", "打开空调","关闭空调","打开电视机","关闭电视机"
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }
	
	private void initUI() {

        setContentView(R.layout.activity_scenes_event);
        setTitle(R.string.activity_scensesetting_event);
        showBackwardView(R.string.button_back, true);
        showForwardView(R.string.button_add, true);
        listView = (SwipeMenuListView) findViewById(R.id.activity_sc_ev_list);
        layout_ok = (LinearLayout) findViewById(R.id.activity_sc_ev_ok);
    }
	
	
	private void initData() {
		Intent intent = getIntent();
		if ( null != intent ) {
			event = intent.getStringExtra("event");
			if (!"".equals(event)) {
				String[] events = event.split("\\|");
				for (int i = 0; i < events.length; i++) {
					eventMap.put(events[i], eventName[Integer.valueOf(events[i])]);
					adapterData.add(eventName[Integer.valueOf(events[i])]);
				}
			}
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterData);
        listView.setAdapter(adapter);
        
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);
        
     // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                    	Iterator<Map.Entry<String, String>> it = eventMap.entrySet().iterator();
                    	while(it.hasNext()){  
                             Map.Entry<String, String> entry= it.next();
                             if (adapterData.get(position).equals(entry.getValue()))
                            	 it.remove();
                    	}
                    	for(Map.Entry<String,String> entry:eventMap.entrySet()){  
      					  Log.d("ScenesMap2",entry.getKey());
      			        }
                    	adapterData.remove(position);
                    	adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
        layout_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
	        	String event = "";
	        	for(Map.Entry<String,String> entry:eventMap.entrySet()){  
					  event += entry.getKey() + "|";
			    }
	        	//去掉最后 |
	        	if (!"".equals(event)) {
	        		event = event.substring(0,event.length()-1);
	        		intent.putExtra("event", event);
		        	ScenesEventActivity.this.setResult(5, intent);
		        	finish();
	        	}
	        	else {
	        		builder3.setTitle("提醒");
					builder3.setNegativeButton("确定", null);
					builder3.setMessage("请添加事件！");
					builder3.show();
	        	}
			}
		});
        
        
	}
	
	@Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.layout_ti_button_forward:
        	View outerView = LayoutInflater.from(this).inflate(R.layout.layout_status_air_dialog, null);
    		WheelView wv = (WheelView) outerView.findViewById(R.id.activity_st_ai_di_view);
    		wv.setOffset(1);
    		//放入数据
            wv.setItems(Arrays.asList(eventName));
            //选中的是第几个
            wv.setSeletion(currentEventId);
            //选中项监听
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                	currentEvent = item;
                	
                	currentEventId = selectedIndex-1;
                }
            });
    		
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder.setTitle("添加事件");
			builder.setNegativeButton("取消", null);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!eventMap.containsKey(currentEventId+"")) {
						eventMap.put(currentEventId+"", currentEvent);
						adapterData.add(currentEvent);
						adapter.notifyDataSetChanged();
					} else {
						builder1.setTitle("提醒");
						builder1.setNegativeButton("确定", null);
						builder1.setMessage("事件已存在，请重新选择！");
						builder1.show();
					}
					dialog.dismiss();
				}
			});
			builder.setView(outerView);
			builder.show();
        	break;
        default:
            break;
        }
    }
	
	private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
	
	
}

