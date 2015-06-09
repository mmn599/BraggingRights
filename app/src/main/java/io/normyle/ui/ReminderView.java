package io.normyle.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class ReminderView extends TextView {

    //TODO: CHANGE TO BE PART OF A BETTER OBJECT HIEARCHY (GOALITEMVIEW)
    Context context;
    Date reminderDate;
    LayoutInflater inflater;
    int selected_color;
    int unselected_color;

    private static final SimpleDateFormat DATE_FORMAT_FOR_DISPLAY =
            new SimpleDateFormat("HH:mm MM-dd-yyyy");

    public ReminderView(Context context) {
        super(context);
    }

    public ReminderView(Context context, Date reminderDate, OnClickListener listener) {
        super(context);

        this.context = context;
        this.reminderDate = reminderDate;

        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        setClickable(true);
        setOnClickListener(listener);

        this.setGravity(Gravity.CENTER);
        this.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


       // this.setText(Goal.createSpannableString(DATE_FORMAT_FOR_DISPLAY.format(reminderDate), false));

        this.setText("M T W Th F     3pm");

        selected_color = context.getResources().getColor(R.color.accent);
        unselected_color = this.getCurrentTextColor();

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

    public ReminderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReminderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //slides note off screen
    public void deleteNoteAnimation(Animator.AnimatorListener listener) {
        ViewPropertyAnimator animator = this.animate();
        animator.setListener(listener);
        animator.setDuration(500);
        animator.translationX(Constants.SCREEN_WIDTH);
        animator.start();
    }

    public String getReminderString() {
        return Goal.dateFormatForReminders.format(reminderDate);
    }

    //deletes the view after the animation is done
    public static class ReminderAnimatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        ReminderView oldView;

        public ReminderAnimatorListener(LinearLayout ll, ReminderView oldView,
                                    Activity activity) {
            super();
            this.ll = ll;
            this.oldView = oldView;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}
