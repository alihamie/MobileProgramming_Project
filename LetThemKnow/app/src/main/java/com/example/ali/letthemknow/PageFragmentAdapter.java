package com.example.ali.letthemknow;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ali on 1/31/2017.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Tasks", "Team", "Steps" };
    private Context context;

    public PageFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0 ) {
            return TeamFragment.newInstance(position + 1);      // return instance of the tasks fragment
        }else  if(position == 1){
            return PedFragment.newInstance("","");      // return instance of the teams fragment
        }else {
            return TeamFragment.newInstance(position + 1);      // return instance of the steps fragment
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
