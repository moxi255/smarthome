package com.example.abc.smarthome.adapter;

import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abc.smarthome.api.ApiMethod;
import com.example.abc.smarthome.http.AsyncHttpResponseHandler;
import com.example.abc.smarthome.model.Equipment;
import com.example.abc.smarthome.ui.MainApplication;
import com.example.abc.smarthome.utils.DialogUtils;
import com.example.abc.smarthome.utils.ValidateUtils;




public class EquipmentsListViewAdapter extends ArrayAdapter<Equipment>
{
	private int resourceId;

	public EquipmentsListViewAdapter(Context context, int resource, List<Equipment> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Equipment equipment = getItem(position);
		View view;
		final ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.equipmentImage = (ImageView) view.findViewById(R.id.item_al_image);
			viewHolder.equipmentName = (TextView) view.findViewById(R.id.item_al_name);
			viewHolder.equipmentButton = (Button) view.findViewById(R.id.item_al_onoff);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.equipmentImage.setImageResource(equipment.getImageId());
		viewHolder.equipmentName.setText(equipment.getName());
		
		//使listview里的按钮能点击
		viewHolder.equipmentButton.setFocusable(false);
		if (equipment.getState() == 0 || equipment.getState() == 2)
			viewHolder.equipmentButton.setSelected(false);
		else if (equipment.getState() == 1)
			viewHolder.equipmentButton.setSelected(true);
			
		viewHolder.equipmentButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (equipment.getState() == 0 || equipment.getState() == 2) {
					DialogUtils.showProgressDialog(getContext(), true);
					ApiControl(equipment, "open", 1);
					viewHolder.equipmentButton.setSelected(true);
				}
				else if (equipment.getState() == 1){
					DialogUtils.showProgressDialog(getContext(), true);
					ApiControl(equipment, "close", 0);
					viewHolder.equipmentButton.setSelected(false);
				} 
			}
		});
		
		//是否显示便捷按钮
		if (equipment.getType() <= 3) 
			viewHolder.equipmentButton.setVisibility(View.VISIBLE);
	    else
			viewHolder.equipmentButton.setVisibility(View.INVISIBLE);
		
		return view;
	}
	
	class ViewHolder {
		
		ImageView equipmentImage;
		
		TextView equipmentName;
		
		Button equipmentButton;
	}
	
	/**
	 * 便捷开关
	 */
	private void ApiControl(final Equipment equipment, String order, final int i) {
		if (equipment.getType() == 0) {
			ApiMethod.controlLight(equipment.getId(), order, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					if (ValidateUtils.validationOrder(responseBody)) {
						equipment.setState(i);
						MainApplication.equipmentsMap.put(equipment.getId(), equipment);
						DialogUtils.showProgressDialog(getContext(), false);
						if (i == 1)
							DBbean.getInstance().insertEquipmentLog(equipment, "打开");
						else if (i == 0)
							DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
					} else {
						DialogUtils.showProgressDialog(getContext(), false);
						DialogUtils.showToast(getContext(), "控制失败，请重试！");
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					DialogUtils.showProgressDialog(getContext(), false);
					DialogUtils.showToast(getContext(), "控制失败，请重试！");
				}
			});
		} else if (equipment.getType() == 1) {
			ApiMethod.controlCurtain(equipment.getId(), order, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					if (ValidateUtils.validationOrder(responseBody)) {
						equipment.setState(i);
						MainApplication.equipmentsMap.put(equipment.getId(), equipment);
						DialogUtils.showProgressDialog(getContext(), false);
						if (i == 1)
							DBbean.getInstance().insertEquipmentLog(equipment, "打开");
						else if (i == 0)
							DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
					} else {
						DialogUtils.showProgressDialog(getContext(), false);
						DialogUtils.showToast(getContext(), "控制失败，请重试！");
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					DialogUtils.showProgressDialog(getContext(), false);
					DialogUtils.showToast(getContext(), "控制失败，请重试！");
				}
			});
		} else if (equipment.getType() == 2) {
			ApiMethod.controlAv("tv", "power", new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					if (ValidateUtils.validationOrder(responseBody)) {
						equipment.setState(i);
						MainApplication.equipmentsMap.put(equipment.getId(), equipment);
						DialogUtils.showProgressDialog(getContext(), false);
						if (i == 1)
							DBbean.getInstance().insertEquipmentLog(equipment, "打开");
						else if (i == 0)
							DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
					} else {
						DialogUtils.showProgressDialog(getContext(), false);
						DialogUtils.showToast(getContext(), "控制失败，请重试！");
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					DialogUtils.showProgressDialog(getContext(), false);
					DialogUtils.showToast(getContext(), "控制失败，请重试！");
				}
			});
		} else if (equipment.getType() == 3) {
			ApiMethod.controlAir("air", order, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					if (ValidateUtils.validationOrder(responseBody)) {
						equipment.setState(i);
						MainApplication.equipmentsMap.put(equipment.getId(), equipment);
						DialogUtils.showProgressDialog(getContext(), false);
						if (i == 1)
							DBbean.getInstance().insertEquipmentLog(equipment, "打开");
						else if (i == 0)
							DBbean.getInstance().insertEquipmentLog(equipment, "关闭");
					} else {
						DialogUtils.showProgressDialog(getContext(), false);
						DialogUtils.showToast(getContext(), "控制失败，请重试！");
					}
				}
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					DialogUtils.showProgressDialog(getContext(), false);
					DialogUtils.showToast(getContext(), "控制失败，请重试！");
				}
			});
		}
	}
	
}
