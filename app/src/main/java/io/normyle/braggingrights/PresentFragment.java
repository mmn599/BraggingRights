package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalAdapter;


public class PresentFragment extends Fragment implements View.OnClickListener,ListView.OnItemClickListener {

    ListView goalsListView;
    FloatingActionButton fab;
    HashMap<String,Integer> goalsMap;
    List<Goal> goalsToDisplay;
    Activity activity;
    GoalAdapter goalAdapter;
    View view;
    TextView txtviewIntro;

    boolean display_present;

    public static final String WHICH_GOALS = "WHICH_GOALS";

    public PresentFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String whichGoals = (String) getArguments().get(WHICH_GOALS);
        if(whichGoals.equals(MainActivity.PASTGOALS)) {
            display_present = false;
        }
        else {
            display_present = true;
        }
        goalsToDisplay = new ArrayList<Goal>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_present, container, false);

        txtviewIntro = (TextView) view.findViewById(R.id.txtview_goals_intro);
        txtviewIntro.setTextColor(Color.GRAY);

        fab = (FloatingActionButton) view.findViewById(R.id.btn_action_button);
        fab.setOnClickListener(this);
        goalsMap = new HashMap<String,Integer>();

        MySQLiteHelper db = new MySQLiteHelper(activity);
        List<Goal> goals = db.getAllGoals();
        db.close();

        displayGoals(display_present);

        goalsListView = (ListView)view.findViewById(R.id.listview_goals);
        goalsListView.setAdapter(goalAdapter);
        goalsListView.setOnItemClickListener(this);

        for(Goal goal : goals) {
            goalsMap.put(goal.getTitle(),goal.getId());
        }

        fab.attachToListView(goalsListView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO: check if shit has changed
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public void displayGoals(boolean display_present) {
        this.display_present = display_present;
        MySQLiteHelper db = new MySQLiteHelper(activity);
        List<Goal> goals = db.getAllGoals();
        goalsToDisplay.clear();
        for(Goal goal : goals) {
            if(goal.getComplete()==Goal.INCOMPLETE && display_present) {
                goalsToDisplay.add(goal);
            }
            else if(goal.getComplete()==Goal.COMPLETE && !display_present) {
                goalsToDisplay.add(goal);
            }
        }
        if(goalAdapter==null) {
            goalAdapter = new GoalAdapter(activity,
                    R.layout.goal_listview_row, goalsToDisplay);
        }
        else {
            goalsListView.post(new Runnable() {
                @Override
                public void run() {
                    goalAdapter.notifyDataSetChanged();
                }
            });
        }
        if(goals.size()==0 && display_present) {
            txtviewIntro.setText("Press the action button to add a goal.");
            txtviewIntro.setVisibility(View.VISIBLE);
        }
        else if(!display_present && Goal.getCompleteGoals(goals).size()==0) {
            txtviewIntro.setText("When you complete a goal it will be displayed here.");
            txtviewIntro.setVisibility(View.VISIBLE);
        }
        else {
            txtviewIntro.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onClick(View v) {
        if(v.getId()==R.id.btn_action_button) {
            startActivity(new Intent(activity,CreateGoalActivity.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String title = ((TextView) view.findViewById(R.id.txtview_goal_row_title)).getText().toString();
        Intent intent = new Intent(activity, GoalViewActivity.class);
        intent.putExtra("GOAL_ID", goalsMap.get(title));
        startActivity(intent);
    }

}
