package com.ky.kyandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> mFragments;
	
	public ViewPageAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setFragments(ArrayList<Fragment> fragments) {
		this.mFragments = fragments;
	}

	public ArrayList<Fragment> getFragments() {
		return mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		if (mFragments != null && mFragments.size() > 0) {
			return mFragments.get(position);
		} else {
			return null;
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public int getCount() {
		if (mFragments != null) {
			return mFragments.size();
		} else {
			return 0;
		}
	}

	/**
	 * 重写此方法，不做实现，可防止子fragment被回收
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}
	
	

}
