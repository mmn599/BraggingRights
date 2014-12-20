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
import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class TaskView extends FrameLayout {

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
        View view = inflater.inflate(R.layout.task_listview_row,this,true);
        title = (TextView) view.findViewById(R.id.txtview_task_row_title);
        title.setText(Goal.createSpannableString(taskString));

        //TODO: PERFORMANCE: maybe make this drawable static
        this.setBackground(context.getResources().getDrawable(R.drawable.listviewselector));
    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void completeTaskAnimation(int width, Animator.AnimatorListener listener) {
        ViewPropertyAnimator animator = this.animate();
        animator.setListener(listener);
        animator.setDuration(1000);
        animator.translationX(width);
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
