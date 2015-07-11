package io.normyle.braggingrights;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalTypeViewer;
import io.normyle.ui.MagicListener;

/**
 * Fragment within MainActivity in charge of displaying the user's goal history
 * 
 * Honestly, this code sucks. The various chart setup methods need to be consolidated into
 * one.
 *

 */
public class PersonhoodFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, MagicListener {

    LineChart mChart;
    static String[] ACCOMPLISHMENT_TYPE_STRINGS = {
            "Goals Completed","Goals Created","Tasks Completed","Practice Points"};
    List<Goal> mGoals;
    Drawable mBackground;
    TextView mTxtviewTypeTitle;

    TextView mTxtviewGoals;
    TextView mTxtviewTasks;
    TextView mTxtviewPP;

    TextView mTxtviewGoalsDes;
    TextView mTxtviewTasksDes;
    TextView mTxtviewPPDes;

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

        mChart = (LineChart) view.findViewById(R.id.chart_data);

        mGoals = getGoals();

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_which_chart);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(),R.layout.spinner_accomplishment_types, ACCOMPLISHMENT_TYPE_STRINGS);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        setupCompletedGoals();
        setupChart();

        Legend legend = mChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        mTxtviewTypeTitle = (TextView) view.findViewById(R.id.txtview_goal_type);

        mTxtviewGoals = (TextView) view.findViewById(R.id.txtview_goals);
        mTxtviewTasks = (TextView) view.findViewById(R.id.txtview_tasks);
        mTxtviewPP = (TextView) view.findViewById(R.id.txtview_ventures);

        mTxtviewGoalsDes = (TextView) view.findViewById(R.id.txtview_goals_des);
        mTxtviewTasksDes = (TextView) view.findViewById(R.id.txtview_tasks_des);
        mTxtviewPPDes = (TextView) view.findViewById(R.id.txtview_ventures_des);

        GoalTypeViewer viewer = (GoalTypeViewer) view.findViewById(R.id.viewer_goal_types);
        viewer.setListener(this);
        viewer.selectFirst();
        mBackground = view.getBackground();
        setupTextInfo(viewer.getSelected());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupPracticePoints() {
        mChart.clear();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes(getActivity())) {
            //get all goals of goalType
            List<Goal> relevantGoals = Goal.getGoalsOfType(mGoals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.set(Calendar.MONTH, beginning.get(Calendar.MONTH) - (4-i));
                beginning.set(Calendar.DAY_OF_MONTH,
                        beginning.getActualMinimum(Calendar.DAY_OF_MONTH));
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                Calendar end = GregorianCalendar.getInstance();
                end.set(Calendar.MONTH, end.get(Calendar.MONTH) - (4-i));
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                Date a = beginning.getTime();
                Date b = end.getTime();
                //get ventures within time period of goals of right type
                List<Goal.Venture> relevantVentures = new ArrayList<>();
                for (Goal.Venture venture : Goal.getAllVentures(relevantGoals)) {
                    if (venture.date.after(a) && venture.date.before(b)) {
                        relevantVentures.add(venture);
                    }
                }
                yVals.add(new Entry(relevantVentures.size(),i));
                if(first) {
                    xVals.add(new SimpleDateFormat("MMMM yyyy", Locale.US).format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleColor(goalType.getColor());
            set.setCircleSize(5f);
            set.setValueTextSize(10f);
            set.setValueFormatter(new MyValueFormatter());
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mChart.getAxisLeft().setAxisMaxValue(10);
        mChart.setData(data);
    }

    private void setupTasks() {
        mChart.clear();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes(getActivity())) {
            //get all goals of goalType
            List<Goal> relevantGoals = Goal.getGoalsOfType(mGoals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.set(Calendar.MONTH, beginning.get(Calendar.MONTH) - (4-i));
                beginning.set(Calendar.DAY_OF_MONTH,
                        beginning.getActualMinimum(Calendar.DAY_OF_MONTH));
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                Calendar end = GregorianCalendar.getInstance();
                end.set(Calendar.MONTH, end.get(Calendar.MONTH) - (4-i));
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                Date a = beginning.getTime();
                Date b = end.getTime();
                //get ventures within time period of goals of right type
                List<Goal.Task> relevantTasks = new ArrayList<>();
                for (Goal.Task task : Goal.getAllCompleteTasks(relevantGoals)) {
                    if (task.completeDate.after(a) && task.completeDate.before(b)) {
                        relevantTasks.add(task);
                    }
                }
                yVals.add(new Entry(relevantTasks.size(),i));
                if(first) {
                    xVals.add(new SimpleDateFormat("MMMM yyyy", Locale.US).format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleColor(goalType.getColor());
            set.setCircleSize(5f);
            set.setValueTextSize(10f);
            set.setValueFormatter(new MyValueFormatter());
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mChart.getAxisLeft().setAxisMaxValue(10);
        mChart.setData(data);
    }

    private void setupCompletedGoals() {
        mChart.clear();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes(getActivity())) {
            //get all goals of goalType
            //TODO: don't get all goals
            List<Goal> relevantGoals = Goal.getGoalsOfType(mGoals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.set(Calendar.MONTH, beginning.get(Calendar.MONTH) - (4-i));
                beginning.set(Calendar.DAY_OF_MONTH,
                        beginning.getActualMinimum(Calendar.DAY_OF_MONTH));
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                Calendar end = GregorianCalendar.getInstance();
                end.set(Calendar.MONTH, end.get(Calendar.MONTH) - (4-i));
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                Date a = beginning.getTime();
                Date b = end.getTime();
                List<Goal> complete = new ArrayList<Goal>();
                for (Goal goal : relevantGoals) {
                    if (goal.getComplete()==Goal.COMPLETE
                            && goal.getCompleteDate().after(a) &&
                            goal.getCompleteDate().before(b)) {
                        complete.add(goal);
                    }
                }
                yVals.add(new Entry(complete.size(),i));
                if(first) {
                    xVals.add(new SimpleDateFormat("MMMM yyyy", Locale.US).format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleSize(5f);
            set.setCircleColor(goalType.getColor());
            set.setValueTextSize(10f);
            set.setValueFormatter(new MyValueFormatter());
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mChart.getAxisLeft().setAxisMaxValue(10);
        mChart.setData(data);
    }


    private void setupCreatedGoals() {
        mChart.clear();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes(getActivity())) {
            //get all goals of goalType
            //TODO: don't get all goals
            List<Goal> relevantGoals = Goal.getGoalsOfType(mGoals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.set(Calendar.MONTH, beginning.get(Calendar.MONTH) - (4-i));
                beginning.set(Calendar.DAY_OF_MONTH,
                        beginning.getActualMinimum(Calendar.DAY_OF_MONTH));
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                Calendar end = GregorianCalendar.getInstance();
                end.set(Calendar.MONTH, end.get(Calendar.MONTH) - (4-i));
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                Date a = beginning.getTime();
                Date b = end.getTime();
                List<Goal> complete = new ArrayList<Goal>();
                for (Goal goal : relevantGoals) {
                    if (goal.getStartDate().after(a) &&
                            goal.getStartDate().before(b)) {
                        complete.add(goal);
                    }
                }
                yVals.add(new Entry(complete.size(),i));
                if(first) {
                    xVals.add(new SimpleDateFormat("MMMM yyyy", Locale.US).format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleSize(5f);
            set.setCircleColor(goalType.getColor());
            set.setValueTextSize(10f);
            set.setValueFormatter(new MyValueFormatter());
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mChart.getAxisLeft().setAxisMaxValue(10);
        mChart.setData(data);
    }


    private List<Goal> getGoals() {
        MySQLiteHelper db = new MySQLiteHelper(getActivity());
        List<Goal> goals = db.getAllGoals();
        db.close();
        return goals;
    }

    private void setupChart() {

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new MyValueFormatter());

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);

        mChart.getAxisRight().setEnabled(false);

        mChart.getLineData().setValueTextSize(13f);
        mChart.getLineData().setValueFormatter(new MyValueFormatter());

        mChart.setBackground(mBackground);
        mChart.setDrawGridBackground(false);

        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
        mChart.setDescriptionPosition(point.x - 20, 60);
        mChart.setDescriptionTextSize(15);
        mChart.setDescriptionColor(getResources().getColor(R.color.accent));

        mChart.setClickable(false);
        mChart.setTouchEnabled(false);
    }

    public static class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }

    }


    private void setupTextInfo(String type) {
        if(type.equals(UNSELECTED_STRING)) {
            return;
        }
        mTxtviewTypeTitle.setText(type + " totals:");
        Constants.GoalType goalType = null;
        for(Constants.GoalType curtype : Constants.getGoalTypes(getActivity())) {
            if(curtype.getType().equals(type)) {
                goalType = curtype;
            }
        }
        if(goalType==null) {
            throw new RuntimeException();
        }
        List<Goal> goals = Goal.getGoalsOfType(mGoals,goalType);
        int numGoals = 0;
        int numTasks = 0;
        int numVentures = 0;
        for(Goal goal : goals) {
            if(goal.getComplete()==Goal.COMPLETE) {
                numGoals++;
            }
            for(Goal.Task task : goal.getTasks()) {
                if(task.complete==Goal.COMPLETE) {
                    numTasks++;
                }
            }
            numVentures += goal.getVentures().size();
        }
        mTxtviewGoals.setText(numGoals + "");
        mTxtviewTasks.setText(numTasks + "");
        mTxtviewPP.setText(numVentures + "");

        if(numGoals==1) {
            mTxtviewGoalsDes.setText("Goal");
        }
        if(numTasks==1) {
            mTxtviewTasksDes.setText("Task");
        }
        if(numVentures==1) {
            mTxtviewPPDes.setText("Practice Point");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = ((TextView)view).getText().toString();
        if(value.equals("Practice Points")){
            setupPracticePoints();
        }
        else if(value.equals("Tasks Completed")) {
            setupTasks();
        }
        else if(value.equals("Goals Completed")){
            setupCompletedGoals();
        }
        else if(value.equals("Goals Created")) {
            setupCreatedGoals();
        }
    }


    @Override
    public void onChange(String type) {
        if(!type.equals(UNSELECTED_STRING)) {
            setupTextInfo(type);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setupPracticePoints();
    }

}

