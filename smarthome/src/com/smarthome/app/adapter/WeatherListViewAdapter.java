package com.smarthome.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.model.Weather;

/**
 * @author smmh
 *
 */
public class WeatherListViewAdapter  extends ArrayAdapter<Weather>
{
	private int resourceId;
	
	public WeatherListViewAdapter(Context context, int resource, List<Weather> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Weather weather = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.equipmentContext = (TextView) view.findViewById(R.id.item_lo_context);
			viewHolder.equipmentDate = (TextView) view.findViewById(R.id.item_lo_date);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.equipmentContext.setText("温度: " + weather.getTemp() + "  湿度: " + weather.getHumidity());
		viewHolder.equipmentDate.setText(weather.getDatetime());
		
		return view;
	}
	
	class ViewHolder {
		
		TextView equipmentContext;
		
		TextView equipmentDate;
	}
	
	
}


