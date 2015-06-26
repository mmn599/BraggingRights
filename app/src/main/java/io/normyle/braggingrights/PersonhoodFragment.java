package io.normyle.braggingrights;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
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

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.MyMarkerView;

public class PersonhoodFragment extends Fragment {

    LineChart mTaskChart;
    LineChart mGoalsChart;
    LineChart mVenturesChart;

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

        mTaskChart = (LineChart) view.findViewById(R.id.chart_tasks);
        mGoalsChart = (LineChart) view.findViewById(R.id.chart_goals);
        mVenturesChart = (LineChart) view.findViewById(R.id.chart_ventures);

        List<Goal> goals = getGoals();

        setupTasks(goals);
        setupGoals(goals);
        setupVentures(goals);

        List<LineChart> charts = new ArrayList<>();
        charts.add(mTaskChart);
        charts.add(mGoalsChart);
        charts.add(mVenturesChart);
        setupCharts(charts);

        return view;
    }

    private void setupVentures(List<Goal> goals) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes().values()) {
            //get all goals of goalType
            List<Goal> relevantGoals = Goal.getGoalsOfType(goals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.setFirstDayOfWeek(Calendar.SUNDAY);
                beginning.set(Calendar.DAY_OF_WEEK, beginning.getFirstDayOfWeek());
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                beginning.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
                Calendar end = GregorianCalendar.getInstance();
                end.setFirstDayOfWeek(Calendar.SUNDAY);
                end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
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
                    xVals.add(new SimpleDateFormat("M d").format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleColor(goalType.getColor());
            set.setCircleSize(5f);
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mVenturesChart.setData(data);
    }

    private void setupTasks(List<Goal> goals) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes().values()) {
            //get all goals of goalType
            List<Goal> relevantGoals = Goal.getGoalsOfType(goals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.setFirstDayOfWeek(Calendar.SUNDAY);
                beginning.set(Calendar.DAY_OF_WEEK, beginning.getFirstDayOfWeek());
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                beginning.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
                Calendar end = GregorianCalendar.getInstance();
                end.setFirstDayOfWeek(Calendar.SUNDAY);
                end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
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
                    xVals.add(new SimpleDateFormat("M d").format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleColor(goalType.getColor());
            set.setCircleSize(5f);
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mTaskChart.setData(data);
    }

    private void setupGoals(List<Goal> goals) {
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        boolean first = true;
        ArrayList<String> xVals = new ArrayList<String>();
        for(Constants.GoalType goalType : Constants.getGoalTypes().values()) {
            //get all goals of goalType
            //TODO: don't get all goals
            List<Goal> relevantGoals = Goal.getGoalsOfType(goals, goalType);
            //initialize data for graph
            ArrayList<Entry> yVals = new ArrayList<Entry>();
            for (int i = 0; i < 5; i++) {
                //get time period
                Calendar beginning = GregorianCalendar.getInstance();
                beginning.setFirstDayOfWeek(Calendar.SUNDAY);
                beginning.set(Calendar.DAY_OF_WEEK, beginning.getFirstDayOfWeek());
                beginning.set(Calendar.HOUR, 0);
                beginning.set(Calendar.MINUTE, 0);
                beginning.set(Calendar.SECOND, 0);
                beginning.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
                Calendar end = GregorianCalendar.getInstance();
                end.setFirstDayOfWeek(Calendar.SUNDAY);
                end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                end.set(Calendar.HOUR, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);
                end.set(Calendar.WEEK_OF_YEAR, beginning.get(Calendar.WEEK_OF_YEAR) - (4-i));
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
                    xVals.add(new SimpleDateFormat("M d").format(beginning.getTime()));
                }
            }
            first = false;
            LineDataSet set = new LineDataSet(yVals, goalType.getType());
            set.setColor(goalType.getColor());
            set.setCircleSize(5f);
            set.setCircleColor(goalType.getColor());
            dataSets.add(set);
        }
        LineData data = new LineData(xVals, dataSets);
        mGoalsChart.setData(data);
    }

    private List<Goal> getGoals() {
        MySQLiteHelper db = new MySQLiteHelper(getActivity());
        List<Goal> goals = db.getAllGoals();
        db.close();
        return goals;
    }

    private void setupCharts(List<LineChart> charts) {

        for(LineChart chart : charts) {

            // no description text
            chart.setDescription("");
            chart.setNoDataTextDescription("You need to provide data for the chart.");

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setAxisMaxValue(20);
            leftAxis.setAxisMinValue(0);
            leftAxis.setStartAtZero(false);
            leftAxis.setDrawGridLines(false);
            leftAxis.setValueFormatter(new MyValueFormatter());

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);

            chart.getAxisRight().setEnabled(false);

            chart.getLineData().setValueTextSize(13f);
            chart.getLineData().setValueFormatter(new MyValueFormatter());
        }
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
}
