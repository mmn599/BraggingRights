package io.normyle.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.normyle.braggingrights.PersonhoodFragment;
import io.normyle.data.Constants;

/**
 * Created by matthew on 6/26/15.
 */
public class GoalTypeViewer extends LinearLayout implements View.OnClickListener {

    private int mSpacing;
    private Context mContext;
    private List<Constants.GoalType> mData;
    private MagicListener mListener;
    private boolean mIgnoreReclicks = true;

    private GoalTypeView mSelected = null;

    public GoalTypeViewer(Context context) {
        super(context);
        mContext = context;
        mData = Constants.getGoalTypes(mContext);
        mSpacing = 60;
        mListener = null;
    }

    public GoalTypeViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mData = Constants.getGoalTypes(mContext);
        mSpacing = 60;
        mListener = null;
        setupIcons();
    }

    public void setData(List<Constants.GoalType> data) {
        mData = data;
        setupIcons();
    }

    public void setSpacing(int spacing) {
        mSpacing = spacing;
        setupIcons();
    }

    protected void setupIcons() {
        this.removeAllViews();
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, mSpacing, 0);
        if(mData!=null) {
            for (int i=0;i<mData.size();i++) {
                Constants.GoalType type = mData.get(i);
                GoalTypeView view = new GoalTypeView(mContext);
                view.setImageResource(type.getImageId());
                view.setColor(type.getColor());
                if(i!=(mData.size()-1)) {
                    view.setLayoutParams(params);
                }
                view.setTag(type.getType());
                if(i==0) {
                    view.setSelected(true);
                    mSelected = view;
                }
                view.setOnClickListener(this);
                this.addView(view);
            }
        }
    }

    public String getSelected() {
        if(mSelected!=null) {
            return (String) mSelected.getTag();
        }
        throw new RuntimeException();
    }

    public void clearSelected() {
        if(mSelected!=null) {
            mSelected.setSelected(false);
        }
        if(mListener!=null) {
            mListener.onChange(MagicListener.UNSELECTED_STRING);
        }
        mSelected = null;
    }

    public void setReclicks(boolean ignore) {
        mIgnoreReclicks = ignore;
    }

    public void updateData() {
        List<Constants.GoalType> goalTypes = Constants.getGoalTypes(mContext);
        setData(goalTypes);
    }

    public void setListener(MagicListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mSelected!=null) {
            mSelected.setSelected(false);
        }
        if(!(mSelected==v) || mIgnoreReclicks) {
            v.setSelected(true);
            mSelected = (GoalTypeView) v;
            if (mListener != null) {
                mListener.onChange((String) v.getTag());
            }
        }
        else {
            v.setSelected(false);
            mSelected = null;
            if(mListener!=null) {
                mListener.onChange(MagicListener.UNSELECTED_STRING);
            }
        }
    }
}
