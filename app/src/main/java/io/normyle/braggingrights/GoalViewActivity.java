package io.normyle.braggingrights;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.TaskAdapter;

public class GoalViewActivity extends ActionBarActivity {

    TextView txtTitle;
    ImageView imgIcon;
    Goal goal;
    ListView listViewTasks;

    //TODO CHANGE THE WAY GOALS ARE PASSED TO THIS ACTIVITY (BY SERIALIZING GOAL)
    //FOR PERFORMANCE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        int goal_id = getIntent().getIntExtra("GOAL_ID",0);
        MySQLiteHelper db = new MySQLiteHelper(this);
        goal = db.getGoal(goal_id);
        db.close();

        txtTitle = (TextView) findViewById(R.id.txtview_goal_title);
        imgIcon = (ImageView) findViewById(R.id.imgview_goal_icon);
        listViewTasks = (ListView) findViewById(R.id.listview_tasks);

        List<String> taskList = goal.getTaskList();
        listViewTasks.setAdapter(new TaskAdapter(this,
                R.layout.task_listview_row,taskList.toArray(new String[taskList.size()])));

        txtTitle.setText(goal.getTitle());
        if(goal.getType().equals("Mind")) {
            imgIcon.setImageResource(R.drawable.mind_icon);
        }
        else if(goal.getType().equals("Body")) {
            imgIcon.setImageResource(R.drawable.body_icon);
        }
        else {
            imgIcon.setImageResource(R.drawable.spirit_icon);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_goal_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
