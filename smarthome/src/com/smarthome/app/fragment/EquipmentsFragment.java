package com.smarthome.app.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.smarthome.app.R;
import com.smarthome.app.adapter.EquipmentsListViewAdapter;
import com.smarthome.app.db.DBbean;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.ActivityUtils;

@SuppressLint("InflateParams")
public class EquipmentsFragment extends BaseFragment
{
	PullToZoomListViewEx listView;
	List<Equipment> adapterData; 
	EquipmentsListViewAdapter adapter;
	
	//绑定视图
	@Override
	protected View bindLayout(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_allequipment, null, false);
		return view;
	}
	
	public void OnActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * 跳转回来重新刷新adapter
	 */
	public void onResume() {
		super.onResume();
		for (int i = 0; i < adapterData.size(); i++) {
			Equipment equipment = adapterData.get(i);
			adapterData.get(i).setState(MainApplication.equipmentsMap.get(equipment.getId()).getState());
		}
		adapter.notifyDataSetChanged();
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View rootView = bindLayout(inflater);
    	listView = (PullToZoomListViewEx) rootView.findViewById(R.id.allequipment_ptzlv);
    	
    	adapterData = DBbean.getInstance().queryAllEquipmentsToList();
    	for (int i = 0; i < adapterData.size(); i++) {
//    		adapterData.get(i).setImageId( MainApplication.equipmentsMap.get(adapterData.get(i).getId()).getImageId() );
    	}
    	adapter = new EquipmentsListViewAdapter(this.getActivity(), R.layout.item_allequipment, adapterData);
        listView.setAdapter(adapter);
        
        
        listView.setOnItemClickListener(new OnItemClickListener() {  
	        @SuppressLint("NewApi")
			@Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                   long arg3) {  
	               if(arg2 > 0)
	               {
	            	   ActivityUtils.startOtherActivity(adapterData.get(arg2-1), getActivity());
//		               changeSort(adapterData, arg2-1);
//		               adapter.notifyDataSetChanged();
	               }
	           }  
	       }); 
        
        //实体化 PullToZoomListViewEx
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        @SuppressWarnings("unused")
		int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        
        return rootView;
    }
	
	/**
	 * 点击列排到最前面
	 */
//	private void changeSort(List<Equipment> list, int position)
//	{
//		Equipment equipment = list.get(position);
//		list.remove(position);
//		list.add(0, equipment);
//	}
	
	
}
