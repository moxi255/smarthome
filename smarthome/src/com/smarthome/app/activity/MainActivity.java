package com.smarthome.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.githang.viewpagerindicator.IconTabPageIndicator;
import com.smarthome.app.R;
import com.smarthome.app.adapter.FragmentAdapter;
import com.smarthome.app.fragment.BaseFragment;
import com.smarthome.app.fragment.EquipmentsFragment;
import com.smarthome.app.fragment.ScenesFragment;
import com.smarthome.app.fragment.SettingFragment;
import com.smarthome.app.fragment.TypeFragment;
import com.smarthome.app.utils.DialogUtils;
import com.smarthome.app.widget.CustomViewPager;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	private CustomViewPager mViewPager;
    private IconTabPageIndicator mIndicator;
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
        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        //设置viewpager的缓存页面为选中页面的左右各3个
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
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
	 
	 
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
