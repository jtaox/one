package me.jtao.yjh.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;

import me.jtao.yjh.ui.fragment.ABaseFragment;


/**
 * Created by jiangtao on 2016/8/1.
 */
public class YJViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ABaseFragment> fragments;
    public YJViewPagerAdapter(FragmentManager fm, List<ABaseFragment> list) {
        super(fm);
        fragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getPageTitle();
    }

}
