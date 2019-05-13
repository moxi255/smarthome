package com.example.abc.smarthome.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.smarthome.app.R;
import com.smarthome.app.activity.FeedbackActivity;
import com.smarthome.app.activity.GeneralActivity;
import com.smarthome.app.activity.LogActivity;

public class SettingFragment extends BaseFragment
{
	
	private LinearLayout linearLayout0, linearLayout1, linearLayout2, linearLayout3;
	
	@SuppressLint("InflateParams")
	@Override
	protected View bindLayout(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_setting, null, false);
		return view;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    	View rootView = bindLayout(inflater);
    	
    	initUI(rootView);
    	
        return rootView;
    }
	
	private void initUI(View rootView) {
		linearLayout0 = (LinearLayout) rootView.findViewById(R.id.fragment_se_message);
		linearLayout1 = (LinearLayout) rootView.findViewById(R.id.fragment_se_log);
		linearLayout2 = (LinearLayout) rootView.findViewById(R.id.fragment_se_general);
		linearLayout3 = (LinearLayout) rootView.findViewById(R.id.fragment_se_feedback);
		MyClickListener listener = new MyClickListener();
		linearLayout0.setOnClickListener(listener);
		linearLayout1.setOnClickListener(listener);
		linearLayout2.setOnClickListener(listener);
		linearLayout3.setOnClickListener(listener);
	}
	
	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.fragment_se_message:
				startActivity(new Intent(getActivity(), LogActivity.class).putExtra("equipmentId", "message"));
				break;
			case R.id.fragment_se_log:
				startActivity(new Intent(getActivity(), LogActivity.class).putExtra("equipmentId", "all"));
				break;
			case R.id.fragment_se_general:
				startActivity(new Intent(getActivity(), GeneralActivity.class));
				break;
			case R.id.fragment_se_feedback:
				startActivity(new Intent(getActivity(), FeedbackActivity.class));
				break;
			default:
				break;
			}
		}
		
	}
	

}

