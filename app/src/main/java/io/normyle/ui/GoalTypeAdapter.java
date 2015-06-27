package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;

/**
 * Created by matthew on 6/26/15.
 */
public class GoalTypeAdapter extends ArrayAdapter<Constants.GoalType>{

    Context context;
    int layoutResourceId;
    List<Constants.GoalType> data;

    public GoalTypeAdapter(Context context, int layoutResourceId, List<Constants.GoalType> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        Constants.GoalType type = data.get(position);

        GoalTypeView view = (GoalTypeView) row.findViewById(R.id.goaltype_view);
        TextView txtDescription = (TextView) row.findViewById(R.id.txtview_type_description);
        view.setImageResource(type.getImageId());
        view.setColor(type.getColor());
        txtDescription.setText(type.getType());

        return row;
    }

}
