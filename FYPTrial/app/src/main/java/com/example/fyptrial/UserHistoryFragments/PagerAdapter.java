package com.example.fyptrial.UserHistoryFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm,int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:
                YesterdayHistoryFragment tab1 = new YesterdayHistoryFragment();
                return tab1;
            case 1:
                TodayHistoryFragment tab2 = new TodayHistoryFragment();
                return  tab2;
            case 2:
                FurtherHistoryFragment tab3 = new FurtherHistoryFragment();
                return tab3;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
