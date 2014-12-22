package io.normyle.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
public class TaskView extends FrameLayout {


    //TODO: CHANGE TO BE PART OF A BETTER OBJECT HIEARCHY (GOALITEMVIEW)
    Context context;
    TextView title;
    String taskString;
    LayoutInflater inflater;


    public TaskView(Context context) {
        super(context);
    }

    public TaskView(Context context,String taskString,OnClickListener listener) {
        super(context);

        this.context = context;
        this.taskString = taskString;

        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        setClickable(true);
        setOnClickListener(listener);
        View view = inflater.inflate(R.layout.taskview_layout,this,true);
        title = (TextView) view.findViewById(R.id.txtview_task_row_title);
        title.setText(Goal.createSpannableString(taskString, true));
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

    public String getTaskString() {
        return taskString;
    }

    public static class TaskAnimatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        TaskView oldView;
        String newString;
        Activity activity;
        OnClickListener listener;

        public TaskAnimatorListener(LinearLayout ll, TaskView oldView, String newString,
                                    Activity activity, OnClickListener listener) {
            super();
            this.ll = ll;
            this.oldView = oldView;
            this.newString = newString;
            this.activity = activity;
            this.listener = listener;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
            ll.addView(new TaskView(activity,newString,listener));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}