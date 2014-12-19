package io.normyle.braggingrights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalAdapter;


public class PresentFragment extends Fragment implements View.OnClickListener,ListView.OnItemClickListener {

    ListView goalsListView;
    FloatingActionButton fab;
    MySQLiteHelper db;
    HashMap<String,Integer> goalsMap;
    Activity activity;

    public PresentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_present, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.btn_action_button);
        fab.setOnClickListener(this);
        goalsMap = new HashMap<String,Integer>();

        db = new MySQLiteHelper(activity);
        List<Goal> goals = db.getAllGoals();
        db.close();

        List<Goal> presentGoals = new ArrayList<Goal>();
        for(Goal goal : goals) {
            if(goal.getComplete()==Goal.INCOMPLETE) {
                presentGoals.add(goal);
            }
        }

        Goal[] goalsArray = presentGoals.toArray(new Goal[presentGoals.size()]);
        GoalAdapter goalsAdapter = new GoalAdapter(activity,
                R.layout.goal_listview_row, goalsArray);

        goalsListView = (ListView)view.findViewById(R.id.listview_goals);
        goalsListView.setAdapter(goalsAdapter);
        goalsListView.setOnItemClickListener(this);

        //TODO: may cause performance issues
        for(Goal goal : goals) {
            goalsMap.put(goal.getTitle(),goal.getId());
        }

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
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
