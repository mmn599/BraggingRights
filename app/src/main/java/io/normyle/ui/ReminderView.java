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
public class ReminderView extends FrameLayout {

    //TODO: CHANGE TO BE PART OF A BETTER OBJECT HIEARCHY (GOALITEMVIEW)
    Context context;
    TextView title;
    String reminderString;
    LayoutInflater inflater;


    public ReminderView(Context context) {
        super(context);
    }

    public ReminderView(Context context, String reminderString, OnClickListener listener) {
        super(context);

        this.context = context;
        this.reminderString = reminderString;

        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        setClickable(true);
        setOnClickListener(listener);
        View view = inflater.inflate(R.layout.noteview_layout,this,true);
        title = (TextView) view.findViewById(R.id.txtview_note_contents);
        title.setText(Goal.createSpannableString(reminderString,false));
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
        return reminderString;
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
