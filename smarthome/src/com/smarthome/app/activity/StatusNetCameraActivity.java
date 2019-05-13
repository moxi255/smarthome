package com.smarthome.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarthome.app.R;
import com.smarthome.app.fragment.MediaPlayFragment;
import com.smarthome.app.fragment.MediaPlayOnlineFragment;

/**
 * @author smmh
 *
 */
public class StatusNetCameraActivity extends FragmentActivity implements MediaPlayFragment.BackHandlerInterface{
	
	private MediaPlayFragment mMediaPlayFragment;
	private TextView tv_title;
	private Button bt_back;
	private RelativeLayout layout_title;
	
	public static final int IS_VIDEO_ONLINE = 1;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }
	
	private void initUI() {
        setContentView(R.layout.activity_status_netcamera);
        layout_title = (RelativeLayout) findViewById(R.id.layout_titlebar);
        tv_title = (TextView) findViewById(R.id.layout_ti_text_title);
        tv_title.setText("网络摄像头");
        bt_back = (Button) findViewById(R.id.layout_ti_button_backward);
        bt_back.setVisibility(View.VISIBLE);
        bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        
      //嵌入使用的帧
		MediaPlayFragment mediaPlayFragment; //引用的布局帧
		mediaPlayFragment = new MediaPlayOnlineFragment();
		changeFragment(mediaPlayFragment, false);
    }
	
	public void changeFragment(Fragment targetFragment, boolean isAddToStack) {
		if (isAddToStack) {
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, targetFragment).addToBackStack(null).commitAllowingStateLoss();
		} else {
			getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, targetFragment).commitAllowingStateLoss();
		}
		
	}
	
	@Override
	public void onBackPressed() {  	
        if(mMediaPlayFragment == null || !mMediaPlayFragment.onBackPressed()) {  
            super.onBackPressed();  
        }  
    }
	
	/* (non-Javadoc)
	 * @see com.smarthome.app.fragment.MediaPlayFragment.BackHandlerInterface#setSelectedFragment(com.smarthome.app.fragment.MediaPlayFragment)
	 */
	@Override
	public void setSelectedFragment(MediaPlayFragment backHandledFragment) {
		this.mMediaPlayFragment = backHandledFragment;
	}
		
	//横竖屏切换需要,隐藏标题栏
	public void toggleTitle(boolean isShow) {
		layout_title.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

}