package com.smarthome.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.smarthome.app.R;
import com.smarthome.app.activity.ScenesSettingActivity;
import com.smarthome.app.adapter.ScenesListViewAdapter;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.widget.AlertDialog;

public class ScenesFragment extends BaseFragment
{
	private SwipeMenuListView listView;
	private ImageView bt_add;
	List<Scenes> adapterData;
	ScenesListViewAdapter adapter;
	
	/**
	 * 跳转回来重新刷新adapter
	 */
	public void onResume() {
		super.onResume();
		List<Scenes> list = DBbean.getInstance().queryAllScenes();
		while (adapterData.size() > 0) {
			adapterData.remove(0);
		}
		for (int i = 0; i < list.size(); i++) {
			adapterData.add(list.get(i));
		}
		adapter.notifyDataSetChanged();
	}
	
	
	@Override
	protected View bindLayout(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_scenes, null, false);
		return view;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View rootView = bindLayout(inflater);
    	listView = (SwipeMenuListView) rootView.findViewById(R.id.fragment_scenes_lv);
    	bt_add = (ImageView) rootView.findViewById(R.id.fragment_sc_add);
    	
    	bt_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().startActivity(new Intent(getActivity(), ScenesSettingActivity.class).putExtra("mode", "new"));
			}
		});
 
    	adapterData = new ArrayList<Scenes>();
    	initScenes();
    	
    	
    	adapter = new ScenesListViewAdapter(this.getActivity(), R.layout.item_scenes, adapterData);
        listView.setAdapter(adapter);
        
     // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("设置");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
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
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
     // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                    	getActivity().startActivity(new Intent(getActivity(), ScenesSettingActivity.class).putExtra("mode", adapterData.get(position).getId()+""));
                        break;
                    case 1:
                        // delete
                        builder.setTitle("提示");
                        builder.setMessage("删除后将无法恢复，确认删除?");
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
        					@Override
        					public void onClick(DialogInterface dialog, int which) {
        						DBbean.getInstance().deleteScenesById(adapterData.get(position).getId()+"");
        						adapterData.remove(position);
        						Log.d("Scenes", position+"");
                            	adapter.notifyDataSetChanged();
        						dialog.dismiss();
        					}
        				});
                        builder.show();
                    	
                        break;
                }
                return false;
            }
        });
        
        
        return rootView;
    }
	
	private void initScenes() {
		List<Scenes> list = DBbean.getInstance().queryAllScenes();
		for (int i = 0; i < list.size(); i++) {
			adapterData.add(list.get(i));
		}
	}
	
	private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
	
	
	
	
	

}
