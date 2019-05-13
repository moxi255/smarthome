package com.smarthome.app.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.githang.viewpagerindicator.IconPagerAdapter;
import com.smarthome.app.fragment.BaseFragment;

/**
 * fragment适配器
 * @author smmh
 *
 */
public class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter 
{
    private List<BaseFragment> mFragments;

    public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getIconResId(int index) {
        return mFragments.get(index).getIconId();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

}
