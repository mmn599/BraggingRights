package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;

public class GoalAdapter extends ArrayAdapter<Goal> {

    Context context;
    int layoutResourceId;
    List<Goal> data = null;

    public GoalAdapter(Context context, int layoutResourceId, List<Goal> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new WeatherHolder();
            holder.goalTypeView = (GoalTypeView) row.findViewById(R.id.typeview_goal_row_type);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtview_goal_row_title);
            holder.txtDescription = (TextView) row.findViewById(R.id.txtview_goal_row_description);
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        Goal goal = data.get(position);
        holder.txtTitle.setText(goal.getTitle());

        //TODO: this is crappy code. change to hash
        List<Constants.GoalType> goalTypes = Constants.getGoalTypes(context);
        String typeString = goal.getType();
        Constants.GoalType type = null;
        for(Constants.GoalType gt : goalTypes) {
            if(typeString.equals(gt.getType())) {
                type = gt;
            }
        }
        holder.goalTypeView.setImageResource(type.getImageId());
        holder.goalTypeView.setColor(type.getColor());

        List<Goal.Task> taskList = goal.getTasks();
        holder.txtDescription.setText(Goal.createSpannableString(taskList, true, true, true));

        return row;
    }

    static class WeatherHolder
    {
        GoalTypeView goalTypeView;
        TextView txtTitle;
        TextView txtDescription;
    }

}