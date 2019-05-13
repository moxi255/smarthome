package com.example.abc.smarthome.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.abc.smarthome.adapter.FragmentAdapter;
import com.example.abc.smarthome.fragment.BaseFragment;
import com.example.abc.smarthome.fragment.SettingFragment;
import com.example.abc.smarthome.fragment.TypeFragment;
import com.example.abc.smarthome.utils.DialogUtils;
import com.example.abc.smarthome.widget.CustomViewPager;


import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	private CustomViewPager mViewPager;
    private ImageView TVimageView;
    private long mExitTime;
    
    @SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
	}

	 //初始化界面
	
	 private void initViews() {

	  }
	  public void onIVDoor(View v){

      }
	 private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        BaseFragment EquipmentsFragment = new EquipmentsFragment();
        EquipmentsFragment.setTitle("全部");
        EquipmentsFragment.setIconId(R.drawable.tab_home_selector);
        fragments.add(EquipmentsFragment);

         BaseFragment scenesFragment = new ScenesFragment();
         scenesFragment.setTitle("场景");
         scenesFragment.setIconId(R.drawable.tab_paper_selector);
         fragments.add(scenesFragment);
        
        BaseFragment typeFragment = new TypeFragment();
        typeFragment.setTitle("类别");
        typeFragment.setIconId(R.drawable.tab_toggle_selector);
        fragments.add(typeFragment);

        BaseFragment settingFragment = new SettingFragment();
        settingFragment.setTitle("设置");
        settingFragment.setIconId(R.drawable.tab_gear_selector);
        fragments.add(settingFragment);


        return fragments;
	}
	
	/**
	 * 双击退出
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
             if ((System.currentTimeMillis() - mExitTime) > 2000) {
            	 DialogUtils.showToastShort(this, "再按一次退出程序");
                 mExitTime = System.currentTimeMillis();
             } else {
                 finish();
             }
             return true;
        }
        return super.onKeyDown(keyCode, event);
	}
	 
	 

}
