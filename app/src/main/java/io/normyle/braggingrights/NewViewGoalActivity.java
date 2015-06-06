package io.normyle.braggingrights;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalTypeView;

public class NewViewGoalActivity extends ActionBarActivity {

    Goal mGoal;
    GoalTypeView mGoalType;
    TextView mTxtviewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_view_goal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int goal_id = getIntent().getExtras().getInt("GOAL_ID", 0);

        MySQLiteHelper db = new MySQLiteHelper(this);
        mGoal = db.getGoal(goal_id);
        db.close();


        mGoalType = (GoalTypeView) findViewById(R.id.goaltypeview_goal_icon);
        mTxtviewTitle = (TextView) findViewById(R.id.txtview_goal_title);

        Constants.GoalType type = Constants.getGoalTypes().get(mGoal.getType());
        mGoalType.setImageResource(type.getImageId());
        mGoalType.setColor(type.getColor());

        mTxtviewTitle.setText(mGoal.getTitle());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
