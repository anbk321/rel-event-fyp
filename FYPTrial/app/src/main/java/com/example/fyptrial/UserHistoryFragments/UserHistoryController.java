package com.example.fyptrial.UserHistoryFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.fyptrial.R;
import com.example.fyptrial.UserHistory;
import com.example.fyptrial.UserRequest;
import com.google.android.material.tabs.TabLayout;


public class UserHistoryController extends AppCompatActivity
{

    private TabLayout tabLayout = null;

    private int[] tabIcons = {R.drawable.yesterdays_history_icon,R.drawable.todays_history_icon,R.drawable.yesterdays_history_icon};

    private ViewPager viewPager = null;

    private PagerAdapter pagerAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_history_controller);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));

        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                viewPager.setCurrentItem(tab.getPosition());
                startActivity(new Intent(UserHistoryController.this, UserHistory.class));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


    }
}
