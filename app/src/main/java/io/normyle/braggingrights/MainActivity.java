package io.normyle.braggingrights;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
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


public class MainActivity extends ActionBarActivity implements View.OnClickListener,ListView.OnItemClickListener {

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    ListView drawerList;
    CharSequence drawerTitle;
    CharSequence title;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        setTitle("Present");
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

        // Create a new Fragment to be placed in the activity layout
        PresentFragment presentFragment = new PresentFragment();

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,presentFragment).commit();

        //sets up a few constants
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        Constants.SCREEN_WIDTH = size.x;
        Constants.SCREEN_HEIGHT = size.y;
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
            PresentFragment newFragment = new PresentFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.commit();
        }
        //past
        else if(position==1) {
            setTitle("Past");
            PastFragment newFragment = new PastFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.commit();
        }
        //personhood
        else if(position==2) {
            setTitle("Personhood");
            actionBar.setIcon(R.drawable.ic_launcher);
        }

        drawerLayout.closeDrawers();
    }

}
