package com.eightunity.unitypicker;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.eightunity.unitypicker.notification.NotificationFragment;
import com.eightunity.unitypicker.profile.ProfileFragment;
import com.eightunity.unitypicker.search.SearchFragment;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.ViewPagerAdapter;
import com.eightunity.unitypicker.watch.WatchFragment;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(2);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_action_glasses,
                R.drawable.ic_notifications,
                R.drawable.ic_search,
                R.drawable.ic_account_circle
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new WatchFragment(), getResources().getString(R.string.watch_footer));
        adapter.addFrag(new NotificationFragment(), getResources().getString(R.string.notification_footer));
        adapter.addFrag(new SearchFragment(), getResources().getString(R.string.search_footer));
        adapter.addFrag(new ProfileFragment(), getResources().getString(R.string.profile_footer));
        viewPager.setAdapter(adapter);
    }

}
