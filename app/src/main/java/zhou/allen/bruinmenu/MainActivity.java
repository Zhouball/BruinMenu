package zhou.allen.bruinmenu;


///TODO: Fix force quit. Google, stackoverflow code, and look at samples

/**
 * easter
 * -Easter egg
 *
 * swiper
 * -Make sure the page updates every meal period (http://stackoverflow.com/questions/10849552/update-viewpager-dynamically/17855730#17855730)
 *
 * sliders
 * -Make the list_items look better (make two textviews, one has bold kitchen, other has food)
 * -If not open, show it in red
 * -vegetarian marker
 * -change fontFamilies
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
        });
        //Add all the Tabs to the TabHost
        for (int i = 0; i < viewAdapter.getCount(); i++) {
            tabHost.addTab(tabHost.newTab().setText(viewAdapter.getPageTitle(i)).setTabListener(this));
        }

        // preparing list data based on time of day
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 3 && hour < 10) {
            currentMenu = 1;
        } else if(hour < 16) {
            currentMenu = 2;
        } else {
            currentMenu = 3;
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

        String menus[] = {"Swipes", "Breakfast", "Lunch", "Dinner"};

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num) {
                case 0:
                    fragment = SwipesLeftFragment.newInstance();
                    break;
                case 1:
                    Calendar c = Calendar.getInstance();
                    int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                    if(dayOfWeek == 1 || dayOfWeek == 7) fragment = MenuFragment.newInstance("lunch");
                    else fragment = MenuFragment.newInstance("breakfast");
                    break;
                case 2:
                    fragment = MenuFragment.newInstance("lunch");
                    break;
                case 3:
                    fragment = MenuFragment.newInstance("dinner");
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
