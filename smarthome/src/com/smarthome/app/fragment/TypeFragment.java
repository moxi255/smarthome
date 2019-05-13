package com.smarthome.app.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.adapter.TypeListViewAdapter;
import com.smarthome.app.model.Equipment;
import com.smarthome.app.model.Type;
import com.smarthome.app.ui.MainApplication;
import com.smarthome.app.utils.ActivityUtils;

public class TypeFragment extends BaseFragment
{
	ListView listView;
	TextView text_title;
	Button button_back;
	List<Type> adapterData;
	TypeListViewAdapter adapter;
	
	private static int depth = 0;
	
	@SuppressLint("InflateParams")
	@Override
	protected View bindLayout(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_type, null, false);
		return view;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View rootView = bindLayout(inflater);
    	listView = (ListView) rootView.findViewById(R.id.fragment_type_lv);
    	text_title = (TextView) rootView.findViewById(R.id.fragment_ty_title);
    	button_back = (Button) rootView.findViewById(R.id.fragment_ty_back);
    	
    	adapterData = new ArrayList<Type>();
    	initData0();
    	
    	adapter = new TypeListViewAdapter(this.getActivity(), R.layout.item_type, adapterData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {  
	           @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                   long arg3) {  
	                if (depth == 0) {
	                	text_title.setText(adapterData.get(arg2).getName());
	                	initData1(arg2);
	                	button_back.setVisibility(View.VISIBLE);
	                	adapter.notifyDataSetChanged();
	                	depth = 1;
	                } else if (depth == 1) {
	                	Equipment equipment = MainApplication.equipmentsMap.get(adapterData.get(arg2).getEquipmentId());
	                	ActivityUtils.startOtherActivity(equipment, getActivity());
	                }
	           }  
	       }); 
        
        button_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initData0();
				button_back.setVisibility(View.INVISIBLE);
				text_title.setText("类别");
				adapter.notifyDataSetChanged();
				depth = 0;
			}
		});
        
        return rootView;
    }
	
	private void initData0() {
		adapterData.clear();
		Type Type0 = new Type("灯光", R.drawable.image_light);
		adapterData.add(Type0);
		Type Type1 = new Type("窗帘", R.drawable.image_curtain);
		adapterData.add(Type1);
		Type Type2 = new Type("多媒体", R.drawable.image_av);
		adapterData.add(Type2);
		Type Type3 = new Type("空调", R.drawable.image_air);
		adapterData.add(Type3);
		Type Type4 = new Type("环境传感器", R.drawable.image_sensor);
		adapterData.add(Type4);
		Type Type5 = new Type("安防传感器", R.drawable.image_prosensor);
		adapterData.add(Type5);
		Type Type6 = new Type("摄像头", R.drawable.image_camera);
		adapterData.add(Type6);
	}
	
	private void initData1(int i) {
		adapterData.clear();
		for (Map.Entry<String, Equipment> entry : MainApplication.equipmentsMap.entrySet()) {
			if (entry.getValue().getType() == i || (i == 6 && entry.getValue().getType() > 6)) {
				adapterData.add(new Type(entry.getValue().getName(), entry.getValue().getImageId(), entry.getValue().getId()));
			} 
		}
	}

}
