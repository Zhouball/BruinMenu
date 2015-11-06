package zhou.allen.bruinmenu;

/**
 * sqlite
 * -Store Table of Dining Halls, Table of Kitchens-Hall relations (to select list of kitchens), and food-kitchen relations
 * -Store nutritional data with food (link)
 * TODO: -Fix updating every time error
 *
 * swiper
 * -Make sure the page updates every meal period (http://stackoverflow.com/questions/10849552/update-viewpager-dynamically/17855730#17855730)
 *
 * list-view
 * -If dining hall not open, show it in red
 *
 * settings
 * TODO: -update frequency, turn off notifications
 *
 * notify-favorites
 * TODO: -End the branch and checkout to list-view with the working code (notification in updateDBService instead of refreshscreenactivity; remove all the string ArrayLists; check both right and left column parsing)
 *
 * favorites
 * TODO: -Modify intent to go to the correct page and open correct dining hall in expandable list view
 * TODO: -Change icon in notification (and all the other icons)
 *
 * sliders
 * -Change font families (font size in sp (not dip), different font type)
 * -App icon
 **/
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener, MenuFragment.OnFragmentInteractionListener, SwipesLeftFragment.OnFragmentInteractionListener{

    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    int currentMenu = -1;
    int NUM_PAGES = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set default 

        //get ViewPager and MaterialTabHost
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setDistanceToTriggerSync(100);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING) mSwipeRefreshLayout.setEnabled(false);
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
        if (hour > 3 && hour < 10) {
            currentMenu = 0;
        } else if(hour < 16) {
            currentMenu = 1;
        } else {
            currentMenu = 2;
        }

        viewPager.setCurrentItem(currentMenu);


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
        if (id == R.id.action_credits) {
            Intent i = new Intent(this, CreditsActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
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
        currentMenu = tab.getPosition();
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

        String menus[] = {"Breakfast", "Lunch", "Dinner", "Swipes"};

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
                    if(dayOfWeek == 1 || dayOfWeek == 7) fragment = MenuFragment.newInstance("lunch");
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
