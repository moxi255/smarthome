package com.smarthome.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Scenes;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.AlarmUtils;

public class ScenesListViewAdapter extends ArrayAdapter<Scenes>
{
	private int resourceId;

	public ScenesListViewAdapter(Context context, int resource, List<Scenes> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Scenes scenes = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.scenesLayout = (RelativeLayout) view.findViewById(R.id.item_sc);
			viewHolder.scenesName = (TextView) view.findViewById(R.id.item_sc_name);
			viewHolder.scenesTime = (TextView) view.findViewById(R.id.item_sc_time);
			viewHolder.scenesButton = (Button) view.findViewById(R.id.item_sc_onoff);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.scenesName.setText(scenes.getName());
		viewHolder.scenesTime.setText(scenes.getTime());
		viewHolder.scenesButton.setFocusable(false);
		if (scenes.getState() == 0) {
			viewHolder.scenesLayout.setBackgroundColor(Color.parseColor("#eeeef4"));
			viewHolder.scenesName.setTextColor(Color.parseColor("#9b9ba0"));
			viewHolder.scenesTime.setTextColor(Color.parseColor("#9b9ba0"));
			viewHolder.scenesButton.setSelected(false);
		} else if (scenes.getState() > 0) {
			viewHolder.scenesLayout.setBackgroundColor(Color.parseColor("#ffffff"));
			viewHolder.scenesName.setTextColor(Color.parseColor("#000000"));
			viewHolder.scenesTime.setTextColor(Color.parseColor("#000000"));
			viewHolder.scenesButton.setSelected(true);
		}
		
		viewHolder.scenesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (scenes.getState() == 0) {
					scenes.setState(1);
					DBbean.getInstance().updateScenes(scenes);
					viewHolder.scenesLayout.setBackgroundColor(Color.parseColor("#ffffff"));
					viewHolder.scenesName.setTextColor(Color.parseColor("#000000"));
					viewHolder.scenesTime.setTextColor(Color.parseColor("#000000"));
					viewHolder.scenesButton.setSelected(true);
					AlarmUtils.setAlarm(getContext(), MainApplication.aManager, scenes);
				} else if (scenes.getState() == 1) {
					scenes.setState(0);
					DBbean.getInstance().updateScenes(scenes);
					viewHolder.scenesLayout.setBackgroundColor(Color.parseColor("#eeeef4"));
					viewHolder.scenesName.setTextColor(Color.parseColor("#9b9ba0"));
					viewHolder.scenesTime.setTextColor(Color.parseColor("#9b9ba0"));
					viewHolder.scenesButton.setSelected(false);
					AlarmUtils.cancelAlarm(getContext(), MainApplication.aManager, scenes);
				}
				
			}
		});
		return view;
	}
	
	class ViewHolder {
		RelativeLayout scenesLayout;
		
		TextView scenesName;
		
		TextView scenesTime;
		
		Button scenesButton;
	}
	
	
	
}

