package io.normyle.braggingrights;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.ui.DrawerAdapter;


/**
 * MainActivity class, primarily serves to setup and enable the user to switch between three
 * fragments in the MainActivity's FrameLayout UI.
 *
 * The three fragments are GoalsFragment for viewing goals, PersonhoodFragment for viewing goal history,
 * and SettingsFragment for add and removing goal categories.
 *
 * GoalsFragment sometimes contains In Progress (Present) goals, sometimes Completed (Past) goals
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener,ListView.OnItemClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ActionBar mActionBar;

    boolean mViewingGoals;

    //Keys to specify which fragment to open when onResume is called
    public static final String PASTGOALS = "PAST";
    public static final String PRESENTGOALS = "PRESENT";
    public static final String PERSONHOODFRAGMENT = "PERSONHOOD";
    public static final String WHICH_FRAGMENT_KEY = "WHICH_FRAGMENT";

    //Titles for action bar depending on which fragment is opened
    public static final String PRESENT_TITLE = "In Progress";
    public static final String PAST_TITLE = "Completed";
    public static final String PERSONHOOD_TITLE = "Personhood";
    public static final String CATEGORIES_TITLE = "Categories";


    /*boolean indicating whether the user has setup at least one goal category
      if false, automatically opens SettingsFragment with intro text*/
    private boolean mSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks how many goal categories the user has created. If zero, the app needs to be setup.
        mSetup = Constants.isSetup(this);

        //Sets up navigation drawer
        mActionBar = getSupportActionBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);
        String areas[] = {PRESENT_TITLE,PAST_TITLE,PERSONHOOD_TITLE,CATEGORIES_TITLE};
        drawerList.setAdapter(new DrawerAdapter(this,R.layout.drawer_listview_row,areas));
        drawerList.setOnItemClickListener(this);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close)
         {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //Don't let the user navigate elsewhere in the app if no goal categories
        if(mSetup) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
        }

        //sets up a few constants, TODO: bad form to store static constants, switch to ContentProvider
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Constants.SCREEN_WIDTH = size.x;
        Constants.SCREEN_HEIGHT = size.y;
    }

    /**
     * Called from SettingsFragment once user has setup at least one goal category.
     * Allows navigation to rest of app.
     */
    public void finishedSetup() {
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mSetup = true;
    }

    /**
     * Sets up proper fragment in the FrameLayout depending on the calling intent
     * If the calling intent is null, just pull up Goals Fragment w/ in progress goals
     * Otherwise, the calling intent will specify which fragment to load in WHICH_FRAGMENT key
     */
    @Override
    protected void onResume() {
        super.onResume();
        Fragment initialFragment;
        if(mSetup) {
            //if app is setup, default to viewing in progress goals
            mViewingGoals = true;
            initialFragment = new GoalsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(GoalsFragment.WHICH_GOALS, PRESENTGOALS);
            initialFragment.setArguments(bundle);
            setTitle(PRESENT_TITLE);
            Intent callingIntent = getIntent();

            //if there's a calling intent, may need to display another fragment
            if (callingIntent != null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    if (extras.containsKey(WHICH_FRAGMENT_KEY)) {
                        String s = getIntent().getExtras().getString(WHICH_FRAGMENT_KEY);
                        if (s.equals(PASTGOALS)) {
                            //display goals fragment with past goals
                            bundle.putString(GoalsFragment.WHICH_GOALS, PASTGOALS);
                            setTitle(PAST_TITLE);
                        } else if (s.equals(PERSONHOODFRAGMENT)) {
                            //display personhood fragment
                            initialFragment = new PersonhoodFragment();
                            setTitle(PERSONHOOD_TITLE);
                            mViewingGoals = false;
                        }
                    }
                }
            }
        }
        else {
            //app isn't setup, load settings fragment
            initialFragment = new SettingsFragment();
        }

        // Add the fragment to the fragment_container FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,initialFragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if(v.getId()==R.id.btn_action_button) {
            startActivity(new Intent(this, CreateGoalActivity.class));
        }
    }


    //constants defining fragment positions
    public static final int POS_PRESENT = 0;
    public static final int POS_PAST = 1;
    public static final int POS_PERSONHOOD = 2;
    public static final int POS_SETTINGS = 3;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //goals fragment, either present (in progress), or past (completed)
        if(position==POS_PRESENT || position==POS_PAST) {
            Bundle bundle = new Bundle();
            boolean display_present = false;
            if(position==POS_PRESENT) {
                setTitle(PRESENT_TITLE);
                bundle.putString(GoalsFragment.WHICH_GOALS, PRESENTGOALS);
                display_present = true;
            }
            else {
                setTitle(PAST_TITLE);
                bundle.putString(GoalsFragment.WHICH_GOALS, PASTGOALS);
                display_present = false;
            }
            if(mViewingGoals) {
                //don't reload fragment if already viewing goals, just reload which goals are viewed
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                ((GoalsFragment) fragment).displayGoals(display_present);
            }
            else {
                GoalsFragment newFragment = new GoalsFragment();
                newFragment.setArguments(bundle);
                updateFragment(newFragment);
            }
        }
        //personhood
        else if(position==POS_PERSONHOOD) {
            mViewingGoals = false;
            setTitle(PERSONHOOD_TITLE);
            updateFragment(new PersonhoodFragment());
        }
        //settings
        else if(position==POS_SETTINGS) {
            mViewingGoals = false;
            setTitle(CATEGORIES_TITLE);
            updateFragment(new SettingsFragment());
        }
        mDrawerLayout.closeDrawers();
    }

    private void updateFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    public boolean isSetup() {
        return mSetup;
    }

}
