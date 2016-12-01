package com.eightunity.unitypicker;

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

    private static final String PARAM_CURRENT_PAGE = "PARAM_CURRENT_PAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate is called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        initFragment(savedInstanceState);

        setupTabIcons();
        setTabListner();

        if (savedInstanceState == null) {
            setStartPage();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState is called");

        super.onSaveInstanceState(outState);

        outState.putInt(PARAM_CURRENT_PAGE, currentPage);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState is called");

        super.onRestoreInstanceState(savedInstanceState);

        opentPage(savedInstanceState.getInt(PARAM_CURRENT_PAGE));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideBackActionBar();

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mainFragment);

        if (f instanceof WatchFragment) {
            currentPage = WATCH_PAGE;
        } else if (f instanceof NotificationFragment) {
            currentPage = NOTIFICATION_PAGE;
        } else if (f instanceof SearchFragment) {
            currentPage = SEARCH_PAGE;
        } else if (f instanceof ProfileFragment) {
            currentPage = PROFILE_PAGE;
        }else if (f instanceof MatchFragment) {
            currentPage = MATCH_PAGE;
        }

        setTab(currentPage);
    }

    @Override
    public void onArticleSelected(String username, int searchWordID, String searchWordDetail, String searchTypeDesc) {
        Log.d(TAG, "onArticleSelected is called {username :" + username + " | searchWordID :" + searchWordID + " | searchWordDetail :" + searchWordDetail + " | searchTypeDesc :" + searchTypeDesc);

        if (matchFragment.getArguments() == null) {
            matchFragment.setArguments(new Bundle());
        }

        matchFragment.getArguments().putString(MatchFragment.PARAM_USERNAME, username);
        matchFragment.getArguments().putInt(MatchFragment.PARAM_SEARCH_WORD_ID, searchWordID);
        matchFragment.getArguments().putString(MatchFragment.PARAM_SEARCH_WORD_DETAIL, searchWordDetail);
        matchFragment.getArguments().putString(MatchFragment.PARAM_SEARCH_TYPE_DESC, searchTypeDesc);

        opentPage(MATCH_PAGE);
        showBackActionBar();
    }



    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (Fragment f : getSupportFragmentManager().getFragments()){
                /*if (f instanceof WatchFragment) {
                    watchFragment = (WatchFragment)f;
                } else */if (f instanceof NotificationFragment) {
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
    }

    private void setTabListner() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "TEMP A currentPage : " + currentPage);
                opentPage(tab.getPosition());
                hideBackActionBar();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "TEMP B currentPage : " + currentPage);
                if (currentPage == MATCH_PAGE) {
                    onBackPressed();
                }
            }
        });
    }

    public void opentPage(int page) {
        Log.d(TAG, "opentPage is called {page : " + page);

        if (page == WATCH_PAGE) {
            setTab(WATCH_PAGE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, watchFragment)
                    .commit();
            currentPage = WATCH_PAGE;
        } else if (page == NOTIFICATION_PAGE) {
            setTab(NOTIFICATION_PAGE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, notificationFragment)
                    .commit();
            currentPage = NOTIFICATION_PAGE;
        } else if (page == SEARCH_PAGE) {
            setTab(SEARCH_PAGE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, searchFragment)
                    .commit();
            currentPage = SEARCH_PAGE;
        } else if (page == PROFILE_PAGE) {
            setTab(PROFILE_PAGE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.mainFragment, profileFragment)
                    .commit();
            currentPage = PROFILE_PAGE;
        }else if (page == MATCH_PAGE) {
            setTab(WATCH_PAGE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, matchFragment)
                    .addToBackStack(null)
                    .commit();
            showBackActionBar();
            currentPage = MATCH_PAGE;
        }
    }

    private void setTab(int page) {
        if (page == MATCH_PAGE) {
            tabLayout.getTabAt(WATCH_PAGE).select();
        } else {
            tabLayout.getTabAt(page).select();
        }
    }

    private void setStartPage() {
        Log.d(TAG, "setStartPage is called");

        if (getIntent().getStringExtra(FirebaseMsgService.NOTIFICATION_INTENT) != null) {
            opentPage(NOTIFICATION_PAGE);
        } else {
            opentPage(SEARCH_PAGE);
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

    public void showBackActionBar() {
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);

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
