package com.ky.kyandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 类名称：FragmentAdapter<br/>
 * 类描述：<br/>
 * 
 * 创建人： cz <br/>
 * 创建时间：2016年9月8日 下午2:48:49 <br/>
 * @updateRemark 修改备注：
 *     
 */
public class FragmentAdapter extends FragmentPagerAdapter {

	/** Fragment List */
	private List<Fragment> fragmentList;

	public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
