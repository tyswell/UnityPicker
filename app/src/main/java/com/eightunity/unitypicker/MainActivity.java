package com.eightunity.unitypicker;

import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.eightunity.unitypicker.match.MatchFragment;
import com.eightunity.unitypicker.notification.NotificationFragment;
import com.eightunity.unitypicker.profile.ProfileFragment;
import com.eightunity.unitypicker.search.SearchFragment;
import com.eightunity.unitypicker.ui.BaseActivity;
import com.eightunity.unitypicker.utility.notification.FirebaseMsgService;
import com.eightunity.unitypicker.watch.WatchFragment;

public class MainActivity extends BaseActivity implements WatchFragment.OnHeadlineSelectedListener{

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;

    private int currentPage = -1;

    public static final int WATCH_PAGE = 0;
    public static final int NOTIFICATION_PAGE = 1;
    public static final int SEARCH_PAGE = 2;
    public static final int PROFILE_PAGE = 3;
    public static final int MATCH_PAGE = 4;

    private WatchFragment watchFragment;
    private NotificationFragment notificationFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private MatchFragment matchFragment;

    private static final String FRAGMENT_MATCH_TAG = "FRAGMENT_MATCH_TAG";

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (savedInstanceState != null) {
            for (Fragment f : getSupportFragmentManager().getFragments()){
                if (f instanceof WatchFragment) {
                    watchFragment = (WatchFragment)f;
                } else if (f instanceof NotificationFragment) {
                    notificationFragment = (NotificationFragment)f;
                } else if (f instanceof SearchFragment) {
                    searchFragment = (SearchFragment)f;
                } else if (f instanceof ProfileFragment) {
                    profileFragment = (ProfileFragment)f;
                }else if (f instanceof MatchFragment) {
                    matchFragment = (MatchFragment)f;
                }
            }
        }

        if (watchFragment == null) {
            watchFragment = new WatchFragment();
        }

        if (notificationFragment == null) {
            notificationFragment = new NotificationFragment();
        }

        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }

        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }

        if (matchFragment == null) {
            matchFragment = new MatchFragment();
        }

        setupTabIcons();
        setTabListner();

        if (savedInstanceState == null) {
            setStartPage();
        }
    }

    private void setTabListner() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "tab.getPosition()=" + tab.getPosition());
                Log.d(TAG, "tabLayout.getSelectedTabPosition()=" + tabLayout.getSelectedTabPosition());

                if (tab.getPosition() == WATCH_PAGE) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.mainFragment, watchFragment)
                            .commit();
                    currentPage = WATCH_PAGE;
                } else if (tab.getPosition() == NOTIFICATION_PAGE) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.mainFragment, notificationFragment)
                            .commit();
                    currentPage = NOTIFICATION_PAGE;
                } else if (tab.getPosition() == SEARCH_PAGE) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.mainFragment, searchFragment)
                            .commit();
                    currentPage = SEARCH_PAGE;
                } else if (tab.getPosition() == PROFILE_PAGE) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.mainFragment, profileFragment)
                            .commit();
                    currentPage = PROFILE_PAGE;
                }
                hideBackActionBar();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (currentPage == MATCH_PAGE) {
                    onBackPressed();
                }
            }
        });
    }

    private void setStartPage() {
        if (getIntent().getStringExtra(FirebaseMsgService.NOTIFICATION_INTENT) != null) {
            tabLayout.getTabAt(NOTIFICATION_PAGE).select();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, notificationFragment)
                    .addToBackStack(null)
                    .commit();
            currentPage = NOTIFICATION_PAGE;

        } else {
            tabLayout.getTabAt(SEARCH_PAGE).select();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFragment, searchFragment)
                    .addToBackStack(null)
                    .commit();
            currentPage = SEARCH_PAGE;
        }
    }

    private void setupTabIcons() {
        tabLayout.removeAllTabs();

        int[] tabIcons = {
                R.drawable.ic_action_glasses,
                R.drawable.ic_notifications,
                R.drawable.ic_search,
                R.drawable.ic_account_circle
        };

        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[0]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[1]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[2]));
        tabLayout.addTab(tabLayout.newTab().setIcon(tabIcons[3]));
    }

    @Override
    public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) {
        if (matchFragment.getArguments() != null) {
            matchFragment.getArguments().putString("A1", username);
            matchFragment.getArguments().putInt("A2", searchWordID);
            matchFragment.getArguments().putString("A3", searchWordDetail);
            matchFragment.getArguments().putString("A4", searchTypeDesc);
        } else {
            Bundle args = new Bundle();
            args.putString("A1", username);
            args.putInt("A2", searchWordID);
            args.putString("A3", searchWordDetail);
            args.putString("A4", searchTypeDesc);
            matchFragment.setArguments(args);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, matchFragment, FRAGMENT_MATCH_TAG)
                .addToBackStack(null)
                .commit();
        currentPage = MATCH_PAGE;
        showBackActionBar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("CURRENT_ITEM", currentPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        int currentPage = savedInstanceState.getInt("CURRENT_ITEM");
        Log.d(TAG, "currentPage=" + currentPage);

        if (currentPage == WATCH_PAGE) {
            Log.d(TAG, "watchFragment="+watchFragment);

            tabLayout.getTabAt(WATCH_PAGE).select();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, watchFragment)
                    .commit();
            currentPage = WATCH_PAGE;
        } else if (currentPage == NOTIFICATION_PAGE) {
            tabLayout.getTabAt(NOTIFICATION_PAGE).select();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, notificationFragment)
                    .commit();
            currentPage = NOTIFICATION_PAGE;
        } else if (currentPage == SEARCH_PAGE) {
            tabLayout.getTabAt(SEARCH_PAGE).select();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, searchFragment)
                    .commit();
            currentPage = SEARCH_PAGE;
        } else if (currentPage == PROFILE_PAGE) {
            tabLayout.getTabAt(PROFILE_PAGE).select();
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, profileFragment)
                    .commit();
            currentPage = PROFILE_PAGE;
        }else if (currentPage == MATCH_PAGE) {
            tabLayout.getTabAt(WATCH_PAGE).select();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, matchFragment, FRAGMENT_MATCH_TAG)
                    .addToBackStack(null)
                    .commit();
            showBackActionBar();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideBackActionBar();
    }

    public void showBackActionBar() {
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                hideBackActionBar();
            }
        });

    }


    public void hideBackActionBar() {
        toolbar.setNavigationIcon(null);
    }


}
