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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    Goal.Reminder reminder;

    private static final SimpleDateFormat DATE_FORMAT_FOR_DISPLAY =
            new SimpleDateFormat("MM/dd HH:mm");

    public ReminderView(Context context) {
        super(context);
    }

    public ReminderView(Context context, Goal.Reminder reminder, OnClickListener listener) {
        super(context);

        this.context = context;

        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        setClickable(true);
        setOnClickListener(listener);

        String dayString = "";
        String[] d = {"S","M","T","W","Th","F","S"};
        for(int i=0;i<7;i++) {
            if(reminder.days[i]) {
                dayString += d[i] + " ";
            }
        }

//        this.setGravity(Gravity.CENTER);
        this.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        this.reminder = reminder;

        if(reminder.repeating) {
            this.setText(Constants.createBulletString(reminder.note +
                    (reminder.note.length()>0 ? " ":"")+new SimpleDateFormat("HH:mm", Locale.US).format(reminder.calendar.getTime()) +
                    " on " + dayString));
        }
        else {
            String string = (reminder.note.length()>0 ? reminder.note + " ":"") +
                    new SimpleDateFormat("HH:mm", Locale.US).format(reminder.calendar.getTime()) + " on " +
                    new SimpleDateFormat("MMMM d", Locale.US).format(reminder.calendar.getTime());
            string = string.trim();
            this.setText(Constants.createBulletString(string));
        }

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

    //slides note off screen
    public void deleteNoteAnimation(Animator.AnimatorListener listener) {
        ViewPropertyAnimator animator = this.animate();
        animator.setListener(listener);
        animator.setDuration(500);
        animator.translationX(Constants.SCREEN_WIDTH);
        animator.start();
    }

    public Goal.Reminder getReminder() {
        return reminder;
    }

    public String getReminderString() {
        return Goal.dateFormatForReminders.format(reminderDate);
    }

    //deletes the view after the animation is done
    public static class ReminderAnimatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        ReminderView oldView;
        Activity activity;

        public ReminderAnimatorListener(LinearLayout ll, ReminderView oldView,
                                    Activity activity) {
            super();
            this.ll = ll;
            this.oldView = oldView;
            this.activity = activity;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
            if(ll.getChildCount()==0) {
                TextView view = new TextView(activity);
                view.setTag("INFO");
                view.setText("Click the clipboard to add a goal reminder.");
                view.setGravity(Gravity.CENTER_VERTICAL);
                ll.addView(view);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }


}
