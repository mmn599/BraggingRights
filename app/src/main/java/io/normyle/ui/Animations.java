package io.normyle.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.matthew.braggingrights.R;

/**
 * Created by MatthewNew on 12/19/2014.
 */
public class Animations {

    //deletes the view after the animation is done
    public static class ViewTerminatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        View oldView;
        Activity activity;
        //TASKS OR REMINDERS
        String which;

        public ViewTerminatorListener(LinearLayout ll, View oldView,
                                    Activity activity, String which) {
            super();
            this.ll = ll;
            this.oldView = oldView;
            this.activity = activity;
            this.which = which;
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
                if (which.equals("REMINDERS")) {
                    view.setText("Click the clock add a goal reminder.");
                }
                else if (which.equals("TASKS")) {
                    view.setText("Click the clipboard to add a goal task.");
                }
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
