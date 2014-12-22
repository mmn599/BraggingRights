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
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.DrawerAdapter;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,ListView.OnItemClickListener {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    ListView drawerList;
    CharSequence drawerTitle;
    CharSequence title;
    ActionBar actionBar;

    public static final String PASTFRAGMENT = "PAST";
    public static final String PRESENTFRAGMENT = "PRESENT";
    public static final String PERSONHOODFRAGMENT = "PERSONHOOD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        String areas[] = {"Present","Past","Personhood"};
        drawerList.setAdapter(new DrawerAdapter(this,R.layout.drawer_listview_row,areas));
        drawerList.setOnItemClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close)  /* "close drawer" description for accessibility */
         {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //sets up a few constants
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Constants.SCREEN_WIDTH = size.x;
        Constants.SCREEN_HEIGHT = size.y;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Create a new Fragment to be placed in the activity layout
        Fragment initialFragment = new PresentFragment();
        setTitle("Present");
        Intent callingIntent = getIntent();
        if(callingIntent!=null) {
            Bundle extras = getIntent().getExtras();
            if(extras!=null) {
                if (extras.containsKey("WHICH_FRAGMENT")) {
                    String s = getIntent().getExtras().getString("WHICH_FRAGMENT");
                    if (s.equals(PASTFRAGMENT)) {
                        initialFragment = new PastFragment();
                        setTitle("Past");
                    } else if (s.equals(PERSONHOODFRAGMENT)) {
                        //TODO: add in personhood fragment
                    }
                }
            }
        }
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,initialFragment).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if(v.getId()==R.id.btn_action_button) {
            startActivity(new Intent(this,CreateGoalActivity.class));
        }
    }

    //TODO: get better performance with background threads and shit
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //present
        if(position==0) {
            setTitle("Present");
            updateFragment(new PresentFragment());
        }
        //past
        else if(position==1) {
            setTitle("Past");
            updateFragment(new PastFragment());
        }
        //personhood
        else if(position==2) {
            setTitle("Personhood");
            updateFragment(new PersonhoodFragment());
        }
        drawerLayout.closeDrawers();
    }

    private void updateFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

}
