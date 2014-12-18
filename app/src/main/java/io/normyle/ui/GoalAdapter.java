package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;

public class GoalAdapter extends ArrayAdapter<Goal> {

    Context context;
    int layoutResourceId;
    Goal data[] = null;

    public GoalAdapter(Context context, int layoutResourceId, Goal[] data) {
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
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgview_goal_row_image);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtview_goal_row_title);
            holder.txtDescription = (TextView) row.findViewById(R.id.txtview_goal_row_description);


            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        Goal goal = data[position];
        holder.txtTitle.setText(goal.getTitle());
        if(goal.getType().equals("Mind")) {
            holder.imgIcon.setImageResource(R.drawable.mind_icon);
        }
        else if(goal.getType().equals("Body")) {
            holder.imgIcon.setImageResource(R.drawable.body_icon);
        }
        else {
            holder.imgIcon.setImageResource(R.drawable.spirit_icon);
        }
        List<String> taskList = goal.getTaskList();
        holder.txtDescription.setText(Goal.createTasksSpannableString(taskList,true));

        return row;
    }

    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDescription;
    }

}