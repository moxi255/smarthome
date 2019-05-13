package com.example.abc.smarthome.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.smarthome.model.Type;


public class TypeListViewAdapter extends ArrayAdapter<Type>
{
	private int resourceId;

	public TypeListViewAdapter(Context context, int resource, List<Type> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Type type = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.equipmentImage = (ImageView) view.findViewById(R.id.lv_type_item_image);
			viewHolder.equipmentName = (TextView) view.findViewById(R.id.lv_type_item_tv1);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.equipmentImage.setImageResource(type.getImageId());
		viewHolder.equipmentName.setText(type.getName());
		
		return view;
	}
	
	class ViewHolder {
		ImageView equipmentImage;
		
		TextView equipmentName;
		
	}
	
	
}

