package io.normyle.ui;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class TaskView extends TextView {


    //TODO: CHANGE TO BE PART OF A BETTER OBJECT HIEARCHY (GOALITEMVIEW)
    Context context;
    String taskString;
    Goal.Task mTask;
    LayoutInflater inflater;
    int selected_color;
    int unselected_color;


    public TaskView(Context context) {
        super(context);
    }

    public TaskView(Context context,Goal.Task task,OnClickListener listener) {
        super(context);

        this.context = context;
        this.taskString = task.task;
        this.mTask = task;

        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        setClickable(true);
        setOnClickListener(listener);
        this.setText(Goal.createSpannableString(mTask, true));
//        this.setGravity(Gravity.CENTER);
        this.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        selected_color = context.getResources().getColor(R.color.accent);
        unselected_color = this.getCurrentTextColor();

    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void completeTaskAnimation(Animator.AnimatorListener listener) {
        ViewPropertyAnimator animator = this.animate();
        animator.setListener(listener);
        animator.setDuration(500);
        animator.translationX(Constants.SCREEN_WIDTH);
        animator.start();
    }

    public Goal.Task getTask() {
        return mTask;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if(selected) {
            this.setTextColor(selected_color);
        }
        else {
            this.setTextColor(unselected_color);
        }
    }

    public String getTaskString() {
        return taskString;
    }

    public static class TaskAnimatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        TaskView oldView;
        Goal.Task newTask;
        Activity activity;
        OnClickListener listener;

        public TaskAnimatorListener(LinearLayout ll, TaskView oldView, Goal.Task newTask,
                                    Activity activity, OnClickListener listener) {
            super();
            this.ll = ll;
            this.oldView = oldView;
            this.newTask = newTask;
            this.activity = activity;
            this.listener = listener;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
            ll.addView(new TaskView(activity,newTask,listener));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}
