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
public class GoalTypeAdapter extends RecyclerView.Adapter<GoalTypeAdapter.ViewHolder> implements View.OnClickListener {

    List<Constants.GoalType> mData;
    View mSelected;
    int mSelectedColor;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public GoalTypeView mGoalTypeView;
        public ViewHolder(View v, View.OnClickListener listener) {
            super(v);
            v.setOnClickListener(listener);
            mTextView = (TextView) v.findViewById(R.id.txtview_type_description);
            mGoalTypeView = (GoalTypeView) v.findViewById(R.id.goaltype_view);
        }
    }

    public GoalTypeAdapter(List<Constants.GoalType> data, int color) {
        mData = data;
        mSelectedColor = color;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goaltypelayout, parent, false);
        ViewHolder vh = new ViewHolder(v, this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Constants.GoalType type = mData.get(position);
        holder.mGoalTypeView.setImageResource(type.getImageId());
        holder.mGoalTypeView.setColor(type.getColor());
        holder.mTextView.setText(type.getType());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getSelectedTypeDescription() {
        if(mSelected!=null) {
            return ((TextView)(mSelected)
                    .findViewById(R.id.txtview_type_description)).getText().toString();
        }
        return null;
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
