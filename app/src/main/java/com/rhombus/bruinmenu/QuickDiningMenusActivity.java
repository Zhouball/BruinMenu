package com.rhombus.bruinmenu;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class QuickDiningMenusActivity extends AppCompatActivity implements MaterialTabListener, LoadWebViewFragment.OnFragmentInteractionListener {

    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private QuickDiningMenusActivity.ViewPagerAdapter viewAdapter;

    int currentMenu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_dining_menus);

        //get ViewPager and MaterialTabHost
        viewPager = (ViewPager) findViewById(R.id.quickDining_viewPager);
        tabHost = (MaterialTabHost) findViewById(R.id.quickDining_materialTabHost);
        viewAdapter = new QuickDiningMenusActivity.ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Add all the Tabs to the TabHost
        for (int i = 0; i < viewAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(viewAdapter.getPageTitle(i)).setTabListener(this));
        }

        viewPager.setCurrentItem(currentMenu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.credits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
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

        String menus[] = {"BCafe", "Cafe1919", "Rendezvous", "GrabNGo", "Hedrick Study"};

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case 0:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Menus/BruinCafe");
                    break;
                case 1:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Menus/Cafe1919");
                    break;
                case 2:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Menus/Rendezvous");
                    break;
                case 3:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Menus/GrabNGo");
                    break;
                case 4:
                    fragment = LoadWebViewFragment.newInstance("http://menu.dining.ucla.edu/Menus/HedrickStudy");
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
