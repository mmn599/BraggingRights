package io.normyle.braggingrights;

import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.StrikerTextView;
import io.normyle.ui.TaskAdapter;

public class GoalViewActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    TextView txtTitle;
    TextView txtStartTime;
    TextView txtEndTime;
    ImageView imgIcon;
    Goal goal;
    ListView listViewTasks;
    TaskAdapter taskAdapter;
    FloatingActionButton fab;
    int current_position = -1;


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

        fab = (FloatingActionButton) findViewById(R.id.btn_complete_action_button);
        fab.setOnClickListener(this);

        txtTitle = (TextView) findViewById(R.id.txtview_goal_title);
        imgIcon = (ImageView) findViewById(R.id.imgview_goal_icon);
        txtStartTime = (TextView) findViewById(R.id.txtview_start_time);
        txtEndTime = (TextView) findViewById(R.id.txtview_end_time);
        listViewTasks = (ListView) findViewById(R.id.listview_tasks);

        List<String> taskList = goal.getTaskList();
        taskAdapter = new TaskAdapter(this,
                R.layout.task_listview_row,taskList.toArray(new String[taskList.size()]));
        listViewTasks.setAdapter(taskAdapter);
        listViewTasks.setOnItemClickListener(this);

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

        txtStartTime.setText("Started on: \n" + goal.getStartDateString());
        if(goal.getComplete()==Goal.COMPLETE) {
            txtEndTime.setText("Completed on: \n"+goal.getCompletedDateString());
            fab.setVisibility(View.GONE);
        }
        else {
            txtEndTime.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        MySQLiteHelper db = new MySQLiteHelper(this);
        if(current_position!=-1) {
            String newString = goal.updateTask(taskAdapter.getItem(current_position), 1);
            Toast.makeText(this, newString, Toast.LENGTH_LONG).show();
        }
        else {
            goal.setComplete(1);
            Toast.makeText(this,"Goal complete!",Toast.LENGTH_LONG).show();
        }
        db.updateGoal(goal);
        db.close();
    }

    //TODO: bug if the user selects then scrolls
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        current_position = position;
        StrikerTextView textView = (StrikerTextView) view.findViewById(R.id.txtview_task_row_title);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        textView.strikeAnimation(width);
    }
}
