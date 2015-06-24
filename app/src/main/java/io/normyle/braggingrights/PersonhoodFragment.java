package io.normyle.braggingrights;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

public class PersonhoodFragment extends Fragment {

    GraphView graphVentures;
    GraphView graphTasks;
    GraphView graphGoals;

    public PersonhoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personhood, container, false);

        graphVentures = (GraphView) view.findViewById(R.id.graph_ventures);
        graphTasks = (GraphView) view.findViewById(R.id.graph_tasks);
        graphGoals = (GraphView) view.findViewById(R.id.graph_goals);

        setupVentures(getVentures());
        setupTasks(getTasks());
        setupGoals(getGoals());

        return view;

    }

    private void setupVentures(List<Goal.Venture> ventures) {

    }

    private void setupTasks(List<Goal.Task> tasks) {

    }

    private void setupGoals(List<Goal> goals) {

    }

    private List<Goal.Venture> getVentures() {
        List<Goal> goals = getGoals();
        List<Goal.Venture> ventures = new ArrayList<Goal.Venture>();
        for(Goal goal : goals) {
            for(Goal.Venture venture : goal.getVentures()) {
                ventures.add(venture);
            }
        }
        return ventures;
    }

    private List<Goal.Task> getTasks() {
        List<Goal> goals = getGoals();
        List<Goal.Task> tasks = new ArrayList<Goal.Task>();
        for(Goal goal : goals) {
            for(Goal.Task task : goal.getTasks()) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private List<Goal> getGoals() {
        MySQLiteHelper db = new MySQLiteHelper(getActivity());
        List<Goal> goals = db.getAllGoals();
        db.close();
        return goals;
    }
}
