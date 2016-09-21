package com.eightunity.unitypicker;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.eightunity.unitypicker.match.MatchFragment;
import com.eightunity.unitypicker.notification.NotificationFragment;
import com.eightunity.unitypicker.profile.ProfileFragment;
import com.eightunity.unitypicker.search.SearchFragment;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.ui.NonSwipeViewPager;
import com.eightunity.unitypicker.ui.ViewPagerAdapter;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.eightunity.unitypicker.watch.WatchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;

    public static final int WATCH_PAGE = 0;
    public static final int NOTIFICATION_PAGE = 1;
    public static final int SEARCH_PAGE = 2;
    public static final int PROFILE_PAGE = 3;
    public static final int MATCH_PAGE = 4;

    private Fragment watchFragment;
    private Fragment notificationFragment;
    private Fragment searchFragment;
    private Fragment profileFragment;
    private Fragment matchFragment;

    private ViewPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        watchFragment= new WatchFragment();
//        notificationFragment = new NotificationFragment();
//        searchFragment = new SearchFragment();
//        profileFragment = new ProfileFragment();
//        matchFragment = new MatchFragment();

        viewPager = (NonSwipeViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (position != MATCH_PAGE) {
                        tabLayout.getTabAt(position).select();
                    }
                }
            });

        hideBackActionBar();


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                hideBackActionBar();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (viewPager.getCurrentItem() == MATCH_PAGE) {
                    viewPager.setCurrentItem(WATCH_PAGE);
                    hideBackActionBar();
                }
            }
        });

        setupTabIcons();

        setStartPage();
    }

    public void showBackActionBar() {
//        toolbar.getNavigationIcon().setVisible(true, true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == MATCH_PAGE) {
                    viewPager.setCurrentItem(WATCH_PAGE);
                }
                hideBackActionBar();
            }
        });

    }


    public void hideBackActionBar() {
//        toolbar.getNavigationIcon().setVisible(false, false);
        toolbar.setNavigationIcon(null);
    }

    private void setStartPage() {
        if (getIntent().getStringExtra(FirebaseMsgService.NOTIFICATION_INTENT) != null) {
            viewPager.setCurrentItem(NOTIFICATION_PAGE);
        } else {
            viewPager.setCurrentItem(SEARCH_PAGE);
        }
    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_action_glasses,
                R.drawable.ic_notifications,
                R.drawable.ic_search,
                R.drawable.ic_account_circle
        };

//        tabLayout.getTabAt(WATCH_PAGE).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(NOTIFICATION_PAGE).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(SEARCH_PAGE).setIcon(tabIcons[2]);
//        tabLayout.getTabAt(PROFILE_PAGE).setIcon(tabIcons[3]);

        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[3]));
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        watchFragment = WatchFragment.newInstance();
        notificationFragment= NotificationFragment.newInstance();
        searchFragment = SearchFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        matchFragment = MatchFragment.newInstance();

        adapter.addFrag(watchFragment, getResources().getString(R.string.watch_footer));
        adapter.addFrag(notificationFragment, getResources().getString(R.string.notification_footer));
        adapter.addFrag(searchFragment, getResources().getString(R.string.search_footer));
        adapter.addFrag(profileFragment, getResources().getString(R.string.profile_footer));
        adapter.addFrag(matchFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        int currentPage = viewPager.getCurrentItem();
        if (currentPage == MATCH_PAGE) {
            viewPager.setCurrentItem(WATCH_PAGE);
        }
        else {
            viewPager.setCurrentItem(SEARCH_PAGE);
        }
    }

}
