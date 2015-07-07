package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
public class GoalTypeAdapter extends ArrayAdapter<Constants.GoalType> implements View.OnClickListener {

    List<Constants.GoalType> mData;
    View mSelected;
    Context mContext;
    int mResourceID;
    int mSelectedColor;

    public GoalTypeAdapter(Context context, int resource_id, List<Constants.GoalType> data, int selected_color) {
        super(context,resource_id,data);
        mData = data;
        mSelectedColor = selected_color;
        mResourceID = resource_id;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResourceID, parent, false);
        }

        Constants.GoalType type = mData.get(position);
        ImageView img = (ImageView) convertView.findViewById(R.id.goaltype_view);
        TextView txt = (TextView) convertView.findViewById(R.id.txtview_type_description);
        img.setImageResource(type.getImageId());
        txt.setText(type.getType());

        convertView.setOnClickListener(this);

        return convertView;
    }

    public View getSelected() {
        return mSelected;
    }

    @Override
    public void onClick(View v) {
        if(mSelected!=null) {
            mSelected.setSelected(false);
            mSelected.setBackgroundColor(Color.TRANSPARENT);
        }
        mSelected = v;
        mSelected.setSelected(true);
        mSelected.setBackgroundColor(mSelectedColor);
    }
}
