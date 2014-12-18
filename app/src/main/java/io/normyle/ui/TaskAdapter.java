package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/17/2014.
 */
public class TaskAdapter extends ArrayAdapter<String> {


    Context context;
    int layoutResourceId;
    String data[] = null;

    public TaskAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new Holder();
            holder.taskTitle = (TextView) row.findViewById(R.id.txtview_task_row_title);

            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        String task = data[position];
        holder.taskTitle.setText(task);

        return row;
    }

    static class Holder
    {
        TextView taskTitle;
    }

}
