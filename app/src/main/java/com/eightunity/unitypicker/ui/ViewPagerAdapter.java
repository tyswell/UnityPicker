package com.eightunity.unitypicker.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.eightunity.unitypicker.MainActivity;
import com.eightunity.unitypicker.match.MatchFragment;
import com.eightunity.unitypicker.model.account.User;
import com.eightunity.unitypicker.notification.NotificationFragment;
import com.eightunity.unitypicker.profile.ProfileFragment;
import com.eightunity.unitypicker.search.SearchFragment;
import com.eightunity.unitypicker.watch.WatchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 4/27/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter implements WatchFragment.OnHeadlineSelectedListener{

    private static final String TAG = "ViewPagerAdapter";

    private WatchFragment watchFragment;
    private NotificationFragment notificationFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private MatchFragment matchFragment;

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 :
                watchFragment  = WatchFragment.newInstance();
                watchFragment.setMyFragmentListener(this);
                return watchFragment;

            case 1 :
                notificationFragment = NotificationFragment.newInstance();
                return notificationFragment;

            case 2 :
                searchFragment = SearchFragment.newInstance();
                return searchFragment;

            case 3 :
                profileFragment = ProfileFragment.newInstance();
                return profileFragment;

            case 4 :
                matchFragment = new MatchFragment();
                return matchFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title, int i) {
        if (i == 0) {
            watchFragment = (WatchFragment) fragment;
        } else if (i == 1) {
            notificationFragment = (NotificationFragment)fragment;
        } else if (i == 2) {
            searchFragment = (SearchFragment)fragment;
        } else if (i == 3) {
            profileFragment = (ProfileFragment) fragment;
        } else if (i == 4) {
            matchFragment = (MatchFragment) fragment;
        }
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) {
        if (matchFragment == null) {
            matchFragment = (MatchFragment)getItem(MainActivity.MATCH_PAGE);
        }
        matchFragment.startArtical(username, searchWordID, searchWordDetail, searchTypeDesc);
    }
}
