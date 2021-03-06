package com.rhombus.bruinmenu;

/**
 * swiper
 * -Make sure the page updates every meal period (http://stackoverflow.com/questions/10849552/update-viewpager-dynamically/17855730#17855730)
 *
 * settings
 * TODO: -update frequency setting in menu
 *
 * favorites
 * TODO: -Modify intent to go to the correct page and open correct dining hall in expandable list view
 *
 * sliders
 * -Change font families (font size in sp (not dip), different font type)
 * TODO: -App icon
 * TODO: -Edit credits (emails, names, special thanks?.. etc)
 **/
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener, MenuFragment.OnFragmentInteractionListener, SwipesLeftFragment.OnFragmentInteractionListener, LoadWebViewFragment.OnFragmentInteractionListener {

    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    int currentMenu = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set default settings
        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        //get ViewPager and MaterialTabHost
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewAdapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setDistanceToTriggerSync(200);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
                if(currentMenu == 4) mSwipeRefreshLayout.setEnabled(false); //disable refreshing when on hours of operation page
                else mSwipeRefreshLayout.setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING) mSwipeRefreshLayout.setEnabled(false); //disable refreshing when changing pages
                else if(currentMenu == 4) mSwipeRefreshLayout.setEnabled(false); //disable refreshing when on hours of operation page
                else mSwipeRefreshLayout.setEnabled(true);
            }
        });
        //Add all the Tabs to the TabHost
        for (int i = 0; i < viewAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(viewAdapter.getPageTitle(i)).setTabListener(this));
        }

        // preparing list data based on time of day
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            currentMenu = 0;
        } else if(hour < 16) {
            currentMenu = 1;
        } else {
            currentMenu = 2;
        }

        viewPager.setCurrentItem(currentMenu);
        currentMenu = 4; //TODO: tempfix to prevent refresh from occurring in the hours of operation fragment


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_quick_dining_menu) {
            Intent i = new Intent(this, QuickDiningMenusActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.action_credits) {
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshContent(){
        Intent i = new Intent(this, RefreshScreenActivity.class);
        startActivity(i);
        mSwipeRefreshLayout.setRefreshing(false);
        finish();
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        //currentMenu = tab.getPosition(); //TODO: tempfix with the `currentMenu = 4` line. Fix why currentMenu isn't being changed when you swipe
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        String menus[] = {"Breakfast", "Lunch", "Dinner", "Swipes", "Hours"};

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case 0:
                    Calendar c = Calendar.getInstance();
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                    if(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) fragment = MenuFragment.newInstance("lunch");
                    else fragment = MenuFragment.newInstance("breakfast");
                    break;
                case 1:
                    fragment = MenuFragment.newInstance("lunch");
                    break;
                case 2:
                    fragment = MenuFragment.newInstance("dinner");
                    break;
                case 3:
                    fragment = SwipesLeftFragment.newInstance();
                    break;
                case 4:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Hours");
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return menus.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return menus[position];
        }
    }
}
